package org.internetrt.core.siblings

import org.internetrt.CONSTS;
abstract class ClusterManagerImpl extends ClusterManager {

  val DHTRing = List(
    (0, NodeRef.getNode("192.168.3.145")),
    (10768, NodeRef.getNode("192.168.3.155")),
    (2754673, NodeRef.getNode("192.168.3.234")),
    (275467373, NodeRef.getNode("192.168.3.123")),
    (1725467373, NodeRef.getNode("192.168.3.122")),
    (2054636373, NodeRef.getNode("192.168.3.121"))
    );

  def getNodeRef(uid: String): Option[NodeRef] = {
    val desiredNode: NodeRef = DHTRing.filter(_._1 < uid.hashCode()).headOption match {
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

