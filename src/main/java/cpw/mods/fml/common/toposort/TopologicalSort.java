package cpw.mods.fml.common.toposort;

import java.util.*;

public class TopologicalSort {
    public TopologicalSort() {
    }

    public static <T> List<T> topologicalSort(TopologicalSort.DirectedGraph<T> graph) {
        DirectedGraph<T> rGraph = reverse(graph);
        List<T> sortedResult = new ArrayList<T>();
        Set<T> visitedNodes = new HashSet<T>();
        // A list of "fully explored" nodes. Leftovers in here indicate cycles in the graph
        Set<T> expandedNodes = new HashSet<T>();

        for (T node : rGraph)
        {
            explore(node, rGraph, sortedResult, visitedNodes, expandedNodes);
        }

        return sortedResult;
    }

    public static <T> TopologicalSort.DirectedGraph<T> reverse(TopologicalSort.DirectedGraph<T> graph) {
        DirectedGraph<T> result = new DirectedGraph<T>();

        for (T node : graph)
        {
            result.addNode(node);
        }

        for (T from : graph)
        {
            for (T to : graph.edgesFrom(from))
            {
                result.addEdge(to, from);
            }
        }

        return result;
    }

    public static <T> void explore(T node, TopologicalSort.DirectedGraph<T> graph, List<T> sortedResult, Set<T> visitedNodes, Set<T> expandedNodes) {
        // Have we been here before?
        if (visitedNodes.contains(node))
        {
            // And have completed this node before
            if (expandedNodes.contains(node))
            {
                // Then we're fine
                return;
            }

            System.out.printf("%s: %s\n%s\n%s\n", node, sortedResult, visitedNodes, expandedNodes);
            throw new ModSortingException("There was a cycle detected in the input graph, sorting is not possible", node, visitedNodes);
        }

        // Visit this node
        visitedNodes.add(node);

        // Recursively explore inbound edges
        for (T inbound : graph.edgesFrom(node))
        {
            explore(inbound, graph, sortedResult, visitedNodes, expandedNodes);
        }

        // Add ourselves now
        sortedResult.add(node);
        // And mark ourselves as explored
        expandedNodes.add(node);
    }

    public static class DirectedGraph<T> implements Iterable<T> {
        private final Map<T, SortedSet<T>> graph = new HashMap();
        private List<T> orderedNodes = new ArrayList();

        public DirectedGraph() {
        }

        public boolean addNode(T node) {
            if (this.graph.containsKey(node)) {
                return false;
            } else {
                this.orderedNodes.add(node);
                this.graph.put(node, new TreeSet(new Comparator<T>() {
                    public int compare(T o1, T o2) {
                        return TopologicalSort.DirectedGraph.this.orderedNodes.indexOf(o1) - TopologicalSort.DirectedGraph.this.orderedNodes.indexOf(o2);
                    }
                }));
                return true;
            }
        }

        public void addEdge(T from, T to) {
            if (this.graph.containsKey(from) && this.graph.containsKey(to)) {
                ((SortedSet)this.graph.get(from)).add(to);
            } else {
                throw new NoSuchElementException("Missing nodes from graph");
            }
        }

        public void removeEdge(T from, T to) {
            if (this.graph.containsKey(from) && this.graph.containsKey(to)) {
                ((SortedSet)this.graph.get(from)).remove(to);
            } else {
                throw new NoSuchElementException("Missing nodes from graph");
            }
        }

        public boolean edgeExists(T from, T to) {
            if (this.graph.containsKey(from) && this.graph.containsKey(to)) {
                return ((SortedSet)this.graph.get(from)).contains(to);
            } else {
                throw new NoSuchElementException("Missing nodes from graph");
            }
        }

        public Set<T> edgesFrom(T from) {
            if (!this.graph.containsKey(from)) {
                throw new NoSuchElementException("Missing node from graph");
            } else {
                return Collections.unmodifiableSortedSet((SortedSet)this.graph.get(from));
            }
        }

        public Iterator<T> iterator() {
            return this.orderedNodes.iterator();
        }

        public int size() {
            return this.graph.size();
        }

        public boolean isEmpty() {
            return this.graph.isEmpty();
        }

        public String toString() {
            return this.graph.toString();
        }
    }
}
