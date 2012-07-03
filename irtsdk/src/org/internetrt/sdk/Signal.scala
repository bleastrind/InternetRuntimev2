package org.internetrt.sdk.util
import java.util.List
abstract class term
case class Signal(name:String, description:String, require:String) extends term
case class Application(name:String, ID:String, signals:List[Signal]) extends term
