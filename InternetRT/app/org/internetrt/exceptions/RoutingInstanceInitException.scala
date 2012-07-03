package org.internetrt.exceptions

case class RoutingInstanceInitException(missingOptions:Map[String,Seq[scala.xml.Node]]) extends Exception {

}