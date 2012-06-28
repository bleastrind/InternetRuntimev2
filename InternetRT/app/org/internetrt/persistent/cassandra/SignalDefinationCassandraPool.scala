package org.internetrt.persistent.cassandra

import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import org.internetrt.persistent.SignalDefinationPool
import scala.xml.Elem
import java.nio.ByteBuffer

class SignalDefinationCassandraPool(cluster:Cluster) 
extends KeyValueCassandraPool[String,Elem](cluster,"InternetRT_Global","SignalDefination")
with SignalDefinationPool{
  def KeySerializer = StringSerializer.get() 
  def ValueSerializer = XMLSerializer;
  
  object XMLSerializer extends AbstractSerializer[Elem]{
    
    def fromByteBuffer(buffer:ByteBuffer):Elem={
      scala.xml.XML.loadString(new String(buffer.array()));
    }
    
    def toByteBuffer(value:Elem):ByteBuffer={
      val xml =scala.xml.Utility.trim(value)
      ByteBuffer.wrap(xml.toString.getBytes())
    }
  }
}
