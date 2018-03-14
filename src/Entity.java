import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

interface Entity
{


   static final String BLOB_KEY = "blob";
   static final String BLOB_ID_SUFFIX = " -- blob";
   static final int BLOB_PERIOD_SCALE = 4;
   static final int BLOB_ANIMATION_MIN = 50;
   static final int BLOB_ANIMATION_MAX = 150;

   static final String ORE_ID_PREFIX = "ore -- ";
   static final int ORE_CORRUPT_MIN = 20000;
   static final int ORE_CORRUPT_MAX = 30000;


   static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

   static final Random rand = new Random();



   public PImage getCurrentImage();


   public Point getPosition();

   public void setPosition(Point pos);

}
