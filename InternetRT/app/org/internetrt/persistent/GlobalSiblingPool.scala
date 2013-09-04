package org.internetrt.persistent;

trait GlobalSiblingPool{  
	
	def addNode(DHTNumber:String,nodeName:String, ip:String, ipAlternative:String):Boolean
	def deleteNode(nodeName:String):Boolean
	/**
	 * Seq[domain,(token,ip,ipAlternative)]
	 */
	def getAll():Seq[(String,(String,String,String))]
}

class StubGlobalSiblingPool extends GlobalSiblingPool {  
	import scala.collection.mutable;
	val list:Map[String, (String,String,String)] = Map.empty

	def addNode(DHTNumber:String,nodeName:String, ip:String, ipAlternative:String):Boolean = {
	  
	  list + (nodeName->(DHTNumber,ip,ipAlternative))
	  true
	}
	def deleteNode(nodeName:String):Boolean={
	    list - (nodeName)
	  true
	}
	def getAll():Seq[(String,(String,String,String))]={
	  list toSeq
	}
}