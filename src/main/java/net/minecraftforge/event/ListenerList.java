package net.minecraftforge.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ListenerList {
    private static ArrayList<net.minecraft.net.minecraftforge.event.ListenerList> allLists = new ArrayList();
    private static int maxSize = 0;
    private net.minecraft.net.minecraftforge.event.ListenerList parent;
    private net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst[] lists = new net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst[0];

    public ListenerList() {
        allLists.add(this);
        this.resizeLists(maxSize);
    }

    public ListenerList(net.minecraft.net.minecraftforge.event.ListenerList parent) {
        allLists.add(this);
        this.parent = parent;
        this.resizeLists(maxSize);
    }

    public static void resize(int max) {
        if (max > maxSize) {
            Iterator i$ = allLists.iterator();

            while(i$.hasNext()) {
                net.minecraft.net.minecraftforge.event.ListenerList list = (net.minecraft.net.minecraftforge.event.ListenerList)i$.next();
                list.resizeLists(max);
            }

            maxSize = max;
        }
    }

    public void resizeLists(int max) {
        if (this.parent != null) {
            this.parent.resizeLists(max);
        }

        if (this.lists.length < max) {
            net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst[] newList = new net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst[max];

            int x;
            for(x = 0; x < this.lists.length; ++x) {
                newList[x] = this.lists[x];
            }

            for(; x < max; ++x) {
                if (this.parent != null) {
                    newList[x] = new net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst(this.parent.getInstance(x));
                } else {
                    newList[x] = new net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst();
                }
            }

            this.lists = newList;
        }
    }

    public static void clearBusID(int id) {
        Iterator i$ = allLists.iterator();

        while(i$.hasNext()) {
            net.minecraft.net.minecraftforge.event.ListenerList list = (net.minecraft.net.minecraftforge.event.ListenerList)i$.next();
            list.lists[id].dispose();
        }

    }

    protected net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst getInstance(int id) {
        return this.lists[id];
    }

    public net.minecraft.net.minecraftforge.event.IEventListener[] getListeners(int id) {
        return this.lists[id].getListeners();
    }

    public void register(int id, EventPriority priority, net.minecraft.net.minecraftforge.event.IEventListener listener) {
        this.lists[id].register(priority, listener);
    }

    public void unregister(int id, net.minecraft.net.minecraftforge.event.IEventListener listener) {
        this.lists[id].unregister(listener);
    }

    public static void unregiterAll(int id, net.minecraft.net.minecraftforge.event.IEventListener listener) {
        Iterator i$ = allLists.iterator();

        while(i$.hasNext()) {
            net.minecraft.net.minecraftforge.event.ListenerList list = (net.minecraft.net.minecraftforge.event.ListenerList)i$.next();
            list.unregister(id, listener);
        }

    }

    private class ListenerListInst {
        private boolean rebuild;
        private net.minecraft.net.minecraftforge.event.IEventListener[] listeners;
        private ArrayList<ArrayList<net.minecraft.net.minecraftforge.event.IEventListener>> priorities;
        private net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst parent;

        private ListenerListInst() {
            this.rebuild = true;
            int count = EventPriority.values().length;
            this.priorities = new ArrayList(count);

            for(int x = 0; x < count; ++x) {
                this.priorities.add(new ArrayList());
            }

        }

        public void dispose() {
            Iterator i$ = this.priorities.iterator();

            while(i$.hasNext()) {
                ArrayList<net.minecraft.net.minecraftforge.event.IEventListener> listeners = (ArrayList)i$.next();
                listeners.clear();
            }

            this.priorities.clear();
            this.parent = null;
            this.listeners = null;
        }

        private ListenerListInst(net.minecraft.net.minecraftforge.event.ListenerList.ListenerListInst parent) {
            this();
            this.parent = parent;
        }

        public ArrayList<net.minecraft.net.minecraftforge.event.IEventListener> getListeners(EventPriority priority) {
            ArrayList<net.minecraft.net.minecraftforge.event.IEventListener> ret = new ArrayList((Collection)this.priorities.get(priority.ordinal()));
            if (this.parent != null) {
                ret.addAll(this.parent.getListeners(priority));
            }

            return ret;
        }

        public net.minecraft.net.minecraftforge.event.IEventListener[] getListeners() {
            if (this.shouldRebuild()) {
                this.buildCache();
            }

            return this.listeners;
        }

        protected boolean shouldRebuild() {
            return this.rebuild || this.parent != null && this.parent.shouldRebuild();
        }

        private void buildCache() {
            ArrayList<net.minecraft.net.minecraftforge.event.IEventListener> ret = new ArrayList();
            EventPriority[] arr$ = EventPriority.values();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                EventPriority value = arr$[i$];
                ret.addAll(this.getListeners(value));
            }

            this.listeners = (net.minecraft.net.minecraftforge.event.IEventListener[])ret.toArray(new net.minecraft.net.minecraftforge.event.IEventListener[0]);
            this.rebuild = false;
        }

        public void register(EventPriority priority, net.minecraft.net.minecraftforge.event.IEventListener listener) {
            ((ArrayList)this.priorities.get(priority.ordinal())).add(listener);
            this.rebuild = true;
        }

        public void unregister(net.minecraft.net.minecraftforge.event.IEventListener listener) {
            Iterator i$ = this.priorities.iterator();

            while(i$.hasNext()) {
                ArrayList<IEventListener> list = (ArrayList)i$.next();
                list.remove(listener);
            }

        }
    }
}
