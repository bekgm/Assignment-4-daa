package edu.smart;

import edu.smart.graph.scc.Condensation;
import edu.smart.graph.scc.TarjanSCC;
import edu.smart.graph.topo.TopoSort;
import edu.smart.util.Graph;
import edu.smart.util.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TopoTests {
    @Test
    public void topoOnCondensation(){
        Graph g = new Graph(4);
        g.addEdge(0,1,1);
        g.addEdge(1,2,1);
        g.addEdge(2,1,1); // cycle {1,2}
        g.addEdge(2,3,1);
        Metrics M = new Metrics();
        TarjanSCC scc = new TarjanSCC(g, M);
        scc.run();
        Graph dag = Condensation.build(g, scc.comp(), scc.compCount());
        int[] order = TopoSort.kahn(dag, M);
        assertEquals(dag.n(), order.length);
        int indeg0 = 0;
        for (int u=0; u<dag.n(); u++) for (Graph.Edge e: dag.edgesFrom(u)) if (e.v==order[0]) indeg0++;
        assertEquals(0, indeg0);
    }
}
