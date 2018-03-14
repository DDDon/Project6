import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public abstract class ActiveEntity extends WorldEntity {

    private int actionPeriod;

    public ActiveEntity(String id, Point position,
                        List<PImage> images, int actionPeriod){
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }


    protected abstract void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore);

    protected abstract void executeActivity(WorldModel world,
                                  ImageStore imageStore, EventScheduler scheduler);




    protected Activity createActivityAction(WorldModel world,
                                            ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }

    protected int getActionPeriod(){return this.actionPeriod;}






}
