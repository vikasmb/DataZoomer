package org.dm;

import java.util.*;
import java.util.List;
import java.util.Map;

public class QueryOptimizer {
	private Map<String,String> attributeCubeMap;
	private String previousGroupAttribute;
	private String previousQueryName;
	private boolean haveCubed;
	private List<String> allAttributes;
	private List<String> cubedAttributes;
	private String cubeName;
	

	public QueryOptimizer(String cubeName, List<String> cubedAttributes, List<String> allAttributes) {
		super();
		this.allAttributes = allAttributes;
		this.cubedAttributes = cubedAttributes;
		this.cubeName = cubeName;
		this.previousQueryName = "";

		haveCubed = false;
	}

	public List<String> getOptimizedQueries(String originalquery, String queryattribute, String currentPrediction){


		int i = 0;
		int count = 0;
		int pos;
		List<String> optimizedQueryList= new ArrayList<String>();

		String [] tmpQuery = originalquery.split(" ");
		List<String> splitQuery = new ArrayList<String>();

		for (i=0; i< tmpQuery.length; i++){
			splitQuery.add(tmpQuery[i]);
		}

		if (containsNonCubedAttributes(queryattribute) == false){
			pos = splitQuery.indexOf("FILTER");
			pos++;
			splitQuery.set(pos,this.cubeName);
				
			pos = splitQuery.indexOf(queryattribute);

			if (pos >= 0){
				splitQuery.set(pos, "group." + queryattribute);
			}
			
			optimizedQueryList.add(constructQuery(splitQuery));
			optimizedQueryList.add(flattenCube(splitQuery.get(0)));
			optimizedQueryList.add(groupOnPrediction(splitQuery.get(0), currentPrediction));		
		
		} else if (containsPreviousGrouping(queryattribute) == true){
			System.out.println("Contains previous grouping");
			pos = splitQuery.indexOf("FILTER");
			pos++;
			splitQuery.set(pos,"tmp");
			pos = splitQuery.indexOf(queryattribute);
			splitQuery.set(pos, "group");

			optimizedQueryList.add(constructQuery(splitQuery));
			optimizedQueryList.add(flattenGroup(splitQuery.get(0)));
			optimizedQueryList.add(groupOnPrediction(splitQuery.get(0), currentPrediction));

		} else {
			optimizedQueryList.add(constructQuery(splitQuery));
			optimizedQueryList.add(groupOnPrediction(splitQuery.get(0), currentPrediction));			
		}

		previousGroupAttribute = currentPrediction;
		previousQueryName = splitQuery.get(0);

		return optimizedQueryList;
	}

	
	public String flattenGroup(String name){
		return name + " = FOREACH " + name + " GENERATE flatten(" + this.previousQueryName + ");";
	}

	public boolean containsPreviousGrouping(String queryattribute){
		if (queryattribute.equals(this.previousGroupAttribute)){
			return true;
		}

		return false;
	}

	public String groupOnPrediction(String name, String prediciton){
		return "tmp = GROUP " + name + " by " + prediciton+";";
	}

	public String flattenCube(String name){
		return name + " = foreach " + name + " GENERATE flatten(cube);";
	}

	public String constructQuery(List<String> split){
		String query = split.get(0);

		for (int i=1; i<split.size(); i++){
			query = query + " " + split.get(i);	
		}

		return query;
	}

	public boolean containsNonCubedAttributes(String queryattribute){
		int i;
		int k;

			if (allAttributes.contains(queryattribute)) {
				if (!(cubedAttributes.contains(queryattribute))) {
					System.out.println("return true");
					return true;
				}
			}
			System.out.println("return false");
					
		return false;
	}
}
