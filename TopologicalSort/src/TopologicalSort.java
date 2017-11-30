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
        
    } 
    public void addEdge(String n1, String n2) { 
    //PRE: the vertices n1 and n2 have already been added 
    //POST:the new edge (n1, n2) has been added to the n1 edge list 
        
    } 
    public String topoSort() {
    //if the graph contains a cycle return null 
    //otherwise return a string containing the names of vertices separated by blanks 
    //in topological order. 
        return null; 
    } 
    public static void main(String args[]) throws IOException{ 
    //see problem statement 
        
    } 
}
