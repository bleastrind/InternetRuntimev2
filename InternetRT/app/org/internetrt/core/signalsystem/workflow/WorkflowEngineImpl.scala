package org.internetrt.core.signalsystem.workflow

import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.model.Routing
import org.internetrt.core.model.RoutingInstance
import org.internetrt.persistent.RoutingInstancePool
import java.util.UUID
import org.internetrt.exceptions.RoutingInstanceInitException

abstract class WorkflowEngineImpl extends WorkflowEngine {

  val routingInstancePool: RoutingInstancePool

  def initWorkflow(userID: String, routings: Seq[Routing], options: Map[String, String]): RoutingInstance = {
    try {

      val newinstance = generateInstanceByRouting(userID, routings, options)

      routingInstancePool.put(newinstance.id, newinstance)
      newinstance
    } catch {
      case e: RoutingInstanceInitException => null;
    }
  }
  
  def checkStatus(routings: Seq[Routing], options: Map[String, String])={
    val routingIndexedRequestListeners = routings
    	.map(r => r.xml \\ "RequestListener"  map (node => (r.xml \ "@id" text ,node)) 
    	).flatten
    if(routingIndexedRequestListeners.length == 0)
      new NoRequestListener()
    else if(routingIndexedRequestListeners.length == 1){
      val selectedRouting = routings.filter( 
        		  r => (r.xml \ "@id" text) == routingIndexedRequestListeners.head._1
          ).head
      val selectedRequestID:String = routingIndexedRequestListeners.head._2 \\ "RequestListener" \ "@id" text;
      OkState(selectedRouting , selectedRequestID )
    }
    else{

      val conflicts = routingIndexedRequestListeners.map(pair=> 
        <Choice><RoutingId>{pair._1}</RoutingId><RequestListenerId>{pair._2 \ "@id" text}</RequestListenerId>{pair._2}</Choice> 
      )
      
      if(options == null || options.get("requestListenerIndex") == None)
        OptionMissingState(Map("requestListenerIndex" -> conflicts));
      else{
	      val conflictChoiece = scala.xml.XML.loadString(options("requestListenerIndex"))
	      val selectedChoice = conflicts.filter(node => 
	        (node \ "RoutingId") == (conflictChoiece \ "RoutingId") && 
	        (node \ "RequestListenerId") == (conflictChoiece \ "RequestListenerId"))
	      if(selectedChoice.size == 1)  {
	        OkState(routings
	            .filter( r=> (r.xml \ "@id" text) == (conflictChoiece \ "RoutingId" text))
	            .head
	            
	            ,  
	            conflictChoiece \ "RequestListenerId" text)
	      }else
	    	  OptionMissingState(Map("requestListenerIndex" -> conflicts))
      }
    }
  }

  def getOkState = {
    
  }
  
  def getRoutingInstaceByworkflowID(workflowID: String): Option[RoutingInstance] = {
    routingInstancePool.get(workflowID)
  }

  def dispatchEvents(workflowID:String, userID:String, routing:Seq[Routing]){
    
  }
  def generateInstanceByRouting(userID: String, routings: Seq[Routing], options: Map[String, String]): RoutingInstance = {

    val xmlrouting =
      <Routing id = "fdsafs">
        <Signal id="1" runat="client">
          <from>client</from>
          <user>u</user>
          <name>share</name>
          <vars>
            <var><key>uri</key></var>
            <var><key>uri2</key></var>
          </vars>
        </Signal>
        <Adapter from="1" to="2">
          <mapper>
            <key from="uri" to="URI"/>
            <value transformer="default"/>
          </mapper>
          <mapper>
            <key from="uri2" to="URI2"/>
            <value transformer="default"/>
          </mapper>
        </Adapter>
        <Adapter from="1" to="3">
          <mapper>
            <key from="uri" to="URI"/>
            <value transformer="default"/>
          </mapper>
          <mapper>
            <key from="uri2" to="URI2"/>
            <value transformer="default"/>
          </mapper>
        </Adapter>
        <RequestListener id="2"  type="httpget" runat="appid" >
    	  <description>hello</description>
          <url>http://safs</url>
          <params>
            <param><key>URI</key><value><var/></value></param>
          </params>
          <headers>
            <header><key>URI2</key><value><var/></value></header>
            <header><key>routingInstanceID</key><value><ID/></value></header>
          </headers>
        </RequestListener>
        <EventListener id="3"  type="httpget" runat="appid" >
    	  <description>hello2</description>
          <url>http://safs</url>
          <params>
            <param><key>URI</key><value><var/></value></param>
          </params>
          <headers>
            <header><key>URI2</key><value><var/></value></header>
            <header><key>routingInstanceID</key><value><ID/></value></header>
          </headers>
        </EventListener>
      </Routing>
      
    val actualRoutings = Seq(Routing(userID,xmlrouting))
    val (requestRouting,requestListenerID) = checkStatus(actualRoutings, options) match{
        case OkState(r,id) => (r,id)
        case OptionMissingState(options) => throw new RoutingInstanceInitException(options)
        case _ => return null 
      }

    val workflowID = UUID.randomUUID().toString()

    val routingInstance = 
      <RoutingInstance>
      <id>{ workflowID } </id>
      {requestRouting.xml \ "Signal"}
      {requestRouting.xml \ "Adapter" filter ( node => (node \ "@to" text) == requestListenerID )}
      {requestRouting.xml \ "RequestListener"}
      
    dispatchEvents(workflowID,userID, routing);
      </RoutingInstance>
/*    val xml =
      <RoutingInstance>
        <id>{ workflowID } </id>
        <Signal id="1" runat="client">
          <from>client</from>
          <user>u</user>
          <name>share</name>
          <vars>
            <var><key>uri</key></var>
            <var><key>uri2</key></var>
          </vars>
        </Signal>
        <Adapter from="1" to="2">
          <mapper>
            <key from="uri" to="URI"/>
            <value transformer="default"/>
          </mapper>
          <mapper>
            <key from="uri2" to="URI2"/>
            <value transformer="default"/>
          </mapper>
        </Adapter>
        <RequestListener id="2" type="httpget" runat="app">
          <url>http://safs</url>
          <params>
            <param><key>URI</key><value><var/></value></param>
          </params>
          <headers>
            <header><key>URI2</key><value><var/></value></header>
            <header><key>routingInstanceID</key><value><ID/></value></header>
          </headers>
        </RequestListener>
      </RoutingInstance>
      */
    new RoutingInstance(userID, routingInstance)
  }
}