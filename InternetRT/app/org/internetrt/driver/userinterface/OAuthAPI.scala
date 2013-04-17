package org.internetrt.driver.userinterface
import play.api.mvc.Controller
import play.api.mvc.Action
import org.internetrt.CONSTS
import org.internetrt.SiteInternetRuntime
import org.internetrt.SiteUserInterface
import play.api.templates.Html
import org.internetrt.exceptions.ApplicationNotInstalledException
import org.internetrt.exceptions.ConsideredException

object OAuthAPI extends Controller {

  def install() = Action {
    request =>
      try {
        org.internetrt.util.Debuger.debug("[OAuthAPI:install]:" + request.session)

        request.session.get(CONSTS.SESSIONUID) match {
          case Some(userID) => {

            val appID = request.body.asFormUrlEncoded.get.get("appID").get.head;
            val appxml = request.body.asFormUrlEncoded.get.get("appxml").get.head;
            val redirect_uri = request.body.asFormUrlEncoded.get.get("redirect_uri").get.head;
            val accesses = request.body.asFormUrlEncoded.get.get("accesses").getOrElse(Seq.empty);
            if (SiteUserInterface.installApp(userID, appxml, accesses, false)) {
              val code = SiteUserInterface.getAuthcodeForServerFlow(appID, userID, redirect_uri);
              Redirect(redirect_uri + "?code=" + code + accesses.foldLeft("")((res, i) => res + "&accesses=" + i));
            } else
              Ok("Install failed!")
          }
          case None => {
            Ok(views.html.login(request.uri,"login first"))
          }
        }
      } catch {
        case e:ConsideredException => BadRequest(e.toString());
      }
  }

  def authorize() = Action {
    request =>
      try {
        org.internetrt.util.Debuger.debug("[OAuthAPI:authorize]:" + request.session)

        request.session.get(CONSTS.SESSIONUID) match {
          case Some(userID) => {

            val appID = request.queryString.get("appID").get.head;
            val redirect_uri = request.queryString.get("redirect_uri").get.head;
            try {
              val code = SiteUserInterface.getAuthcodeForServerFlow(appID, userID, redirect_uri);
              org.internetrt.util.Debuger.debug("Redirect URL:" + redirect_uri);
              Redirect(redirect_uri + "?code=" + code + "&msg=success");
              //Ok(Html("<a href='http://localhost:9001'>back to old url</a>"))
            } catch {
              case e: ApplicationNotInstalledException => {
                val app = SiteUserInterface.queryApp(appID)
                if(app == null)
                  Redirect(redirect_uri + "?msg=AppNotRegistered");
                else if(app.isRoot)
					Redirect(redirect_uri + "?msg=RootAppMustInstallFirst");
				else
                  Ok(views.html.auth(app, redirect_uri))
              }
            }

          }
          case None => {
            Ok(views.html.login(request.uri,"login first"))
          }
        }
      } catch {
        case e:ConsideredException => BadRequest(e.toString());
      }
  }
}