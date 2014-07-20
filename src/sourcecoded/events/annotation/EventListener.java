package sourcecoded.events.annotation;

import sourcecoded.events.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The EventListener annotation is used to declare a method as a Listener (or handler if you prefer) for the SourceEvents eventsystem
 * @author SourceCoded
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface EventListener {

    /**
     * The priority of the event
     */
    public EventPriority priority() default EventPriority.NORMAL;

    /**
     * Allow cancelled events?
     */
    public boolean allowCancelled() default false;

    /**
     * Respect Event Class inheritance?
     */
    public boolean respectsInheritance() default true;

}
