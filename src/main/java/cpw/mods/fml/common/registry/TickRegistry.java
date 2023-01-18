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
package cpw.mods.fml.common.registry;

import com.google.common.collect.Queues;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SingleIntervalHandler;

import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

public class TickRegistry {
    private static PriorityQueue<TickRegistry.TickQueueElement> clientTickHandlers = Queues.newPriorityQueue();
    private static PriorityQueue<TickRegistry.TickQueueElement> serverTickHandlers = Queues.newPriorityQueue();
    private static AtomicLong clientTickCounter = new AtomicLong();
    private static AtomicLong serverTickCounter = new AtomicLong();

    public TickRegistry() {
    }

    public static void registerScheduledTickHandler(IScheduledTickHandler handler, Side side) {
        getQueue(side).add(new TickRegistry.TickQueueElement(handler, getCounter(side).get()));
    }

    private static PriorityQueue<TickRegistry.TickQueueElement> getQueue(Side side) {
        return side.isClient() ? clientTickHandlers : serverTickHandlers;
    }

    private static AtomicLong getCounter(Side side) {
        return side.isClient() ? clientTickCounter : serverTickCounter;
    }

    public static void registerTickHandler(ITickHandler handler, Side side) {
        registerScheduledTickHandler(new SingleIntervalHandler(handler), side);
    }

    public static void updateTickQueue(List<IScheduledTickHandler> ticks, Side side) {
        synchronized(ticks) {
            ticks.clear();
            long tick = getCounter(side).incrementAndGet();
            PriorityQueue<TickRegistry.TickQueueElement> tickHandlers = getQueue(side);

            while(tickHandlers.size() != 0 && ((TickRegistry.TickQueueElement)tickHandlers.peek()).scheduledNow(tick)) {
                TickRegistry.TickQueueElement tickQueueElement = (TickRegistry.TickQueueElement)tickHandlers.poll();
                tickQueueElement.update(tick);
                tickHandlers.offer(tickQueueElement);
                ticks.add(tickQueueElement.ticker);
            }
        }
    }

    public static class TickQueueElement implements Comparable<TickRegistry.TickQueueElement> {
        private long next;
        public IScheduledTickHandler ticker;

        public TickQueueElement(IScheduledTickHandler ticker, long tickCounter) {
            this.ticker = ticker;
            this.update(tickCounter);
        }

        public int compareTo(TickRegistry.TickQueueElement o) {
            return (int)(this.next - o.next);
        }

        public void update(long tickCounter) {
            this.next = tickCounter + (long)Math.max(this.ticker.nextTickSpacing(), 1);
        }

        public boolean scheduledNow(long tickCounter) {
            return tickCounter >= this.next;
        }
    }
}
