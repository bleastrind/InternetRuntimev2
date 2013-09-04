package org.internetrt.core.siblings

import java.net.ServerSocket
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.InputStreamReader
import org.internetrt.persistent._
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._
import org.internetrt.Prop
import java.net.Socket

abstract class TwoTierClusterManagerImpl extends ClusterManager {

  val globalSiblingPool: GlobalSiblingPool
  val localSiblingPool: LocalSiblingPool

  init()
  private def getDHTNumber(DHTList:List[Int]) = {
    
    val dhtList = (Int.MaxValue :: 0 ::DHTList).sorted
	  
	  var res = 0
	  var mink = 0
	  
	  for (i <- 1 until dhtList.size)
	  {
		  if (dhtList(i) - dhtList(i-1) > res)
		  {
			  res = dhtList(i) - dhtList(i-1)
			  mink = dhtList(i-1)
		  }
	  }
	  
	  (mink + res / 2).toString()
	}
  def addGlobalNode(domain:String,ip:String,ipAlternative:String) = {
    globalSiblingPool.addNode(getDHTNumber(Tier1DHTRing.map(_._1)),domain,ip,ipAlternative);
    new updateAllNode(this,true).start()
  }
  def deleteGlobalNode(domain:String){
    globalSiblingPool.deleteNode(domain)
     new updateAllNode(this,true).start()
  }
  def addLocalNode(ip:String) = {
    val dhtList = Tier2DHTRing.map(_._1)
    localSiblingPool.addNode(
        getDHTNumber(dhtList) ,
        ip)
    new updateAllNode(this,false).start()
  }
  def deleteLocalNode(ip:String) = {
    localSiblingPool.deleteNode(ip)
    new updateAllNode(this,false).start()
  }
  def getAllGlobalNode()={
    val nodeLists = globalSiblingPool.getAll()
    
    val TierDHTRing = new ListBuffer[(Int, NodeRef)]()
    for (node <- nodeLists) {
      val tokentuple = (node._2._1.toInt, nodeRefFactory.getNodeRef(node._2._2))
      TierDHTRing += tokentuple
    }
    TierDHTRing.toList
  }
  
  def getAllLocalNode()={
    val nodeLists = localSiblingPool.getAll()

    val TierDHTRing = new ListBuffer[(Int, NodeRef)]()
    for (node <- nodeLists) {
      val tokentuple = (node._2.toInt, nodeRefFactory.getNodeRef(node._1))
      TierDHTRing += tokentuple
    }
    TierDHTRing.toList
  }
  
  var Tier1DHTRing:List[(Int, NodeRef)] = null
  var Tier2DHTRing:List[(Int, NodeRef)] = null
  
  def init()
  {
	  Tier1DHTRing = getAllGlobalNode()
	  
	  Tier2DHTRing = getAllLocalNode()
	  
  }
  /*
  val Tier1DHTRing = List(
    (0, NodeRef.getNode("192.168.3.145")),
    (10768, NodeRef.getNode("192.168.3.155")),
    (2754673, NodeRef.getNode("192.168.3.234")),
    (275467373, NodeRef.getNode("192.168.3.123")),
    (1725467373, NodeRef.getNode("192.168.3.122")),
    (2054636373, NodeRef.getNode("192.168.3.121")));
*/
  val Tier1Agent = List(nodeRefFactory.getNodeRef(Prop.AgentNode));
/*
  val Tier2DHTRing = List(
    (0, NodeRef.getNode("192.168.3.145")),
    (10768, NodeRef.getNode("192.168.3.155")),
    (2754673, NodeRef.getNode("192.168.3.234")),
    (275467373, NodeRef.getNode("192.168.3.123")),
    (1725467373, NodeRef.getNode("192.168.3.122")),
    (2054636373, NodeRef.getNode("192.168.3.121")));
*/
  /**
   * Return the next node to handle the request
   */
  def getNodeRef(uid: String): Option[NodeRef] = {
    val desiredTier1Node: NodeRef = getDHTNode(uid, Tier1DHTRing)

    if (Tier1Agent.contains(desiredTier1Node)) {
      val desiredTier2Node = getDHTNode(uid, Tier2DHTRing)
      if (Prop.ThisIP == desiredTier2Node.IP) {

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
    nodeRefFactory.getNodeRef(ip)
  }
}

  class updateAllNode(manager:TwoTierClusterManagerImpl,isGlobal:Boolean) extends Thread{
	  
	  override def run() = {
	    
	    val (ips,port) = if(isGlobal) 
	      (manager.globalSiblingPool.getAll().map(x=>x._2._2),Prop.GlobalPort)
	    else 
	      (manager.localSiblingPool.getAll().map(x=>x._1),Prop.LocalPort);
	        
		    for (ip <- ips)
		    {
		      val n = manager.nodeRefFactory.getSocketNodeRef(ip,port)
			    n.update()
		    }
	    
	  }
  }
