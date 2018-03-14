public class Activity implements Action {

    //private ActionKind kind;
    private ActiveEntity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Activity(ActiveEntity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        //this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }


    public void executeAction(EventScheduler scheduler)  // changed action to this
    {
                this.executeActivityAction(scheduler);
    }

    private void executeActivityAction(
            EventScheduler scheduler) //may need another getter, changed action to this
    {
                this.entity.executeActivity(this.world,
                        this.imageStore, scheduler);

    }


}
