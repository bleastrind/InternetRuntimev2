package org.internetrt.persistent
/**
 * Attation!!  ApplicationAccessPool is redundant with AppPool! Keep consistent in programming logic
 */
trait ApplicationAccessPool extends KeyValueResourcePool[String,(Seq[String],Boolean)] {}
class StubApplicationAccessPool extends ApplicationAccessPool with KeyValueMemoryResourcePool[String,(Seq[String],Boolean)]
