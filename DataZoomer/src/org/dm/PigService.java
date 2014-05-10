package org.dm;

import org.apache.pig.ExecType;
import org.apache.pig.PigServer;

public class PigService {
	private static PigService _instance;
	private  PigServer server;
	
	private PigService() {
		String command = "trialData = LOAD '/home/vikas/sampleText' using PigStorage(',');";
		try {
			server = new PigServer(ExecType.LOCAL);
			server.registerQuery(command);
			command = "cubedData = cube trialData BY CUBE(user_agent,gender,region_id,size_id);";
			server.registerQuery(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PigService getInstance(){
		if(_instance==null){
			_instance=new PigService();
		}
		return _instance;
	}

	public  PigServer getServer() {
		return server;
	}

	public void setServer(PigServer server) {
		this.server = server;
	}
}
