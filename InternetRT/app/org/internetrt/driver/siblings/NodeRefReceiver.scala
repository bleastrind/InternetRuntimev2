package org.internetrt.driver.siblings

import play.api.mvc.Controller
import play.api.mvc.Action
import org.internetrt.CONSTS
import org.internetrt.driver.userinterface.ClientMessageActor
import org.internetrt.driver.userinterface.Client
import java.util.UUID
import org.internetrt.core.io.userinterface.ClientStatus
import scala.concurrent.ExecutionContext.Implicits.global
import org.internetrt._
import org.internetrt.core.siblings.NodeRef
import org.internetrt.util.Debuger

object NodeRefReceiver extends Controller {

  def sendevent = Action {
    request =>
	  val uid = request.queryString.get(CONSTS.SESSIONUID) match {
        case Some(list) => list.head
        case None => CONSTS.ANONYMOUS
      }
      val msg = request.queryString.get(CONSTS.MSG) match {
        case Some(list) => list.head
        case None => ""
      }
      val allowedStatus = request.queryString.get(CONSTS.ALLOWEDSTATUS).getOrElse(ClientStatus.All.map(_ toString))

      Debuger.debug("[NodeRefReciever:sendevent]uid="+uid);
      SiteUserInterface.clientsManager.sendevent(uid, msg, allowedStatus)

      Ok
  }

  def response = Action {
    request =>
      val uid = request.queryString.get(CONSTS.SESSIONUID) match {
        case Some(list) => list.head
        case None => CONSTS.ANONYMOUS
      }
      val msg = request.queryString.get(CONSTS.MSG) match {
        case Some(list) => list.head
        case None => ""
      }
      val fromip = request.queryString.get(CONSTS.FROMIP) match {
        case Some(list) => list.head //get the first status
        case None => ClientStatus.Active.toString()
      }

      val success = request.queryString.get(CONSTS.MSGID) match {
        case Some(list) => SiteUserInterface.clientsManager.response(uid, msg, list.head)
        case None => false
      }
      Ok(success.toString())
  }

  def ask = Action {
    request =>
      val uid = request.queryString.get(CONSTS.SESSIONUID) match {
        case Some(list) => list.head
        case None => CONSTS.ANONYMOUS
      }
      val msg = request.queryString.get(CONSTS.MSG) match {
        case Some(list) => list.head
        case None => ""
      }

      val allowedStatus = request.queryString.get(CONSTS.ALLOWEDSTATUS).getOrElse(ClientStatus.All.map(_ toString))

      val fromip = request.queryString.get(CONSTS.FROMIP) match {
        case Some(list) => list.head //get the first status
        case None => ClientStatus.Active.toString()
      }

      val answer = SiteUserInterface.clientsManager.ask(uid, msg, allowedStatus)

      Async {
        answer.map(Ok(_))
      }
  }
  def join = Action {
    request =>
      val uid = request.queryString.get(CONSTS.SESSIONUID) match {
        case Some(list) => list.head //get the first client id
        case None => CONSTS.ANONYMOUS // else a new one
      }
      val status = request.queryString.get(CONSTS.CLIENTSTATUS) match {
        case Some(list) => list.head //get the first status
        case None => ClientStatus.Active.toString()
      }
      val fromIP = request.queryString.get(CONSTS.FROMIP) match {
        case Some(list) => list.head
        case None => throw new Exception("The First node should not be joined by other node")
      }
      val driver = SiblingDriver.activeNewNode(uid,
        (msg: String, msgID: Option[String]) => {
          val requireNode = SiteInternetRuntime.clusterManager.getNodeRefByIP(fromIP)
          requireNode.joincallback( uid, msg, Seq(status))
        }, status)

      Debuger.debug("[NodeRefReciever:join]uid="+uid);
      SiteUserInterface.clientsManager.join(uid, driver)

      Ok
  }
  
  def joincallback = Action{
    request=>
      val uid = request.queryString.get(CONSTS.SESSIONUID) match {
        case Some(list) => list.head
        case None => CONSTS.ANONYMOUS
      }
      val msg = request.queryString.get(CONSTS.MSG) match {
        case Some(list) => list.head
        case None => ""
      }
      val status = request.queryString.get(CONSTS.ALLOWEDSTATUS) match {
        case Some(list) => list //get the first status
        case None => Seq(ClientStatus.Active.toString())
      }
     
      Debuger.debug("[NodeRefReciever:joincallback]uid="+uid);
      SiteUserInterface.clientsManager.joincallback(uid,msg,status)
      

      Ok
  }

}
