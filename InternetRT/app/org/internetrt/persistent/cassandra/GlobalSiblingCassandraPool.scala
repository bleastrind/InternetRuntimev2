package org.internetrt.persistent.cassandra

import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import org.internetrt.persistent.AppPool
import me.prettyprint.hector.api.factory.HFactory
import org.internetrt.core.model.Application
import me.prettyprint.cassandra.service.template.ThriftSuperCfTemplate
import me.prettyprint.hector.api.ddl.ComparatorType
import me.prettyprint.cassandra.service.ThriftKsDef
import java.util.Arrays
import java.nio.ByteBuffer
import me.prettyprint.hector.api.exceptions.HectorException
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate
import org.internetrt.util.Debuger
import org.internetrt.persistent.LocalSiblingPool
import scala.collection.mutable.ListBuffer
import org.internetrt.persistent.GlobalSiblingPool

//unfinished

//TODO

class GlobalSiblingCassandraPool(cluster: Cluster)
  extends GlobalSiblingPool {
  val keyspacename = "GlobalSibling_Node"
  val cfname = "GlobalSibling_Token"
  var template: ThriftColumnFamilyTemplate[String, String] = null
  def KeySerializer = StringSerializer.get()
  def ValueSerializer = StringSerializer.get();
  init()
  def init() {
    try {
      val keyspaceDef = cluster.describeKeyspace(keyspacename);
      if (keyspaceDef == null)
        createSchema()

      val keyspace = HFactory.createKeyspace(keyspacename, cluster);

      template = new ThriftColumnFamilyTemplate[String, String](keyspace,
        cfname,
        StringSerializer.get(),
        StringSerializer.get());
    } catch {
      case e: Exception => Debuger.error("[AppCasandraPool]ConnectionFailed" + e)
    }
  }

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



  def addNode(DHTNumber:String,nodeName: String, ip: String, ipAlternative: String): Boolean = {
    val updater = template.createUpdater("siblingNodes")
    updater.setValue(nodeName, DHTNumber.toString()+","+ip+","+ipAlternative, StringSerializer.get())
    
    try {
      template.update(updater)
      true
    } catch {
      case e: HectorException => {
        Debuger.error("[GlobalSibingCassandraPool]Connection Failed!" + e)
        e.printStackTrace()
        false //TODO handle exception ...
      }
      case _: Throwable => false
    }
  }
  def deleteNode(nodeName: String): Boolean = {
    
    val updater = template.createUpdater("siblingNodes")
    updater.deleteColumn(nodeName)
    true
  }
  def getAll(): Seq[(String, (String,String,String))] = {
    val res = template.queryColumns("siblingNodes")
    val keyList = res.getColumnNames().toArray().toList
    val listBuffer = new ListBuffer[(String, (String,String,String))]()
    for (key <- keyList) {
      val vals = res.getColumn(key.toString()).toString().split(",")
      val values:(String,String,String) = (vals(0),vals(1),vals(2))
      listBuffer += ((key.toString(), values))
    }
    listBuffer.toSeq
  }
}
