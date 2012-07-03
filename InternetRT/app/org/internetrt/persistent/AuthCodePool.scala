package org.internetrt.persistent
import org.internetrt.core.security.AccessToken

trait AuthCodePool extends KeyValueResourcePool[String,(String,String)] {}
class StubAuthCodePool extends AuthCodePool with KeyValueMemoryResourcePool[String,(String,String)]
