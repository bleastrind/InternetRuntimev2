package org.internetrt.test
import org.specs2.mutable.Specification
import org.internetrt.core.signalsystem.workflow.WorkflowEngineImpl
import org.internetrt.persistent.StubRoutingInstancePool
import org.internetrt.core.model.Routing
import org.internetrt.core.signalsystem.workflow.OptionMissingState
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.internetrt.core.signalsystem.workflow.OkState

@RunWith(classOf[JUnitRunner])
class WorkflowUnitTest extends Specification{
	"The workflow initActionOption" should{
	  "return missingOptions when multi route and no option" in {
	    val workflowEngine = new WorkflowEngineImpl{
	      val routingInstancePool = new StubRoutingInstancePool()
	    }
	    workflowEngine.checkStatus(Seq(rout1,rout2),Map.empty)
	    match{
	      case OptionMissingState(options)=>{
	        val choice1 = <Choice><RoutingId>fdsafs</RoutingId><RequestListenerId>2</RequestListenerId><RequestListener id="2"  type="httpget" runat="appid" >
    	  <description>hello</description>
          <url>http://safs</url>
          <params>
            <param><key>URI</key><value><var/></value></param>
          </params>
          <headers>
            <header><key>URI2</key><value><var/></value></header>
            <header><key>routingInstanceID</key><value><ID/></value></header>
          </headers>
        </RequestListener></Choice>
	          val choice2 = <Choice><RoutingId>12</RoutingId><RequestListenerId>2</RequestListenerId><RequestListener id="2"  type="httpget" runat="appid" >
    	  <description>hello</description>
          <url>http://safs</url>
          <params>
            <param><key>URI</key><value><var/></value></param>
          </params>
          <headers>
            <header><key>URI2</key><value><var/></value></header>
            <header><key>routingInstanceID</key><value><ID/></value></header>
          </headers>
        </RequestListener></Choice>
	        val optionmap = options.getOrElse("requestListenerIndex", Seq.empty)
	        System.out.println(optionmap.head)
	        System.out.println(choice1)
	        optionmap.contains(choice1) and optionmap.contains(choice1)
	    }
	      case _ => failure
	    }
	  }
	  
	  "return OkState when multi route with proper option" in {
	    val workflowEngine = new WorkflowEngineImpl{
	      val routingInstancePool = new StubRoutingInstancePool()
	    }
	    workflowEngine.checkStatus(Seq(rout1,rout2),Map("requestListenerIndex" ->( <Choice><RoutingId>12</RoutingId><RequestListenerId>2</RequestListenerId></Choice>).toString))
	    match{
	      case OkState(r,id)=>{
	        r == rout2 and id == "2"
	      }
	      case _ => failure
	    }
	  }
	}
	
	
	val rout1 = Routing("u", <Routing id = "fdsafs">
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
      </Routing>)
      
      val rout2=Routing("u", <Routing id = "12">
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
      </Routing>)
}