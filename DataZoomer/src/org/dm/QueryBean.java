package org.dm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.swing.JOptionPane;

import org.apache.pig.PigServer;
import org.apache.pig.data.Tuple;

import java.io.IOException;
import java.util.Properties;

//import org.apache.pig.ExecType;
//import org.apache.pig.PigServer;

@ManagedBean
@SessionScoped
public class QueryBean {

	private String queryString;
	private QueryExecutor executor;
    private StringBuilder strBuilder=null;
    private String resultString=new String(); 
	public QueryBean() {
	}

	public String getQueryString() {
		// System.out.println("Getting qs");
		return queryString;
	}

	public void setQueryString(String queryString) {
		// System.out.println("Setting qs");

		this.queryString = queryString;
	}

	/*
	 * public String handleCommand(String command, String[] params) {
	 * 
	 * String outStr = ""; command="\""+command+"\"";
	 * System.out.println("Executing command: " + command); if(executor==null){
	 * executor=new QueryExecutor(); } //executor.execute(command); return
	 * outStr; }
	 */

	public String doAction() {
		// System.out.println("In actionmethod");
		return null;
	}

	public void handlecommand() {
		// System.out.println("In Actionlistener");
		String outStr = "";
		String command = queryString;
		// command="\""+command+"\"";
		System.out.println("Executing command: " + command);
		if (executor == null) {
			executor = new QueryExecutor();
		}
		if (command.contains("FILTER")) {
			executor.execute(command, true);
		} else {
			if (!command.contains("DUMP")) {
				executor.execute(command, false);
			} else {
				PigServer server=PigService.getInstance().getServer();
				Iterator<Tuple> i;
				try {
					String alias1=command.split("\\s+")[1];
					String alias2=alias1.split(";")[0];
					for(String key:server.getAliasKeySet()){
						System.out.println("Current alias:"+key);
					}
					System.out.println("Dumping:"+alias2);
					i = server.openIterator(alias2);
					strBuilder=new StringBuilder();
					while (i.hasNext()) {
						strBuilder.append(i.next()+"\n");						
					}
					resultString=strBuilder.toString();
					//System.out.println("REsult is "+resultString);
				} catch (IOException e) {
					e.printStackTrace();
				}
				

			}

		}
		// executor.execute(command);
		// return outStr;
	}

	public static void main(String[] args) {
		QueryBean qb = new QueryBean();
		String[] params = null;
		/*
		 * qb.handleCommand(
		 * "A = load '/home/vikas/sampleText' using PigStorage(',');", params);
		 * qb.handleCommand("STORE A into '/tmp/dir2' using PigStorage(',');",
		 * params);
		 */
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

}
