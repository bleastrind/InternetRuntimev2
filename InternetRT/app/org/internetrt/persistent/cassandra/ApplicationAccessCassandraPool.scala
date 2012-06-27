package org.internetrt.persistent.cassandra
import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import java.nio.ByteBuffer
import scala.xml.NodeSeq
import org.internetrt.persistent.ApplicationAccessPool

class ApplicationAccessCassandraPool(cluster:Cluster) 
  extends KeyValueCassandraPool[String,(Seq[String],Boolean)](cluster,"InternetRT_Personal","ApplicationAccess")
  with ApplicationAccessPool{
  def KeySerializer = StringSerializer.get() 
  def ValueSerializer = TupleSerializer;
  
  object TupleSerializer extends AbstractSerializer[(Seq[String],Boolean)]{
    
    def fromByteBuffer(buffer:ByteBuffer):(Seq[String],Boolean)={
      val xml = scala.xml.XML.loadString(new String(buffer.array()));
      val nodeseq = xml \ "AccessRequests" \ "access";
      
      (nodeseq.toSeq map( _.text),(xml \ "root" text).toBoolean)
    }
    
    def toByteBuffer(value:(Seq[String],Boolean)):ByteBuffer={
      val xml =scala.xml.Utility.trim(<v>
    	  <AccessRequests>
      		{NodeSeq.fromSeq(value._1 map (str => <access>str</access>))}
    	  </AccessRequests>
      		<root>{value._2}</root>
      	</v>)
      ByteBuffer.wrap(xml.toString.getBytes())
    }
  }
}
