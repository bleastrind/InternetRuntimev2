package org.internetrt.core.siblings

import org.internetrt.CONSTS;
abstract class ClusterManagerImpl extends ClusterManager {

  val DHTRing = List(
    (0, NodeRef.getNode("192.168.3.145")),
    (493379200, NodeRef.getNode("192.168.3.160"))
    );

  def getNodeRef(uid: String): Option[NodeRef] = {
    val desiredNode: NodeRef = DHTRing.filter(_._1 > uid.hashCode()).headOption match {
      case Some((i, n)) => n
      case None => DHTRing.head._2
    }

    if (CONSTS.ThisIP == desiredNode.IP)
      None
    else
      Some(desiredNode)
  }

  def getNodeRefByIP(ip: String): NodeRef = {
    NodeRef.getNode(ip)
  }
}

