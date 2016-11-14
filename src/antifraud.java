import java.awt.Window;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Struct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.type.IntersectionType;

public class antifraud {

	public static class Node
	{
		public Node()
		{}
		public String Src;
		public String Des;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 HashMap<String, HashSet<String>> network = new HashMap<>();
		 network = BuildNetwork();
		 ValidateTransactaion(network);
		 
	}
	
	private static HashMap<String, HashSet<String>> BuildNetwork()
	{
		HashMap<String, HashSet<String>> network = new HashMap<>();
		
		try (BufferedReader br = Files.newBufferedReader
				(Paths.get("paymo_input\\batch_payment.txt"), 
						StandardCharsets.UTF_8)) 
		{
		    for (String line = null; (line = br.readLine()) != null;) 
		    {
		        if(line.indexOf("time")>-1)
		        continue;
		        else
		        {
		        	Node node = GetSrcDes(line);
		        	if(network.containsKey(node.Des) )
		        	{
		        		HashSet< String > child = network.get(node.Des);
		        		child.add(node.Src);
		        	}
		        	else
		        	{

		        		HashSet<String> child = new HashSet<>();
		        		child.add(node.Src);
		        		network.put(node.Des, child);
		        	}
		        	
		        	if( network.containsKey(node.Src))
		        	{
		        		HashSet<String> child = network.get(node.Src);
		        		child.add(node.Des);
		        	}
		        	else
		        	{
		        		HashSet<String> child = new HashSet<>();
		        		child.add(node.Des);
		        		network.put(node.Src, child);
		        		
		        	}
		    
		        	
		        	
		        }
		        
		    }
		    br.close();
		   
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return network;
	}

	private static void ValidateTransactaion(HashMap<String, HashSet<String>> network)
	{
		int index = 1;
	if(network== null || network.size()==0)
		return;
	//create the output file by checking the latest ourput  file number
		String path= "paymo_output\\output";
		String name = String.valueOf(index);
		File file = new File(path + name + ".txt");

		while(file.exists())
		{
		     index++;
		     // reassign file this while will terminate when #.txt doesnt exist
		     name = String.valueOf(index);
		     file = new File(path+ name + ".txt");
		} // the while should end here then we check again that #.txt doesnt exist and try to create it
		
		
		if(!file.exists())
		{
			
		
		try (BufferedReader br = Files.newBufferedReader(Paths.get("paymo_input\\stream_payment.txt"), StandardCharsets.UTF_8)) 
		{
			 file.createNewFile();
			    FileWriter fw = new FileWriter(file.getAbsoluteFile());
			    BufferedWriter bw = new BufferedWriter(fw);
			for (String line = null; (line = br.readLine()) != null;) 
			{
				if(line.indexOf("time")>-1) continue;
				boolean status = false;
				Node node = GetSrcDes(line);
				String src = node.Src;
				String des = node.Des;
				// are two persons exist?
				if (!network.containsKey(src) || !network.containsKey(des))
				{
					status = false;
					bw.write("unverified");
					bw.newLine();
					continue;
				}
				//first condition: are two persons first_friend?
				else if(network.get(src).contains(des))
				{

					status= true;
					bw.write("trusted");
					bw.newLine();
					continue;
				}
				// second fiend?
				else 
				{
					Set<String> first = new HashSet<String>(network.get(src));
					Set<String > temp = new HashSet<String>();
					Set<String> second = new HashSet<String>( second=network.get(des));
					if(first.size()> second.size())
					{
					 temp.addAll(first);
					 temp.retainAll(second);
					}
					else
					{
						temp.addAll(second);
						temp.retainAll(first);
					}
					//are they second_friend?
					if(temp!= null && temp.size()>0)
					{

						status = true;
						bw.write("trusted");
						bw.newLine();
						continue;
						
					}
				
					// if they are not friend of friend of each other so I am goind to check from both side
					// it means I am retrieving  the friend of friend of first person and same for second person. 
					// it mean instead of retrieving the friend of first person three times, 
					//retrieving the friend of each person(first and second ) two times and finding the overlaps.
					HashSet<String> srcCh2 = new HashSet<>();
					
					HashSet<String> desCh2 = new HashSet<>();
					HashSet<String> temp2 = new HashSet<>();
					for (String ch : first) 
						srcCh2.addAll(network.get(ch));
					srcCh2.addAll(first);
					for (String ch :second) 
						desCh2.addAll(network.get(ch));
					desCh2.addAll(second);
					if(srcCh2.size()> desCh2.size())
					{

						temp2.addAll(srcCh2);
						temp2.retainAll(desCh2);
					}
					else
					{

						temp2.addAll(desCh2);
						temp2.retainAll(srcCh2);
					}
			
					if(temp2!= null && temp2.size()>0)
					{

						status = true;
						bw.write("trusted");
						bw.newLine();
						continue;
					}					
					
						
				

					
				}
				status = false;
				bw.write("unverified");
				bw.newLine();
				
			}
			br.close();
			bw.close();
			System.out.println("Done");
		}
		
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			// TODO: handle exception
		}
		}

	}
	
	private static Node GetSrcDes(String line )
	{
		Node resutl = new Node();
		String[] str= line.split(",");
		resutl.Des=str[2].trim();
		resutl.Src= str[1].trim();
		return resutl;
		
	}
	

}
