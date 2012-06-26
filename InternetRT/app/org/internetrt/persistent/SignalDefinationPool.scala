package org.internetrt.persistent

trait SignalDefinationPool extends KeyValueResourcePool[String,scala.xml.Elem] {}
class StubSignalDefinationPool extends SignalDefinationPool with KeyValueMemoryResourcePool[String,scala.xml.Elem]
