import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

/**
 * An undirected, weighted, simple graph.  The vertex set is static; edges
 * may be added but not removed.
 *
 * @param K the type of the vertices
 * @author Jim Glenn
 * @version 0.1 2015-10-27
 */

public class WeightedGraph<K> implements Graph<K>
{
    /**
     * This graph's vertex set.
     */
    private Set<K> verts;

    /**
     * This graph's adjacency lists.
     */
    private Map<K, List<HashMap<K, Integer>>> adjLists;

    /**
     * Creates a graph with the given vertex set and no edges.
     *
     * @param v a collection of vertices
     */
    public WeightedGraph(List<K> v)
	{
	    // make ourselves a private copy of the vertex set
	    verts = new HashSet<K>(v);

	    // set up empty adkacency lists for each vertex
	    adjLists = new HashMap<K, List<HashMap<K, Integer>>>();
	    for (K src : verts)
		{
		    adjLists.put(src, new ArrayList<HashMap<K, Integer>>());
		}
	}

    /**
     * Adds the given edge to this graph if it does not already exist.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     */
    public void addEdge(K u, K v, int w)
    {
	if (u.equals(v))
	    {
		throw new IllegalArgumentException("adding self loop");
	    }

	// get u's adjacency list
	List<HashMap<K, Integer>> adj = adjLists.get(u);

	// check for edge already being there
	int count=0;
	for (HashMap<K, Integer> A:adj){
		for (K vertex:A.keySet()){
			if (!vertex.equals(v))
		    {
				count++;

		    }
		}
		
	}
	if (count==adj.size()){
		HashMap<K, Integer> edge;
		edge=new HashMap<K, Integer>();
		edge.put(v, w);
		// edge is not already there -- add to both adjacency lists
		adj.add(edge);
		HashMap<K, Integer> edge2=new HashMap<K, Integer>();
		edge2.put(u, w);
		adjLists.get(v).add(edge2);
	}
	
    }
    public void addSimple(K u, K v, int w){
    	if (u.equals(v))
	    {
		throw new IllegalArgumentException("adding self loop");
	    }
    	
    	// get u's adjacency list
    	List<HashMap<K, Integer>> adj = adjLists.get(u);
    	HashMap<K, Integer> edge=new HashMap<K, Integer>();
    	edge.put(v,w);
    	adj.add(edge);
    }
    
    public void removeEdge(K u, K v)
    {

	// get u's adjacency list
	List<HashMap<K, Integer>> adj = adjLists.get(u);

	// check for edge already being there
	for (int i=0;i<adj.size();i++){
		for (K vertex:adj.get(i).keySet()){
			if (vertex.equals(v))
		    {
				adj.remove(adj.get(i));
				

		    }
		}
		
		
	}
	
	List<HashMap<K, Integer>> adj2 = adjLists.get(v);

	// check for edge already being there
	for (int i=0;i<adj2.size();i++){
		for (K vertex:adj2.get(i).keySet()){
			if (vertex.equals(u))
		    {
				adj2.remove(adj2.get(i));

		    }
		}
		
		
	}
	
    }
    public void simpleRemove(K u, K v){
    	List<HashMap<K, Integer>> adj = adjLists.get(u);
    	
    	for (int i=0;i<adj.size();i++){
    		for (K vertex:adj.get(i).keySet()){
    			if (vertex.equals(v))
    		    {
    				adj.remove(adj.get(i));

    		    }
    		}
    		
    	}
    }
    
    public List<HashMap<K, Integer>> get(K key){
    	return adjLists.get(key);
    }

    /**
     * Determines if the given edge is present in this graph.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     * @return true if and only if the edge (u, v) is in this graph
     */
    public boolean hasEdge(K u, K v)
	{
    	List<HashMap<K, Integer>> adj = adjLists.get(u);

    	// check for edge already being there
    	int count=0;
    	for (HashMap<K, Integer> A:adj){
    		for (K vertex:A.keySet()){
    			if (!vertex.equals(v))
    		    {
    				count++;

    		    }
    		}
    		
    	}
    	if (count==adj.size()) return false;
    	return true;
	}

    /**
     * Returns an iterator over the vertices in this graph.
     *
     * @return an iterator over the vertices in this graph
     */
    public Iterator<K> iterator()
    {
	return (new HashSet<K>(verts)).iterator();
    }

    /**
     * Returns an iterator over the neighbors of the vertices in this graph.
     *
     * @param v a vertex in this graph
     * @return an iterator over the vertices in this graph
     */
    public Iterable<K> neighbors(K v)
    {
    	List<K> list=new ArrayList<K>();
    	List<HashMap<K, Integer>> adj = adjLists.get(v);
    	for (HashMap<K,Integer> A:adj){
    		for (K vertex:A.keySet()){
    			list.add(vertex);
    		}
    	}
	return new ArrayList<K>(list);
    }

    /**
     * Returns a printable represenation of this graph.
     *
     * @return a printable representation of this graph
     */
    public String toString()
    {
	return adjLists.toString();
    }

	public Set<K> keySet() {
		// TODO Auto-generted method stub
		return adjLists.keySet();
	}


	
}