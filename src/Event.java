final class Event
{
   private Action action;
   public long time;
   public Entity entity;

   public Event(Action action, long time, Entity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }

   public Action getAction(){
      return this.action;
   }
}
