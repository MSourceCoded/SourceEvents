package sourcecoded.events;

import sourcecoded.events.annotation.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The EventBus for the SourceEvents API. This is used to register handlers and raise events
 * @author SourceCoded
 */
public class EventBus {

    private ArrayList<Object> listeners = new ArrayList<Object>();

    /**
     * Register either a Class or Instance of an Object as a handler for the eventsystem
     *
     * Instances will allow you to access non-static methods
     * Classes can only access Static methods
     */
    public void register(Object object) {
        listeners.add(object);
    }

    /**
     * Unregister an object from the handlers list
     */
    public void unregister(Object object) {
        listeners.remove(object);
    }

    /**
     * Unregister all handlers of this type
     */
    public void unregisterAllOfType(Class type) {
        for (Object curr : listeners.toArray()) {
            Class currClass;
            if (curr instanceof Class)
                currClass = (Class) curr;
            else currClass = curr.getClass();

            if (currClass == type)
                listeners.remove(curr);

        }
    }

    /**
     * Get the listener list in the form of a read-only list
     */
    public List<Object> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * Raise an event to all the available listeners
     */
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
        for (Object currentHandler : listeners) {
            Method[] methods;

            if (currentHandler instanceof Class)
                methods = ((Class) currentHandler).getMethods();
             else
                methods = currentHandler.getClass().getMethods();

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

                if (!method.isAccessible()) continue;

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
