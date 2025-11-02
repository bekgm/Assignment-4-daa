package edu.smart.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonIO {
    public static class Dataset {
        public final boolean directed;
        public final int n;
        public final int[][] edges; // [m][3]: u,v,w
        public final int source;
        public final String weightModel;
        public Dataset(boolean directed, int n, int[][] edges, int source, String weightModel){
            this.directed = directed; this.n = n; this.edges = edges; this.source = source; this.weightModel = weightModel;
        }
    }

    public static Dataset read(String path) throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(new File(path));
        boolean directed = root.get("directed").asBoolean(true);
        int n = root.get("n").asInt();
        int source = root.has("source") ? root.get("source").asInt() : 0;
        String weightModel = root.has("weight_model") ? root.get("weight_model").asText("edge") : "edge";
        JsonNode edgesNode = root.get("edges");
        int m = edgesNode.size();
        int[][] edges = new int[m][3];
        for (int i=0;i<m;i++){
            JsonNode e = edgesNode.get(i);
            edges[i][0] = e.get("u").asInt();
            edges[i][1] = e.get("v").asInt();
            edges[i][2] = e.has("w") ? e.get("w").asInt() : 1;
        }
        return new Dataset(directed, n, edges, source, weightModel);
    }
}
