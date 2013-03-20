package org.internetrt.driver.siblings

import play.api.mvc.Controller
import play.api.mvc.Action
import org.internetrt.CONSTS
import org.internetrt.driver.userinterface.ClientMessageActor
import org.internetrt.driver.userinterface.Client
import java.util.UUID
import org.internetrt.core.io.userinterface.ClientStatus

import scala.concurrent.ExecutionContext.Implicits.global

object ClientRequest extends Controller {


  def response = Action{
    request=>
      val uid = request.queryString.get(CONSTS.SESSIONUID) match{
        case Some(x::xs) => x
        case _ => CONSTS.ANONYMOUS
      }
      val msg = request.queryString.get(CONSTS.MSG) match{
        case Some(x::xs) => x
        case _ => ""
      }
      val success = request.queryString.get(CONSTS.MSGID) match{ 
        case Some(x::xs) => ClientMessageActor.clientsManager.response(uid,msg,x)
        case _ => false
      }
      
      Ok(success.toString())      
  }

  def longpolling = Action{
    request =>
      val uid = request.queryString.get(CONSTS.SESSIONUID) match {
        case Some(list)=>list.head //get the first client id
        case None=> CONSTS.ANONYMOUS // else a new one
      }
      val cid = request.queryString.get(CONSTS.CLIENTID) match {
        case Some(list)=>list.head //get the first client id
        case None=> UUID.randomUUID().toString() // else a new one
      }
      val status = request.queryString.get(CONSTS.CLIENTSTATUS) match{
        case Some(list)=>list.head //get the first status
        case None=> ClientStatus.Active.toString()
      }
      Async{
        Client.getLongPoolingResult(uid,cid,status).map(Ok(_)) 
      }
  }
  
}
