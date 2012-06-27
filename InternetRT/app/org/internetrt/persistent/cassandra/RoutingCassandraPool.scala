package org.internetrt.persistent.cassandra
import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import java.nio.ByteBuffer
import scala.xml.NodeSeq
import org.internetrt.persistent.ApplicationAccessPool
import org.internetrt.core.model.Routing
import org.internetrt.persistent.RoutingResourcePool
import me.prettyprint.hector.api.factory.HFactory
import me.prettyprint.hector.api.ddl.ComparatorType
import me.prettyprint.cassandra.service.ThriftKsDef
import java.util.Arrays
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate
import me.prettyprint.hector.api.query.SliceQuery
import me.prettyprint.cassandra.service.ColumnSliceIterator
import me.prettyprint.hector.api.beans.DynamicComposite
import me.prettyprint.cassandra.serializers.DynamicCompositeSerializer
import org.internetrt.core.signalsystem.Signal
import me.prettyprint.hector.api.exceptions.HectorException

class RoutingCassandraPool(cluster: Cluster)
  extends RoutingResourcePool {
  
  val keyspacename = "InternetRT_Personal"
  val cfname = "Routing"
  
val keyspaceDef = cluster.describeKeyspace(keyspacename);
  if (keyspaceDef == null)
    createSchema()

  def createSchema() {
    val cfDef = HFactory.createColumnFamilyDefinition(keyspacename,
      cfname,
      ComparatorType.BYTESTYPE);

    val newKeyspace = HFactory.createKeyspaceDefinition(keyspacename,
      ThriftKsDef.DEF_STRATEGY_CLASS,
      1,
      Arrays.asList(cfDef));
    
    
    // Add the schema to the cluster.
    // "true" as the second param means that Hector will block until all nodes see the change.
    cluster.addKeyspace(newKeyspace, true);
  }

  val keyspace = HFactory.createKeyspace(keyspacename, cluster);
  val compTemplate  = new ThriftColumnFamilyTemplate[String, Routing](keyspace,
                cfname, StringSerializer.get(), RoutingSerializer);

  def getRoutingsBySignal(signal: Signal): Seq[Routing] = {
	val query = HFactory.createSliceQuery(keyspace, StringSerializer.get(),RoutingSerializer, StringSerializer.get())
	 .setColumnFamily(cfname)
	 .setKey(getKey(signal))
	 .setRange(null,null,false, 10000);

	val iterator =  new ColumnSliceIterator[String,Routing,String](query, null, new ColumnSliceIterator.ColumnSliceFinish[Routing]{
	  def function = null
	}, false);
	
	val scalaseq = scala.collection.JavaConverters.asScalaIteratorConverter(iterator).asScala.toSeq
	scalaseq map (column => column.getName())
  }
  def saveRouting(r: Routing) = {
	val key = getKey(r)
    val updater = compTemplate.createUpdater(key);
	
	updater.setValue(r,"",StringSerializer.get())
	try {
      compTemplate.update(updater)
      true
    } catch {
      case e: HectorException => false //TODO handle exception ...
      case _ => false
    }
  }
  
  def getKey(signal:Signal)=signal.user+signal.id+signal.from
  def getKey(r:Routing) = {
    val id = r.xml \ "Signal" \\ "name" text;
	var from = r.xml \ "Signal" \\ "from" text;
	r.userID + id + from
	}
  
    object RoutingSerializer extends AbstractSerializer[Routing]{
    
    def fromByteBuffer(buffer:ByteBuffer):Routing={
      val xml = scala.xml.XML.loadString(new String(buffer.array()));
      Routing(xml \ "userID" text, scala.xml.XML.load(xml \ "xml" toString))
    }
    
    def toByteBuffer(value:Routing):ByteBuffer={
      val xml =scala.xml.Utility.trim(
          <v>
    		  <userID>{value.userID}</userID>
          <xml>{value.xml}</xml>
    		  </v>
          ).toString()
      ByteBuffer.wrap(xml.getBytes())
    }
  }
}