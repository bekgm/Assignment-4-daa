package edu.smart.util;

import java.util.*;

/** Directed weighted graph with parallel edges allowed. */
public class Graph {
    public static class Edge {
        public final int u, v;
        public final int w;
        public Edge(int u, int v, int w) { this.u = u; this.v = v; this.w = w; }
        @Override public String toString(){ return u + "->" + v + "(" + w + ")"; }
    }

    private final int n;
    private final List<List<Edge>> adj;

    public Graph(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }
    public int n(){ return n; }
    public List<List<Edge>> adj(){ return adj; }

    public void addEdge(int u, int v, int w){
        adj.get(u).add(new Edge(u, v, w));
    }

    public List<Edge> edgesFrom(int u){ return adj.get(u); }

    public Graph reverse(){
        Graph r = new Graph(n);
        for (int u=0; u<n; u++) for (Edge e: adj.get(u)) r.addEdge(e.v, e.u, e.w);
        return r;
    }
}
