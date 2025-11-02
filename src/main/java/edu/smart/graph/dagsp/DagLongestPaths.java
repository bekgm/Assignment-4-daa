package edu.smart.graph.dagsp;

import edu.smart.util.Graph;

import java.util.*;

/** Longest path in a DAG via max-DP over topo order (edge weights). */
public class DagLongestPaths {
    private final Graph dag;
    private final int[] topo;
    private long[] best;
    private int[] parent;

    public DagLongestPaths(Graph dag, int[] topo){
        this.dag = dag; this.topo = topo;
    }

    public void run(){
        int n = dag.n();
        best = new long[n];
        parent = new int[n];
        Arrays.fill(best, Long.MIN_VALUE/4);
        Arrays.fill(parent, -1);
        // sources (indegree 0) start at 0
        int[] indeg = new int[n];
        for (int u=0; u<n; u++) for (Graph.Edge e: dag.edgesFrom(u)) indeg[e.v]++;
        for (int u=0; u<n; u++) if (indeg[u]==0) best[u]=0;

        for (int u: topo){
            if (best[u] <= Long.MIN_VALUE/8) continue;
            for (Graph.Edge e: dag.edgesFrom(u)){
                int v = e.v;
                long nv = best[u] + e.w;
                if (nv > best[v]){
                    best[v] = nv;
                    parent[v] = u;
                }
            }
        }
    }

    public long[] best(){ return best; }
    public int[] parent(){ return parent; }

    public int argmax(){
        int n = dag.n();
        int arg = 0;
        for (int i=1;i<n;i++) if (best[i] > best[arg]) arg = i;
        return arg;
    }

    public List<Integer> reconstruct(int t){
        List<Integer> res = new ArrayList<>();
        if (best[t] <= Long.MIN_VALUE/8) return res;
        for (int cur=t; cur!=-1; cur=parent[cur]) res.add(cur);
        Collections.reverse(res);
        return res;
    }
}
