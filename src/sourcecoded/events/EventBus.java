package sourcecoded.events;

import sourcecoded.events.annotation.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventBus {

    private ArrayList<Class> listeners = new ArrayList<Class>();

    public void register(Class clazz) {
        listeners.add(clazz);
    }

    public ArrayList<Class> getListeners() {
        return listeners;
    }

    public void raiseEvent(final AbstractEvent event) {
        new Thread() {
            public void run() {
                try {
                    raise(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void raise(final AbstractEvent event) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for (Class currentHandler : listeners) {
            Method[] methods = currentHandler.getMethods();

            ArrayList<Method> valid = new ArrayList<Method>();

            for (Method method : methods) {
                EventListener annotation = method.getAnnotation(EventListener.class);
                if (annotation == null)
                    continue;

                Class[] params = method.getParameterTypes();

                if (params.length != 1)
                    continue;

                if (!event.getClass().getSimpleName().equals(params[0].getSimpleName()))
                    continue;

                valid.add(method);
            }

            Comparator<Method> comp = new Comparator<Method>() {
                @Override
                public int compare(Method o1, Method o2) {
                    EventListener a1 = o1.getAnnotation(EventListener.class);
                    EventListener a2 = o2.getAnnotation(EventListener.class);

                    if (a1.priority().ordinal() < a2.priority().ordinal())
                        return -1;

                    if (a1.priority().ordinal() > a2.priority().ordinal())
                        return 1;

                    return 0;
                }

                @Override
                public boolean equals(Object obj) {
                    return false;
                }
            };

            Collections.sort(valid, comp);

            for (Method method : valid) {
                EventListener annotation = method.getAnnotation(EventListener.class);

                if (event.isCancelled()) {
                    if (annotation.allowCancelled())
                        method.invoke(currentHandler, event);
                } else
                    method.invoke(currentHandler, event);
            }

        }
    }


}
