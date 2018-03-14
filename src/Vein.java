import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Vein extends ActiveEntity{


    public Vein(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {

        super(id, position, images, actionPeriod);

    }


    protected void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {
            Ore ore = WorldModel.createOre(ORE_ID_PREFIX + this.getID(),
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(WorldModel.getOreKey()));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
    }


    protected void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {

                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getActionPeriod());
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }





}
