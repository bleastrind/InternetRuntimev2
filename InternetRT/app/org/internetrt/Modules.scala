package org.internetrt
import org.internetrt.core._
import org.internetrt.core.security._
import org.internetrt.persistent._
import org.internetrt.core.io.IOManagerImpl
import org.internetrt.core.configuration.ConfigurationSystem
import org.internetrt.core.signalsystem.SignalSystemImpl
import org.internetrt.core.signalsystem.workflow.WorkflowEngine
import org.internetrt.core.model.Routing
import org.internetrt.core.model.RoutingInstance
import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.signalsystem.workflow.WorkflowEngineImpl
import org.internetrt.core.configuration.ConfigurationSystemImpl
import org.internetrt.core.io.userinterface.UserInterface
import me.prettyprint.hector.api.Cluster
import me.prettyprint.hector.api.factory.HFactory
import org.internetrt.persistent.cassandra._
import org.internetrt.core.io.userinterface.ClientsManagerImpl
import org.internetrt.core.siblings.ClusterManagerImpl
import org.internetrt.core.io.userinterface.ClusterConsideredClientsManager
import org.internetrt.persistent.cassandra.LocalSiblingCassandraPool
import scala.io.Source
import org.internetrt.core.siblings.TwoTierClusterManagerImpl
import java.util.Properties
import java.io.FileInputStream
import java.io.IOException
import org.internetrt.core.siblings.ClientPusingNodeRefFactoryImpl
import org.internetrt.core.siblings.ClusterSignalingNodeRefFactoryImpl
import org.internetrt.core.siblings.NodeRefFactory

/**
 * This object control all the connections in the website
 */
object SiteInternetRuntime extends InternetRuntime {

  object authCenter extends {
    val global = SiteInternetRuntime.this
  } with CassandraAuthCenter

  object signalSystem extends {
    val global = SiteInternetRuntime.this
  } with CassandraSignalSystem

  object ioManager extends {
    val global = SiteInternetRuntime.this
  } with StandardManager

  object confSystem extends {
    val global = SiteInternetRuntime.this
  } with CassandraConfigurationSystem
  
  object aclSystem extends {
    val global = SiteInternetRuntime.this
  } with CassandraAccessControlSystem

  //test database should change MemoryClusterManagerSystem into CassandraClusterManagerSystem
  object clusterManager extends{
    val global = SiteInternetRuntime.this
  } with MemoryClusterManagerSystem
  
}


trait StandardManager extends IOManagerImpl{
  object clientsManager extends ClientsManagerImpl with ClusterConsideredClientsManager {
    val global = SiteInternetRuntime
  }
}

trait MemoryConfigurationSystem extends ConfigurationSystemImpl {
  object globalAppPool extends StubGlobalAppPool
  object appPool extends StubAppPool
  object routingResourcePool extends MemoryRoutingResourcePool
}

trait MemoryAuthCenter extends AuthCenterImpl {
  object internalUserPool extends StubInternalUserPool
  object accessTokenPool extends StubAccessTokenPool
  object authCodePool extends StubAuthCodePool
  object appOwnerPool extends StubAppOwnerPool
}

trait MemorySignalSystem extends SignalSystemImpl {
  object workflowEngine extends WorkflowEngineImpl {
    val global = SiteInternetRuntime
    object routingInstancePool extends StubRoutingInstancePool
  }
  object signalDefinationPool extends StubSignalDefinationPool
}
trait MemoryAccessControlSystem extends AccessControlSystemImpl{
  object applicationAccessPool extends StubApplicationAccessPool
}


trait RealNodeRefFactoryImpl extends ClientPusingNodeRefFactoryImpl with ClusterSignalingNodeRefFactoryImpl with NodeRefFactory{}

trait MemoryClusterManagerSystem extends TwoTierClusterManagerImpl{
  val localSiblingPool = new StubLocalSiblingPool()
  val globalSiblingPool = new StubGlobalSiblingPool()
  val nodeRefFactory = new{
      val manager = MemoryClusterManagerSystem.this
  } with RealNodeRefFactoryImpl
}

trait CassandraClusterManagerSystem extends TwoTierClusterManagerImpl{
  val localSiblingPool = LocalNodeCassandra.localSiblingPool
  val globalSiblingPool = GlobalNodeCassandra.globalSiblingPool
  val nodeRefFactory = new{
      val manager = CassandraClusterManagerSystem.this
  } with RealNodeRefFactoryImpl
}

trait CassandraConfigurationSystem extends ConfigurationSystemImpl {
  val appPool = Cassandra.appPool
  val routingResourcePool = Cassandra.routingPool
  val globalAppPool = Cassandra.globalAppPool
}

trait CassandraAuthCenter extends AuthCenterImpl {
  val internalUserPool = Cassandra.internalUserPool
  val accessTokenPool = Cassandra.accessTokenPool
  val authCodePool = Cassandra.authCodePool
  val appOwnerPool = Cassandra.appOwnerPool
}

trait CassandraSignalSystem extends SignalSystemImpl {
  object workflowEngine extends WorkflowEngineImpl {
    val global = SiteInternetRuntime
    val routingInstancePool = Cassandra.routingInstancePool
  }
  val signalDefinationPool = Cassandra.signalDefinationPool
}
trait CassandraAccessControlSystem extends AccessControlSystemImpl{
  val applicationAccessPool = Cassandra.applicationAccessPool
}

object LocalNodeCassandra{
  val localCluster = HFactory.getOrCreateCluster("Local Cluster", "127.0.0.1:9162")
  val localSiblingPool = new LocalSiblingCassandraPool(localCluster)
}

object GlobalNodeCassandra{
  val globalCluster = HFactory.getOrCreateCluster("Global Cluster", "127.0.0.1:9161")
  val globalSiblingPool = new GlobalSiblingCassandraPool(globalCluster)
}


object Cassandra{

	val testCluster = HFactory.getOrCreateCluster("Test Cluster", "192.168.3.123:9160")

	val accessTokenPool = new AccessTokenCassandraPool(testCluster)
	val applicationAccessPool = new ApplicationAccessCassandraPool(testCluster)
	val appOwnerPool = new AppOwnerCassandraPool(testCluster)
	val appPool = new AppCassandraPool(testCluster)
	val authCodePool = new AuthCodeCassandraPool(testCluster)
	val internalUserPool = new InternalUserCassandraPool(testCluster)
	val routingPool = new RoutingCassandraPool(testCluster)
	val routingInstancePool = new RoutingInstanceCassandraPool(testCluster)
	val signalDefinationPool = new SignalDefinationCassandraPool(testCluster)
	val globalAppPool = new GlobalAppCassandraPool(testCluster)
}

object SiteUserInterface extends UserInterface {
  val global = SiteInternetRuntime
  val clientsManager = global.ioManager.clientsManager
}

object CONSTS {
  val SESSIONUID = "UID";
  val CLIENTID = "CID";
  val MSGID = "msgID";
  val MSG = "msg";
  val ALLOWEDSTATUS = "allowedStatus";

  val CLIENTSTATUS = "CLIENTSTATUS";
  val ANONYMOUS = "Anonymous";

  val ACCESSTOKEN = "access_token";
  val FROMIP = "fromip";
  
  
}

object Prop{
  val prop = new Properties(); 
    try {
        //load a properties file from class path, inside static method
      val stream = new FileInputStream("../../conf/config.properties")
        prop.load(stream)

        } 
   catch{
     case ex:Exception => ex.printStackTrace()
    }
  
  val ThisIP = prop.getProperty("LocalIP")
  val NodeType = prop.getProperty("NodeType")
  val AgentNode = prop.getProperty("AgentNode")
  val GlobalPort = prop.getProperty("GlobalPort").toInt
  val LocalPort = prop.getProperty("LocalPort").toInt
}
