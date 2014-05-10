import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;


public class initCounter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader br;
		try
		{
		br = new BufferedReader(new FileReader("/Users/mariyamkhalid/Documents/workspace/history.txt"));
		String line;
		HashMap<Integer,Integer> counts=new HashMap<Integer, Integer>();
		while ((line = br.readLine()) != null) {
		   String[] a=line.split(" ");	
		   System.out.println(a[0]);

		   if(counts.containsKey(Integer.parseInt(a[0])))
		   {
			   counts.put(Integer.parseInt(a[0]), counts.get(Integer.parseInt(a[0])) +1 );
			   
		   }
		   else
		   {
			   counts.put(Integer.parseInt(a[0]), 1);
		   }
		}
		
		
		String content = "This is the content to write into file";
		 
		File file = new File("/Users/mariyamkhalid/Documents/workspace/initial.txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for(Integer a:counts.keySet())
		{
			if(counts.get(a)>1)
			{	String field=""+a+"\n";
				bw.write(field);
			}
			
		}
		
		bw.close();
		br.close();

		}
		catch(Exception e)
		{
			
		}

	}

}
