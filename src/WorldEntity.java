import processing.core.PImage;
import java.util.List;

public abstract class WorldEntity implements Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;  //changed to public just for a sec ;)

    public WorldEntity(String id, Point position,
                       List<PImage> images){

        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;

    }


    public PImage getCurrentImage()
    {
        return (this).images.get(this.imageIndex);

    }

    protected String getID(){return this.id;}

    public Point getPosition(){
        return this.position;
    }

    public void setPosition(Point pos){
        this.position = pos;
    }

    protected int getImageIndex(){return this.imageIndex;}

    protected List<PImage> getImages(){return this.images;}

    protected void setImageIndex(int imageIndex){this.imageIndex = imageIndex;}



    public abstract <R> R accept(EntityVisitor<R> visitor);


}
