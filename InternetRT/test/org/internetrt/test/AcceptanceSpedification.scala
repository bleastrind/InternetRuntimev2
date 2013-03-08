package org.internetrt.test
import org.specs2.mutable.Specification
import org.specs2._
import org.specs2.specification._
import org.specs2.mock.Mockito
import org.internetrt.core._
import org.internetrt.core.signalsystem.SignalSystem
import org.internetrt.core.security.AuthCenter
import org.internetrt.core.io.IOManager
import org.internetrt.core.configuration.ConfigurationSystem
import org.internetrt.MemorySignalSystem
import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.signalsystem.SignalResponse
import org.internetrt.MemoryAuthCenter
import org.internetrt.core.signalsystem.ObjectResponse
import org.internetrt.core.signalsystem.ObjectResponse
import org.internetrt.core.model.RoutingInstance
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.internetrt.MemoryConfigurationSystem
import org.internetrt.core.model.Application
import org.internetrt.core.security.AccessControlSystem
import org.internetrt.CassandraAuthCenter
import org.internetrt.CassandraConfigurationSystem
import org.internetrt.CassandraSignalSystem
import org.internetrt.core.model.Routing
import org.internetrt.core.io.userinterface.UserInterface

@RunWith(classOf[JUnitRunner])
class SignalSpedification extends Specification with Mockito {
  override def is =
    "This is a specification to check the signal dispatching works well" ^
      p ^
      "Preparation" ^
      """The application should registered as "appid" with secret "secret"  """ ! install1 ^
      """The application2 should registered as "appid2" with secret "secret2"  """ ! install2 ^
      """A routing should be set between application and application2 """ ! setrouting ^
      p ^
      "The sip type signal should" ^
      "Given the sip request" ^ (request) ^
      "Receive a sip response and sendTo another client" ^ (sip) ^
      "Ask accesstoken and userID from routinginstance id" ^ (requesttoclient) ^
      "Received the result" ^ (data) ^
      end

  object TestEnvironment extends InternetRuntime {
    object signalSystem extends {
      val global = TestEnvironment.this
    } with MemorySignalSystem

    object authCenter extends {
      val global = TestEnvironment.this

    } with MemoryAuthCenter

    object confSystem extends {
      val global = TestEnvironment.this
    } with MemoryConfigurationSystem

    val ioManager = mock[IOManager]
    val aclSystem = mock[AccessControlSystem]
  }

  object TestUserInterface extends UserInterface {
    val global = TestEnvironment
  }

  var appid = ""
  var appsec = ""
  var appid2 = ""
  var appsec2 = ""
  def install1 = {
    println("-2");
    success
  }
  def install2 = {
    println("-1");

    success
  }

  def setrouting = {
    println("0");

    success
  }

  object request extends Given[SignalResponse] {
    def extract(text: String): SignalResponse = {
      val (id, sec) = TestEnvironment.authCenter.registerApp("secrecret")
      appid = id
      appsec = sec
      TestEnvironment.confSystem.installApp("user", Application(<AppID>{ id }</AppID>))

      val (id2, sec2) = TestEnvironment.authCenter.registerApp("secrret2")
      appid2 = id2
      appsec2 = sec2
      TestEnvironment.confSystem.installApp("user", Application(<AppID>{ id }</AppID>))

      TestEnvironment.confSystem.confirmRouting("user", Routing("user", <Routing><Signal><name>signalname</name><from>{ appid }</from></Signal><RequestListener id="1"/></Routing>))

      println("1");
      val code = TestUserInterface.getAuthcodeForServerFlow(appid, "user", "http")
      val accessToken = TestEnvironment.getAccessTokenByAuthtoken(appid, code, appsec)
      TestEnvironment.initActionFromThirdPart(accessToken.value, "signalname", null, null) // head response return the routing

    }
  }
  object sip extends When[SignalResponse, (String, String)] {
    def extract(p: SignalResponse, text: String): (String, String) = {
      println("2");
      val data = "";
      val routingInstaceID = p.asInstanceOf[ObjectResponse].asObject[scala.xml.Elem] \ "id" text;
      (data, routingInstaceID)
    }
  }
  object requesttoclient extends When[(String, String), String] {
    def extract(p: (String, String), text: String) = {
      println("3");
      val authcode = TestEnvironment.getAuthcodeForActionFlow(appid2, appsec2, p._2);
      val accesstoken = TestEnvironment.getAccessTokenByAuthtoken(appid2, authcode, appsec2);
      TestEnvironment.getUserIDByAccessToken(accesstoken.value) // execute return the routing instace
    }
  }
  object data extends Then[String] {
    def extract(p: String, text: String) = p === "user"
  }
}