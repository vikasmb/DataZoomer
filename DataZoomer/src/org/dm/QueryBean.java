package org.dm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.swing.JOptionPane;

import java.io.IOException;
import java.util.Properties;

//import org.apache.pig.ExecType;
//import org.apache.pig.PigServer;

@ManagedBean
@SessionScoped
public class QueryBean {
	
	private String queryString;
    private QueryExecutor executor;
    
	public QueryBean() {
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/*public String handleCommand(String command, String[] params) {
	
		String outStr = "";
		command="\""+command+"\"";
		System.out.println("Executing command: " + command);
		if(executor==null){
			executor=new QueryExecutor();
		}
		//executor.execute(command);
		return outStr;
	}*/
	
	public void handleCommand() {
		System.out.println("In Actionlistener");
		String outStr = "";
		String command=queryString;
		//command="\""+command+"\"";
		System.out.println("Executing command: " + command);
		if(executor==null){
			executor=new QueryExecutor();
		}
		if(command.contains("FILTER")){
			executor.execute(command);
		}
		//executor.execute(command);
		//return outStr;
	}

	public static void main(String[] args) {
		QueryBean qb = new QueryBean();
		String[] params = null;
		/*qb.handleCommand(
				"A = load '/home/vikas/sampleText' using PigStorage(',');",
				params);
		qb.handleCommand("STORE A into '/tmp/dir2' using PigStorage(',');",
				params);*/
	}

}
