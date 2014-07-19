package sourcecoded.events.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Apply this annotation to a class extending AbstractEvent to make the event cancelable
 * @author SourceCoded
 */
@Retention(value = RUNTIME)
@Target(value = TYPE)
public @interface Cancelable { }
