package edu.smart;

import edu.smart.graph.dagsp.DagLongestPaths;
import edu.smart.graph.dagsp.DagShortestPaths;
import edu.smart.graph.topo.TopoSort;
import edu.smart.util.Graph;
import edu.smart.util.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DagSPTests {
    @Test
    public void shortestAndLongestOnSimpleDag(){
        Graph dag = new Graph(4);
        dag.addEdge(0,1,1);
        dag.addEdge(0,2,5);
        dag.addEdge(1,3,1);
        dag.addEdge(2,3,1);
        Metrics M = new Metrics();
        int[] topo = TopoSort.kahn(dag, M);
        DagShortestPaths sp = new DagShortestPaths(dag, topo, M);
        sp.run(0);
        assertEquals(2, sp.dist()[3]);
        DagLongestPaths lp = new DagLongestPaths(dag, topo);
        lp.run();
        assertTrue(lp.best()[3] >= 6); // path 0->2->3
    }
}
