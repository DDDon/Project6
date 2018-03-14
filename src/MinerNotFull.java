import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner {

    public MinerNotFull(String id, Point position,
                        List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod)
    {

        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }

    @Override
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {

                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getActionPeriod());
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0), this.getAnimationPeriod());

    }

    protected boolean moveTo( WorldModel world,
                                   Entity target, EventScheduler scheduler)
    {
        if (adjacent(this.getPosition(), target.getPosition()))
        {
            this.setResourceCount();
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = this.nextPosition( world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);  // changed
            }
            return false;
        }
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }



}
