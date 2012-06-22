package org.internetrt.persistent
import org.internetrt.core.model.Routing


	trait RoutingResourcePool {
		def getRoutingsBySignal(signaluri:String):Seq[Routing];
		
		def saveRouting(r:Routing):Boolean;
	}


	class MemoryRoutingResourcePool extends RoutingResourcePool {
	    val a:scala.collection.mutable.Map[String,Seq[Routing]]  = scala.collection.mutable.Map.empty[String,Seq[Routing]] 
		//a.put("")
	    
	    def getRoutingsBySignal(signalID:String):Seq[Routing] = {
		   a.get(signalID) match {
		     case Some(s) => s
		     case _ => Seq.empty
		   }
		}
	    def saveRouting(r:Routing)=false
	}
