package org.internetrt.persistent
import org.internetrt.core.model.RoutingInstance

trait RoutingInstancePool extends KeyValueResourcePool[String,RoutingInstance] {}
class StubRoutingInstancePool extends RoutingInstancePool with KeyValueMemoryResourcePool[String,RoutingInstance]
