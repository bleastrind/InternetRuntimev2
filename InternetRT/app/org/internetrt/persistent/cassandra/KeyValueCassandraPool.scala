package org.internetrt.persistent.cassandra
import org.internetrt.persistent.KeyValueResourcePool
import me.prettyprint.hector.api._
import me.prettyprint.hector.api.factory.HFactory
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition
import me.prettyprint.hector.api.ddl.KeyspaceDefinition
import me.prettyprint.hector.api.ddl.ComparatorType
import me.prettyprint.cassandra.service.ThriftKsDef
import java.util.Arrays
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.hector.api.exceptions.HectorException
abstract class KeyValueCassandraPool[K, V](cluster: Cluster, keyspacename: String, cfname: String) extends KeyValueResourcePool[K, V] {

  def KeySerializer: Serializer[K]
  def ValueSerializer: Serializer[V]

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

  val template = new ThriftColumnFamilyTemplate[K, String](keyspace,
    cfname,
    KeySerializer,
    StringSerializer.get());

  def put(k: K, v: V) = {
    val updater = template.createUpdater(k);
    updater.setValue("value", v, ValueSerializer);
    try {
      template.update(updater)
      true
    } catch {
      case e: HectorException => false //TODO handle exception ...
      case _ => false
    }
  }
  def get(k: K): Option[V] = {
    try {
      val res = template.queryColumns(k);
      if (res.hasResults())
        Some(ValueSerializer.fromBytes(res.getByteArray("value")))
      else
        None;
    } catch {
      case e: HectorException => None //TODO handle exception ...
    }
  }
}