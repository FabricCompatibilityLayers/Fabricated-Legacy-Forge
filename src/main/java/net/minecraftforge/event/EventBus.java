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
    private ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = new ConcurrentHashMap();
    private final int busID;

    public EventBus() {
        this.busID = maxID++;
        ListenerList.resize(this.busID + 1);
    }

    public void register(Object target) {
        Set<? extends Class<?>> supers = TypeToken.of(target.getClass()).getTypes().rawTypes();
        for (Method method : target.getClass().getMethods())
        {
            for (Class<?> cls : supers)
            {
                try
                {
                    Method real = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    if (real.isAnnotationPresent(ForgeSubscribe.class))
                    {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length != 1)
                        {
                            throw new IllegalArgumentException(
                                    "Method " + method + " has @ForgeSubscribe annotation, but requires " + parameterTypes.length +
                                            " arguments.  Event handler methods must require a single argument."
                            );
                        }

                        Class<?> eventType = parameterTypes[0];

                        if (!Event.class.isAssignableFrom(eventType))
                        {
                            throw new IllegalArgumentException("Method " + method + " has @ForgeSubscribe annotation, but takes a argument that is not a Event " + eventType);
                        }

                        register(eventType, target, method);
                        break;
                    }
                }
                catch (NoSuchMethodException e)
                {
                    ;
                }
            }
        }
    }

    private void register(Class<?> eventType, Object target, Method method) {
        try {
            Constructor<?> ctr = eventType.getConstructor();
            ctr.setAccessible(true);
            Event event = (Event)ctr.newInstance();
            ASMEventHandler listener = new ASMEventHandler(target, method);
            event.getListenerList().register(this.busID, listener.getPriority(), listener);
            ArrayList<IEventListener> others = (ArrayList)this.listeners.get(target);
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
        ArrayList<IEventListener> list = listeners.remove(object);
        for (IEventListener listener : list)
        {
            ListenerList.unregiterAll(busID, listener);
        }
    }

    public boolean post(Event event) {
        IEventListener[] listeners = event.getListenerList().getListeners(busID);
        for (IEventListener listener : listeners)
        {
            listener.invoke(event);
        }
        return (event.isCancelable() ? event.isCanceled() : false);
    }
}
