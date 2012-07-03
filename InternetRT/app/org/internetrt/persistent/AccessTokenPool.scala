package org.internetrt.persistent
import org.internetrt.core.security.AccessToken


trait AccessTokenPool extends KeyValueResourcePool[String,(AccessToken,String,String)] {}

class StubAccessTokenPool extends AccessTokenPool 
with KeyValueMemoryResourcePool[String,(AccessToken,String,String)]
