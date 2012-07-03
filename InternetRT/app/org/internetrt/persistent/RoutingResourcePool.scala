package org.internetrt.persistent
import org.internetrt.core.model.Routing
import org.internetrt.core.signalsystem.Signal


	trait RoutingResourcePool {
		def getRoutingsBySignal(signal:Signal):Seq[Routing];
		
		def saveRouting(r:Routing):Boolean;
	}


	class MemoryRoutingResourcePool extends RoutingResourcePool {
	    val a:scala.collection.mutable.Map[String,List[Routing]]  = scala.collection.mutable.Map.empty[String,List[Routing]] 
		//a.put("")
	    
	    def getRoutingsBySignal(signal:Signal):Seq[Routing] = {
		   a.get(signal.user + signal.id + signal.from) match {
		     case Some(s) => s
		     case _ => Seq.empty
		   }
		}
	    def saveRouting(r:Routing)={
	      val id = r.xml \ "Signal" \\ "name" text;
	      var from = r.xml \ "Signal" \\ "from" text;
	      val key = r.userID + id + from
	      val oldseq = a.getOrElse( key  , List.empty)
	      a.put(key, r::oldseq)
	      true
	    }
	}
