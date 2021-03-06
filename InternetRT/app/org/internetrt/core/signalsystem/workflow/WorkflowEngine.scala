package org.internetrt.core.signalsystem.workflow
import org.internetrt.core.model.Routing
import org.internetrt.core.model.RoutingInstance
import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.InternetRuntime
trait CheckedStatus{}
case class NoRequestListener(signal:scala.xml.Node) extends CheckedStatus{}
case class OkState(requestRouting:Routing, requestListener:scala.xml.Node) extends CheckedStatus{}
case class OptionMissingState(options:Map[String,Seq[scala.xml.Node]]) extends CheckedStatus{}

	trait WorkflowEngine {
		val global:InternetRuntime
	  
		def initWorkflow(userID:String ,vars:Map[String,Seq[String]],routings: Seq[Routing],options:Map[String,String]):RoutingInstance
		
		def checkStatus(routings: Seq[Routing], options: Map[String, String]):CheckedStatus
		
		def getRoutingInstaceByworkflowID(workflowID:String):Option[RoutingInstance]
	}
