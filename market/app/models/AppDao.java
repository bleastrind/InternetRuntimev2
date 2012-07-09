package models;
/*
 * class for AppDao with some methods
 */

import java.util.*;
import me.prettyprint.hector.api.beans.*; 
import static me.prettyprint.hector.api.factory.HFactory.*;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.*;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.*;
import me.prettyprint.cassandra.serializers.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppDao {
	final static String KEYSPACE = "InternetRuntime";
	final static String CF = "App";
	final static StringSerializer se = new StringSerializer();
	
	Cluster cluster = getOrCreateCluster("Test Cluster", "127.0.0.1:9160");
	Keyspace keyspace = createKeyspace(KEYSPACE, cluster);	
	Mutator<String> m = createMutator(keyspace, se);
	
	public void save(App instance){
		m.insert(instance.getId(), CF, createStringColumn("name", instance.getName()));
		m.insert(instance.getId(), CF, createStringColumn("information", instance.getInformation()));
		m.insert(instance.getId(), CF, createStringColumn("installUrl", instance.getInstallUrl()));			
	}
	

	public void delete(App instance){	
		
	//	m = createMutator(keyspace, se);
	//	m.addDeletion(instance.getId(), CF, null, se);
		instance.getId().equals("");
		m.delete(instance.getId(), CF, null, se);
	//	m.execute();
	}
	
	public List<App> getAllApps()
	{
		RangeSlicesQuery<String, String, String> rangeSlicesQuery = createRangeSlicesQuery(keyspace, se, se, se);
		rangeSlicesQuery.setColumnFamily(CF);
		rangeSlicesQuery.setRange("", "", false, 100);
		rangeSlicesQuery.setRowCount(100);
		QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
		OrderedRows<String, String, String> orderedRows = result.get();
		List<Row<String, String, String>> rows = orderedRows.getList();
		
		List<App> appList = new ArrayList<App>();
		ColumnQuery<String, String, String> columnQuery = createStringColumnQuery(keyspace);	
		for(Row<String, String, String> row: rows)
		{
			ColumnQuery<String, String, String> rowQuery = columnQuery.setColumnFamily(CF).setKey(row.getKey());
			if (rowQuery.setName("name").execute().get() != null){
				App temp = new App(row.getKey(),
					rowQuery.setName("name").execute().get().getValue(),
					rowQuery.setName("information").execute().get().getValue(),
					rowQuery.setName("installUrl").execute().get().getValue()
				);
				appList.add(temp);
			}
		}
		return appList;		
	}	
	public App findById(String id)
	{
		System.out.println("Find APP by ID:"+id);
		ColumnQuery<String, String, String> columnQuery = createStringColumnQuery(keyspace);
		ColumnQuery<String, String, String> rowQuery = columnQuery.setColumnFamily(CF).setKey(id);
		App app = new App(id,
			rowQuery.setName("name").execute().get().getValue(),
			rowQuery.setName("information").execute().get().getValue(),
			rowQuery.setName("installUrl").execute().get().getValue()
		);
		return app;
	}
}

