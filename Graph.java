import java.util.*;

public class Graph
{
    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );

    /**
     * Add a new edge to the graph.
     */
    public void addEdge( String sourceName, String destName, double cost )
    {
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        v.adj.add( new Edge( w, cost ) );
    }

    /**
     * Driver routine to handle unreachables and print total cost.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     */
    public void printPath( String destName )
    {
        Vertex w = vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            //System.out.print( "(Cost is: " + w.dist + ") " );
            printPath( w,0 );
            if (w.multiple==false)
               System.out.println( );
        }
    }
    
    public double getCost(String destName){
      Vertex w = vertexMap.get( destName );
      if (w!=null){
         return w.dist;
      }else
         return INFINITY;
    }

    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    public Vertex getVertex( String vertexName )
    {
        Vertex v = vertexMap.get( vertexName );
        if( v == null )
        {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }

    /**
     * Recursive routine to print shortest path to dest
     * after running shortest path algorithm. The path
     * is known to exist.
     */
    public void printPath( Vertex dest , int count)
    {
        if (dest.multiple==true && count ==0){
            System.out.println("multiple solutions cost " + (int)dest.dist);
        }else{
			if( dest.prev != null )
           {
               printPath( dest.prev ,1);
               System.out.print( " " );
           }
           System.out.print( dest.name );
		   
       }
    }
    
    /**
     * Initializes the vertex output info prior to running
     * any shortest path algorithm.
     */
    public void clearAll( )
    {
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }

    /**
     * Single-source weighted shortest-path algorithm. (Dijkstra) 
     * using priority queues based on the binary heap
     */
    public void dijkstra( String startName )
    {
        PriorityQueue<Path> pq = new PriorityQueue<Path>( );

        Vertex start = vertexMap.get( startName );
        if( start == null )
            return;

        clearAll( );
        pq.add( new Path( start, 0 ) ); start.dist = 0;
        
        int nodesSeen = 0;
        while( !pq.isEmpty( ) && nodesSeen < vertexMap.size( ) )
        {
            Path vrec = pq.remove( );
            Vertex v = vrec.dest;
            if( v.scratch != 0 )  // already processed v
                continue;
                
            v.scratch = 1;
            nodesSeen++;

            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                double cvw = e.cost;
                
                if( cvw < 0 ){
                    System.out.println("Graph has negative edges" );
                    break;
                }
              
                /*System.out.println(" ");
                System.out.println("start " + v.name);
                System.out.println("dest " + w.name);
                System.out.println("w.dist " + w.dist);
                System.out.println("v.dist + cvw" + (v.dist+cvw));*/
                if( w.dist > v.dist + cvw )
                {
                    w.dist = v.dist +cvw;
                    w.prev = v;
                    pq.add( new Path( w, w.dist ) );
                    if(v.multiple == true){
                     w.multiple = true;
                     //System.out.println(w.name + " MULTIPLE");
                     }
               }else if ((w.dist == v.dist + cvw)){
               //System.out.println(w.name + " multiple");
                  w.multiple = true;
                  w.scratch=0;
                }
            }
        }
        
    }

   }

