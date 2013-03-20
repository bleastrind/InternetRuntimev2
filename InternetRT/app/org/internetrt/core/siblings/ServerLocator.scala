package org.internetrt.core.siblings

class ServerLocator {
	def getNodeRef(uid:String):Option[NodeRef] = {
	  Some(new NodeRef("192.168.3.145"))
	}
	
	
}

trait RunAt {
  def before(uid:String) = {
    new ServerLocator()
  }
}