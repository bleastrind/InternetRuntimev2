package org.internetrt.sdk.util

case class DescribedListenerConfig(appName:String,description:String,override val node:scala.xml.Node) extends ListenerConfig(node){
  
}
class ListenerConfig(pnode:scala.xml.Node){
  def node = pnode
}
case class GlobalData(map:Map[String,String]){}
case class DataAdapter(node:scala.xml.Node){}

case class ListenerDataFormat(kind:String, map:Map[String,DataAdapter])


import java.util.List
abstract class term
case class Signal(name:String, description:String, require:String, from:String, kind:String) extends term
case class Application(name:String, ID:String, signals:List[Signal]) extends term
case class RootApplication(name:String, ID:String, secret:String, accessRequests: List[String]) extends term
