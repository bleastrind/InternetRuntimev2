package org.internetrt.sdk.util
import scala.xml.XML$
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.internetrt.sdk.exceptions.FormatErrorException

object RoutingXmlParser{
	
	//-->instance
	//def ROUTING_INSTANCE_ID_KEY = "r_ID"
	def HASHKEY = "hashdata"


	  
    def getTo(listener:ListenerConfig ): String = {
	  val signalListener = listener.node;
	  val requestType = (signalListener \ "@runat").toString();
	  FormatErrorException.checkNonEmptyTerm(requestType, "signalListener.@runat")
	  return requestType
	}
    
	
	def getListenerType(listener:ListenerConfig): String = {
	  val signalListener = listener.node;
	  val requestType = signalListener \ "@type";
	  requestType.toString();
	}
	
	def getListenerUrl(listener:ListenerConfig): String = {
	  val signalListener = listener.node;
	  val requestUrl = (signalListener \ "URL").text;
	  FormatErrorException.checkNonEmptyTerm(requestUrl, "signalListener.URL")
	  requestUrl.toString();
	}
	
	def getParamsAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	   paramsAdapter(listener)
	}	
	
	def paramsAdapter(listener:ListenerConfig): Map[String,DataAdapter] = {
	   val signalListener = listener.node;
	   val params = signalListener \ "Adapter" \"params" \ "param" map{(param)=>
	      ( param \ "key" text )-> DataAdapter(param \ "value" head)
	   }
	   Map(params:_*);
	}
	
	def getHeadersAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	  headersAdapter(listener)
	}
	def headersAdapter(listener:ListenerConfig): Map[String,DataAdapter] = {
	   val signalListener = listener.node;
	   val params = signalListener \ "Adapter" \"headers" \ "header" map{(header)=>
	     ( header \ "key" text )-> DataAdapter(header \ "value" head)
	   }
	   Map(params:_*);
	} 
	
	def getAnchorAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	  anchorAdapter(listener)
	}
	def anchorAdapter(listener:ListenerConfig): Map[String,DataAdapter] = {
	   val signalListener = listener.node;
	   System.out.println(signalListener)
	   val params = signalListener \ "Adapter" \"anchor" map{(header)=>
	     ( HASHKEY )-> DataAdapter(header \ "value" head)
	   }
	   Map(params:_*);
	} 
	
	def getBodyAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	   bodyAdapter(listener)
	}	     
	   
	def bodyAdapter(listener:ListenerConfig): Map[String,DataAdapter] = {
	   val signalListener = listener.node;
	   val params =signalListener \ "Adapter" \"body" map{(header)=>
	     ( header \ "key" text )-> DataAdapter(header \ "value" head)
	   }
	   Map(params:_*);
	}	  
	
	def getRequiredFormats(listener:ListenerConfig):java.util.List[ListenerDataFormat] = {
	  Seq(("params",paramsAdapter(listener)) , 
	      ("headers",headersAdapter(listener)) ,
	      ("body",bodyAdapter(listener)),
	      ("anchor",anchorAdapter(listener))) map {
	    p => ListenerDataFormat(p._1,p._2)
	  }
	}
}

class RoutingXmlParser(xml:String)  {
 
     val xmlFile = scala.xml.XML.loadString(xml);
     //--->instance
     /*
     def getExtData():GlobalData = {
       GlobalData(Map(RoutingXmlParser.ROUTING_INSTANCE_ID_KEY -> (xmlFile \ "id" head).text ))
     }
     */

    def getFrom(): String = {
      val signal = xmlFile \ "signal";
	  val from = (signal \ "from").text;
	  FormatErrorException.checkNonEmptyTerm(from,"signal.from")
	  return from.toString();
    }
	
	def getParamsAdapter(): java.util.Map[String,DataAdapter] = {
	   RoutingXmlParser.getParamsAdapter(getRequestListener)
	}
	
	def getHeadersAdapter(): java.util.Map[String,DataAdapter] = {
	   RoutingXmlParser.getHeadersAdapter(getRequestListener)
	}
	   
	def getBodyAdapter(): java.util.Map[String,DataAdapter] = {
	   RoutingXmlParser.getBodyAdapter(getRequestListener)
	}	
	
	def paramsAdapter() = {
	   RoutingXmlParser.paramsAdapter(getRequestListener)
	}
	
	def headersAdapter() = {
	   RoutingXmlParser.headersAdapter(getRequestListener)
	}
	   
	def bodyAdapter()= {
	   RoutingXmlParser.bodyAdapter(getRequestListener)
	}


	   def getRequestListener():ListenerConfig = {
	   try{
	     ListenerConfig(xmlFile\ "RequestListener" head)
	     }catch{
	        case _ => null
	     }
	   }
	   
	   def getEventListeners():java.util.List[ListenerConfig] = {
	     val listeners = xmlFile\ "EventListener" map (node => ListenerConfig(node));
	     scala.collection.JavaConversions.seqAsJavaList(listeners)
	   }

	   //--->instance 
	   /*
	  def getRoutingInstanceId () ={
		 
	    val routingInstanceId = ( xmlFile \ "id").text
	    throwFormatException(routingInstanceId, "routingInstanceId")
	    routingInstanceId
	   }
	   */
}
