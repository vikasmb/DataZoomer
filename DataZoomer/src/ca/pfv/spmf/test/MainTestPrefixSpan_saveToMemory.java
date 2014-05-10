package ca.pfv.spmf.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan.AlgoPrefixSpan;
import ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan.SequentialPattern;
import ca.pfv.spmf.input.sequence_database_list_integers.SequenceDatabase;


/**
 * Example of how to use the PrefixSpan algorithm in source code.
 * @author Philippe Fournier-Viger
 */
public class MainTestPrefixSpan_saveToMemory {

	public static void main(String [] arg) throws IOException{    
		// Load a sequence database
		SequenceDatabase sequenceDatabase = new SequenceDatabase(); 
		sequenceDatabase.loadFile(fileToPath("contextPrefixSpan.txt"));
		// print the database to console
		sequenceDatabase.print();
		
		// Create an instance of the algorithm 
		AlgoPrefixSpan algo = new AlgoPrefixSpan(); 
//		algo.setMaximumPatternLength(3);
		
		// execute the algorithm with minsup = 50 %
		algo.runAlgorithm(sequenceDatabase, 0.01, null);    
		algo.printStatistics(sequenceDatabase.size());
		
		// create hashSet of 
		HashMap<String, String> nextVal = new HashMap<String, String>();
		HashMap<String, Integer> nextValScore = new HashMap<String, Integer>();
		List<SequentialPattern> level2 =algo.getSecond();
		for(SequentialPattern pattern:level2)
		{
			String query=pattern.get(0).toString();
			String next=pattern.get(1).toString();
			if( nextVal.containsKey(query))
			{
				if(pattern.getAbsoluteSupport()>nextValScore.get(query))
				{
					nextVal.put(query,next);
					nextValScore.put(query,pattern.getAbsoluteSupport());	
				}
			}
			else
			{
				nextVal.put(query,next);
				nextValScore.put(query,pattern.getAbsoluteSupport());
			}
		}
		
		
		
		//save hashmap
        try
        {
               FileOutputStream fos =
                  new FileOutputStream("hashmap.ser");
               ObjectOutputStream oos = new ObjectOutputStream(fos);
               oos.writeObject(nextVal);
               oos.close();
               fos.close();
               System.out.printf("Serialized HashMap data is saved in hashmap.ser");
        }catch(IOException ioe)
         {
               ioe.printStackTrace();
         }		
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestPrefixSpan_saveToMemory.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}