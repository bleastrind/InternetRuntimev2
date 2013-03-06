package org.internetrt.sdk.util
import org.internetrt.sdk.exceptions.FormatErrorException
import org.internetrt.sdk.exceptions.DataNotEnoughException
import scala.util.matching.Regex
import java.util.regex.Pattern


object ListenerRequestGenerator{
  val parser = RoutingXmlParser;
  
  def generateDataByFormat(signalData: java.util.Map[String, String], format:ListenerDataFormat, extData:GlobalData):java.util.Map[String,String]={
    val data = scala.collection.JavaConversions.asMap(signalData).mapValues(v => Seq(v)) 
    val result = generateDataByFormat(data, format.map, extData)
    scala.collection.JavaConversions.mapAsJavaMap(result)
  }
  def generateDataByFormat(signalData: scala.collection.Map[String, Seq[String]], format:ListenerDataFormat, extData:GlobalData):Map[String,String]={
    generateDataByFormat(signalData, format.map, extData) 
  }
  
  def generateDataByFormat(signalData: scala.collection.Map[String, Seq[String]], format: Map[String, DataAdapter],extData:GlobalData): Map[String, String] = {
    try{
      format map (p => (generateDataByAdapter(p._1, signalData, p._2 ,extData)))
    }catch{
      case e:java.util.NoSuchElementException => throw new DataNotEnoughException(format.keys.foldLeft[String]("")((all,t) => all + "," + t))
    }
  }
  
  private def generateDataByAdapter(key:String, data:scala.collection.Map[String, Seq[String]], adapter:DataAdapter, extData:GlobalData)={
    adapter  match {
      case DataAdapter(<value><var/></value>) => (key,data.get(key).head.head)
      case DataAdapter(<value><var><newkey>{newkey}</newkey></var></value>)=>(newkey.text,data.get(key).head.head)
      case DataAdapter(<value><var><ID/></var></value>) => (key,extData.map.get(RoutingInstanceXmlParser.ROUTING_INSTANCE_ID_KEY).get)
      case DataAdapter(<value>{text}</value>) => {
        val m = Pattern.compile("\\{(\\w+)\\}").matcher(text.text)
        val finaltext = new StringBuffer(text.text.length()+30) 
        while(m.find){
          val newdata = data.get(m.group(1)) match{
            case Some(realdata) => realdata.head        
            case None => m.group(0)
          }
          m.appendReplacement(finaltext,newdata)
        }
        m.appendTail(finaltext)
        (key, finaltext.toString())
      }
      case DataAdapter(x) => {
          System.out.println("Not supported format:"+x)
    	  throw new FormatErrorException("Format Error!"+x)
      }
    }
  }
}
class ListenerRequestGenerator{}


