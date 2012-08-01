package models;

import static me.prettyprint.hector.api.factory.HFactory.createKeyspace;
import static me.prettyprint.hector.api.factory.HFactory.createMutator;
import static me.prettyprint.hector.api.factory.HFactory.createRangeSlicesQuery;
import static me.prettyprint.hector.api.factory.HFactory.createStringColumnQuery;
import static me.prettyprint.hector.api.factory.HFactory.getOrCreateCluster;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

public class SignalDefDao {

	final static String KEYSPACEINRT = "InternetRT_Global";
	final static String CFRT = "SignalDefination";
	
	final static StringSerializer se = new StringSerializer();
	
	Cluster cluster = getOrCreateCluster("Test Cluster", config.properties.canssadraAddress);

	Keyspace keyspacert = createKeyspace(KEYSPACEINRT,cluster);
	Mutator<String> mrt = createMutator(keyspacert, se);
	
	public List<String> getAllSignals(){
		RangeSlicesQuery<String, String, String> rangeSlicesQuery = createRangeSlicesQuery(keyspacert, se, se, se);
		rangeSlicesQuery.setColumnFamily(CFRT);
		rangeSlicesQuery.setRange("", "", false, 100);
		rangeSlicesQuery.setRowCount(100);
		QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
		OrderedRows<String, String, String> orderedRows = result.get();
		List<Row<String, String, String>> rows = orderedRows.getList();
		
		List<String> appList = new ArrayList<String>();
		ColumnQuery<String, String, String> columnQuery = createStringColumnQuery(keyspacert);	
		for(Row<String, String, String> row: rows)
		{
			ColumnQuery<String, String, String> rowQuery = columnQuery.setColumnFamily(CFRT).setKey(row.getKey());
			//HColumn<String,String> defination = rowQuery.setName("value").execute().get();
			//if (defination != null){
				appList.add(row.getKey());
			//}
		}
		return appList;				
	}
}
