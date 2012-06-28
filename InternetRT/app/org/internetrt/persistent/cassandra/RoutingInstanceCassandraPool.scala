package org.internetrt.persistent.cassandra
import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import java.nio.ByteBuffer
import scala.xml.NodeSeq
import org.internetrt.persistent.ApplicationAccessPool
import org.internetrt.core.model.RoutingInstance
import org.internetrt.persistent.RoutingInstancePool
import scala.xml.Elem

class RoutingInstanceCassandraPool(cluster:Cluster) 
  extends KeyValueCassandraPool[String,RoutingInstance](cluster,"InternetRT_Personal","RoutingInstance")
  with RoutingInstancePool{
  def KeySerializer = StringSerializer.get() 
  def ValueSerializer = TupleSerializer;
  
  object TupleSerializer extends AbstractSerializer[RoutingInstance]{
    
    def fromByteBuffer(buffer:ByteBuffer):RoutingInstance={
      val xml = scala.xml.XML.loadString(new String(buffer.array()));
      RoutingInstance(xml \ "userID" text, scala.xml.XML.loadString(xml \ "RoutingInstance" toString))
    }
    
    def toByteBuffer(value:RoutingInstance):ByteBuffer={
      val xml =scala.xml.Utility.trim(
          <v>
    		  <userID>{value.userID}</userID>
              {value.xml}
    		  </v>
          ).toString()
      ByteBuffer.wrap(xml.getBytes())
    }
  }
}
