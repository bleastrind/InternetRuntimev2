package org.internetrt.persistent.cassandra
import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import org.internetrt.persistent.AuthCodePool
import java.nio.ByteBuffer
class AuthCodeCassandraPool(cluster:Cluster) 
	extends KeyValueCassandraPool[String,(String,String)](cluster,"InternetRT_Personal","AuthCode")
	with AuthCodePool{
  def KeySerializer = StringSerializer.get() 
  def ValueSerializer = TupleSerializer;
  
    object TupleSerializer extends AbstractSerializer[(String,String)]{
    
    def fromByteBuffer(buffer:ByteBuffer):(String,String)={
      val xml = scala.xml.XML.loadString(new String(buffer.array()));

      (xml \ "appid" text,xml \ "userid" text)
    }
    
    def toByteBuffer(value:(String,String)):ByteBuffer={
      val xml =scala.xml.Utility.trim(<v>
    	  <appid>{value._1}</appid>
      	  <userid>{value._2}</userid>
      	</v>)
      ByteBuffer.wrap(xml.toString.getBytes())
    }
  }
}