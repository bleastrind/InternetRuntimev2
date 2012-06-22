package org.internetrt.persistent

trait AppOwnerPool extends KeyValueResourcePool[String,String] {}
class StubAppOwnerPool extends AppOwnerPool with KeyValueMemoryResourcePool[String,String]