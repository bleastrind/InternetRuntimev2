package org.internetrt.test
import org.specs2.mutable.Specification
import org.internetrt.core.signalsystem.workflow.WorkflowEngineImpl
import org.internetrt.persistent.StubRoutingInstancePool
import org.internetrt.core.model.Routing
import org.internetrt.core.signalsystem.workflow.OptionMissingState
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.internetrt.core.signalsystem.workflow.OkState
import org.internetrt.Cassandra
import org.specs2.mock.Mockito
import org.internetrt.core.InternetRuntime

@RunWith(classOf[JUnitRunner])
class WorkflowUnitTest extends Specification with Mockito{
	"The workflow initActionOption" should{
	  "return missingOptions when multi route and no option" in {
	    val workflowEngine = new WorkflowEngineImpl{
	      val global = mock[InternetRuntime]
	      val routingInstancePool = new StubRoutingInstancePool()
	    }
	    val rs = workflowEngine.checkStatus(Seq(rout1,rout2),Map.empty)
	    System.out.println("[WorkflowUnitTest : WorkflowUnitTest]: "+"workflowEngine.checkStatus: "+rs);
	    rs match{
	      case OptionMissingState(options)=>{
	        val choice1 = scala.xml.Utility.trim(<Choice><RoutingId>0</RoutingId><RequestListenerId>0</RequestListenerId><RequestListener>1</RequestListener></Choice>);
	          val choice2 = <Choice><RoutingId>1</RoutingId><RequestListenerId>0</RequestListenerId><RequestListener>2</RequestListener></Choice>;
	        val optionmap = options.getOrElse("requestListenerIndex", Seq.empty) map (n => n.toString())
	        
	        optionmap.contains(choice1.toString()) and optionmap.contains(choice2.toString())
	    }
	      case _ => failure
	    }
	  }
	  
	  "return OkState when multi route with proper option" in {
	    val workflowEngine = new WorkflowEngineImpl{
	      val global = mock[InternetRuntime]
	      val routingInstancePool = new StubRoutingInstancePool()
	    }
	    workflowEngine.checkStatus(Seq(rout1,rout2),Map("requestListenerIndex" ->( <Choice><RoutingId>1</RoutingId><RequestListenerId>0</RequestListenerId></Choice>).toString))
	    match{
	      case OkState(r,id)=>{
	        r == rout2 and id == (rout2.xml \ "RequestListener" head)
	      }
	      case _ => failure
	    }
	  }
	}
	
	
	val rout1 = Routing("u", <Routing>
        <Signal>
          <from>client</from>
          <name>share</name>
          <vars>
            <var><key>uri</key></var>
            <var><key>uri2</key></var>
          </vars>
        </Signal>
        <RequestListener>1</RequestListener>
        <EventListener type="httpget" runat="appid" >
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
      
      val rout2=Routing("u", <Routing>
        <Signal>
          <from>client</from>
          <name>share</name>
          <vars>
            <var><key>uri</key></var>
            <var><key>uri2</key></var>
          </vars>
        </Signal>
 <RequestListener>2</RequestListener>
        <EventListener  type="httpget" runat="appid" >
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