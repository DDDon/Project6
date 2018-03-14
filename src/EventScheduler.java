import java.util.*;

final class EventScheduler
{
   public PriorityQueue<Event> eventQueue;
   public Map<Entity, List<Event>> pendingEvents;
   public double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }



   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = this.pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            this.eventQueue.remove(event);
         }
      }
   }
   public void updateOnTime(long time)             // changed scheduler to 'this'
   {
      while (!this.eventQueue.isEmpty() &&
              this.eventQueue.peek().time < time)
      {
         Event next = this.eventQueue.poll();

         this.removePendingEvent(next);

         next.getAction().executeAction(this);
      }
   }

    public void scheduleEvent(Entity entity, Action action, long afterPeriod)
    {
        long time = System.currentTimeMillis() +
                (long)(afterPeriod * this.timeScale);
        Event event = new Event(action, time, entity);

        this.eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = this.pendingEvents.getOrDefault(entity,
                new LinkedList<>());
        pending.add(event);
        this.pendingEvents.put(entity, pending);
    }

   private void removePendingEvent(Event event)
   {
      List<Event> pending = this.pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }






}
