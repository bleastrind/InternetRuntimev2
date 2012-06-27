package org.internetrt.persistent
import org.internetrt.core.model.Application

trait AppPool {
    def installApplication(userID:String, id:String, app:Application)
	//def getAppOwnerByID(userID:String, id:String):String
	def getApp(userID:String, id:String):Option[Application]
	def getAppIDsByUserID(userID:String):Seq[String]
}

class StubAppPool extends AppPool{
  val innerMap = scala.collection.mutable.Map.empty[String,Application]
  def installApplication(userID:String, id:String, app:Application) = {
    System.out.println("Warnning:Using stubapppool! Only one user is valid!")
    System.out.println(userID);
	System.out.println(id);
    innerMap += (id -> app)
  }
  def getAppOwnerByID(userID:String, id:String)={
	  System.out.println(userID);
	  System.out.println(id);
	  getApp(userID, id).get.appOwner
  }
  
  def getApp(userID:String, id:String) = {
      innerMap.get(id) 
  }
  
  def getAppIDsByUserID(userID:String):Seq[String] = {
    innerMap.keys.toSeq
  }
}