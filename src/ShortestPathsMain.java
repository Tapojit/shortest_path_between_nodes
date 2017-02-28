import java.util.*;



public class ShortestPathsMain
{
    //A map of all graph nodes as keys and their predecessor as value
	static Map<String, String> prev;
	//a map of all graph nodes as keys and shortest distance from start to given key as value
    static Map<String, Integer> dist;
    //holds list of nodes between g nodes.
    static Map<String,Map<String,List<String>>> cornerMap;
    //true if edge has been visited, otherwise false
    static Map<String,Map<String,Boolean>> edgeVisited;
    
	public static void main(String[] args)
    {
	Scanner in = new Scanner(System.in);

	// read the first line with the dimensions of the grid
	int width = in.nextInt();
	int height = in.nextInt();
	int n = in.nextInt();

	// THIS WILL MAKE A ARRAY (okay, a list of lists since Java won't allow
	// arrays of generics) OF GRAPHS FOR THE INDIVIDUAL CELLS --
	
	// make an empty graph for each cell
	List<List<WeightedGraph<String>>> g = new ArrayList<List<WeightedGraph<String>>>();
	for (int r = 0; r < height; r++)
	    {
		List<WeightedGraph<String>> row = new ArrayList<WeightedGraph<String>>();
		for (int c = 0; c < width; c++)
		    {
			// make the list of vertices in this cell starting
			// with the corners...
			List<String> verts = new ArrayList<String>();
			verts.add("g" + r + "." + c); // upper left
			verts.add("g" + (r + 1) + "." + c); // lower left
			verts.add("g" + r + "." + (c + 1)); // upper right
			verts.add("g" + (r + 1) + "." + (c + 1)); // lower right

			//...then the interior vertices
			for (int k = 0; k < n; k++)
			    {
				verts.add("v" + r + "." + c + "." + k);
			    }

			// add that graph!
			row.add(new WeightedGraph<String> (verts));
		    }
		g.add(row);
	    }

	// loop over edges to add
	String from;
	while (!(from = in.next()).equals("queries"))
	    {
		String to = in.next();
		int w = in.nextInt();
		
		// the to vertex is always in the interior of the cell
		assert to.charAt(0) == 'v';

		// figure out from the to vertex which cell we're in
		StringTokenizer tok = new StringTokenizer(to.substring(1), ".");	
		int r = Integer.parseInt(tok.nextToken());
		int c = Integer.parseInt(tok.nextToken());
		
		// add the edge to the correct cell
		g.get(r).get(c).addEdge(from, to, w);
	    }

	// MAKE YOUR CORNER GRAPH HERE (might want the ability to label edges with paths
	// they represent)

	// process the queries
	//System.out.println(g);
	cornerMapPopulator(g, width,height);
	while (in.hasNext())
	    {
		from = in.next();
		String to = in.next();
		System.out.println(Dijkstra(g,from,to));
			    }
	
    }
	
	
    public static String Dijkstra(List<List<WeightedGraph<String>>> g, String start, String end){
    	
    	PriorityQueue n=new PriorityQueue();
    	
    	
    	
    	StringTokenizer tok1 = new StringTokenizer(end.substring(1), ".");
		int endR = Integer.parseInt(tok1.nextToken());
		int endC = Integer.parseInt(tok1.nextToken());
		

    	StringTokenizer tok2 = new StringTokenizer(start.substring(1), ".");
		int startR = Integer.parseInt(tok2.nextToken());
		int startC = Integer.parseInt(tok2.nextToken());
		

    	List<String> fromCorners=new ArrayList<String>();
    	fromCorners.add("g" + startR + "." + startC);
    	fromCorners.add("g" + (startR + 1) + "." + startC);
    	fromCorners.add("g" + startR + "." + (startC + 1));
    	fromCorners.add("g" + (startR + 1) + "." + (startC + 1));
    	
    	List<String> toCorners=new ArrayList<String>();
    	toCorners.add("g" + endR + "." + endC);
    	toCorners.add("g" + (endR + 1) + "." + endC);
    	toCorners.add("g" + endR + "." + (endC + 1));
    	toCorners.add("g" + (endR + 1) + "." + (endC + 1));
    	


    	
    	for (String A:g.get(startR).get(startC).keySet()){
    		n.addItem(A, Integer.MAX_VALUE);
    	}
    	for (String B:g.get(endR).get(endC).keySet()) {
    		if (!n.contains(B)){
    			n.addItem(B, Integer.MAX_VALUE);
    		}
    	}
    	
		
		dist.put(start, 0);
    	n.changePriority(start,0);
    	String f=(String)n.peekTop();
    	while (n.getSize()!=0){
    		n.removeItem();
    		
    		//if node being visited is a g node and is from source grid
    		if (f.substring(0,1).equals("g")&&fromCorners.contains(f)){
       		
        		
        		//list of maps of neighboring nodes of 'g' nodes. Maps contain a key indicating neighboring node and edge weight as value
        		List<HashMap<String, Integer>> neighbors=new ArrayList<HashMap<String,Integer>>();
        		
        		for (HashMap<String,Integer> A:g.get(startR).get(startC).get(f)){
        			String[]m=new String[1];
        			for (String a:A.keySet()){
        				m[0]=a;
        			}
        			
        			StringTokenizer tok = new StringTokenizer(m[0].substring(1), ".");
        			int R = Integer.parseInt(tok.nextToken());
        			int C = Integer.parseInt(tok.nextToken());
        			
        			if (m[0].substring(0,1).equals("v")){
        				if(R==startR&&C==startC){
        					neighbors.add(A);
        				}
        			}
        			else {
        				if (toCorners.contains(m[0])){
        					neighbors.add(A);
        				}
        			}
        			
        		}
        		if (toCorners.contains(f)){
        			for (HashMap<String,Integer> A:g.get(endR).get(endC).get(f)){
            			String[]m=new String[1];
            			for (String a:A.keySet()){
            				m[0]=a;
            			}
            			
            			StringTokenizer tok = new StringTokenizer(m[0].substring(1), ".");
            			int R = Integer.parseInt(tok.nextToken());
            			int C = Integer.parseInt(tok.nextToken());
            			
            			if (m[0].substring(0,1).equals("v")){
            				if(R==endR&&C==endC){
            					neighbors.add(A);
            				}
            			}
            			else {
            				if (toCorners.contains(m[0])){
            					neighbors.add(A);
            				}
            			}
            			
            		}
        		}
        		
        		for (int i=0;i<neighbors.size();i++){
        			String[] m=new String[1];
        			for (String a:neighbors.get(i).keySet()){
        				m[0]=a;
        			}
        			if (dist.get(m[0])>dist.get(f)+neighbors.get(i).get(m[0])) {
        				dist.put(m[0],dist.get(f)+neighbors.get(i).get(m[0]));
        				prev.put(m[0], f);
        				n.changePriority(m[0], dist.get(m[0]));
        			}
        			}
        		
        		
        		
        	}
    		//if g-nodes are from destination grid
    		else if(f.substring(0,1).equals("g")&&toCorners.contains(f)){
    			List<HashMap<String, Integer>> neighbors=new ArrayList<HashMap<String,Integer>>();
    			
    			for (HashMap<String,Integer> A:g.get(endR).get(endC).get(f)){
        			String[]m=new String[1];
        			for (String a:A.keySet()){
        				m[0]=a;
        			}
        			
        			StringTokenizer tok = new StringTokenizer(m[0].substring(1), ".");
        			int R = Integer.parseInt(tok.nextToken());
        			int C = Integer.parseInt(tok.nextToken());
        			
        			if (m[0].substring(0,1).equals("v")){
        				if(R==endR&&C==endC){
        					neighbors.add(A);
        				}
        			}
        			else {
        				if (toCorners.contains(m[0])){
        					neighbors.add(A);
        				}
        			}
        			
        		}
        		
        		
        		for (int i=0;i<neighbors.size();i++){
        			String[] m=new String[1];
        			for (String a:neighbors.get(i).keySet()){
        				m[0]=a;
        			}
        			if (dist.get(m[0])>dist.get(f)+neighbors.get(i).get(m[0])) {
        				dist.put(m[0],dist.get(f)+neighbors.get(i).get(m[0]));
        				prev.put(m[0], f);
        				n.changePriority(m[0], dist.get(m[0]));
        			}
        			}
    		}
    		//for v nodes
    		else {
    		
    		StringTokenizer tok = new StringTokenizer(f.substring(1), ".");
    		int fromR = Integer.parseInt(tok.nextToken());
    		int fromC = Integer.parseInt(tok.nextToken());
    		
    		List<HashMap<String, Integer>> neighbors=g.get(fromR).get(fromC).get(f);
    		
    		for (int i=0;i<neighbors.size();i++){
    			String[] m=new String[1];
    			for (String a:neighbors.get(i).keySet()){
    				m[0]=a;
    			}
    			if (dist.get(m[0])>dist.get(f)+neighbors.get(i).get(m[0])) {
    				dist.put(m[0],dist.get(f)+neighbors.get(i).get(m[0]));
    				prev.put(m[0], f);
    				n.changePriority(m[0], dist.get(m[0]));
    			}
    			}}
    		if (f.equals(end)) break;
    		f=(String)n.peekTop();
    		}
    	//creating list of nodes traversed to reach destination
    	String k=f;
		List<String> path=new ArrayList<String>();
    	path.add(k);
    	while (!k.equals(start)){
    		k=prev.get(k);
    		if (!k.equals(start)&&k.substring(0,1).equals("g")&&prev.get(k).substring(0,1).equals("g")){
    			path.addAll(cornerMap.get(k).get(prev.get(k)));
    			k=prev.get(k);
    		}
    		else path.add(k);}
    	
    	Collections.reverse(path);
    	String res=Integer.toString(dist.get(end))+" "+path;
    	//removing g nodes from adjacency lists of g nodes..
    	
    	for (String L:dist.keySet()){
    		if (dist.get(L)!=Integer.MAX_VALUE)dist.put(L, Integer.MAX_VALUE);
    	}
    	
    	return res;
    	
    }

    
    public static void graphTracer(List<List<WeightedGraph<String>>> g,List<String> allGpoints){
    	
    	
    	for (String A:allGpoints){
    		PriorityQueue g1=new PriorityQueue();
    		
    		dist.put(A, 0);
    		
    		//list of possible g-nodes which can be reached from g-node "A"
    		List<String> dest=new ArrayList<String>();
    		for (String B:cornerMap.get(A).keySet()){
    			if (edgeVisited.get(A).get(B)==false) dest.add(B);
    		}
    		int count=dest.size();
    		
    		StringTokenizer tok1 = new StringTokenizer(A.substring(1), ".");
    		int fromR1 = Integer.parseInt(tok1.nextToken());
    		int fromC1 = Integer.parseInt(tok1.nextToken());
    		int sizeR1=g.size();
    		int sizeC1=g.get(0).size();
    		
    		int toR1;
    		int toC1;
			
			//determines grids in which a given 'g' node is present
    		if (fromR1!=0&&fromR1!=sizeR1){
    			toR1=fromR1;
    			fromR1--;
    			
    		}
    		else if(fromR1==0){
    			toR1=fromR1;
    		}
    		else {
    			fromR1--;
    			toR1=fromR1;
    		}
    		
    		if (fromC1!=0&&fromC1!=sizeC1){
    			toC1=fromC1;
    			fromC1--;
    			
    		}
    		else if(fromC1==0){
    			toC1=fromC1;
    		}
    		else {
    			fromC1--;
    			toC1=fromC1;
    		}
    		
    		
    		
    		g1.addItem(A, 0);
    		String f=A;
    		while (count>0){
    			g1.removeItem();
    			if (f.substring(0,1).equals("g")){
    			StringTokenizer tok = new StringTokenizer(f.substring(1), ".");
        		int fromR = Integer.parseInt(tok.nextToken());
        		int fromC = Integer.parseInt(tok.nextToken());
        		int sizeR=g.size();
        		int sizeC=g.get(0).size();
    			
        		int toR;
        		int toC;
    			List<HashMap<String, Integer>> neighbors=new ArrayList<HashMap<String,Integer>>();
    			
    			//determines grids in which a given 'g' node is present
        		if (fromR!=0&&fromR!=sizeR){
        			toR=fromR;
        			fromR--;
        			
        		}
        		else if(fromR==0){
        			toR=fromR;
        		}
        		else {
        			fromR--;
        			toR=fromR;
        		}
        		
        		if (fromC!=0&&fromC!=sizeC){
        			toC=fromC;
        			fromC--;
        			
        		}
        		else if(fromC==0){
        			toC=fromC;
        		}
        		else {
        			fromC--;
        			toC=fromC;
        		}
        		
        		for (int l=fromR;l>=fromR&&l<=toR;l++){
        			for (int a=fromC;a>=fromC&&a<=toC;a++){
        				neighbors.addAll(g.get(l).get(a).get(f));
        			}
        		}
        		
        		for(int m=0;m<neighbors.size();m++){
        			String[] a=new String[1];
        			for (String N:neighbors.get(m).keySet()){
        				a[0]=N;
        			}
        			if (dist.get(a[0])>dist.get(f)+neighbors.get(m).get(a[0])) {
        				dist.put(a[0], dist.get(f)+neighbors.get(m).get(a[0]));
        				if (!g1.contains(a[0])) g1.addItem(a[0], dist.get(a[0]));
        				else g1.changePriority(a[0], dist.get(a[0]));
            			prev.put(a[0], f);
        			}
        			
        		}
        		}
    			else {
    	    		
    	    		StringTokenizer tok = new StringTokenizer(f.substring(1), ".");
    	    		int fromR = Integer.parseInt(tok.nextToken());
    	    		int fromC = Integer.parseInt(tok.nextToken());
    	    		
    	    		List<HashMap<String, Integer>> neighbors=g.get(fromR).get(fromC).get(f);
    	    		
    	    		for (int q=0;q<neighbors.size();q++){
    	    			String[] m=new String[1];
    	    			for (String a:neighbors.get(q).keySet()){
    	    				m[0]=a;
    	    			}
    	    			if (dist.get(m[0])>dist.get(f)+neighbors.get(q).get(m[0])) {
    	    				dist.put(m[0],dist.get(f)+neighbors.get(q).get(m[0]));
    	    				prev.put(m[0], f);
    	    				if (!g1.contains(m[0])) g1.addItem(m[0], dist.get(m[0]));
            				else g1.changePriority(m[0], dist.get(m[0]));
    	    			}
    	    			}}
        		
        		if (g1.getSize()!=0)f=(String) g1.peekTop();
        		for (String Q:dest){
        			if (f.equals(Q)&&edgeVisited.get(A).get(Q)==false){
        				count--;
        				edgeVisited.get(A).put(Q,true);
        				edgeVisited.get(Q).put(A,true);
        				
        				//adding g-node paths to cornerMap
        				String k=f;
        				List<String> path=new ArrayList<String>();
        		    	path.add(k);
        		    	while (!k.equals(A)){
        		    		k=prev.get(k);
        		    		if (!k.equals(A)&&k.substring(0,1).equals("g")&&prev.get(k).substring(0,1).equals("g")){
        		    			path.addAll(cornerMap.get(k).get(prev.get(k)));
        		    			k=prev.get(k);
        		    		}
        		    		else path.add(k);}
        		    	
        		    	cornerMap.get(path.get(0)).put(A,path);
    	        		List<String> rev=new ArrayList<String>();
    	        		for (int o=path.size()-1;o>=0;o--) rev.add(path.get(o));
    		    		cornerMap.get(A).put(path.get(0),rev);
    		    		break;
        		    	        		    	

        				
        			}
        		}
        		

    	}
    		//adding appropriate g-nodes to adjacency lists of g-nodes in "g"
    		for (int t=0;t<dest.size();t++){
    			
    			StringTokenizer tok2 = new StringTokenizer(dest.get(t).substring(1), ".");
        		int fromR2 = Integer.parseInt(tok2.nextToken());
        		int fromC2 = Integer.parseInt(tok2.nextToken());
        		int sizeR2=g.size();
        		int sizeC2=g.get(0).size();
        		
        		int toR2;
        		int toC2;
    			
    			//determines grids in which a given 'g' node is present
        		if (fromR2!=0&&fromR2!=sizeR2){
        			toR2=fromR2;
        			fromR2--;
        			
        		}
        		else if(fromR2==0){
        			toR2=fromR2;
        		}
        		else {
        			fromR2--;
        			toR2=fromR2;
        		}
        		
        		if (fromC2!=0&&fromC2!=sizeC2){
        			toC2=fromC2;
        			fromC2--;
        			
        		}
        		else if(fromC2==0){
        			toC2=fromC2;
        		}
        		else {
        			fromC2--;
        			toC2=fromC2;
        		}
    			
        		for (int l=fromR1;l>=fromR1&&l<=toR1;l++){
        			for (int a=fromC1;a>=fromC1&&a<=toC1;a++){
        				g.get(l).get(a).addSimple(A, dest.get(t), dist.get(dest.get(t)));
        			}
        		}
        		
        		for (int l=fromR2;l>=fromR2&&l<=toR2;l++){
        			for (int a=fromC2;a>=fromC2&&a<=toC2;a++){
        				g.get(l).get(a).addSimple(dest.get(t), A, dist.get(dest.get(t)));
        			}
        		}
    			
    		}
    		//resetting "dist"
    		for (String L:dist.keySet()){
        		if (dist.get(L)!=Integer.MAX_VALUE)dist.put(L, Integer.MAX_VALUE);
        	}
    		
    }
    	
    }
    
    public static void cornerMapPopulator(List<List<WeightedGraph<String>>> g, int width,int height){
    	cornerMap=new HashMap<String, Map<String,List<String>>>();
    	edgeVisited=new HashMap<String, Map<String,Boolean>>();
    	
    	prev=new HashMap<String, String>();
    	dist=new HashMap<String, Integer>();
    	//initialized priority queue for graph nodes; priority is based on shortest distance from source to given node 
    	for (int i=0;i<g.size();i++){
    		for (int a=0;a<g.get(i).size();a++){
    			for(String A:g.get(i).get(a).keySet()){
    				prev.put(A, null);
    				dist.put(A, Integer.MAX_VALUE);
    			}
    		}
    	}
    	List<String> allGpoints=new ArrayList<String>();
    	
    	for (int i=0;i<=width;i++){
    		for (int a=0;a<=height;a++){
    			allGpoints.add("g"+i+"."+a);
    		}
    	}
    	for (int i=0;i<allGpoints.size();i++){
    		Map<String,List<String>> addS=new HashMap<String,List<String>>();
    		Map<String,Boolean>visitedS=new HashMap<String,Boolean>();
    		for (int m=0;m<allGpoints.size();m++){
    			if (!allGpoints.get(m).equals(allGpoints.get(i))){
    				addS.put(allGpoints.get(m),null);
    				visitedS.put(allGpoints.get(m), false);
    			}
    			
    			
    		}
    		cornerMap.put(allGpoints.get(i), addS);
			edgeVisited.put(allGpoints.get(i), visitedS);
    		
    		
    	}
    	
    	graphTracer(g, allGpoints);
    	
    	
    }
   
    }
