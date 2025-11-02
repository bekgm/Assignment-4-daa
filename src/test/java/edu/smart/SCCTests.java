package edu.smart;

import edu.smart.graph.scc.TarjanSCC;
import edu.smart.util.Graph;
import edu.smart.util.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SCCTests {
    @Test
    public void smallCycleAndChain(){
        Graph g = new Graph(5);
        g.addEdge(0,1,1);
        g.addEdge(1,2,1);
        g.addEdge(2,0,1);
        g.addEdge(2,3,1);
        g.addEdge(3,4,1);
        Metrics M = new Metrics();
        TarjanSCC t = new TarjanSCC(g, M);
        t.run();
        assertEquals(3, t.compCount()); // {0,1,2}, {3}, {4}
        List<List<Integer>> comps = t.components();
        assertTrue(comps.stream().anyMatch(c -> c.size()==3));
    }
}
