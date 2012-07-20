package org.internetrt.core.signalsystem.workflow

import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.model.Routing
import org.internetrt.core.model.RoutingInstance
import org.internetrt.persistent.RoutingInstancePool
import java.util.UUID
import org.internetrt.exceptions.RoutingInstanceInitException
import org.internetrt.sdk.util.ListenerConfig
import org.internetrt.sdk.util.ListenerRequestGenerator
import org.internetrt.sdk.exceptions.DataNotEnoughException
import org.internetrt.sdk.util.GlobalData
import org.internetrt.sdk.util.RoutingXmlParser

abstract class WorkflowEngineImpl extends WorkflowEngine {

  import global.ioManager

  val routingInstancePool: RoutingInstancePool

  def initWorkflow(userID: String, vars: Map[String, Seq[String]], routings: Seq[Routing], options: Map[String, String]): RoutingInstance = {
    try {

      val newinstance = generateInstanceByRouting(userID, vars, routings, options)
      System.out.println("The Routing Instance:" + newinstance)
      routingInstancePool.put(newinstance.id, newinstance)
      newinstance
    } catch {
      case e: RoutingInstanceInitException => null;
    }
  }

  def checkStatus(routings: Seq[Routing], options: Map[String, String]) = {
    val routingIndexedRequestListeners = routings
      .map(r => r.xml \\ "RequestListener" map (node => (r.xml \ "@id" text, node))).flatten
    if (routingIndexedRequestListeners.length == 0)
      NoRequestListener(routings.head.xml \ "Signal" head)
    else if (routingIndexedRequestListeners.length == 1) {
      val selectedRouting = routings.filter(
        r => (r.xml \ "@id" text) == routingIndexedRequestListeners.head._1).head
      val selectedRequestID: String = routingIndexedRequestListeners.head._2 \\ "RequestListener" \ "@id" text;
      OkState(selectedRouting, selectedRequestID)
    } else {

      val conflicts = routingIndexedRequestListeners.map(pair =>
        <Choice><RoutingId>{ pair._1 }</RoutingId><RequestListenerId>{ pair._2 \ "@id" text }</RequestListenerId>{ pair._2 }</Choice>)

      if (options == null || options.get("requestListenerIndex") == None)
        OptionMissingState(Map("requestListenerIndex" -> conflicts));
      else {
        val conflictChoiece = scala.xml.XML.loadString(options("requestListenerIndex"))
        val selectedChoice = conflicts.filter(node =>
          (node \ "RoutingId") == (conflictChoiece \ "RoutingId") &&
            (node \ "RequestListenerId") == (conflictChoiece \ "RequestListenerId"))
        if (selectedChoice.size == 1) {
          OkState(routings
            .filter(r => (r.xml \ "@id" text) == (conflictChoiece \ "RoutingId" text))
            .head,
            conflictChoiece \ "RequestListenerId" text)
        } else
          OptionMissingState(Map("requestListenerIndex" -> conflicts))
      }
    }
  }

  def getOkState = {

  }

  def getRoutingInstaceByworkflowID(workflowID: String): Option[RoutingInstance] = {
    routingInstancePool.get(workflowID)
  }

  def tryEventListener(workflowID: String, vars: Map[String, Seq[String]], uid: String, config: ListenerConfig) = {
    try {
      System.out.println("ConfigXML:"+config.node)
      val url = ListenerRequestGenerator.generateSignalListenerUrl(vars, config, GlobalData(Map(RoutingXmlParser.ROUTING_INSTANCE_ID_KEY -> workflowID)))
      ioManager.sendToUrl(uid, url, null)
      None
    } catch {
      case e: DataNotEnoughException => Some(config)
    }
  }

  def dispatchEvents(workflowID: String, vars: Map[String, Seq[String]], userID: String, routings: Seq[Routing]) = {
    System.out.println("Dispatching");
    val eventListenerNodes = routings map (r => r.xml \ "EventListener" toSeq) flatten;
    val eventListenerConfigs = eventListenerNodes map (node => ListenerConfig(node));
    val leftEventHandlers = eventListenerConfigs map (config => tryEventListener(workflowID, vars, userID, config)) flatten;
    leftEventHandlers map (config => config.node)
  }
  def generateInstanceByRouting(userID: String, vars: Map[String, Seq[String]], routings: Seq[Routing], options: Map[String, String]): RoutingInstance = {

    val actualRoutings = if (routings == null) Seq.empty else routings

    val workflowID = UUID.randomUUID().toString()
    
    val (requestRouting, requestListenerID) = checkStatus(actualRoutings, options) match {
      case OkState(r, id) => (r, id)
      case OptionMissingState(options) => throw new RoutingInstanceInitException(options)
      case NoRequestListener(r) => {
        val routingInstance =
          <RoutingInstance>
            <id>{ workflowID }</id>
            {r}
            { scala.xml.NodeSeq.fromSeq(dispatchEvents(workflowID, vars, userID, routings)) }
          </RoutingInstance>

        return new RoutingInstance(userID, routingInstance)
      }
      case _ => return null
    }

    val routingInstance =
      <RoutingInstance>
        <id>{ workflowID }</id>
        { requestRouting.xml \ "Signal" }
        { requestRouting.xml \ "Adapter" filter (node => (node \ "@to" text) == requestListenerID) }
        { requestRouting.xml \ "RequestListener" }
        { scala.xml.NodeSeq.fromSeq(dispatchEvents(workflowID, vars, userID, routings)) }
      </RoutingInstance>

    new RoutingInstance(userID, routingInstance)
  }
}