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
import org.internetrt.persistent.AppOwnerPool
import org.internetrt.persistent.StubInternalUserPool
import org.internetrt.persistent.StubAccessTokenPool
import org.internetrt.persistent.StubAuthCodePool
import org.internetrt.persistent.StubAppOwnerPool
import org.internetrt.util.Encrypt
import org.internetrt.core.io.userinterface.UserInterface

@RunWith(classOf[JUnitRunner])
class OAuthServerSpec extends Specification with Mockito {
  override def is =
    """This is a specification to check the oauth server works well""" ^
      p ^
      "Preparation" ^
      """The application should registered as "appid" with secret "secret"  """ ! install ^
      //      "[Function]notify the intereptors"                                     ! e1^
      //      "[Safe]start with 'Hello'"                                             ! e2^
      //      "[Performance]end with 'world'"                                        ! e3^
      //                                                                                p^
      "The client should" ^
      "Get the auth code first with appID:${appid} and userID:${userid}" ^ (auth) ^
      "ask access code with auth code and appID:${appid},appSec:${secret}" ^ (access) ^
      "access resource with access code" ^ (request) ^
      end
  //"Verify the Preparation"													! verify

  //    def e1 = "Hello world" must have size(11)
  //    def e2 = "Hello world" must startWith("Hello")
  //    def e3 = "Hello world" must endWith("world")

  object TestEnvironment extends InternetRuntime {
    val signalSystem = mock[SignalSystem]

    object authCenter extends {
      val global = TestEnvironment.this

    } with AuthCenterImpl {
      object internalUserPool extends StubInternalUserPool
      object accessTokenPool extends StubAccessTokenPool
      object authCodePool extends StubAuthCodePool
      val appOwnerPool = mock[AppOwnerPool]
      override def getUserIDAppIDPair(accessToken: String): (String, String) = {
        super.getUserIDAppIDPair(accessToken)
      }
    }

    val aclSystem = mock[AccessControlSystem]
    val ioManager = mock[IOManager]
    val confSystem = mock[ConfigurationSystem]

  }
  object TestUserInterface extends UserInterface {
    val global = TestEnvironment
  }
  def install = {
    val confSystem = TestEnvironment.confSystem
    TestEnvironment.authCenter.appOwnerPool.get("appid") returns Some(Encrypt.encrypt("secret"))
    success
  }

  object auth extends Given[String] {
    def extract(text: String): String = {
      val (a, u) = extract2(text)

      TestUserInterface.getAuthcodeForServerFlow(a, u, "http")
    }
  }
  object access extends When[String, (AccessToken, String)] {
    def extract(p: String, text: String) = {
      val (a, s) = extract2(text)
      (TestEnvironment.getAccessTokenByAuthtoken(a, p, s), s)
    }
  }
  object request extends Then[(AccessToken, String)] {
    def extract(p: (AccessToken, String), text: String): Result = {

      val (user, app) = TestEnvironment.authCenter.getUserIDAppIDPair(p._1.value);
      val uid = TestEnvironment.getUserIDByAccessToken(p._1.value)
      app === "appid" and user === "userid" and user === uid

    }
  }

}