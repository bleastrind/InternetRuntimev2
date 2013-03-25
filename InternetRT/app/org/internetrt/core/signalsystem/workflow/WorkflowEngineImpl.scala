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
import org.internetrt.sdk.util.ListenerDataFormat
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.internetrt.sdk.util.HttpHelper
import org.internetrt.exceptions.InputFormatErrorException
import org.internetrt.sdk.util.RoutingInstanceXmlParser

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
    case class RoutingListenerLocator(routingIndex: Int, routing: Routing, listenerIndex: Int, listenernode: scala.xml.Node);
    val routingListenerLocators = routings.zipWithIndex.map {
      routingIndexpair =>
        (routingIndexpair._1.xml \\ "RequestListener" zipWithIndex) map {
          listenerIndexpair =>
            RoutingListenerLocator(
              routingIndexpair._2,
              routingIndexpair._1,
              listenerIndexpair._2,
              listenerIndexpair._1)
        }
    }.flatten
    if (routingListenerLocators.length == 0)
      NoRequestListener(routings.head.xml \ "Signal" head)
    else if (routingListenerLocators.length == 1) {
      val selectedRouting = routingListenerLocators.head.routing
      val requestListener = routingListenerLocators.head.listenernode
      OkState(selectedRouting, requestListener)
    } else {

      val conflicts = routingListenerLocators.map(locator =>
        <Choice><RoutingId>{ locator.routingIndex }</RoutingId><RequestListenerId>{ locator.listenerIndex }</RequestListenerId>{ locator.listenernode }</Choice>)

      if (options == null || options.get("requestListenerIndex") == None)
        OptionMissingState(Map("requestListenerIndex" -> conflicts));
      else {
        try {
          val conflictChoiece = scala.xml.XML.loadString(options("requestListenerIndex"))
          val routingIndex = (conflictChoiece \ "RoutingId" text).toInt
          val listenerIndex = (conflictChoiece \ "RequestListenerId" text).toInt

          val selectedRouting = routings(routingIndex)
          OkState(selectedRouting, (selectedRouting.xml \ "RequestListener")(listenerIndex))
        } catch {
          case e: IndexOutOfBoundsException => throw new InputFormatErrorException("Index is not consistency with the request")
          case _:Throwable => throw new InputFormatErrorException("requestListenerIndex Received:"+options("requestListenerIndex")+"\nBut Need format like:<Choice><RoutingId>1</RoutingId><RequestListenerId>0</RequestListenerId></Choice>")
        }
      }
    }
  }

  def getOkState = {

  }

  def getRoutingInstaceByworkflowID(workflowID: String): Option[RoutingInstance] = {
    routingInstancePool.get(workflowID)
  }

  def tryEventListener(workflowID: String, vars: Map[String, Seq[String]], uid: String, config: ListenerConfig): Option[ListenerConfig] = {
    try {
      System.out.println("ConfigXML:" + config.node)
      val formats: Seq[ListenerDataFormat] = RoutingXmlParser.getRequiredFormats(config)

      val paramdata = formats.map(format => {
        if (format.kind == "params" || format.map.size == 0)
          ListenerRequestGenerator.generateDataByFormat(vars, format, GlobalData(Map(RoutingInstanceXmlParser.ROUTING_INSTANCE_ID_KEY -> workflowID)))
        else
          return Some(config)
      }).headOption.getOrElse(Map.empty[String, String]);

      val params = HttpHelper.generatorParamString(scala.collection.JavaConversions.mapAsJavaMap(paramdata));
      val url = RoutingXmlParser.getListenerUrl(config) + "?" + params;

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
      case NoRequestListener(signal) => {
        val routingInstance = RoutingInstanceXmlParser.createXml(
            workflowID,
            signal,
            scala.xml.NodeSeq.fromSeq(dispatchEvents(workflowID, vars, userID, routings)))
 

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