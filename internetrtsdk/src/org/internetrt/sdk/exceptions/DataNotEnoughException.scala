package org.internetrt.sdk.exceptions

class DataNotEnoughException(msg:String) extends Exception(msg) {
	def this() = this("")
}