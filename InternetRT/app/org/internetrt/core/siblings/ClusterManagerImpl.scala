package org.internetrt.core.siblings

abstract class ClusterManagerImpl extends ClusterManager {
	def getNodeRef(uid:String):Option[NodeRef] = {
	  Some(new NodeRef("192.168.3.145"))
	  None
	}
	
	 def getNodeRefByIP(ip:String):NodeRef = {
	   NodeRef.getNode(ip)
	 }
}

