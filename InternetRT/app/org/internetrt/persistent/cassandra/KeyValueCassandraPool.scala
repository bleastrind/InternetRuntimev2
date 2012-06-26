package org.internetrt.persistent.cassandra
import org.internetrt.persistent.KeyValueResourcePool

trait KeyValueCassandraPool[K,V] extends KeyValueResourcePool[K,V]{
  def put(k:K,v:V):Option[V]
  def get(k:K):Option[V]
}