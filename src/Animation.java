public class Animation implements Action {

    private AnimatedEntity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Animation(AnimatedEntity entity, WorldModel world,
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
                this.executeAnimationAction(scheduler);
    }


    private void executeAnimationAction(
            EventScheduler scheduler)  // changed action to this
    {
        this.entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    this.entity.createAnimationAction(
                            Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }



}
