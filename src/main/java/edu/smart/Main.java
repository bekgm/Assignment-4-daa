package edu.smart;

import edu.smart.graph.dagsp.DagLongestPaths;
import edu.smart.graph.dagsp.DagShortestPaths;
import edu.smart.graph.scc.Condensation;
import edu.smart.graph.scc.TarjanSCC;
import edu.smart.graph.topo.TopoSort;
import edu.smart.util.Graph;
import edu.smart.util.JsonIO;
import edu.smart.util.Metrics;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

        // Allow running without CLI arg (students testing in IDE)
        String inputFile = (args.length > 0)
                ? args[0]
                : "data/small_manual.json";

        JsonIO.Dataset ds = JsonIO.read(inputFile);
        Graph g = new Graph(ds.n);
        for (int[] e: ds.edges) g.addEdge(e[0], e[1], e[2]);

        Metrics M = new Metrics();

        // 1) SCC
        TarjanSCC tarjan = new TarjanSCC(g, M);
        M.time("SCC.time", tarjan::run);
        int[] comp = tarjan.comp();
        int cc = tarjan.compCount();
        List<List<Integer>> comps = tarjan.components();

        System.out.println("Input File: " + inputFile);
        System.out.println("SCC count = " + cc);
        for (int i = 0; i < comps.size(); i++) {
            List<Integer> c = comps.get(i);
            System.out.println("  C" + i + " size=" + c.size() + " nodes=" + c);
        }
        System.out.println("SCC metrics: dfsVisits=" + M.get("SCC.dfsVisits") +
                " dfsEdgeTraversals=" + M.get("SCC.dfsEdgeTraversals"));
        System.out.println("SCC time (ms): " + (M.nanos("SCC.time")/1e6));

        // 2) Condensation
        Graph dag = Condensation.build(g, comp, cc);
        int mDag = 0;
        for (int u = 0; u < dag.n(); u++) mDag += dag.edgesFrom(u).size();
        System.out.println("Condensation DAG: nodes=" + dag.n() + " edges=" + mDag);

        // 3) Topo
        int[] topo = TopoSort.kahn(dag, M);
        System.out.println("Topological order: " + Arrays.toString(topo));
        System.out.println("Topo metrics: pushes=" + M.get("Topo.pushes") +
                " pops=" + M.get("Topo.pops"));

        // 4) DAG Shortest (edge weights) from SCC(source)
        int sccSource = comp[Math.max(0, Math.min(ds.source, ds.n-1))];
        DagShortestPaths sp = new DagShortestPaths(dag, topo, M);
        M.time("DAGSP.time", () -> sp.run(sccSource));
        long[] dist = sp.dist();

        System.out.println("Shortest distances from SCC(source=" + ds.source +
                " -> C" + sccSource + "):");
        for (int v = 0; v < dag.n(); v++) {
            System.out.println("  C" + v + ": " + (dist[v] >= Long.MAX_VALUE/8 ? "INF" : dist[v]));
        }
        for (int t = 0; t < dag.n(); t++) {
            List<Integer> path = sp.reconstruct(t);
            if (!path.isEmpty()) System.out.println("  Path to C" + t + ": " + path);
        }
        System.out.println("DAG-SP metrics: relaxations=" + M.get("DAG.relaxations"));
        System.out.println("DAG-SP time (ms): " + (M.nanos("DAGSP.time")/1e6));

        // 5) Longest path (critical path) on DAG
        DagLongestPaths lp = new DagLongestPaths(dag, topo);
        M.time("DAGLP.time", lp::run);
        int tmax = lp.argmax();
        long[] best = lp.best();
        List<Integer> crit = lp.reconstruct(tmax);

        System.out.println("Critical path (longest) ends at C" + tmax +
                " length=" + best[tmax] + " path=" + crit);
        System.out.println("DAG-LP time (ms): " + (M.nanos("DAGLP.time")/1e6));

        // Map critical SCC path back to original tasks
        if (!crit.isEmpty()) {
            List<Integer> originalOrder = new ArrayList<>();
            for (int cid: crit) originalOrder.addAll(comps.get(cid));
            System.out.println("Derived order of original tasks along critical component path: " + originalOrder);
        }
    }
}

