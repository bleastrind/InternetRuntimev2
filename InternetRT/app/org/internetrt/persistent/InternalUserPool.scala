package org.internetrt.persistent

trait InternalUserPool extends KeyValueResourcePool[String,(String,String)] {}
class StubInternalUserPool extends InternalUserPool with KeyValueMemoryResourcePool[String,(String,String)]
