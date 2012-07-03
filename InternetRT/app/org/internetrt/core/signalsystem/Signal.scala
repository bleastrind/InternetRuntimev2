package org.internetrt.core.signalsystem

case class Signal(id:String,user:String,from:String,vars:Map[String,Seq[String]])
  
  //def identifier:String//Identifier should be able to recognize the signal and its source  


object Signal{
  val FROMUSERINTERFACE = "userinterface"
}
//
///**
// * A Event signal is a signal don't need response
// */
//class EventSignal{
//  
//}
//
///**
// * A Request signal is a signal need sync response
// */
//class RequestSignal{
//  
//}