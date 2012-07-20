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
import me.prettyprint.cassandra.serializers.LongSerializer
import java.util.Date

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
  val compTemplate = new ThriftColumnFamilyTemplate[String, String](keyspace,
    cfname, StringSerializer.get(), StringSerializer.get());

  def getRoutingsBySignal(signal: Signal): Seq[Routing] = {
    val key = getKey(signal)
    System.out.println("[RoutingCassandraPool:get]"+key)
    val query = HFactory.createSliceQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
      .setKey(key)
      .setColumnFamily(cfname)
      .setRange(null, null, false, 1000)
    //val iterator =  new ColumnSliceIterator[String,String,Routing](query, null, "\uFFFF", false);

    val scalaseq = scala.collection.JavaConversions.collectionAsScalaIterable(query.execute().get().getColumns()).toSeq
    scalaseq map (column => RoutingSerializer.fromString(column.getName()))
  }
  def saveRouting(r: Routing) = {
    val key = getKey(r)
    System.out.println("[RoutingCassandraPool:save]"+key);
    val updater = compTemplate.createUpdater(key);

    updater.setValue(RoutingSerializer.toString(r), "", StringSerializer.get())
    try {
      compTemplate.update(updater)
      true
    } catch {
      case e: HectorException => false //TODO handle exception ...
      case _ => false
    }
  }

  def getKey(signal: Signal) = signal.user + signal.id + signal.from
  def getKey(r: Routing) = {
    val id = r.xml \ "Signal" \\ "name" text;
    var from = r.xml \ "Signal" \\ "from" text;
    r.userID + id + from
  }

  object RoutingSerializer {

    def fromString(str: String): Routing = {
      try {
        val xml = scala.xml.XML.loadString(str)
        val uid = xml \ "userID" text
        val xmlstr = xml \ "Routing" toString;
        Routing(uid, scala.xml.XML.loadString(xmlstr))
      } catch {
        case e => {
          e.printStackTrace()
          throw e
        }
      }
    }

    def toString(value: Routing): String = {
      val xml = scala.xml.Utility.trim(<v>
                                         <userID>{ value.userID }</userID>
                                         { value.xml }
                                       </v>)
      xml toString
    }

  }
}