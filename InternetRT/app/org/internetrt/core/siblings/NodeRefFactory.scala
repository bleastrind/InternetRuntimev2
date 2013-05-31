package org.internetrt.core.siblings
import org.internetrt.driver.siblings.SocketNodeRefReceiver

trait NodeRefFactory extends ClientPusingNodeRefFactory with ClusterSignalingNodeRefFactory{
   init()
}

trait ClientPusingNodeRefFactory{
  def getNodeRef(ip:String):NodeRef
}

trait ClusterSignalingNodeRefFactory{
	def init()
	def getSocketNodeRef(ip:String,port:Int):SocketNodeRef
}

trait ClientPusingNodeRefFactoryImpl extends ClientPusingNodeRefFactory{
    var ClientPusingNodeCache: Map[String, NodeRef] = Map.empty
  def getNodeRef(ip: String): NodeRef = {
    ClientPusingNodeCache.get(ip) match {
      case Some(node) => node
      case None => {
        val node = new NodeRef(ip,"9000")
        ClientPusingNodeCache += (ip -> node)
        node
      }
    }
  }
}
	
trait ClusterSignalingNodeRefFactoryImpl extends ClusterSignalingNodeRefFactory{
  val manager:TwoTierClusterManagerImpl
  def init() = {
    new SocketNodeRefReceiver().start(manager)
  }
  var ClusterSignalingNodeCache: Map[String, SocketNodeRef] = Map.empty
  def getSocketNodeRef(ip: String,port: Int): SocketNodeRef = {
    val key = (ip+":"+port)
    ClusterSignalingNodeCache.get(key) match {
      case Some(node) => node
      case None => {
        val node = new SocketNodeRef(ip,port)
        ClusterSignalingNodeCache += (key -> node)
        node
      }
    }
  }
}