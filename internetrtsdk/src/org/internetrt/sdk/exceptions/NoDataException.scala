package org.internetrt.sdk.exceptions

class NoDataException(msg:String) extends Exception(msg) {
	def this() = this("")
}