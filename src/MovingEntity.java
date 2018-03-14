import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class MovingEntity extends AnimatedEntity {


    public MovingEntity(String id, Point position,
                          List<PImage> images,
                          int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);

    }

    protected static boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }

    protected abstract boolean moveTo(WorldModel world,
                                      Entity target, EventScheduler scheduler);

    protected Point nextPosition(WorldModel world,
                                      Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz,
                this.getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getClass() == Ore.class))) //CHANGED HERE FROM A GETKIND TO A ORE.CLASS
        {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getClass() == Ore.class)))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }






}
