package org.internetrt.persistent.cassandra
import org.internetrt.core.security.AccessToken
import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import java.nio.ByteBuffer
import java.util.Date
import java.text.DateFormat
import org.internetrt.persistent.AccessTokenPool

class AccessTokenCassandraPool(cluster:Cluster) 
	extends KeyValueCassandraPool[String,(AccessToken,String,String)](cluster,"InternetRT_Personal","AccessToken")
	with AccessTokenPool{
  def KeySerializer = StringSerializer.get() 
  def ValueSerializer = TupleSerializer;
  
  object TupleSerializer extends AbstractSerializer[(AccessToken,String,String)]{
    
    def fromByteBuffer(buffer:ByteBuffer):(AccessToken,String,String)={
      val xml = scala.xml.XML.loadString(new String(buffer.array()))
      (AccessToken(xml \ "AccessToken" \ "value" text,
          new Date((xml \ "AccessToken" \ "expire" text).toLong),
          xml \ "AccessToken" \ "refresh" text),
          xml \ "appid" text,
          xml \ "userid" text)
    }
    
    def toByteBuffer(value:(AccessToken,String,String)):ByteBuffer={
      val xml =scala.xml.Utility.trim(<v>
    	  <AccessToken>
      		<value>{value._1.value}</value>
    	  	<expire>{value._1.expire.getTime()}</expire>
      		<refresh>{value._1.refresh}</refresh>
    	  </AccessToken>
      		<appid>{value._2}</appid>
      		<userid>{value._3}</userid>
      	</v>)
      ByteBuffer.wrap(xml.toString.getBytes())
    }
  }
}

