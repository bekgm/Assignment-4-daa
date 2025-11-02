package edu.smart.graph.dagsp;

import edu.smart.util.Graph;
import edu.smart.util.Metrics;

import java.util.*;

/** Single-source shortest paths on a DAG (edge weights). */
public class DagShortestPaths {
    private final Graph dag;
    private final Metrics metrics;
    private final int[] topo;
    private long[] dist;
    private int[] parent;

    public DagShortestPaths(Graph dag, int[] topo, Metrics metrics){
        this.dag = dag; this.topo = topo; this.metrics = metrics;
    }

    public void run(int s){
        int n = dag.n();
        dist = new long[n];
        parent = new int[n];
        Arrays.fill(dist, Long.MAX_VALUE/4);
        Arrays.fill(parent, -1);
        dist[s] = 0;
        // Start from position of s in topo order
        int posS = -1;
        for (int i=0;i<n;i++) if (topo[i]==s) { posS = i; break; }
        if (posS==-1) throw new IllegalArgumentException("source not in DAG");
        for (int i=posS;i<n;i++){
            int u = topo[i];
            if (dist[u] >= Long.MAX_VALUE/8) continue;
            for (Graph.Edge e: dag.edgesFrom(u)){
                int v = e.v;
                long nd = dist[u] + e.w;
                metrics.inc("DAG.relaxations");
                if (nd < dist[v]){
                    dist[v] = nd;
                    parent[v] = u;
                }
            }
        }
    }

    public long[] dist(){ return dist; }
    public int[] parent(){ return parent; }

    public List<Integer> reconstruct(int t){
        List<Integer> path = new ArrayList<>();
        if (dist[t] >= Long.MAX_VALUE/8) return path;
        for (int cur = t; cur!=-1; cur = parent[cur]) path.add(cur);
        Collections.reverse(path);
        return path;
    }
}
