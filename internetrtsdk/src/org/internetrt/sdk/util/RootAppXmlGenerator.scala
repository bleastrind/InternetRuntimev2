package org.internetrt.sdk.util
import scala.collection.JavaConverters.AsScala
import scala.collection.JavaConverters$

class RootAppXmlGenerator (rootApplication : RootApplication){
  
  
  def generateRootApp() = {
    val accessRequest = List.fromArray(rootApplication.accessRequests.toArray())
	 val result = accessRequest.map{(accessRequest) =>
	  <AccessRequest>{accessRequest}</AccessRequest>
	} 
	val accessRequests = scala.xml.NodeSeq.fromSeq(result)
	
	(<Application><Name>{rootApplication.name}</Name><AppID>{rootApplication.ID}</AppID><AccessRequests>{accessRequests}</AccessRequests></Application>).toString()
  }
}