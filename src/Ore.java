import processing.core.PImage;

import java.util.List;

public class Ore extends ActiveEntity{


    public Ore(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);

    }

    @Override
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {

                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getActionPeriod());


    }

    public void executeActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = world.createOreBlob(this.getID() + BLOB_ID_SUFFIX,
                pos, this.getActionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }


}


