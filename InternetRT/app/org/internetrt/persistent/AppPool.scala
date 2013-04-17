package org.internetrt.persistent
import org.internetrt.core.model.Application

trait AppPool {
    def installApplication(userID:String, id:String, app:Application):Boolean
	//def getAppOwnerByID(userID:String, id:String):String
	def getApp(userID:String, id:String):Option[Application]
	def getAppIDsByUserID(userID:String):Seq[String]
}

class StubAppPool extends AppPool{
  val innerMap = scala.collection.mutable.Map.empty[String,Application]
  def installApplication(userID:String, id:String, app:Application) = {
    org.internetrt.util.Debuger.debug("[AppPool : installApplication]: "+"Warnning:Using stubapppool! Only one user is valid!")
    org.internetrt.util.Debuger.debug("[AppPool : installApplication]: "+"userID: "+userID);
	org.internetrt.util.Debuger.debug("[AppPool : installApplication]: "+"id: "+id);
    innerMap += (id -> app)
    true
  }
  def getAppOwnerByID(userID:String, id:String)={
	  org.internetrt.util.Debuger.debug("[AppPool : getAppOwnerByID]: "+"userID: "+userID);
	  org.internetrt.util.Debuger.debug("[AppPool : getAppOwnerByID]: "+"id: "+id);
	  getApp(userID, id).get.appOwner
  }
  
  def getApp(userID:String, id:String) = {
      innerMap.get(id) 
  }
  
  def getAppIDsByUserID(userID:String):Seq[String] = {
    innerMap.keys.toSeq
  }
}