package org.internetrt.persistent;

trait LocalSiblingPool{  
	def addNode(dhtNumber:String,ip:String):Boolean
	def deleteNode(ip:String):Boolean
	// ip -> Token
	def getAll():Seq[(String,String)]
}

class StubLocalSiblingPool extends LocalSiblingPool {  
	import scala.collection.mutable;
	val list:Map[String, String] = Map.empty
	
	def addNode(dhtNumber:String,ip:String):Boolean = {
	  list + (ip -> dhtNumber)
	  true
	}
	//def getAppOwnerByID(userID:String, id:String):String
	def deleteNode(ip:String):Boolean={
	    list - (ip)
	  true
	}
	def getAll():Seq[(String,String)]={
	  list toSeq
	}
}