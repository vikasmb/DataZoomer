package org.dm;

import java.util.List;

public class QueryPredictor {

	public String getNextAttribute(List<String> queryAttributes){
		return "region_id#90"; //Next predicted attribute name#confidence level
	}
}
