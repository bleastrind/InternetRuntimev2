package org.internetrt.test
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2._
import org.specs2.specification._
import org.internetrt.core.InternetRuntime
import org.internetrt.core.signalsystem.SignalSystem
import org.internetrt.core.io._
import org.internetrt.core.security._
import org.internetrt.core.configuration._
import org.internetrt.MemoryAuthCenter
import org.specs2.execute.Result
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.internetrt.persistent.AppPool
import org.internetrt.persistent.StubInternalUserPool
import org.internetrt.persistent.StubAccessTokenPool
import org.internetrt.persistent.StubAuthCodePool
import org.internetrt.core.io.userinterface.UserInterface
import org.internetrt.core.io.userinterface.ClientDriver
import akka.dispatch.Future
import akka.dispatch.Await
import org.internetrt.persistent.StubAppOwnerPool

@RunWith(classOf[JUnitRunner])
class UserInterfaceSpec extends Specification with Mockito {
  override def is =
    "This is a specification to check the userinterface works well" ^
      "A userinterface can register a new user" ! register ^
      "Login with username and password" ^ login ^
      "Then keep the connection which will give answer to server directly" ^ connect ^ end ^
      "Somebody ask from internal system" ^ ask ^
      "And it receive the answer"^	check ^end

  object TestEnvironment extends InternetRuntime {
    val signalSystem = mock[SignalSystem]

    object authCenter extends {
      val global = TestEnvironment.this

    } with AuthCenterImpl {
      object internalUserPool extends StubInternalUserPool
      object accessTokenPool extends StubAccessTokenPool
      object authCodePool extends StubAuthCodePool
      object appOwnerPool extends StubAppOwnerPool

    }

    object ioManager extends {
      val global = TestEnvironment.this
    } with IOManagerImpl
    val confSystem = mock[ConfigurationSystem]
    val aclSystem = mock[AccessControlSystem]
  }

  object TestUserInterface extends UserInterface {
    val global = TestEnvironment
  }
  def register = {
    TestUserInterface.register("user", "pass") match {
      case "success" => success
      case _ => failure
    }
  }
  object login extends Given[String] {
    def extract(text: String): String = {
      TestUserInterface.login("user", "pass")
    }
  }

  object connect extends Then[String] {
    var Userid:String=""
    object stubclientdriver extends ClientDriver {
      def response(data: String, msgID: Option[String] = None) {
        // The stub client will response the ask directly
        TestUserInterface.response(Userid, "received", msgID.get)
      }
    }
    def extract(uid: String, text: String) = {
      
      connect.Userid = uid
      TestUserInterface.join(uid, stubclientdriver)
      success
    }
  }

  object ask extends Given[Future[String]] {
    def extract(text: String) = {
      TestEnvironment.ioManager.readFromClient(connect.Userid,"please tell me received")
    }
  }
  
  object check extends Then[Future[String]]{
    def extract(s: Future[String], text:String) = {
      Await.result(s,akka.util.Duration.Inf) === "received"
    }
  }
}