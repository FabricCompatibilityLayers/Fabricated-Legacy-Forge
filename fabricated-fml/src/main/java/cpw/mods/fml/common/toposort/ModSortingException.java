/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common.toposort;

import java.util.Set;

public class ModSortingException extends RuntimeException
{
    public class SortingExceptionData<T>
    {
        public SortingExceptionData(T node, Set<T> visitedNodes)
        {
            this.firstBadNode = node;
            this.visitedNodes = visitedNodes;
        }

        private T firstBadNode;
        private Set<T> visitedNodes;

        public T getFirstBadNode()
        {
            return firstBadNode;
        }
        public Set<T> getVisitedNodes()
        {
            return visitedNodes;
        }
    }

    private SortingExceptionData sortingExceptionData;

    public <T> ModSortingException(String string, T node, Set<T> visitedNodes)
    {
        super(string);
        this.sortingExceptionData = new SortingExceptionData(node, visitedNodes);
    }

    public <T> SortingExceptionData<T> getExceptionData()
    {
        return sortingExceptionData;
    }

}
