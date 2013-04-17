package org.internetrt.core.siblings

import org.internetrt.CONSTS;
abstract class TwoTierManagerImpl extends ClusterManager {

  val Tier1DHTRing = List(
    (0, NodeRef.getNode("192.168.3.145")),
    (10768, NodeRef.getNode("192.168.3.155")),
    (2754673, NodeRef.getNode("192.168.3.234")),
    (275467373, NodeRef.getNode("192.168.3.123")),
    (1725467373, NodeRef.getNode("192.168.3.122")),
    (2054636373, NodeRef.getNode("192.168.3.121")));

  val Tier1Agent = List(NodeRef.getNode("192.168.3.145"));

  val Tier2DHTRing = List(
    (0, NodeRef.getNode("192.168.3.145")),
    (10768, NodeRef.getNode("192.168.3.155")),
    (2754673, NodeRef.getNode("192.168.3.234")),
    (275467373, NodeRef.getNode("192.168.3.123")),
    (1725467373, NodeRef.getNode("192.168.3.122")),
    (2054636373, NodeRef.getNode("192.168.3.121")));

  /**
   * Return the next node to handle the request
   */
  def getNodeRef(uid: String): Option[NodeRef] = {
    val desiredTier1Node: NodeRef = getDHTNode(uid, Tier1DHTRing)

    if (Tier1Agent.contains(desiredTier1Node)) {
      val desiredTier2Node = getDHTNode(uid, Tier2DHTRing)
      if (CONSTS.ThisIP == desiredTier2Node.IP) {

        None
      } else
        Some(desiredTier2Node)
    } else
      Some(desiredTier1Node)
  }

  def getDHTNode(uid: String, DHTtable: List[(Int, NodeRef)]) = {
    DHTtable.filter(_._1 < uid.hashCode()).headOption match {
      case Some((i, n)) => n
      case None => DHTtable.head._2
    }
  }

  def getNodeRefByIP(ip: String): NodeRef = {
    NodeRef.getNode(ip)
  }
}

