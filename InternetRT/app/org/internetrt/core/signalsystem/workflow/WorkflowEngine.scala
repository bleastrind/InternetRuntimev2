package org.internetrt.core.signalsystem.workflow
import org.internetrt.core.model.Routing
import org.internetrt.core.model.RoutingInstance
import org.internetrt.core.signalsystem.Signal
trait CheckedStatus{}
class NoRequestListener extends CheckedStatus{}
case class OkState(requestRouting:Routing, requestListenerID:String) extends CheckedStatus{}
case class OptionMissingState(options:Map[String,Seq[scala.xml.Node]]) extends CheckedStatus{}

	trait WorkflowEngine {
		def initWorkflow(userID:String ,vars:Map[String,Seq[String]],routings: Seq[Routing],options:Map[String,String]):RoutingInstance
		
		def checkStatus(routings: Seq[Routing], options: Map[String, String]):CheckedStatus
		
		def getRoutingInstaceByworkflowID(workflowID:String):Option[RoutingInstance]
	}
