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
  def getAuthcodeForServerFlow(appID: String, userID: String, redirect_uri: String): String = {
    authCenter.genAuthCode(appID, userID);

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
      case _ => false
    }
  }
  
  def getSignalDefination(name:String):String = {
    signalSystem.getSignalDefination(name).toString
  }

  def triggerEventFromUserInterface(userID: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val signal = initSignalFromUserInterface(userID, signalID, vars, options)
    signalSystem.triggerEvent(signal)
  }
  def executeRequestFromUserInterface(userID: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val signal = initSignalFromUserInterface(userID, signalID, vars, options)
    signalSystem.executeRequest(signal)
  }
  def initActionFromUserinterface(userID: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val signal = initSignalFromUserInterface(userID, signalID, vars, options)
    signalSystem.initAction(signal, options)
  }

  def initActionOptionsFromUserinterface(userID: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]): Map[String, Seq[scala.xml.Node]] = {
    val signal = initSignalFromUserInterface(userID, signalID, vars, options)
    signalSystem.initActionOptions(signal, options)
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
  private def initSignalFromUserInterface(userID: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val appID = Signal.FROMUSERINTERFACE
    Signal(signalID, userID, appID, vars)
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

  def confirmRouting(accessToken: String, xml: String) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)

    if (aclSystem.isRoot(userID, appID)) {

      confSystem.confirmRouting(userID, Routing(userID,scala.xml.XML.load(xml)))
    }
  }

  def getApplications(accessToken: String) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)
    if (aclSystem.checkAccess(userID, appID, "getApplications"))
      confSystem.getAppIDs(userID);
    else
      null
  }

  def getApplicationDetail(id: String, accessToken: String) = {
    val (userID, appID) = authCenter.getUserIDAppIDPair(accessToken)
    aclSystem.checkAccess(userID, appID, "getApplications");
    confSystem.getApp(userID, id) match {
      case Some(app) => app
      case _ => null
    }
  }
}

trait StubSignalSystem extends SignalSystem {

  def initAction(t: Signal, options: Map[String, String]): SignalResponse = null
  def triggerEvent(t: Signal) = null
  def executeRequest(t: Signal) = null
}


