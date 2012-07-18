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
  } with IOManagerImpl

  object confSystem extends {
    val global = SiteInternetRuntime.this
  } with CassandraConfigurationSystem
  
  object aclSystem extends {
    val global = SiteInternetRuntime.this
  } with CassandraAccessControlSystem

}
trait MemoryConfigurationSystem extends ConfigurationSystemImpl {
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

trait CassandraConfigurationSystem extends ConfigurationSystemImpl {
  val appPool = Cassandra.appPool
  val routingResourcePool = Cassandra.routingPool
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

object Cassandra{
	val testCluster = HFactory.getOrCreateCluster("Test Cluster", "192.168.3.145:9160")
	
	val accessTokenPool = new AccessTokenCassandraPool(testCluster)
	val applicationAccessPool = new ApplicationAccessCassandraPool(testCluster)
	val appOwnerPool = new AppOwnerCassandraPool(testCluster)
	val appPool = new AppCassandraPool(testCluster)
	val authCodePool = new AuthCodeCassandraPool(testCluster)
	val internalUserPool = new InternalUserCassandraPool(testCluster)
	val routingPool = new RoutingCassandraPool(testCluster)
	val routingInstancePool = new RoutingInstanceCassandraPool(testCluster)
	val signalDefinationPool = new SignalDefinationCassandraPool(testCluster)
}

object SiteUserInterface extends UserInterface {
  val global = SiteInternetRuntime
}

object CONSTS {
  val SESSIONUID = "UID";
  val CLIENTID = "CID";
  val MSGID = "msgID";

  val CLIENTSTATUS = "CLIENTSTATUS";
  val ANONYMOUS = "Anonymous";

  val ACCESSTOKEN = "access_token";

}
