package edu.smart.graph.topo;

import edu.smart.util.Graph;
import edu.smart.util.Metrics;

import java.util.*;

/** Kahn's algorithm for topological order. */
public class TopoSort {
    public static int[] kahn(Graph dag, Metrics metrics){
        int n = dag.n();
        int[] indeg = new int[n];
        for (int u=0; u<n; u++) for (Graph.Edge e: dag.edgesFrom(u)) indeg[e.v]++;

        Deque<Integer> q = new ArrayDeque<>();
        for (int u=0; u<n; u++) if (indeg[u]==0){ q.add(u); metrics.inc("Topo.pushes"); }

        int[] order = new int[n];
        int idx = 0;

        while(!q.isEmpty()){
            int u = q.remove();
            metrics.inc("Topo.pops");
            order[idx++] = u;
            for (Graph.Edge e: dag.edgesFrom(u)){
                int v = e.v;
                if (--indeg[v]==0){ q.add(v); metrics.inc("Topo.pushes"); }
            }
        }
        if (idx != n) throw new IllegalStateException("Graph is not a DAG");
        return order;
    }
}
