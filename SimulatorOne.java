import java.util.*;
import java.io.*;

public class SimulatorOne{
	public static void main(String args[]){
		//String filepath = args[0];
      
      try{
   		//File f = new File(filepath);
   		//BufferedReader br = new BufferedReader(new FileReader(f));
   		BufferedReader br = new BufferedReader(new InputStreamReader (System.in));
         String line =br.readLine();
         
   		int count =0;
   		int numNodes=0;
   		int numEdges =0;
         int numDrivers =0;
         int numRequests =0;
   		String parts[] = null;
         ArrayList<String> drivers = new ArrayList<String>();
         ArrayList<String> requests = new ArrayList<String>();
         Graph g = new Graph();
         
   		while(line!=null){
   			if (count==0){
   				numNodes = Integer.parseInt(line);
   			}
   			if(count>0 && count<=numNodes){
   				parts = line.split(" ");
   				numEdges= parts.length -1;
               if(numEdges>0){
      				for(int i=1; i<=numEdges; i+=2){
                     g.addEdge(parts[0],parts[i], Integer.parseInt(parts[i+1]));
      				}
               }
   			}
            if(count==numNodes+1){
               numDrivers = Integer.parseInt(line);
            }
            if(count==numNodes+2){
               parts = line.split(" "); 
               for(int i=0; i<parts.length;i++){
                  drivers.add(parts[i]);
               }
            }
            if(count==numNodes+3){
               numRequests = Integer.parseInt(line);
            }
            if(count==numNodes+4){
              parts = line.split(" "); 
               for(int i=0; i<parts.length;i++){
                  requests.add(parts[i]);
               } 
            }
   			count++;
   			line= br.readLine();
   		}//data loaded
         
         double minCost =0;
         double totalCost =0;
         int driverIndex =0;
         boolean unreachable = true;
         int numIterations =0;
         if (requests.size()==2) 
            numIterations = 1;
         else 
            numIterations = requests.size() - 1;
            //System.out.println(numIterations);
         for(int i=0; i<=numIterations; i++){
            //System.out.println("i: " + i);
            if(i%2==0){
               System.out.println("client " + requests.get(i) + " " + requests.get(i+1));
               for(int j=0; j<numDrivers; j++){
                  g.dijkstra(drivers.get(j));
                  totalCost+= g.getCost(requests.get(i));
                  g.dijkstra(requests.get(i));
                  totalCost+= g.getCost(requests.get(i+1));
                  g.dijkstra(requests.get(i+1));
                  totalCost+= g.getCost(drivers.get(j));
                  //System.out.println("truck " + drivers.get(j) + "cost " + totalCost);
                  if (totalCost >= Double.MAX_VALUE){
                     unreachable = true;
                     continue;
                  }
                  if (j == 0){
                     minCost = totalCost;
                     unreachable = false;
                     driverIndex = 0;
                  }else if (totalCost<minCost){
                     unreachable = false;
                     minCost = totalCost;
                     driverIndex = j;
                  }else if (totalCost==minCost && Integer.parseInt(drivers.get(driverIndex))>Integer.parseInt(drivers.get(j))){
                     driverIndex = j; 
                     unreachable = false;
                     minCost = totalCost;
                  }else{
                     
                  }
                  
                  totalCost=0;
               }
               if(unreachable){
                  System.out.println("cannot be helped");
               }else{
                  System.out.println("truck " + drivers.get(driverIndex));
                  g.dijkstra(drivers.get(driverIndex));
                  g.printPath(requests.get(i));
                  System.out.println("pickup " + requests.get(i));
                  g.dijkstra(requests.get(i));
                  g.printPath(requests.get(i+1));
                  System.out.println("dropoff " + requests.get(i+1));
                  g.dijkstra(requests.get(i+1));
                  g.printPath(drivers.get(driverIndex));
                  //System.out.println("");
               }
             }
         
         }
         
      }catch(IOException e){
         System.out.println(e);
      }
	}
}
