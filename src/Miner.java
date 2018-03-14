import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public abstract class Miner extends MovingEntity {

    private int resourceLimit;
    private int resourceCount;

    public Miner(String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod) {

        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;

    }


//    public boolean transform(WorldModel world,  //CHANGED THIS TO BOOLEAN.... CHECK OVER
//                             EventScheduler scheduler, ImageStore imageStore) {
//        MinerNotFull miner = WorldModel.createMinerNotFull(this.getID(), this.resourceLimit,
//                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
//                this.getImages());
//
//        world.removeEntity(miner);
//        scheduler.unscheduleAllEvents(miner);
//
//        world.addEntity(miner);
//        miner.scheduleActions(scheduler, world, imageStore);
//
//        return true;  // MADE NOT VOID AND ADDED THIS RETURN... CHECK!!
//    }

    protected void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler)
    {

        if(this instanceof MinerFull){

            Optional<Entity> fullTarget = world.findNearest(this.getPosition(), Blacksmith.class);

            if (fullTarget.isPresent() &&
                    this.moveTo( world, fullTarget.get(), scheduler))  // changed entity to this
            {
                this.transform( world, scheduler, imageStore);
            }
            else
            {
                scheduler.scheduleEvent(this,
                        this.createActivityAction( world, imageStore),
                        this.getActionPeriod());
            }
        }

        else{
            Optional<Entity> notFullTarget = world.findNearest( this.getPosition(),
                    Ore.class);

            if (!notFullTarget.isPresent() ||
                    !this.moveTo( world, notFullTarget.get(), scheduler) ||
                    !this.transform( world, scheduler, imageStore))
            {
                scheduler.scheduleEvent(this,
                        this. createActivityAction( world, imageStore),
                        this.getActionPeriod());
            }


        }

    }

    protected void setResourceCount(){this.resourceCount += 1;}





//    public Point nextPositionMiner(WorldModel world,
//                                   Point destPos)
//    {
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz,
//                this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x,
//                    this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
//    }



    protected boolean transform(WorldModel world,
                             EventScheduler scheduler, ImageStore imageStore) {
        if (this instanceof MinerNotFull){
            if (this.resourceCount >= this.resourceLimit) {
                MinerFull miner = WorldModel.createMinerFull(this.getID(), this.resourceLimit,  // miner = this
                        this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                        this.getImages());

                world.removeEntity(miner);
                scheduler.unscheduleAllEvents(miner);

                world.addEntity(miner);
                miner.scheduleActions(scheduler, world, imageStore);

                return true;
            }

        return false;
    }

    else{
            MinerNotFull miner = WorldModel.createMinerNotFull(this.getID(), this.resourceLimit,
                    this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(miner);
            scheduler.unscheduleAllEvents(miner);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;

        }

    }



}
