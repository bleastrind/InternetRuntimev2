package org.internetrt.driver.siblings

import play.api.mvc.Controller
import play.api.mvc.Action
import org.internetrt.CONSTS
import org.internetrt.driver.userinterface.ClientMessageActor
import org.internetrt.driver.userinterface.Client
import java.util.UUID
import org.internetrt.core.io.userinterface.ClientStatus
import scala.concurrent.ExecutionContext.Implicits.global
import org.internetrt.SiteUserInterface
import org.internetrt.core.siblings.NodeRef

object NodeRefReceiver extends Controller {

  def sendevent = Action {
    request =>
      val uid = request.queryString.get(CONSTS.SESSIONUID) match {
        case Some(x :: xs) => x
        case _ => CONSTS.ANONYMOUS
      }
      val msg = request.queryString.get(CONSTS.MSG) match {
        case Some(x :: xs) => x
        case _ => ""
      }
      val allowedStatus = request.queryString.get(CONSTS.ALLOWEDSTATUS).getOrElse(ClientStatus.All.map(_ toString))

      SiteUserInterface.clientsManager.sendevent(uid, msg, allowedStatus)

      Ok
  }

//  def response = Action {
//    request =>
//      val uid = request.queryString.get(CONSTS.SESSIONUID) match {
//        case Some(x :: xs) => x
//        case _ => CONSTS.ANONYMOUS
//      }
//      val msg = request.queryString.get(CONSTS.MSG) match {
//        case Some(x :: xs) => x
//        case _ => ""
//      }
//      val success = request.queryString.get(CONSTS.MSGID) match {
//        case Some(x :: xs) => SiteUserInterface.clientsManager.response(uid, msg, x)
//        case _ => false
//      }
//
//      Ok(success.toString())
//  }

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
      val fromip = request.queryString.get(CONSTS.FROMIP) match {
        case Some(list) => list.head //get the first status
        case None => ClientStatus.Active.toString()
      }
      val driver = new SiblingDriver(uid,
        (msg: String, msgID: Option[String]) => {
          NodeRef.getNode(fromip).sendevent(uid, msg, ClientStatus.All.map(_ toString))
        }, status)

      SiteUserInterface.clientsManager.join(uid, driver)

      Ok
  }

}
