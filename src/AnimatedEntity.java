import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity {

    private int animationPeriod;

    public AnimatedEntity(String id, Point position,
                          List<PImage> images,
                          int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;

    }

    public Animation createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }


    public void nextImage()
    {
        this.setImageIndex( (this.getImageIndex() + 1) % this.getImages().size() );
    }







}
