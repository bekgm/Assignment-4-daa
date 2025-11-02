package edu.smart.graph.scc;

import edu.smart.util.Graph;
import edu.smart.util.Metrics;

import java.util.*;

/** Tarjan's algorithm for SCCs. */
public class TarjanSCC {
    private final Graph g;
    private final Metrics metrics;

    private int time = 0;
    private final int[] disc, low, comp;
    private final boolean[] onStack;
    private final Deque<Integer> st = new ArrayDeque<>();
    private int compCount = 0;
    private final List<List<Integer>> components = new ArrayList<>();

    public TarjanSCC(Graph g, Metrics metrics){
        this.g = g; this.metrics = metrics;
        int n = g.n();
        disc = new int[n];
        low = new int[n];
        comp = new int[n];
        onStack = new boolean[n];
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        Arrays.fill(comp, -1);
    }

    public void run(){
        for (int u = 0; u < g.n(); u++) if (disc[u] == -1) dfs(u);
    }

    private void dfs(int u){
        metrics.inc("SCC.dfsVisits");
        disc[u] = low[u] = time++;
        st.push(u);
        onStack[u] = true;
        for (Graph.Edge e: g.edgesFrom(u)){
            metrics.inc("SCC.dfsEdgeTraversals");
            int v = e.v;
            if (disc[v] == -1){
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]){
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        if (low[u] == disc[u]){
            List<Integer> compList = new ArrayList<>();
            while (true){
                int v = st.pop();
                onStack[v] = false;
                comp[v] = compCount;
                compList.add(v);
                if (v == u) break;
            }
            components.add(compList);
            compCount++;
        }
    }

    public int[] comp(){ return comp; }
    public int compCount(){ return compCount; }
    public List<List<Integer>> components(){ return components; }
}
