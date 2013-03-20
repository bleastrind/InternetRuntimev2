package org.internetrt.core.io.siblings
import org.internetrt.sdk.util.HttpHelper
import scala.concurrent.Future
import scala.concurrent.future
import scala.concurrent.ExecutionContext.Implicits.global

class NodeRef(ip:String) {
  def response(uid: String, msg: String, msgID: String) = {
    future{
	  HttpHelper.httpClientGet(construct("/siblings/response",List("uid" -> uid , "msg" -> msg , "msgID" -> msgID)))
    }
  }
  
  def longpulling(uid:String,cid:String,status:String) = {
    future{
      HttpHelper.httpClientGet(construct("/siblings/longpulling", List("uid" -> uid , "cid" -> cid , "status" -> status)))
    }
  }
  
  def sendevent(uid: String, msg: String, allowedStatus: Seq[String]):Future[String] = {
	  future{
	    HttpHelper.httpClientGet(
	      construct("/siblings/sendevent",List("uid" -> uid , "msg" -> msg) ::: allowedStatus.map(("allowedStatus",_)).toList))
	  }
  }
  def ask(uid: String, msg: String, allowedStatus: Seq[String]): Future[String] = {
      future{
	  	  HttpHelper.httpClientGet(construct("/siblings/ask",List("uid" -> uid , "msg" -> msg) ::: allowedStatus.map(("allowedStatus",_)).toList))
      }
  }
  
  private def construct(action:String,params: List[(String,String)]) = {
    val parmstrs =  params.map( pair => pair._1 + "=" + pair._2)
    "http://" + ip + action + "?" + parmstrs.mkString("&")
  }
}