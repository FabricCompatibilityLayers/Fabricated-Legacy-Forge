package net.minecraftforge.event;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private static int maxID = 0;
    private ConcurrentHashMap<Object, ArrayList<net.minecraft.net.minecraftforge.event.IEventListener>> listeners = new ConcurrentHashMap();
    private final int busID;

    public EventBus() {
        this.busID = maxID++;
        net.minecraft.net.minecraftforge.event.ListenerList.resize(this.busID + 1);
    }

    public void register(Object target) {
        Set<? extends Class<?>> supers = TypeToken.of(target.getClass()).getTypes().rawTypes();
        Method[] arr$ = target.getClass().getMethods();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            Iterator i$ = supers.iterator();

            while(i$.hasNext()) {
                Class<?> cls = (Class)i$.next();

                try {
                    Method real = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    if (real.isAnnotationPresent(ForgeSubscribe.class)) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length != 1) {
                            throw new IllegalArgumentException("Method " + method + " has @ForgeSubscribe annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods must require a single argument.");
                        }

                        Class<?> eventType = parameterTypes[0];
                        if (!net.minecraft.net.minecraftforge.event.Event.class.isAssignableFrom(eventType)) {
                            throw new IllegalArgumentException("Method " + method + " has @ForgeSubscribe annotation, but takes a argument that is not a Event " + eventType);
                        }

                        this.register(eventType, target, method);
                        break;
                    }
                } catch (NoSuchMethodException var12) {
                }
            }
        }

    }

    private void register(Class<?> eventType, Object target, Method method) {
        try {
            Constructor<?> ctr = eventType.getConstructor();
            ctr.setAccessible(true);
            net.minecraft.net.minecraftforge.event.Event event = (net.minecraft.net.minecraftforge.event.Event)ctr.newInstance();
            ASMEventHandler listener = new ASMEventHandler(target, method);
            event.getListenerList().register(this.busID, listener.getPriority(), listener);
            ArrayList<net.minecraft.net.minecraftforge.event.IEventListener> others = (ArrayList)this.listeners.get(target);
            if (others == null) {
                others = new ArrayList();
                this.listeners.put(target, others);
            }

            others.add(listener);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public void unregister(Object object) {
        ArrayList<net.minecraft.net.minecraftforge.event.IEventListener> list = (ArrayList)this.listeners.remove(object);
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            net.minecraft.net.minecraftforge.event.IEventListener listener = (net.minecraft.net.minecraftforge.event.IEventListener)i$.next();
            ListenerList.unregiterAll(this.busID, listener);
        }

    }

    public boolean post(Event event) {
        net.minecraft.net.minecraftforge.event.IEventListener[] listeners = event.getListenerList().getListeners(this.busID);
        net.minecraft.net.minecraftforge.event.IEventListener[] arr$ = listeners;
        int len$ = listeners.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            IEventListener listener = arr$[i$];
            listener.invoke(event);
        }

        return event.isCancelable() ? event.isCanceled() : false;
    }
}
