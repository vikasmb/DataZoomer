package org.dm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryOptimizer {
private Map<String,String> attributeCubeMap;

public QueryOptimizer(Map<String, String> attributeCubeMap) {
	super();
	this.attributeCubeMap = attributeCubeMap;
}

public List<Query> getOptimizedQueries(Query originalquery){
	List<Query> optimizedQueryList=new ArrayList<Query>();
	//Query rewriting logic
	return optimizedQueryList;
}
}
