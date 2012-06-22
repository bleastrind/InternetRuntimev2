package org.internetrt.core.signalsystem.workflow
import org.internetrt.core.model.Routing
import org.internetrt.core.model.RoutingInstance
import org.internetrt.core.signalsystem.Signal

	trait WorkflowEngine {
		def initWorkflow(userID:String ,routings: Seq[Routing],options:Map[String,String]):RoutingInstance
		
		//def getRoutingInstance(s:Signal):RoutingInstance
		
		def getRoutingInstaceByworkflowID(workflowID:String):Option[RoutingInstance]
	}
