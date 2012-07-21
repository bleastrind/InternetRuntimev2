package org.internetrt.persistent.cassandra
import org.internetrt.persistent.GlobalAppPool
import org.internetrt.core.model.Application
import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
class GlobalAppCassandraPool(cluster:Cluster)  extends GlobalAppPool{
  val innerPool = new GlobalAppCassandraStoragePool(cluster)
  def put(k: String, v: Application) = {
	  innerPool.put(k,v.xml.toString())
  }
  def get(k: String): Option[Application] = {
	  innerPool.get(k) map (s => Application(scala.xml.XML.loadString(s)))
  }
} 

private[cassandra] class GlobalAppCassandraStoragePool(cluster:Cluster) extends KeyValueCassandraPool[String,String](cluster,"InternetRT_Global","GlobalApp"){
  def KeySerializer = StringSerializer.get() 
  def ValueSerializer = StringSerializer.get() 
}
 