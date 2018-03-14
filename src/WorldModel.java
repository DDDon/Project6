import processing.core.PImage;

import java.util.*;

final class WorldModel
{
    public int numRows;
    public int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;



    private static final int ORE_REACH = 1;

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    private static final int PROPERTY_KEY = 0;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final String MINER_KEY = "miner";
    private static final int MINER_NUM_PROPERTIES = 7;
    private static final int MINER_ID = 1;
    private static final int MINER_COL = 2;
    private static final int MINER_ROW = 3;
    private static final int MINER_LIMIT = 4;
    private static final int MINER_ACTION_PERIOD = 5;
    private static final int MINER_ANIMATION_PERIOD = 6;

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private static final String ORE_KEY = "ore";
    private static final int ORE_NUM_PROPERTIES = 5;
    private static final int ORE_ID = 1;
    private static final int ORE_COL = 2;
    private static final int ORE_ROW = 3;
    private static final int ORE_ACTION_PERIOD = 4;

    private static final String SMITH_KEY = "blacksmith";
    private static final int SMITH_NUM_PROPERTIES = 4;
    private static final int SMITH_ID = 1;
    private static final int SMITH_COL = 2;
    private static final int SMITH_ROW = 3;

    private static final String VEIN_KEY = "vein";
    private static final int VEIN_NUM_PROPERTIES = 5;
    private static final int VEIN_ID = 1;
    private static final int VEIN_COL = 2;
    private static final int VEIN_ROW = 3;
    private static final int VEIN_ACTION_PERIOD = 4;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

    private boolean withinBounds( Point pos)
    {
        return pos.y >= 0 && pos.y < this.numRows &&
                pos.x >= 0 && pos.x < this.numCols;
    }

    public static String getOreKey(){
       return ORE_KEY;
    }

    public static String getQuakeKey(){
       return QUAKE_ID;
    }

    public Optional<Point> findOpenAround(Point pos)
    {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
        {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
            {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (this.withinBounds(newPt) &&
                        !this.isOccupied(newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public void load(Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine())
        {
            try
            {
                if (!this.processLine(in.nextLine(), imageStore))
                {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e)
            {
                System.err.println(String.format("invalid entry on line %d",
                        lineNumber));
            }
            catch (IllegalArgumentException e)
            {
                System.err.println(String.format("issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }

   private  boolean processLine(String line,   // how can i make this object oriented...
                                     ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return this.parseBackground(properties, imageStore);
            case MINER_KEY:
               return this.parseMiner(properties, imageStore);
            case OBSTACLE_KEY:
               return this.parseObstacle(properties,imageStore);
            case ORE_KEY:
               return this.parseOre(properties, imageStore);
            case SMITH_KEY:
               return this.parseSmith(properties,imageStore);
            case VEIN_KEY:
               return this.parseVein(properties, imageStore);
         }
      }

      return false;
   }

    private boolean parseBackground(String [] properties,
                                           ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            this.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }



    /*
     Assumes that there is no entity currently occupying the
     intended destination cell.
  */
    public void addEntity(Entity entity)
    {
        if (this.withinBounds(entity.getPosition()))
        {
            this.setOccupancyCell(entity.getPosition(), entity);  //changed world to this
            this.entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos)
    {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos))
        {
            this.setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);

            entity.setPosition(pos);  // need to add getter method
        }
    }

    public void removeEntity(Entity entity)
    {
        this.removeEntityAt(entity.getPosition());
    }

    private void removeEntityAt(Point pos)
    {
        if (this.withinBounds( pos)
                && this.getOccupancyCell(pos) != null)
        {
            Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
            entity.setPosition(new Point(-1, -1)); //= new Point(-1, -1);
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    private void setBackgroundCell(Point pos,
                                         Background background)
    {
        this.background[pos.y][pos.x] = background;
    }


    private  void setBackground(Point pos,
                                     Background background)
    {
        if (this.withinBounds( pos))
        {
            this.setBackgroundCell(pos, background);
        }
    }

    private void tryAddEntity(Entity entity)
    {
        if (this.isOccupied(entity.getPosition()))
        {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    public Optional<Entity> getOccupant(Point pos)
    {
        if (this.isOccupied(pos))
        {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else
        {
            return Optional.empty();
        }
    }

    public Optional<Entity> findNearest(Point pos,
                                               Class<?> cls)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities)
        {
            if (entity.getClass() == cls)
            {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    private static int distanceSquared(Point p1, Point p2)
    {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }


    private static Optional<Entity> nearestEntity(List<Entity> entities,
                                                 Point pos)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities)
            {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }


    public boolean isOccupied(Point pos)
    {
        return this.withinBounds(pos) &&
                this.getOccupancyCell(pos) != null;
    }



//    public static List<PImage> getImageList(ImageStore imageStore, String key)
//    {
//        return imageStore.images.getOrDefault(key, imageStore.defaultImages);
//    }

    private Background getBackgroundCell(Point pos)
    {
        return this.background[pos.y][pos.x];
    }

    public Optional<PImage> getBackgroundImage(
                                                      Point pos)
    {
        if (this.withinBounds(pos))
        {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        }
        else
        {
            return Optional.empty();
        }
    }

    public Set<Entity> getEntities(){
        return this.entities;
    }




    private boolean parseMiner(String [] properties,
                                     ImageStore imageStore)
    {
        if (properties.length == MINER_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                    Integer.parseInt(properties[MINER_ROW]));
            Entity entity = createMinerNotFull(properties[MINER_ID],
                    Integer.parseInt(properties[MINER_LIMIT]),
                    pt,
                    Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                    Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                    imageStore.getImageList(MINER_KEY));  // Change here
            this.tryAddEntity(entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }

    private  boolean parseObstacle(String [] properties,
                                        ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = createObstacle(properties[OBSTACLE_ID],
                    pt, imageStore.getImageList(OBSTACLE_KEY));  // Change to make object
            this.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    private boolean parseOre(String [] properties,
                                   ImageStore imageStore)
    {
        if (properties.length == ORE_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                    Integer.parseInt(properties[ORE_ROW]));
            Entity entity = createOre(properties[ORE_ID],
                    pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
                    imageStore.getImageList(ORE_KEY)); //Change to imagestore
            this.tryAddEntity(entity);
        }

        return properties.length == ORE_NUM_PROPERTIES;
    }

    private boolean parseSmith(String [] properties,
                                     ImageStore imageStore)
    {
        if (properties.length == SMITH_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                    Integer.parseInt(properties[SMITH_ROW]));
            Entity entity = createBlacksmith(properties[SMITH_ID],
                    pt, imageStore.getImageList(SMITH_KEY));  //change to object oriented
            this.tryAddEntity(entity);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }

    private  boolean parseVein(String [] properties,
                                    ImageStore imageStore)
    {
        if (properties.length == VEIN_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                    Integer.parseInt(properties[VEIN_ROW]));
            Entity entity = createVein(properties[VEIN_ID],
                    pt,
                    Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
                    imageStore.getImageList(VEIN_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == VEIN_NUM_PROPERTIES;
    }

    private Entity getOccupancyCell(Point pos)
    {
        return this.occupancy[pos.y][pos.x];
    }

    private void setOccupancyCell(Point pos,
                                        Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }


    private static Blacksmith createBlacksmith(String id, Point position,
                                          List<PImage> images)
    {
        return new Blacksmith(id, position, images);
    }

    public static MinerFull createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new MinerFull(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public static MinerNotFull createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new MinerNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    private static Obstacle createObstacle(String id, Point position,
                                        List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }

    public static Ore createOre(String id, Point position, int actionPeriod,
                                   List<PImage> images)
    {
        return new Ore(id, position, images,
                actionPeriod);
    }

    public OreBlob createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new OreBlob(id, position, images,
                 actionPeriod, animationPeriod);
    }

    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    private static Vein createVein(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Vein(id, position, images,
                actionPeriod);
    }
}
