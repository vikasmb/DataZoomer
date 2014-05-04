package org.dm;

import java.io.*;
import java.util.*;


public class QueryPredictor {

	private HashMap<String,String> map;
	final public String[] fields={"ymdh","user_id","impressions","clicks","conversions","campaign_id","creative_id","region_id","msa_id","size_id","amt_paid_to_media_seller","amt_paid_to_data_seller","data_revenue_from_buyer","media_revenue_from_buyer","amt_paid_to_broker","section_id","site_id","netspeed_id","user_agent","query_string","pop_type_id","roi_cost","gender","age","creative_frequency","vurl_frequency","language_ids","isp_id","offer_type_id","conversion_id","trigger_by_click","ecpm","second_ecpm","ltv_value","rtb_transaction_type","first_initial_bid_amount","first_initial_bid_price_type","second_initial_bid_amount","second_initial_bid_price_type","screen_type","rich_media_flag","geo_best","geo_best_level","content_rev_share","mobile_wifi","marketplace_type","psa_flag","house_ad_flag","passback_flag","is_coppa","device_segment","connection_segment","os_segment","browser_segment","bill_revenue_from_buyer","container_type"};
	
	
	public QueryPredictor(){
	      try
	      {
	         FileInputStream fis = new FileInputStream("/home/vikas/hashmap.ser");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         map = (HashMap) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(IOException ioe)
	      {
	         ioe.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Class not found");
	         c.printStackTrace();
	         return;
	      }
	      
	      for (String name: map.keySet()){

	            String value = map.get(name);  
	            System.out.println(name + " " + value);  


	      } 

		
		
	}
	
	
	public static void main(String[] args) {

		QueryPredictor q=new QueryPredictor();
		q.start();
		List<String> attrList=new ArrayList<String>();
		attrList.add("region_id");
		//attrList.add("impressions");
		List<String> list=q.getNextAttribute(attrList);
		for(String s:list){
			System.out.println("Predicted:"+s);
		}
				

	}
	
	public void start()
	{
		List<String> queryAttributes= Arrays.asList("site_id");
		List<String> nextAttribute=getNextAttribute(queryAttributes);
		System.out.println("Printing next Attributes");
		for(String next:nextAttribute)
		{
			System.out.println(next);
		}
		
		
	}
	
	
	public List<String> getNextAttribute(List<String> queryAttributes){
		
		List<String> nextFields=new ArrayList<String>();
		String key="";
		for(String queryField:queryAttributes)
		{
			int a=Arrays.asList(fields).indexOf(queryField);
			key=key+a+" ";
			
		}
			
		String next=map.get(key);
		if(next==null)
		{
			return nextFields;
		}
			
		
		String[] indQueries=next.split(" ");

		for(String nextField:indQueries)
		{
			nextFields.add(fields[Integer.parseInt(nextField)]);
		}
		
		
		
		return nextFields;

	}
	

}