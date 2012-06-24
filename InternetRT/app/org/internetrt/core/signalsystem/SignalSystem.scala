package org.internetrt.core.signalsystem
import org.internetrt.core.InternetRuntime
import org.internetrt.core.model.RoutingInstance


  trait SignalSystem{
  
    val global:InternetRuntime
    
    def initAction(t:Signal,options:Map[String,String]):SignalResponse
    def initActionOptions(s:Signal,options:Map[String,String]):Map[String,Seq[scala.xml.Node]]
    //def getHeadResponse(t:Signal):SignalResponse
    def triggerEvent(t:Signal):Any
    def executeRequest(t:Signal):Any
    
    def getRoutingInstaceByworkflowID(workflowID:String):Option[RoutingInstance]
  }
