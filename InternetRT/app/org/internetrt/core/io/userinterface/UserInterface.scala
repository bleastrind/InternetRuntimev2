package org.internetrt.core.io.userinterface
import org.internetrt.core.security.AuthCenter
import org.internetrt.core.security.AccessControlSystem
import org.internetrt.core.InternetRuntime
import java.util.UUID
import org.internetrt.core.model.Application
import scala.xml.XML
import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.model.Routing

abstract class UserInterface {
  
  val global:InternetRuntime
  import global.authCenter
  import global.aclSystem
  import global.confSystem
  import global.signalSystem
  
  val clientManager = ClientsManager
  
  def register(username: String, password: String): String = {
    if (authCenter.registerUser(username, password)){
	
		val uid = authCenter.login(username, password)
		installRootApp(uid, <?xml version="1.0" encoding="UTF-8"?>
		<Application><Name>jsslimclient</Name><AppID>userinterface</AppID><AppOwner>system</AppOwner><Signals><Event runat="userinterface"><Signalname>clients/pageload</Signalname><Description>Client page load.</Description></Event></Signals><SignalHanlders></SignalHanlders></Application> toString)
		"success" 
	}
		else "failed"
  }
  def login(username: String, password: String): String = {
    authCenter.login(username, password)
  }
  
  def installRootApp(uid:String,xml:String):Boolean = {
	val app = Application(XML.loadString(xml))
	aclSystem.grantAccess(uid,app.id, app.accessRequests,true)
	confSystem.installApp(uid, app)
  }
    def confirmRouting(userID:String, xml: String):Boolean = {
      confSystem.confirmRouting(userID, Routing(userID,scala.xml.XML.loadString(xml)))
  }
    
  def response(uid: String, msg: String, msgID: String) = {
    clientManager.response(uid, msg, msgID)
  }

  def join(uid: String, driver: ClientDriver) = {
    clientManager.join(uid, driver)
  }
  
  def getAuthcodeForServerFlow(appID: String, userID: String, redirect_uri: String): String = {
    if(confSystem.getApp(userID,appID) != None)
    	authCenter.genAuthCode(appID, userID);
    else
      null;
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
  private def initSignalFromUserInterface(userID: String, signalID: String, vars: Map[String, Seq[String]], options: Map[String, String]) = {
    val appID = Signal.FROMUSERINTERFACE
    Signal(signalID, userID, appID, vars)
  }
}