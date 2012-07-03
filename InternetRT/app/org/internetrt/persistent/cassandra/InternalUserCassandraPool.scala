package org.internetrt.persistent.cassandra
import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import java.nio.ByteBuffer
import org.internetrt.persistent.InternalUserPool
class InternalUserCassandraPool(cluster:Cluster) 
	extends KeyValueCassandraPool[String,(String,String)](cluster,"InternetRT_Personal","InternalUser")
	with InternalUserPool{
  def KeySerializer = StringSerializer.get() 
  def ValueSerializer = TupleSerializer;
  
    object TupleSerializer extends AbstractSerializer[(String,String)]{
    
    def fromByteBuffer(buffer:ByteBuffer):(String,String)={
      val xml = scala.xml.XML.loadString(new String(buffer.array()));

      (xml \ "uid" text,xml \ "password" text)
    }
    
    def toByteBuffer(value:(String,String)):ByteBuffer={
      val xml =scala.xml.Utility.trim(<v>
    	  <uid>{value._1}</uid>
      	  <password>{value._2}</password>
      	</v>)
      ByteBuffer.wrap(xml.toString.getBytes())
    }
  }
}