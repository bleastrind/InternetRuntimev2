package org.internetrt.core

import model.RoutingInstance
import signalsystem.Signal
import signalsystem.SignalResponse
import org.internetrt.core.signalsystem.ObjectResponse
import org.internetrt.core.security.AccessToken
import org.internetrt.core.security.AuthCenter
import org.internetrt.core.signalsystem.SignalSystem
import org.internetrt.core.io.IOManager
import org.internetrt.core.configuration.ConfigurationSystem
import org.internetrt.core.model.Application
import java.util.UUID
import scala.xml.XML
import org.internetrt.core.model.Routing
import org.internetrt.core.security.AccessControlSystem
import org.internetrt.exceptions.AccessRequestNotGrantedException
import org.internetrt.exceptions.ApplicationNotInstalledException
import org.internetrt.core.io.userinterface._
import org.internetrt.core.siblings.ClusterManager

/**
 * The Facade of the logical system
 */
abstract class InternetRuntime {

  object errReport extends {
    val global: InternetRuntime.this.type = InternetRuntime.this
  }

  /**
   * ***********************************************************************
   * ---------------------------- sub component-----------------------------*
   * ***********************************************************************
   */
  val signalSystem: SignalSystem
  val authCenter: AuthCenter
  val aclSystem: AccessControlSystem
  val ioManager: IOManager
  val confSystem: ConfigurationSystem
  val clusterManager: ClusterManager
  /**
   * ***********************************************************************
   * ---------------------------- security management-----------------------*
   * ***********************************************************************
   */

  /**
   *
   */
  def registerApp(email: String): (String, String) = {
    authCenter.registerApp(email)
  }

  def getAuthcodeForActionFlow(appID: String, appSecret: String, workflowID: String) = {
    authCenter.genAuthCode(appID, appSecret, workflowID)
  }
  def getAccessTokenByAuthtoken(appID: String, authtoken: String, appSecret: String): AccessToken = {
    authCenter.genAccessTokenByAuthToken(authtoken, appID, appSecret)
  }
  def getUserIDByAccessToken(accessToken: String): String = {
    authCenter.getUserIDByAccessToken(accessToken)
  }

  /**
   * ************************************************************************
   * ---------------------------- signal processing-------------------------*
   *   Event is a signal that do not except a response                      *
   *   Request is a signal that except a response                           *
   *   Action is a sequence of the combinations of signals and signal handl-*
   * ers to achieve a meanful behaviour in the userspace, it's managed as w-*
   * orkflow                                                                *
   *
   *   Each request is separated according to it's caller, UserInterface is *
   * granted highest trust and can access the userID directly while ThirdPa-*
   * rt apps have to get accesstoken from security component first          *
   * ************************************************************************
   */
  def registerSignal(name: String, xml: String): Boolean = {
    try {
      signalSystem.registerSignal(name, scala.xml.XML.loadString(xml))
    } catch {
      case _:Throwable => false
    }
  }
  
  def getSignalDefination(name:String):String = {
    signalSystem.getSignalDefination(name).head.toString()
  }

  def triggerEventFromThirdPart(accessToken: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val signal = initSignalFromThirdPart(accessToken, signalID, vars, options)
    signalSystem.triggerEvent(signal)
  }
  def executeRequestFromThirdPart(accessToken: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val signal = initSignalFromThirdPart(accessToken, signalID, vars, options)
    signalSystem.executeRequest(signal)
  }
  def initActionFromThirdPart(accessToken: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val signal = initSignalFromThirdPart(accessToken, signalID, vars, options)
    signalSystem.initAction(signal, options)
  }
  def initActionOptionsFromThirdPart(accessToken: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]): Map[String, Seq[scala.xml.Node]] = {
    val signal = initSignalFromThirdPart(accessToken, signalID, vars, options)
    signalSystem.initActionOptions(signal, options)
  }

  private def initSignalFromThirdPart(accessToken: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)
    Signal(signalID, userID, appID, vars)
  }
  //  def getUserAndFromByAccesstoken(accesstoken:String)={
  //    authCenter.getUserIDAppIDPair(accesstoken)
  //  }
  /**
   * ***********************************************************************
   * ---------------------------- configuration panel-----------------------*
   * ***********************************************************************
   */
  def installApplication(accessToken: String, xml: String) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)

    if (aclSystem.isRoot(userID, appID)) {

      val app = Application(XML.loadString(xml))
      aclSystem.grantAccess(userID, app.id, app.accessRequests, false)
      confSystem.installApp(userID, app)
      true
    } else
      false
  }

  def confirmRouting(accessToken: String, xml: String):Boolean = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)

    if (aclSystem.isRoot(userID, appID)) {
      confSystem.confirmRouting(userID, Routing(userID,scala.xml.XML.loadString(xml)))
    }else
      false
  }

  def getApplications(accessToken: String) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)
    aclSystem.checkAccess(userID, appID, "getApplications")
    confSystem.getAppIDs(userID);
  }

  def getApplicationDetail(id: String, accessToken: String) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)
    aclSystem.checkAccess(userID, appID, "getApplications");
    confSystem.getApp(userID, id) match {
      case Some(app) => app
      case _ => throw new ApplicationNotInstalledException()
    }
  }
    /**
   * ***********************************************************************
   * ---------------------------- client communication---------------------*
   * ***********************************************************************
   */
  def sendEvent(accessToken: String, msg: String,allowedStatus:Seq[String]) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)
    aclSystem.checkAccess(userID, appID, "communicateUser")
    ioManager.sendToClient(userID,msg,allowedStatus);
  }
  def sendEventToActive(accessToken: String, msg: String) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)
    aclSystem.checkAccess(userID, appID, "communicateUser")
    ioManager.sendToClient(userID,msg,Seq(ClientStatus.Active.toString()));
  }
}

trait StubSignalSystem extends SignalSystem {

  def initAction(t: Signal, options: Map[String, String]): SignalResponse = null
  def triggerEvent(t: Signal) = null
  def executeRequest(t: Signal) = null
}


