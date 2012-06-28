package org.internetrt.test
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.internetrt.Cassandra
import org.internetrt.core.model.Application
import org.internetrt.core.model.Routing
import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.model.RoutingInstance
@RunWith(classOf[JUnitRunner])
class CassandraDBUnitTest extends Specification{
	"The internalUserPool DB" should{
		"be able to put&get" in {
		  Cassandra.internalUserPool.put("uid",("213","231")) 
		  val get = Cassandra.internalUserPool.get("uid")
		  get.get == ("213","231")
		}
	}
	"The AppPool DB" should{
		"be able to put&get" in {
		  Cassandra.appPool.installApplication("uid","id",Application(<A/>))
		  Cassandra.appPool.getApp("uid","id").get == Application(<A/>)
		}
	}	
	"The RoutingInstancePool DB" should{
		"be able to put&get" in {
		  Cassandra.routingInstancePool.put("uid",RoutingInstance("uid", <RoutingInstance/>))
		  val res = Cassandra.routingInstancePool.get("uid").get
		  res == RoutingInstance("uid", <RoutingInstance/>)
		}
	}
	"The RoutingPool DB" should{
		"be able to put&get" in {
		  Cassandra.routingPool.saveRouting(Routing("user", <Routing><Signal><name>id</name><from>from</from></Signal></Routing>))
		  val s = Cassandra.routingPool.getRoutingsBySignal(Signal("id","user","from",null))
		  s.contains(Routing("user", <Routing><Signal><name>id</name><from>from</from></Signal></Routing>))
		}
	}
	
	
}