package org.internetrt.persistent

trait KeyValueResourcePool[K,V]{
  def put(k:K,v:V):Option[V]
  def get(k:K):Option[V]
}

trait KeyValueMemoryResourcePool[K,V] extends KeyValueResourcePool[K,V]{
    val innerMap = scala.collection.mutable.Map.empty[K,V]
	def put(k:K,v:V) = innerMap.put(k,v)
	def get(k:K) = innerMap.get(k)
}