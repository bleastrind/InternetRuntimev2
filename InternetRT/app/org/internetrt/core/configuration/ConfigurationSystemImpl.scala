package org.internetrt.core.configuration
import org.internetrt.persistent.AppPool
import org.internetrt.core.model._
import org.internetrt.persistent.RoutingResourcePool
import org.internetrt.core.signalsystem.Signal

abstract class ConfigurationSystemImpl extends AnyRef 
  with ConfigurationSystem{
  
  import global.ioManager
  
  val appPool:AppPool
  val routingResourcePool:RoutingResourcePool
  
  def confirmRouting(userID:String,r:Routing)={
    //val requests = r.xml \ "requests"
    routingResourcePool.saveRouting(r)
  }
  def getRoutingsBySignal(signal:Signal)={
    routingResourcePool.getRoutingsBySignal(signal)
  }
  
  def installApp(userID:String, app:Application):Boolean={
    appPool.installApplication(userID, app.id,app) //TODO check app format is right
  }

  def getAppIDs(userID:String):Seq[String]={
    appPool.getAppIDsByUserID(userID)
  }
  def getApp(userID:String, id:String):Option[Application] ={
    appPool.getApp(userID, id)
  }
}