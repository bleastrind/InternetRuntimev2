package org.internetrt.core.security

import org.internetrt.persistent.ApplicationAccessPool
import org.internetrt.exceptions.AccessRequestNotGrantedException

abstract class AccessControlSystemImpl extends AccessControlSystem {
    val applicationAccessPool:ApplicationAccessPool
	def checkAccess(userID:String ,appID:String ,action:String):Unit = {
	  if(!applicationAccessPool.get(userID+appID).getOrElse((Seq.empty,false))._1.contains(action))
	    throw new AccessRequestNotGrantedException("getApplications")
	}
	
	def grantAccess(userID:String, appID:String, actions:Seq[String], isRoot:Boolean) = {
	  applicationAccessPool.put(userID+appID,(actions,isRoot))
	}
	
	def isRoot(userID:String, appID:String):Boolean = {
	  applicationAccessPool.get(userID+appID).getOrElse((Seq.empty,false))._2

	}
}