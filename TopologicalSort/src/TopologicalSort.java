import java.io.*; 
import java.util.*; 
public class TopologicalSort { 
    //Adjacency list representation of a directed graph 
    //See the class discussion for the details of the representation. 
    private class VertexNode { 
        private String name; 
        private VertexNode nextV; 
        private EdgeNode edges; 
        private int indegree; 
        
        private VertexNode(String n, VertexNode v) { 
            name = n; 
            nextV = v; 
            edges = null; 
            indegree = 0; 
        } 
        @Override
        public String toString() {
            String str = name;
            return str += " : " + indegree;
        }
        
    } 
    private class EdgeNode { 
        private VertexNode vertex1; 
        private VertexNode vertex2; 
        private EdgeNode nextE;
        
        private EdgeNode(VertexNode v1, VertexNode v2,EdgeNode e) { 
            vertex1 = v1; 
            vertex2 = v2; 
            nextE = e; 
        } 
    } 
    private VertexNode vertices; 
    private int numVertices; 
    
    public TopologicalSort() { 
        vertices = null; 
        numVertices = 0; 
    } 
    public void addVertex(String s) { 
    //PRE: the vertex list is sorted in ascending order using the name as the key 
    //POST: a vertex with name s as been add to the vertex list and the vertex list  
    //is sorted in ascending order using the name as the key 
        if(vertices == null) {
            vertices = new VertexNode(s, null); 
            return;
        }
        VertexNode n = vertices;
        while(n.nextV != null && n.name.compareTo(s) > 0) {
            n = n.nextV;
        }
        n.nextV = new VertexNode(s, n.nextV);
        numVertices++;
    } 
    public void addEdge(String n1, String n2) { 
    //PRE: the vertices n1 and n2 have already been added 
    //POST:the new edge (n1, n2) has been added to the n1 edge list 
        VertexNode v1 = find(n1);
        VertexNode v2 = find(n2);
        //System.out.println(v1);
        if(v1.edges == null) {
            v1.edges = new EdgeNode(v1, v2, null);
            v2.indegree++;
            return;
        }
        
        v1.edges.nextE = new EdgeNode(v1, v2, v1.edges.nextE);
        v2.indegree++;
    } 
    
    /**
     * Finds the vertex node with string s
     * @param s name of vertex node
     * @return vertexNode if the node exists or null if the node doesn't exist
     */
    private VertexNode find(String s) {
        VertexNode n = vertices;
        while(n != null && !(n.name.equals(s))) {
            n = n.nextV;
        }
        return n;
    }
    
    private VertexNode find(int k) {
        VertexNode n = vertices;
        while(n != null && n.indegree != k) {
            n = n.nextV;
        }
        return n;
    }
    
    public String topoSort() {
    //if the graph contains a cycle return null 
    //otherwise return a string containing the names of vertices separated by blanks 
    //in topological order.
        VertexNode n = find(0);
        if(n == null) {
           return "No Topological Order";
        }
        
        String str = "";
        while((n = find(0)) != null) {
            n.indegree--;
            str += n.name + " ";
            System.out.println(str);
            EdgeNode e = n.edges;
            while(e != null) {
                System.out.println("Looking at the edges");
                e.vertex2.indegree--;
                e = e.nextE;
            }
        }
        n = vertices;
        System.out.println("Im about to look if there's any left overs. Wish me luck!");
        while(n != null) {
            System.out.println(n);
            if (n.indegree > 0) {
                return "No Topological Order";
            }
            n = n.nextV;
        }
        
        return str;
    } 
    public static void main(String args[]) throws IOException{ 
    //see problem statement 
        Scanner scan = new Scanner(new File(args[0]));
        TopologicalSort topo = new TopologicalSort();
        for (String str : scan.nextLine().split(" ")) {
            System.out.println(str);
            topo.addVertex(str);
        }
        
        while(scan.hasNext()) {
            topo.addEdge(scan.next(), scan.next());
        }
        scan.close();
        System.out.println("I added everything! Lets sort it!");
        System.out.println(topo.topoSort());
        
    } 
}
