import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity {


    public Quake(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);

    }


    public void executeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    @Override
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {

                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getActionPeriod());
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
                        this.getAnimationPeriod());



    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }



}
