package org.internetrt.core.signalsystem

trait Response{
  def getResponse:String
}
/**
 * A signalResponse provide the data need to response a signal
 */
trait SignalResponse extends Response

class RejectResponse(msg:String) extends SignalResponse{
  def getResponse = msg
}

class ObjectResponse(o:Any) extends SignalResponse{
  def getResponse = o.toString()
  def asObject[T] = o.asInstanceOf[T]
}
/**
 * A SignalHeadResponse provide the brief info about how to handle the signal
 */
//trait SignalHeadResponse extends SignalResponse{
//  def getHead:String
//}

/**
 * Contain the signalListeners who received the signal
 */
class EventSignalResponse{
  
}

/**
 * Contain the 
 */
class RequestSignalResponse{
  
}