import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OreBlob extends MovingEntity {


    public OreBlob(String id, Point position,
                List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);

    }


    public void executeActivity(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(
                this.getPosition(), Vein.class);
        long nextPeriod = this.getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveTo(world, blobTarget.get(), scheduler))
            {
                Quake quake = WorldModel.createQuake(tgtPos,   //CHANGED 'ENTITY' TO 'QUAKE'
                        imageStore.getImageList(WorldModel.getQuakeKey()));

                world.addEntity(quake);  //MAY NEED TO PUT (ENTITY)QUAKE  CHECK OVER
                nextPeriod += this.getAnimationPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                nextPeriod);
    }

    protected boolean moveTo(WorldModel world,
                                  Entity target, EventScheduler scheduler)
    {
        if (adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {


                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getActionPeriod());
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0), this.getAnimationPeriod());
    }
    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }


}
