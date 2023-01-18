/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
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

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.versioning.ArtifactVersion;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModSorter {
    private TopologicalSort.DirectedGraph<ModContainer> modGraph;
    private ModContainer beforeAll = new DummyModContainer();
    private ModContainer afterAll = new DummyModContainer();
    private ModContainer before = new DummyModContainer();
    private ModContainer after = new DummyModContainer();

    public ModSorter(List<ModContainer> modList, Map<String, ModContainer> nameLookup) {
        this.buildGraph(modList, nameLookup);
    }

    private void buildGraph(List<ModContainer> modList, Map<String, ModContainer> nameLookup) {
        this.modGraph = new TopologicalSort.DirectedGraph();
        this.modGraph.addNode(this.beforeAll);
        this.modGraph.addNode(this.before);
        this.modGraph.addNode(this.afterAll);
        this.modGraph.addNode(this.after);
        this.modGraph.addEdge(this.before, this.after);
        this.modGraph.addEdge(this.beforeAll, this.before);
        this.modGraph.addEdge(this.after, this.afterAll);

        for(ModContainer mod : modList) {
            this.modGraph.addNode(mod);
        }

        for(ModContainer mod : modList) {
            if (mod.isImmutable()) {
                this.modGraph.addEdge(this.beforeAll, mod);
                this.modGraph.addEdge(mod, this.before);
            } else {
                boolean preDepAdded = false;
                boolean postDepAdded = false;

                for(ArtifactVersion dep : mod.getDependencies()) {
                    preDepAdded = true;
                    String modid = dep.getLabel();
                    if (modid.equals("*")) {
                        this.modGraph.addEdge(mod, this.afterAll);
                        this.modGraph.addEdge(this.after, mod);
                        postDepAdded = true;
                    } else {
                        this.modGraph.addEdge(this.before, mod);
                        if (Loader.isModLoaded(modid)) {
                            this.modGraph.addEdge(nameLookup.get(modid), mod);
                        }
                    }
                }

                for(ArtifactVersion dep : mod.getDependants()) {
                    postDepAdded = true;
                    String modid = dep.getLabel();
                    if (modid.equals("*")) {
                        this.modGraph.addEdge(this.beforeAll, mod);
                        this.modGraph.addEdge(mod, this.before);
                        preDepAdded = true;
                    } else {
                        this.modGraph.addEdge(mod, this.after);
                        if (Loader.isModLoaded(modid)) {
                            this.modGraph.addEdge(mod, nameLookup.get(modid));
                        }
                    }
                }

                if (!preDepAdded) {
                    this.modGraph.addEdge(this.before, mod);
                }

                if (!postDepAdded) {
                    this.modGraph.addEdge(mod, this.after);
                }
            }
        }
    }

    public List<ModContainer> sort() {
        List<ModContainer> sortedList = TopologicalSort.topologicalSort(this.modGraph);
        sortedList.removeAll(Arrays.asList(this.beforeAll, this.before, this.after, this.afterAll));
        return sortedList;
    }
}
