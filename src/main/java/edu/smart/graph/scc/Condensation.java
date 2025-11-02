package edu.smart.graph.scc;

import edu.smart.util.Graph;

public class Condensation {
    /** Build condensation DAG from SCC ids. Keeps parallel edges between components. */
    public static Graph build(Graph g, int[] comp, int compCount){
        Graph dag = new Graph(compCount);
        for (int u=0; u<g.n(); u++){
            int cu = comp[u];
            for (Graph.Edge e: g.edgesFrom(u)){
                int cv = comp[e.v];
                if (cu != cv){
                    dag.addEdge(cu, cv, e.w);
                }
            }
        }
        return dag;
    }
}
