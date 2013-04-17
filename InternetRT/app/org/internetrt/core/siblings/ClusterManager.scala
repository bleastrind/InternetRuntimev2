package org.internetrt.core.siblings

import org.internetrt.core.InternetRuntime

trait ClusterManager {
  val global: InternetRuntime
  
  /**
   * Return None if uid below to the machine itself
   */
  def getNodeRef(uid: String): Option[NodeRef]
  
  def getNodeRefByIP(ip:String):NodeRef
}