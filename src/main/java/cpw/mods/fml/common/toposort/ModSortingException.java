package cpw.mods.fml.common.toposort;

import java.util.Set;

public class ModSortingException extends RuntimeException {
    private ModSortingException.SortingExceptionData sortingExceptionData;

    public <T> ModSortingException(String string, T node, Set<T> visitedNodes) {
        super(string);
        this.sortingExceptionData = new ModSortingException.SortingExceptionData(node, visitedNodes);
    }

    public <T> ModSortingException.SortingExceptionData<T> getExceptionData() {
        return this.sortingExceptionData;
    }

    public class SortingExceptionData<T> {
        private T firstBadNode;
        private Set<T> visitedNodes;

        public SortingExceptionData(T node, Set<T> visitedNodes) {
            this.firstBadNode = node;
            this.visitedNodes = visitedNodes;
        }

        public T getFirstBadNode() {
            return this.firstBadNode;
        }

        public Set<T> getVisitedNodes() {
            return this.visitedNodes;
        }
    }
}
