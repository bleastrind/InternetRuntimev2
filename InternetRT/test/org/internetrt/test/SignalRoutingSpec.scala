///**
// * Sample code for the blog posting:
// * 
// * Installing and using Apache Cassandra With Java Part 4 (Thrift Client)
// * http://www.sodeso.nl/?p=251
// * 
// * Please report any discrepancies that you may find,
// * if you have any requests for examples not mentioned here
// * but are within the scope of the blog posting then also
// * please let me know so i can add them..
// */
//
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.cassandra.thrift.Cassandra;
//import org.apache.cassandra.thrift.Column;
//import org.apache.cassandra.thrift.ColumnOrSuperColumn;
//import org.apache.cassandra.thrift.ColumnParent;
//import org.apache.cassandra.thrift.ColumnPath;
//import org.apache.cassandra.thrift.ConsistencyLevel;
//import org.apache.cassandra.thrift.Deletion;
//import org.apache.cassandra.thrift.InvalidRequestException;
//import org.apache.cassandra.thrift.KeyRange;
//import org.apache.cassandra.thrift.KeySlice;
//import org.apache.cassandra.thrift.Mutation;
//import org.apache.cassandra.thrift.NotFoundException;
//import org.apache.cassandra.thrift.SlicePredicate;
//import org.apache.cassandra.thrift.SliceRange;
//import org.apache.cassandra.thrift.TimedOutException;
//import org.apache.cassandra.thrift.UnavailableException;
//import org.apache.thrift.TException;
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TProtocol;
//import org.apache.thrift.transport.TSocket;
//import org.apache.thrift.transport.TTransport;
//import org.apache.thrift.transport.TTransportException;
//
///**
// * @author Ronald Mathies
// */
//public class Authors {
//
//    private static final String KEYSPACE = "Blog";
//    private static final String COLUMN_FAMILY = "Authors";
//
//    public static final String ENCODING = "utf-8";
//
//    private static TTransport tr = null;
//
//    public static void main(String[] args) throws TException, InvalidRequestException, UnavailableException, UnsupportedEncodingException, NotFoundException, TimedOutException {
//        Cassandra.Client client = setupConnection();
//
//        org.internetrt.util.Debuger.debug("Remove all the authors we might have created before.\n");
//        removeAuthor(client, "Eric Long");
//        removeAuthor(client, "Ronald Mathies");
//        removeAuthor(client, "John Steward");
//
//        org.internetrt.util.Debuger.debug("Create the authors.\n");
//        createAuthor(client, "Eric Long", "eric (at) long.com", "United Kingdom", "01/01/2002");
//        createAuthor(client, "Ronald Mathies", "ronald (at) sodeso.nl", "Netherlands, The", "01/01/2010");
//        createAuthor(client, "John Steward", "john.steward (at) somedomain.com", "Australia", "01/01/2009");
//
//        org.internetrt.util.Debuger.debug("Select Eric Long.\n");
//        selectSingleAuthorWithAllColumns(client, "Eric Long");
//
//        org.internetrt.util.Debuger.debug("Select Ronald Mathies.\n");
//        selectSingleAuthorWithAllColumns(client, "Ronald Mathies");
//
//        org.internetrt.util.Debuger.debug("Select John Steward.\n");
//        selectSingleAuthorWithAllColumns(client, "John Steward");
//
//        org.internetrt.util.Debuger.debug("Select all authors with all columns.\n");
//        selectAllAuthorsWithAllColumns(client);
//
//        org.internetrt.util.Debuger.debug("Select all authors with only the email column.\n");
//        selectAllAuthorsWithOnlyTheEmailColumn(client);
//
//        org.internetrt.util.Debuger.debug("Update John Steward.\n");
//        updateJohnStewardAuthor(client);
//
//        org.internetrt.util.Debuger.debug("Select John Steward.\n");
//        selectSingleAuthorWithAllColumns(client, "John Steward");
//
//        org.internetrt.util.Debuger.debug("Remove email address and birthday from John Steward.\n");
//        deleteEmailAndBirthdayFromJohnSteward(client);
//
//        org.internetrt.util.Debuger.debug("Select John Steward.\n");
//        selectSingleAuthorWithAllColumns(client, "John Steward");
//
//        closeConnection();
//    }
//
//    /**
//     * Open up a new connection to the Cassandra Database.
//     * 
//     * @return the Cassandra Client
//     */
//    private static Cassandra.Client setupConnection() throws TTransportException {
//        try {
//            tr = new TSocket("localhost", 9160);
//            TProtocol proto = new TBinaryProtocol(tr);
//            Cassandra.Client client = new Cassandra.Client(proto);
//            tr.open();
//
//            return client;
//        } catch (TTransportException exception) {
//            exception.printStackTrace();
//        }
//
//        return null;
//    }
//
//    /**
//     * Close the connection to the Cassandra Database.
//     */
//    private static void closeConnection() {
//        try {
//            tr.flush();
//            tr.close();
//        } catch (TTransportException exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * Removes an Author from the Authors ColumnFamily.
//     * cccc
//     * @param client the Corg.apache.thrift;
//
//importassandra Client
//     * @param authorKey The key of the Author
//     */
//    private static void removeAuthor(Cassandra.Client client, String authorKey) {
//        try {
//            ColumnPath columnPath = new ColumnPath(COLUMN_FAMILY);
//            client.remove(KEYSPACE, authorKey, columnPath, System.currentTimeMillis(), ConsistencyLevel.ALL);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * Creates and stores an Author in the Cassandra Database.
//     * 
//     * @param client the Cassandra Client
//     * @param authorKey The key of the Author
//     * @param email the email address
//     * @param country the country
//     * @param registeredSince the registration date
//     */
//    private static void createAuthor(Cassandra.Client client, String authorKey, String email, String country, String registeredSince) {
//        try {
//            long timestamp = System.currentTimeMillis();
//            Map<String, List<ColumnOrSuperColumn>> job = new HashMap<String, List<ColumnOrSuperColumn>>();
//
//            List<ColumnOrSuperColumn> columns = new ArrayList<ColumnOrSuperColumn>();
//            Column column = new Column("email".getBytes(ENCODING), email.getBytes(ENCODING), timestamp);
//            ColumnOrSuperColumn columnOrSuperColumn = new ColumnOrSuperColumn();
//            columnOrSuperColumn.setColumn(column);
//            columns.add(columnOrSuperColumn);
//
//            column = new Column("country".getBytes(ENCODING), country.getBytes(ENCODING), timestamp);
//            columnOrSuperColumn = new ColumnOrSuperColumn();
//            columnOrSuperColumn.setColumn(column);
//            columns.add(columnOrSuperColumn);
//
//            column = new Column("country".getBytes(ENCODING), country.getBytes(ENCODING), timestamp);
//            columnOrSuperColumn = new ColumnOrSuperColumn();
//            columnOrSuperColumn.setColumn(column);
//            columns.add(columnOrSuperColumn);
//
//            column = new Column("registeredSince".getBytes(ENCODING), registeredSince.getBytes(ENCODING), timestamp);
//            columnOrSuperColumn = new ColumnOrSuperColumn();
//            columnOrSuperColumn.setColumn(column);
//            columns.add(columnOrSuperColumn);
//
//            job.put(COLUMN_FAMILY, columns);
//
//            client.batch_insert(KEYSPACE, authorKey, job, ConsistencyLevel.ALL);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * Selects a single author with all the columns from the Cassandra database
//     * and display it in the console.
//     * 
//     * @param client the Cassandra client
//     * @param authorKey The key of the Author
//     */
//    private static void selectSingleAuthorWithAllColumns(Cassandra.Client client, String authorKey) {
//        try {
//            SlicePredicate slicePredicate = new SlicePredicate();
//            SliceRange sliceRange = new SliceRange();
//            sliceRange.setStart(new byte[] {});
//            sliceRange.setFinish(new byte[] {});
//            slicePredicate.setSlice_range(sliceRange);
//
//            ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY);
//            List<ColumnOrSuperColumn> result = client.get_slice(KEYSPACE, authorKey, columnParent, slicePredicate, ConsistencyLevel.ONE);
//
//            printToConsole(authorKey, result);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * Selects all the authors with all the columns from the Cassandra database.
//     * 
//     * @param client the Cassandra client
//     */
//    private static void selectAllAuthorsWithAllColumns(Cassandra.Client client) {
//        try {
//            KeyRange keyRange = new KeyRange(3);
//            keyRange.setStart_key("");
//            keyRange.setEnd_key("");
//
//            SliceRange sliceRange = new SliceRange();
//            sliceRange.setStart(new byte[] {});
//            sliceRange.setFinish(new byte[] {});
//
//            SlicePredicate slicePredicate = new SlicePredicate();
//            slicePredicate.setSlice_range(sliceRange);
//
//            ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY);
//            List<KeySlice> keySlices = client.get_range_slices(KEYSPACE, columnParent, slicePredicate, keyRange, ConsistencyLevel.ONE);
//
//            for (KeySlice keySlice : keySlices) {
//                printToConsole(keySlice.getKey(), keySlice.getColumns());
//            }
//
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * Selects all the authors with only the email column from the Cassandra
//     * database.
//     * 
//     * @param client the Cassandra client
//     */
//    private static void selectAllAuthorsWithOnlyTheEmailColumn(Cassandra.Client client) {
//        try {
//            KeyRange keyRange = new KeyRange(3);
//            keyRange.setStart_key("");
//            keyRange.setEnd_key("");
//
//            List<byte[]> columns = new ArrayList<byte[]>();
//            columns.add("email".getBytes(ENCODING));
//
//            SlicePredicate slicePredicate = new SlicePredicate();
//            slicePredicate.setColumn_names(columns);
//
//            ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY);
//            List<KeySlice> keySlices = client.get_range_slices(KEYSPACE, columnParent, slicePredicate, keyRange, ConsistencyLevel.ONE);
//
//            for (KeySlice keySlice : keySlices) {
//                printToConsole(keySlice.getKey(), keySlice.getColumns());
//            }
//
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * Update the John Steward author with a new email address and a new field, the birthday.
//     * 
//     * @param client the Cassandra client
//     */
//    private static void updateJohnStewardAuthor(Cassandra.Client client) {
//        try {
//            long timestamp = System.currentTimeMillis();
//
//            Map<String, Map<String, List<Mutation>>> job = new HashMap<String, Map<String, List<Mutation>>>();
//            List<Mutation> mutations = new ArrayList<Mutation>();
//
//            // Change the email address
//            Column column = new Column("email".getBytes(ENCODING), "john@steward.nl".getBytes(ENCODING), timestamp);
//            ColumnOrSuperColumn columnOrSuperColumn = new ColumnOrSuperColumn();
//            columnOrSuperColumn.setColumn(column);
//
//            Mutation mutation = new Mutation();
//            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
//            mutations.add(mutation);
//
//            // Add a new column
//            column = new Column("birthday".getBytes(ENCODING), "05-04-1978".getBytes(ENCODING), timestamp);
//            columnOrSuperColumn = new ColumnOrSuperColumn();
//            columnOrSuperColumn.setColumn(column);
//
//            mutation = new Mutation();
//            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
//            mutations.add(mutation);
//
//            Map<String, List<Mutation>> mutationsForColumnFamily = new HashMap<String, List<Mutation>>();
//            mutationsForColumnFamily.put(COLUMN_FAMILY, mutations);
//
//            job.put("John Steward", mutationsForColumnFamily);
//
//            client.batch_mutate(KEYSPACE, job, ConsistencyLevel.ALL);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * Delete the email address and birthday from John Steward.
//     * 
//     * @param client the Cassandra client
//     */
//    private static void deleteEmailAndBirthdayFromJohnSteward(Cassandra.Client client) {
//        try {
//            long timestamp = System.currentTimeMillis();
//
//            // The columns we want to remove
//            List<byte[]> columns = new ArrayList<byte[]>();
//            columns.add("email".getBytes(ENCODING));
//            columns.add("birthday".getBytes(ENCODING));
//
//            // Add the columns to a SlicePredicate
//            SlicePredicate slicePredicate = new SlicePredicate();
//            slicePredicate.setColumn_names(columns);
//
//            Deletion deletion = new Deletion(timestamp);
//            deletion.setPredicate(slicePredicate);
//
//            Mutation mutation = new Mutation();
//            mutation.setDeletion(deletion);
//
//            List<Mutation> mutations = new ArrayList<Mutation>();
//            mutations.add(mutation);
//
//            Map<String, List<Mutation>> mutationsForColumnFamily = new HashMap<String, List<Mutation>>();
//            mutationsForColumnFamily.put(COLUMN_FAMILY, mutations);
//
//            Map<String, Map<String, List<Mutation>>> batch = new HashMap<String, Map<String, List<Mutation>>>();
//            batch.put("John Steward", mutationsForColumnFamily);
//
//            client.batch_mutate(KEYSPACE, batch, ConsistencyLevel.ALL);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * Prints out the information to the console.
//     * 
//     * @param key the key of the Author
//     * @param result the result to print out
//     */
//    private static void printToConsole(String key, List<ColumnOrSuperColumn> result) {
//        try {
//            org.internetrt.util.Debuger.debug("Key: '" + key + "'");
//            for (ColumnOrSuperColumn c : result) {
//                if (c.getColumn() != null) {
//                    String name = new String(c.getColumn().getName(), ENCODING);
//                    String value = new String(c.getColumn().getValue(), ENCODING);
//                    long timestamp = c.getColumn().getTimestamp();
//                    org.internetrt.util.Debuger.debug("  name: '" + name + "', value: '" + value + "', timestamp: " + timestamp);
//                } else {
//
//                }
//            }
//        } catch (UnsupportedEncodingException exception) {
//            exception.printStackTrace();
//        }
//    }
//
//}
//
////package org.specs2
////package mock
////import specification._
////import execute.Result
////import control.Exceptions._
////import org.hamcrest.core.{ IsNull }
////import org.mockito.Matchers.{ anyInt }
////import org.mockito.stubbing._
////import org.mockito.Mockito.withSettings
////import org.mockito.invocation._
////import matcher._
////import junit.framework.AssertionFailedError
////
////class MockitoSpec extends Specification with Mockito with ResultMatchers {  def is =
////                                                                                                                        """
////Mockito is a Java library for mocking.
////
////The following samples are taken from the main documentation which can be found here:
////http://mockito.googlecode.com/svn/tags/latest/javadoc/org/mockito/Mockito.html
////                                                                                                                        """^p^
////  "CREATION"                                                                                                            ^
////  "Mocks can be created"                                                                                                ^
////    "with a name" 								                                                                                      ! creation().e1^
////    "with a default return value"                            					                                                  ! creation().e2^
////    "with a name and default return value"                            					                                        ! creation().e3^
////    "with a default answer"                                           					                                        ! creation().e4^
////    "with settings"                                                   					                                        ! creation().e5^
////																																																												p^
////  "VERIFICATION"                                                                                                        ^
////  "When a mock is created with the mock method"                                                                         ^
////    "it is possible to call methods on the mock" 								                                                        ! aMock().call1^
////    "it is possible to verify that a method has been called" 					                                                  ! aMock().verify1^
////    "if one method has not been called on a mock there will be a failure" 		                                          ! aMock().verify2^
////    "it is possible to check that no calls have been made" 		                                                          ! aMock().verify3^
////    "it is possible to pass byname parameters"          		                                                            ! aMock().verify4^
////    "it is possible to check byname parameters"          		                                                            ! aMock().verify5^
////    "it is possible to check a function parameter"          		                                                        ^
////      "with one argument"                                                                                               ! aMock().verify6^
////      "with one argument and a matcher for the return value"                                                            ! aMock().verify7^
////      "with n arguments"                                                                                                ! aMock().verify8^
////      "with n arguments and a matcher for the return value"                                                             ! aMock().verify9^
////      "as being anything"                                                                                               ! aMock().verify10^
////      "with Nothing as the return type"                                                                                 ! aMock().verify11^
////      "with Any as the return type"                                                                                     ! aMock().verify12^
////                                                                                                                        p^
////    "it is possible to check a partial function parameter"          		                                                ^
////      "with n arguments"                                                                                                ! aMock().verify13^
////      "with n arguments and a matcher for the return value"                                                             ! aMock().verify14^
////      "as being anything"                                                                                               ! aMock().verify15^
////      "when the argument is not defined"                                                                                ! aMock().verify16^
////                                                                                                                        p^
////    "it is possible to verify a function with implicit conversions"          		                                        ^
////      "with a single converted parameter"                                                                               ! aMock().verify17^
////      "with a single converted parameter, using a matcher"                                                              ! aMock().verify18^
////                                                                                                                        p^
////    "it is possible to verify a function with repeated parameters"          		                                        ! aMock().verify19^
////                                                                                                                        endp^
////  "It is also possible to return a specific value from a mocked method"                                                 ^
////    "then when the mocked method is called, the same values will be returned" 	                                        ! aMock().return1^
////    "different successive values can even be returned" 						                                                      ! aMock().return2^
////    "a value can be returned when a parameter of the method matches" 			                                              ^
////      "a hamcrest matcher" 													                                                                    ! aMock().return3^
////      "a specs2 matcher" 														                                                                    ! aMock().return4^
////                                                                                                                        endp^
////  "It is also possible to throw an exception from a mocked method"                                                      ^
////    "then when the mocked method is called, the exception will be thrown" 		                                          ! aMock().throw1^
////    "different successive exceptions can even be thrown" 						                                                    ! aMock().throw2^
////                                                                                                                        p^
////  "A mock can be created and stubbed at the same time"                        	                                        ! aMock().mockAndStub^
////                                                                                                                        p^
////  "NUMBER OF CALLS"                                                                                                     ^
////  "The number of calls to a mocked method can be checked"                                                               ^
////    "if the mocked method has been called once"                                                                         ! calls().calls1^
////    "if the mocked method has been called twice"                                                                        ! calls().calls2^
////    "if the mocked method has been called atLeast n times"                                                              ! calls().calls3^
////    "if the mocked method has been called atMost n times"                                                               ! calls().calls4^
////    "if the mocked method has never been called"                                                                        ! calls().calls5^
////    "if the verification throws an exception, it will be reported as an Error"                                          ! calls().calls6^
////    "if the mocked method has not been called after some calls"                                                         ! calls().calls7^
////    "if the mocked method has not been called after some calls - ignoring stubs"                                        ! calls().calls8^
////                                                                                                                        p^
////  "ORDER OF CALLS"                                                                                                      ^
////  "The order of calls to a mocked method can be checked"                                                                ^
////    "with 2 calls that were indeed in order"                                                                            ! ordered().asExpected1^
////    "with 2 calls that were indeed in order - ignoring stubbed methods"                                                 ! ordered().asExpected2^
////    "with 2 calls that were indeed not in order"                                                                        ! ordered().failed^
////    "with 3 calls that were indeed not in order"                                                                        ! ordered().failed2^
////                                                                                                                        p^
////  "ANSWERS & PARAMETERS CAPTURE"                                                                                        ^
////  "Answers can be created to control the returned a value"                                                              ! callbacks().c1^
////  "Answers can use the mock instance as the second parameter"                                                           ! callbacks().c2^
////  "Answers can use the mock instance, even when the method has 0 parameters"                                            ! callbacks().c3^
////                                                                                                                        p^
////  "A parameter can be captured in order to check its value"                                                             ! captured().e1^
////  "A parameter can be captured in order to check its successive values"                                                 ! captured().e2^
////                                                                                                                        p^
////  "The Mockito trait is reusable in other contexts"                                                                     ^
////    "in mutable specs"                                                                                                  ! reuse().e1^
////    "in JUnit"                                                                                                          ! reuse().e2^
////                                                                                                                        end
////    
////  case class creation() {
////    def e1 = { 
////			val list = mock[java.util.List[String]].as("list1")
////			(there was one(list).add("one")).message must contain("list1.add(\"one\")")
////		}
////    def e2 = {
////			val list = mock[java.util.List[String]].settings(defaultReturn = 10)
////      list.size must_== 10
////    }
////    def e3 = {
////			val list = mock[java.util.List[String]].settings(name = "list1", defaultReturn = 10, extraInterfaces = classesOf[Cloneable, Serializable])
////      (list.size must_== 10) and 
////			((there was one(list).add("one")).message must contain("list1.add(\"one\")"))
////    }
////    def e4 = {
////			val list = mock[java.util.List[String]].defaultAnswer((p1: InvocationOnMock) => "hello")
////      list.get(0) must_== "hello" 
////    }
////    def e5 = {
////			val list = mock[java.util.List[String]](withSettings.name("list1"))
////			(there was one(list).add("one")).message must contain("list1.add(\"one\")")
////		}
////	}
////
////	case class aMock() {
////    val list = mock[java.util.List[String]]
////    val queue = mock[scala.collection.immutable.Queue[String]]
////
////    trait ByName { def call(i: =>Int) = i }
////    val byname = mock[ByName]
////
////    trait WithFunction1 { def call(f: Int => String) = f(0) }
////    val function1 = mock[WithFunction1]
////
////    trait WithFunction2 { def call(f: (Int, Double) => String) = f(1, 2.0) }
////    val function2 = mock[WithFunction2]
////
////    val functionNothing = mock[WithFunctionNothing]
////    val functionAny = mock[WithFunctionAny]
////
////    trait WithPartialFunction { def call(f: PartialFunction[(Int, Double), String]) = f.apply((1, 2.0)) }
////    val partial = mock[WithPartialFunction]
////
////    case class WrappedString(s: String)
////    implicit def wrap(s: String): WrappedString = WrappedString(s)
////    trait WithImplicitConversion { def call[T <% WrappedString](s: T) = s.toString }
////    val converted = mock[WithImplicitConversion]
////
////    trait WithRepeatedParams { def call[T](params: T*) = params.toString }
////    val repeated = mock[WithRepeatedParams]
////
////    def call1 = { list.add("one"); success }
////
////    def verify1 = {
////      list.add("one")
////      there was one(list).add("one")
////    }
////    def verify2 = (there was one(list).add("one")).message must startWith("The mock was not called as expected")
////
////    def verify3 = there were noCallsTo(list)
////
////    def verify4 = {
////      byname.call(10)
////      there was one(byname).call(10)
////    }
////    def verify5 = {
////      byname.call(10)
////      there was one(byname).call(be_>(5))
////    }
////
////    def verify6 = {
////      function1.call((_:Int).toString)
////      there was one(function1).call(1 -> "1")
////    }
////    def verify7 = {
////      function1.call((_:Int).toString)
////      (there was one(function1).call(1 -> startWith("1"))) and
////      ((there was one(function1).call(1 -> startWith("2"))) returns "'1' doesn't start with '2'")
////    }
////    def verify8 = {
////      function2.call((i:Int, d: Double) => (i + d).toString)
////      there was one(function2).call((1, 3.0) -> "4.0")
////    }
////    def verify9 = {
////      function2.call((i:Int, d: Double) => (i + d).toString)
////      there was one(function2).call((1, 3.0) -> haveSize[String](3))
////    }
////    def verify10 = {
////      function2.call((i:Int, d: Double) => (i + d).toString)
////      there was one(function2).call(anyFunction2)
////    }
////    def verify11 = {
////      functionNothing.call((i:Int) => throw new Exception)
////      there was one(functionNothing).call(anyFunction1)
////    }
////    def verify12 = {
////      functionAny.call(() => throw new Exception)
////      there was one(functionAny).call(any[() => Any])
////    }
////    def verify13 = {
////      partial.call { case (i:Int, d: Double) => (i + d).toString }
////      there was one(partial).call((1, 3.0) -> "4.0")
////    }
////    def verify14 = {
////      partial.call { case (i:Int, d: Double) => (i + d).toString }
////      there was one(partial).call((1, 3.0) -> haveSize[String](3))
////    }
////    def verify15 = {
////      partial.call { case (i:Int, d: Double) => (i + d).toString }
////      there was one(partial).call(anyPartialFunction)
////    }
////    def verify16 = {
////      partial.call { case (i:Int, d: Double) if i > 10 => (i + d).toString }
////      there was one(partial).call((1, 3.0) -> "4.0") returns "a PartialFunction defined for (1,3.0)"
////    }
////    def verify17 = {
////      converted.call("test")
////      there was one(converted).call("test")
////    }
////    def verify18 = {
////      converted.call("test")
////      there was one(converted).call(startWith("t"))
////    }
////    def verify19 = {
////      repeated.call(1, 2, 3)
////      (there was one(repeated).call(1, 2, 3)) and
////      ((there was one(repeated).call(1, 2)) returns "WrappedArray(1, 2)")
////    }
////
////    def return1 = {
////      list.add("one") returns true
////      list.add("one") must_== true
////    }
////    def return2 = {
////      list.add("one") returns (true, false, true)
////      (list.add("one"), list.add("one"), list.add("one")) must_== (true, false, true)
////    }
////    def return3 = {
////      list.contains(anArgThat(new IsNull[String])) returns true
////      list.contains(null) must_== true
////    }
////    def return4 = {
////      list.contains(argThat(beMatching(".*o"))) returns true
////      list.contains("o") must_== true
////    }
////    def throw1 = {
////      list.clear() throws new RuntimeException
////      list.clear()
////    } must throwA[RuntimeException]
////
////    def throw2 = {
////      list.clear() throws (new RuntimeException, new IllegalArgumentException)
////      tryo(list.clear())
////      list.clear()
////    } must throwAn[IllegalArgumentException]
////
////    def mockAndStub = {
////      val mocked: java.util.List[String] = mock[java.util.List[String]].contains("o") returns true
////      mocked.contains("o") must beTrue
////    }
////
////  }
////  
////  case class calls() {
////    val list = mock[java.util.List[String]]
////    val list2 = mock[java.util.List[String]]
////
////    list.add("one")
////    1 to 2 foreach { i => list.add("two") }
////    list2.add("one")
////
////    def calls1 = got { one(list).add("one") }  // equivalent to 'there was one(list).add("one")'
////    def calls2 = there were two(list).add("two")
////    def calls3 = there was atLeast(1)(list).add("two")
////    def calls4 = there were atMost(2)(list).add("two")
////    def calls5 = there was no(list).add("four")
////    def calls6 = {
////      val cause = new Exception("cause")
////      val e = new Exception("error", cause)
////      (there was no(list).add(be_==={throw e; "four"})) must throwAn[Exception]
////    }
////    def calls7 = {
////      there was one(list).add("one")
////      there were two(list).add("two")
////      there were noMoreCallsTo(list)
////    }
////    def calls8 = {
////      val list3 = mock[java.util.List[String]]
////      val list4 = mock[java.util.List[String]]
////      list3.contains("3") returns false
////      list4.contains("4") returns false
////
////      list3.add("one")
////      list4.add("one"); list4.add("one")
////      list3.contains("3")
////      list4.contains("4")
////
////      there was one(list3).add("one")
////      there were two(list4).add("one")
////
////      there were noMoreCallsTo(ignoreStubs(list3, list4))
////    }
////  }
////  case class callbacks() {
////    val list = mockAs[java.util.List[String]]("list")
////    def c1 = {
////      list.get(anyInt) answers { i => "The parameter is " + i.toString }
////      list.get(2) must_== "The parameter is 2"
////    }
////    def c2 = {
////      list.get(anyInt) answers { (i, m) => "The parameters are " + (i.asInstanceOf[Array[_]].mkString, m) }
////      list.get(1) must_== "The parameters are (1,list)"
////    }
////    def c3 = {
////      list.size answers { m => m.toString.size }
////      list.size must_== 4
////    }
////  }
////  case class ordered() {
////    val list1 = mock[java.util.List[String]]
////    val list2 = mock[java.util.List[String]]
////
////    def asExpected1: Result = {
////      list1.get(0)
////      list2.get(0)
////      implicit val order = inOrder(list1, list2)
////      (there was one(list1).get(0) then
////                 one(list2).get(0)).message must_== "The mock was called as expected"
////    }
////
////    def asExpected2: Result = {
////      list1.get(1) returns "1"
////
////      // there is an out of order call but to a stubbed method
////      list1.get(1)
////      list1.get(0)
////      list2.get(0)
////
////      implicit val order = inOrder(ignoreStubs(list1, list2))
////      (there was one(list1).get(0) then
////        one(list2).get(0)).message must_== "The mock was called as expected"
////    }
////
////    def failed = {
////      list1.get(0)
////      list2.get(0)
////      implicit val order = inOrder(list1, list2)
////      (there was one(list2)(order).get(0) then
////                 one(list1)(order).get(0)).message must startWith("The mock was not called as expected")
////    }
////
////    def failed2: execute.Result = {
////      list1.get(0); list1.size; list1.get(0); list1.size;
////
////      implicit val order = inOrder(list1)
////      val result = there was one(list1).get(0) then
////                             one(list1).size() then
////                             no(list1) .get(0) then
////                             one(list1).size()
////
////      result.message must startWith("The mock was not called as expected")
////    }
////  }
////  case class captured() {
////    val list = mock[java.util.List[String]]
////    def e1 = {
////     list.get(1)
////      val c = capture[Int]
////       there was one(list).get(c)
////      c.value must_== 1
////    }
////
////    def e2 = {
////      list.get(1)
////      list.get(2)
////      val c = capture[Int]
////      there was two(list).get(c)
////      c.values.toString must_== "[1, 2]"
////    }
////  }
////
////}
////
////case class reuse() extends FragmentExecution with MustMatchers {
////  implicit val args = main.Arguments()
////
////  def e1 = {
////    val s = new mutable.Specification with Mockito {
////      val list = mock[java.util.List[String]]
////      "ex1" in {
////        list.add("one")
////        there was one(list).add("two")
////        1 must_== 1 // to check if the previous expectation really fails
////      }
////    }
////    s.content.fragments.collect { case Example(d, r) => executeBody(r()).isSuccess } must contain (false)
////  }
////
////  def e2 = {
////    val s = new Mockito with JUnitExpectations {
////      val list = mock[java.util.List[String]]
////      def test = {
////        list.add("one")
////        there was one(list).add("two")
////        1 must_== 1 // to check if the previous expectation really fails
////      }
////    }
////    s.test must throwAn[AssertionFailedError]
////  }
////}
////
////trait WithFunctionNothing { def call(f: Int => Nothing) = 1 }
////trait WithFunctionAny { def call(f: () => Any) = 1 }