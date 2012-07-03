package org.internetrt.core.security

import org.internetrt.persistent.ApplicationAccessPool

abstract class AccessControlSystemImpl extends AccessControlSystem {
    val applicationAccessPool:ApplicationAccessPool
	def checkAccess(userID:String ,appID:String ,action:String):Boolean = {
	  applicationAccessPool.get(userID+appID).getOrElse((Seq.empty,false))._1.contains(action)
	}
	
	def grantAccess(userID:String, appID:String, actions:Seq[String], isRoot:Boolean) = {
	  applicationAccessPool.put(userID+appID,(actions,isRoot))
	}
	
	def isRoot(userID:String, appID:String):Boolean = {
	  applicationAccessPool.get(userID+appID).getOrElse((Seq.empty,false))._2

	}
}