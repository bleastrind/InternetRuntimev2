package org.internetrt.driver.userinterface
import play.api.mvc.Controller
import play.api.mvc.Action
import org.internetrt.CONSTS
import org.internetrt.SiteInternetRuntime
import org.internetrt.SiteUserInterface
import org.internetrt.sdk.util.ListenerRequestGenerator
import scala.collection.JavaConversions._
import org.internetrt.sdk.util.RoutingXmlParser
import org.apache.commons.lang.NotImplementedException

object SignalAPI extends Controller {
  def init(signalname: String) = Action {
    request =>
      try {
        if (request.session.get(CONSTS.SESSIONUID) == None)
          Unauthorized("Login Failed")
        else {
          val response = SiteUserInterface.initActionFromUserinterface(
            request.session.get(CONSTS.SESSIONUID).get,
            signalname,
            request.queryString,
            request.queryString.mapValues(seq => seq.headOption.getOrElse("")));

          if (request.queryString.get("format") match {
            case Some(list) => list.head == "browserredirect"
            case _ => false
          }) {
            tryRedirect(request, response)
          } else
            Ok(response.getResponse)
        }
      } catch {
        case e => BadRequest(e.getMessage())
      }
  }

  def initOption(signalname: String) = Action {
    request =>
      try {
        if (request.session.get(CONSTS.SESSIONUID) == None)
          Unauthorized("Login Failed")
        else {
          val response = SiteUserInterface.initActionOptionsFromUserinterface( //TODO  move to interface
            request.session.get(CONSTS.SESSIONUID).get,
            signalname,
            request.queryString,
            request.queryString.mapValues(seq => seq.headOption.getOrElse("")));

          val resultxml = scala.xml.Utility.trim(
            <Options>
              {
                scala.xml.NodeSeq.fromSeq(
                  response.map(entry =>
                    <entry>
                      <key>{ entry._1 }</key>
                      <value>{ entry._2 }</value>
                    </entry>).toSeq)
              }
            </Options>)

          if (request.queryString.get("format") match {
            case Some(list) => list.head == "json"
            case _ => false
          }) {
            import net.liftweb.json._;
            import net.liftweb.json.JsonAST._;
            Ok(Printer.pretty(JsonAST.render(Xml.toJson(resultxml))))
          } else
            Ok(resultxml)
        }
      } catch {
        case e => BadRequest(e.getMessage())
      }
  }

  private def tryRedirect(request: play.api.mvc.Request[play.api.mvc.AnyContent], response: org.internetrt.core.signalsystem.SignalResponse): play.api.mvc.Result = {

    val parser = new RoutingXmlParser(response.getResponse)
    val config = parser.getRequestListener()

    if (parser.getEventListeners().size > 0 || parser.getReqType() != "httpget")
      throw new NotImplementedException("It's only possible when all event listeners are handled and requestListener is httpget")
    else {
      val baseurl = RoutingXmlParser.getListenerUrl(config);
      val params = RoutingXmlParser.getRequiredFormats(config)
        .filter(f => f.kind == "params")
        .headOption match {
          case Some(format) => "?" + ListenerRequestGenerator.generateDataByFormat(
            request.queryString,
            format,
            null)
          case None => ""
        }
      //TODO the data should be Map[String,Map[String]]
      Redirect(baseurl + params)
    };
  }
}