package org.dm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.pig.ExecType;
import org.apache.pig.PigServer;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;

public class QueryExecutor {
	private PigServer pigServer;
	private QueryOptimizer optimizer;
	private QueryPredictor predictor;
	final public String[] fields = { "ymdh", "user_id", "impressions",
			"clicks", "conversions", "campaign_id", "creative_id", "region_id",
			"msa_id", "size_id", "amt_paid_to_media_seller",
			"amt_paid_to_data_seller", "data_revenue_from_buyer",
			"media_revenue_from_buyer", "amt_paid_to_broker", "section_id",
			"site_id", "netspeed_id", "user_agent", "query_string",
			"pop_type_id", "roi_cost", "gender", "age", "creative_frequency",
			"vurl_frequency", "language_ids", "isp_id", "offer_type_id",
			"conversion_id", "trigger_by_click", "ecpm", "second_ecpm",
			"ltv_value", "rtb_transaction_type", "first_initial_bid_amount",
			"first_initial_bid_price_type", "second_initial_bid_amount",
			"second_initial_bid_price_type", "screen_type", "rich_media_flag",
			"geo_best", "geo_best_level", "content_rev_share", "mobile_wifi",
			"marketplace_type", "psa_flag", "house_ad_flag", "passback_flag",
			"is_coppa", "device_segment", "connection_segment", "os_segment",
			"browser_segment", "bill_revenue_from_buyer", "container_type" };
	final List<String> fieldList = Arrays.asList(fields);
	private Random random = new Random();

	public QueryExecutor() {
		pigServer = PigService.getInstance().getServer();
		List<String> cubedAttrs = new ArrayList<String>();
		cubedAttrs.add("user_agent");
		cubedAttrs.add("gender");
		cubedAttrs.add("region_id");
		cubedAttrs.add("size_id");
		optimizer = new QueryOptimizer("cubedData", cubedAttrs, fieldList);
		predictor = new QueryPredictor();
	}

	// Initialise queryoptimizer with the attributes and their corresponding
	// cube

	// Call queryoptimizer with the original query and the predicted attribute
	// got from the query prediction module
	// Query prediction module would return the list of queries to be executed .

	// QueryExecutor would perform the co-ordination of the execution and
	// execute the actual queries over embedded pig interface.

	// Only FILTER commands are executed here
	public void execute(String command, boolean filterCommand) {
		if (!filterCommand) {
			try {
				System.out.println("*******Sending optQuery: " + command
						+ " to Pig Server");
				pigServer.registerQuery(command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		/*
		 * String alias=command.split("\\s*=")[0]; try { pigServer.store(alias,
		 * "/tmp/"+random.nextInt(5000)); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		List<String> queryAttributes = new ArrayList<String>();
		String qAttr = null;
		for (String str : fieldList) {
			if (command.contains(str)) {
				qAttr = str;
				queryAttributes.add(str);
				break;
			}
		}
		List<String> predictedAttributes = predictor
				.getNextAttribute(queryAttributes);
		List<String> optimizedQueries = optimizer.getOptimizedQueries(command,
				qAttr, predictedAttributes.get(0));
		FacesContext context = FacesContext.getCurrentInstance();

		/*
		 * context.addMessage(null, new FacesMessage("Successful", "Hello " +
		 * text)); context.addMessage(null, new FacesMessage("Second Message",
		 * "Additional Info Here..."));
		 */
		for (String optQuery : optimizedQueries) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Query rewritten: " + optQuery, null);
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println(optQuery);
		}
		optimizedQueries.remove(0);
		for (String optQuery : optimizedQueries) {
			try {
				System.out.println("*******Sending optQuery: " + optQuery
						+ " to Pig Server");
				pigServer.registerQuery(optQuery);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			pigServer.store("tmp", "/tmp/" + random.nextInt(5000));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		QueryExecutor exec = new QueryExecutor();
		exec.execute("A = FILTER trialData BY region_id == 5;", true);
	}
}
