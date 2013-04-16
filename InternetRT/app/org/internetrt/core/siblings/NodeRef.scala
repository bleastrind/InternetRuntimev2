package org.internetrt.core.siblings
import org.internetrt.sdk.util.HttpHelper
import scala.concurrent.Future
import scala.concurrent.future
import scala.concurrent.ExecutionContext.Implicits.global
import org.internetrt.CONSTS
import java.net.URLEncoder

class NodeRef(ip: String, port:String = "80") {
  

  def IP = ip
  def response(uid: String, msg: String, msgID: String) {

      HttpHelper.httpClientGet(construct("/siblings/response", List("uid" -> uid, "msg" -> msg, "msgID" -> msgID)))
  
  }

  def join(uid: String,status: String) {
  
      HttpHelper.httpClientGet(construct("/siblings/join", List(CONSTS.SESSIONUID -> uid, CONSTS.CLIENTSTATUS -> status)))
    
  }

  def sendevent(uid: String, msg: String, allowedStatus: Seq[String]) {

      HttpHelper.httpClientGet(
        construct("/siblings/sendevent", List(CONSTS.SESSIONUID -> uid, CONSTS.MSG -> msg) ::: allowedStatus.map((CONSTS.ALLOWEDSTATUS, _)).toList))
    
  }
  def ask(uid: String, msg: String, allowedStatus: Seq[String]):Future[String] = {
    future{
      HttpHelper.httpClientGet(construct("/siblings/ask", List(CONSTS.SESSIONUID -> uid, CONSTS.MSG -> msg) ::: allowedStatus.map((CONSTS.ALLOWEDSTATUS, _)).toList))
    }
  }

  private def construct(action: String, params: List[(String, String)]) = {
    val parmstrs = ((CONSTS.FROMIP, CONSTS.ThisIP) :: params).map(pair => pair._1 + "=" + pair._2)
    "http://" + IP + ":" + port    + action + "?" + URLEncoder.encode(parmstrs.mkString("&"),"UTF-8")
  }
}

object NodeRef {
  var nodeCache: Map[String, NodeRef] = Map.empty
  def getNode(ip: String): NodeRef = {
    nodeCache.get(ip) match {
      case Some(node) => node
      case None => {
        val node = new NodeRef(ip,"9000")
        nodeCache += (ip -> node)
        node
      }
    }
  }
}