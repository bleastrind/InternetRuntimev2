package org.internetrt.core.security

trait AccessControlSystem {
	def checkAccess(userID:String ,appID:String ,action:String):Unit
	
	def isRoot(userID:String, appID:String):Boolean
	
	def grantAccess(userID:String, appID:String, actions:Seq[String], isRoot:Boolean)
}