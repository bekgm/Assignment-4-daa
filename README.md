# Smart City / Smart Campus Scheduling — Graph Toolkit

Implements SCC (Tarjan), condensation DAG, topological sort (Kahn), shortest & longest paths on a DAG (edge weights), plus metrics and datasets.

**Weight model:** edge weights. Condensation keeps parallel edges between components.

## Build & Run
```bash
mvn -q test
mvn -q -Dexec.args="data/small_manual.json" exec:java
```
