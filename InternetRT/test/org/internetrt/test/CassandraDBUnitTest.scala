package org.internetrt.test
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.internetrt.Cassandra
import org.internetrt.core.model.Application
@RunWith(classOf[JUnitRunner])
class CassandraDBUnitTest extends Specification{
	"The DB" should{
		"be able to put" in {
		  Cassandra.internalUserPool.put("uid",("213","231")) 
		}
	}

}