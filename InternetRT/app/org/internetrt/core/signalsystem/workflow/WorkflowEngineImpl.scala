package org.internetrt.core.signalsystem.workflow

import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.model.Routing
import org.internetrt.core.model.RoutingInstance
import org.internetrt.persistent.RoutingInstancePool
import java.util.UUID

abstract class WorkflowEngineImpl extends WorkflowEngine {

  val routingInstancePool:RoutingInstancePool
  
  def initWorkflow(userID:String ,routings: Seq[Routing],options:Map[String,String]): RoutingInstance = { 
    val newinstance = createInstanceByRouting(userID,routings)
    routingInstancePool.put(newinstance.id,newinstance)
    newinstance
 }

 // def getRoutingInstance(s: Signal): RoutingInstance = { null }

  def getRoutingInstaceByworkflowID(workflowID: String): Option[RoutingInstance] = { 
    routingInstancePool.get(workflowID)
 }

  def createInstanceByRouting(userID:String ,routings:Seq[Routing]):RoutingInstance = {
    val workflowID = UUID.randomUUID().toString()
    
    val signalListeners = routings.map(r => r.xml \\ "signalListener")
    val eventListeners = signalListeners.filter(node => node \ "@type" == "request")
    
    val xmlrouting = 
    <Routing>
    	<signal id="1" runat="client">
         <from>client</from>
         <user>u</user>
         <name>share</name>
         <vars>
         	<var><key>uri</key></var>     
         	<var><key>uri2</key></var>         
         </vars>
        </signal>
        <adapter from="1" to="2">
         <mapper>
         <key from="uri" to="URI"/>
         <value transformer="default"/>
         </mapper>
         <mapper>
         <key from="uri2" to="URI2"/>
         <value transformer="default"/>
         </mapper>
        </adapter>
        <signalListener id="2" type="httpget" runat="app">
         <url>http://safs</url>
         <params>
         	<param><key>URI</key><value><var/></value></param>
         </params>
    	 <headers>
          	<header><key>URI2</key><value><var/></value></header>
            <header><key>routingInstanceID</key><value><ID/></value></header>
         </headers>
        </signalListener>
	</Routing>
    val xml = 
    <RoutingInstance>
    	<id>{ workflowID } </id>
    	<signal id="1" runat="client">
         <from>client</from>
         <user>u</user>
         <name>share</name>
         <vars>
         	<var><key>uri</key></var>     
         	<var><key>uri2</key></var>         
         </vars>
        </signal>
        <adapter from="1" to="2">
         <mapper>
         <key from="uri" to="URI"/>
         <value transformer="default"/>
         </mapper>
         <mapper>
         <key from="uri2" to="URI2"/>
         <value transformer="default"/>
         </mapper>
        </adapter>
        <signalListener id="2" type="httpget" runat="app">
         <url>http://safs</url>
         <params>
         	<param><key>URI</key><value><var/></value></param>
         </params>
    	 <headers>
          	<header><key>URI2</key><value><var/></value></header>
            <header><key>routingInstanceID</key><value><ID/></value></header>
         </headers>
        </signalListener>
	</RoutingInstance>
    new RoutingInstance(userID,xml)
  }
}