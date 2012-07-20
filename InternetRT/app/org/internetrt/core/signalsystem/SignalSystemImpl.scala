package org.internetrt.core.signalsystem
import org.internetrt.persistent._
import org.internetrt.core.signalsystem.workflow._
import org.internetrt.core.I18n
import org.internetrt.core.model.RoutingInstance
import org.internetrt.exceptions.ConfigNotPreparedException

abstract class SignalSystemImpl extends SignalSystem {

  import global.confSystem

  val workflowEngine: WorkflowEngine
  val signalDefinationPool: SignalDefinationPool

  def registerSignal(name: String, xml: scala.xml.Elem) = {
    getSignalDefination(name) match {
      case None => {
        signalDefinationPool.put(name, xml)
        true;
      }
      case _ => false
    }
  }

  def getSignalDefination(name: String) = {
    signalDefinationPool.get(name)
  }

  def initActionOptions(s: Signal, options: Map[String, String]): Map[String, Seq[scala.xml.Node]] = {
    workflowEngine.checkStatus(getRouting(s), options) match {
      case OptionMissingState(s) => s
      case _ => Map.empty
    }
  }

  def initAction(s: Signal, options: Map[String, String]): SignalResponse = {
    try {
      val ins = workflowEngine.initWorkflow(s.user, s.vars, getRouting(s), options)
    		 System.out.println("xml"+ins.toString())
      return new ObjectResponse(ins.xml)
    } catch {
      case e:Exception => {
        //e.printStackTrace()
        new RejectResponse(I18n.REJECT+":"+e.getMessage())
      }
    }
  }
  def triggerEvent(t: Signal) = null
  def executeRequest(t: Signal) = null

  def getRoutingInstaceByworkflowID(workflowID: String): Option[RoutingInstance] = {
    workflowEngine.getRoutingInstaceByworkflowID(workflowID)
  }

  private def getRouting(s: Signal) = {
    assert(s != null)
    val routings = confSystem.getRoutingsBySignal(s)
    System.out.println("[SignalSystemImpl:getRouting]:"+routings)
    System.out.println("[SignalSystemImpl:getRouting]:"+s)
    if(routings == null || routings.size < 1)
      throw new ConfigNotPreparedException("Signal:" + s);
    routings
  }

} 
