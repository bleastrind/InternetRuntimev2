package org.internetrt.exceptions

class ConsideredException(msg:String) extends Exception(msg){
	def this() = this("");
}