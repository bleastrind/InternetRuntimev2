package org.internetrt.persistent

import org.internetrt.core.model.Application;

trait GlobalAppPool extends KeyValueResourcePool[String,Application] {}
class StubGlobalAppPool extends GlobalAppPool with KeyValueMemoryResourcePool[String,Application]
