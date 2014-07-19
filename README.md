SourceEvents
============

SourceEvents is a simple Event System API developed for Java.


### Features
  * EventBus' are objects, so you can have multiple eventsystems
  * Ability to register your own events
  * Prioritization from HIGHEST to LOWEST
  * Cancellable events (as well as a bypass)

### Getting started
  The first thing to do is to create an Instance of the EventBus, which is simple
  ```java
    EventBus theEventBus = new EventBus();
  ```
  
  Next, add your handlers via Annotations
  ```java
    @EventListener
    public void theEvent(YourEventHere param) {}
  ```
  
  Next, register your handlers to the EventBus
  ```java
    theEventBus.register(YourClass.class);
  ```
  
  To be noted, you can also use class instances in the form of an object if you wish. This will allow it to work with non-static fields
  ```java
   theEventBus.register(new YourObject());
  ```
      
  That's all! Now you can call your events as follows:
  ```java
    theEventBus.raiseEvent(new YourEvent());
  ```

### Custom Events
  To create a custom event, just make a new class that extends AbstractEvent
  ```java
    public class YourEvent extends AbstractEvent
  ```
  
  This is all you need to do, you can add your own parameters and methods here if you wish

### Annotations
  **@Cancelable**
  Cancelable is an annotation that is put on your Custom Events. This allows events to be canceled as they are called. A standard implementation follows
  
  ```java
    @Cancelable
    public class YourEvent extends AbstractEvent {}
  ```
  
  ```java
    @EventListener
    public static void yourMethod(YourEvent event) {
      event.setCancelled(true);
    }
  ```
  
  EventListeners will not be triggered if the event is cancelled unless they have the argument allowCancelled = true
  
  **@EventListener**
  EventListener is an annotation placed above methods to listen for incoming events. It has 2 properties, priority and allowCancelled.
  
  priority is used to determine the urgent-ness of the listener. It takes the value in form of an EventPriority enum value. By default this is set to EventPriority.NORMAL
  
  allowCancelled, if set to true, will trigger if the event is marked as cancelled. This is set to false by default.
  
  A sample implementation is seen below
  ```java
    @EventListener
    public static void standardListener(YourEvent event) {}
  
    @EventListener(priority = EventPriority.HIGHEST)
    public static void triggerMeFirst(YourEvent event) {}
    
    @EventListener(allowCancelled = true)
    public static void triggerNoMatterWhat(YourEvent event) {}
  ```
