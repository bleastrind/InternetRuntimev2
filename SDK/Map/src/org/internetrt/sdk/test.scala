package org.internetrt.sdk
import org.internetrt.sdk.util.RoutingXmlParser
import org.internetrt.sdk.util.AppXmlParser
import org.internetrt.sdk.util.RoutingGenerator

object test {

  def main(args: Array[String]): Unit = {
//   test RoutingXmlParser
    val routingXmlParser = new RoutingXmlParser
    val url = routingXmlParser.getReqUrl();
    var requestType = routingXmlParser.getReqType()
    var requstfrom = routingXmlParser.getFrom()
    var requetTo = routingXmlParser.getTo()
    val paramsFormat = routingXmlParser.getParamsFormat()
    println(paramsFormat)
    
    //test RoutingGenerator
    val signal = "share"
    val from = "weiAppID"
    val to = "renrenAppID"
    val user = "userAccessToken" 
    
    val routingGenerator = new RoutingGenerator
    val routingXml = routingGenerator.generateRouting(signal,from,to,user)
    //println(routingXml)
    
    //test appXmlParser
    val xmlFile = scala.xml.XML.loadFile("renrenApplication.xml").toString();
    val appXmlParser = new AppXmlParser(xmlFile)
    val urlApp = appXmlParser.getUrl("/signal/share")
//    println(urlApp)
    
  }

}