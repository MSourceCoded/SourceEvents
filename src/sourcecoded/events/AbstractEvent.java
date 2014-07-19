package sourcecoded.events;

import sourcecoded.events.annotation.Cancelable;

/**
 * Extend this to add your own events
 *
 * @author SourceCoded
 */
public abstract class AbstractEvent {

    private boolean isCancelled;
    private final boolean canCancel;

    @SuppressWarnings("ReflectionForUnavailableAnnotation")
    public AbstractEvent() {
        canCancel = this.getClass().getAnnotation(Cancelable.class) != null;
    }

    /**
     * Can this event be cancelled?
     */
    public boolean canCancel() {
        return canCancel;
    }

    /**
     * Is this event cancelled?
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Set this event to be cancelled
     */
    public void setCancelled(boolean cancel) {
        if (!canCancel())
            throw new IllegalArgumentException("Tried to cancel uncancelable event " + this.getClass().toString());

        isCancelled = cancel;
    }

}
