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

class AppCassandraPool(cluster: Cluster) extends AppPool {
  val keyspacename = "InternetRT_Personal"
  val cfname = "App"
  var template: ThriftColumnFamilyTemplate[String, String] = null
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
      case e: Exception => System.out.println(e)
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
  
  
  def installApplication(userID: String, id: String, app: Application) = {
    val updater = template.createUpdater(userID)
    updater.setValue(id, scala.xml.Utility.trim(app.xml).toString(), StringSerializer.get())
    try {
      template.update(updater)
      true
    } catch {
      case e: HectorException => {
        e.printStackTrace()
        false //TODO handle exception ...
      }
      case _ => false
    }
  }
  def getApp(userID: String, id: String): Option[Application] ={
    val res = template.queryColumns(userID)
    val all = getAppIDsByUserID(userID)
    System.out.println("getAppIDsByUserID:"+all(2))
    if (res.hasResults()){
      val appxml = res.getString(id)
      System.out.println("xml:"+appxml);
      Some(Application(scala.xml.XML.loadString(appxml)))
      
    }
    else
      None
  }
  def getAppIDsByUserID(userID: String): Seq[String] = {
    val res = template.queryColumns(userID)
    scala.collection.JavaConversions.collectionAsScalaIterable(res.getColumnNames()) toSeq
  }
}