package org.internetrt.core.signalsystem
import org.internetrt.persistent._
import org.internetrt.core.signalsystem.workflow._
import org.internetrt.core.I18n
import org.internetrt.core.model.RoutingInstance


  abstract class SignalSystemImpl extends SignalSystem{
  
  import global.confSystem
    
  val workflowEngine:WorkflowEngine
  //val routingResourcePool:RoutingResourcePool
  
    //private def getRoutingInstance(s:Signal) = workflowEngine.getRoutingInstance(s)
    
    private def getRouting(s:Signal)={
      val signalID = s.from+s.user+s.id
      confSystem.getRoutingsBySignal(signalID)
    }
    
    def initAction(s:Signal,options:Map[String,String]):SignalResponse ={
	  try{
		  val ins = workflowEngine.initWorkflow(s.user,getRouting(s),options)

	      return new ObjectResponse(ins)
	  }catch{
	    case _ => return new RejectResponse(I18n.REJECT)
	  }
    }
	def triggerEvent(t:Signal)=null
	def executeRequest(t:Signal)=null
//    def handleSignal(s:Signal):SignalResponse={
//      if(getRoutingInstance(s) == null){
//        getRouting(s) match{
//          case Some(r) => workflowEngine.initWorkflow(r)
//          case None => return new RejectResponse(I18n.REJECT)
//        }
//      }
//      val ins = workflowEngine.getRoutingInstance(s)
//      return new ObjectResponse(ins)
//    }
//    
//    def getHeadResponse(s:Signal):SignalResponse={
//      val ins = getRoutingInstance(s)
//      if(ins == null){
//        return new ObjectResponse(getRouting(s))
//      }else
//    	return new ObjectResponse(ins)
//    }
    
    def getRoutingInstaceByworkflowID(workflowID:String):Option[RoutingInstance]={
      workflowEngine.getRoutingInstaceByworkflowID(workflowID)
    }
  } 
