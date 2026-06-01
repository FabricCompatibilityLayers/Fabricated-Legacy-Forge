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
package cpw.mods.fml.common.registry;

import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Queues;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SingleIntervalHandler;

public class TickRegistry
{

    /**
     * We register our delegate here
     * @param handler
     */

    public static class TickQueueElement implements Comparable<TickQueueElement>
    {
        public TickQueueElement(IScheduledTickHandler ticker, long tickCounter)
        {
            this.ticker = ticker;
            update(tickCounter);
        }
        @Override
        public int compareTo(TickQueueElement o)
        {
            return (int)(next - o.next);
        }

        public void update(long tickCounter)
        {
            next = tickCounter + Math.max(ticker.nextTickSpacing(),1);
        }

        private long next;
        public IScheduledTickHandler ticker;

        public boolean scheduledNow(long tickCounter)
        {
            return tickCounter >= next;
        }
    }

    private static PriorityQueue<TickQueueElement> clientTickHandlers = Queues.newPriorityQueue();
    private static PriorityQueue<TickQueueElement> serverTickHandlers = Queues.newPriorityQueue();

    private static AtomicLong clientTickCounter = new AtomicLong();
    private static AtomicLong serverTickCounter = new AtomicLong();

    public static void registerScheduledTickHandler(IScheduledTickHandler handler, Side side)
    {
        getQueue(side).add(new TickQueueElement(handler, getCounter(side).get()));
    }

    /**
     * @param side
     * @return
     */
    private static PriorityQueue<TickQueueElement> getQueue(Side side)
    {
        return side.isClient() ? clientTickHandlers : serverTickHandlers;
    }

    private static AtomicLong getCounter(Side side)
    {
        return side.isClient() ? clientTickCounter : serverTickCounter;
    }
    public static void registerTickHandler(ITickHandler handler, Side side)
    {
        registerScheduledTickHandler(new SingleIntervalHandler(handler), side);
    }

    public static void updateTickQueue(List<IScheduledTickHandler> ticks, Side side)
    {
        synchronized (ticks)
        {
            ticks.clear();
            long tick = getCounter(side).incrementAndGet();
            PriorityQueue<TickQueueElement> tickHandlers = getQueue(side);

            while (true)
            {
                if (tickHandlers.size()==0 || !tickHandlers.peek().scheduledNow(tick))
                {
                    break;
                }
                TickRegistry.TickQueueElement tickQueueElement  = tickHandlers.poll();
                tickQueueElement.update(tick);
                tickHandlers.offer(tickQueueElement);
                ticks.add(tickQueueElement.ticker);
            }
        }
    }

}
