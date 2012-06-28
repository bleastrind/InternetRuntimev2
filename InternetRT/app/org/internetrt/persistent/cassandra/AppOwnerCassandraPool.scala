package org.internetrt.persistent.cassandra

import me.prettyprint.hector.api.Cluster
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.serializers.AbstractSerializer
import org.internetrt.persistent.AppOwnerPool

class AppOwnerCassandraPool(cluster:Cluster) 
extends KeyValueCassandraPool[String,String](cluster,"InternetRT_Global","AppOwner")
with AppOwnerPool{
  def KeySerializer = StringSerializer.get() 
  def ValueSerializer = StringSerializer.get();
  
}
