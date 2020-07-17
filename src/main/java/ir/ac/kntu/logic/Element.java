package ir.ac.kntu.logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Element extends ImageView implements Serializable {
    private int xCenter;
    private int yCenter;
    public Element(){

    }
    public Element(Image image, int xCenter, int yCenter){
        super(image);
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }
    public Element(int xCenter, int yCenter){
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }
    public int getXCenter() {
        return xCenter;
    }

    public void setXCenter(int xCenter) {
        this.xCenter = xCenter;
    }

    public int getYCenter() {
        return yCenter;
    }

    public void setYCenter(int yCenter) {
        this.yCenter = yCenter;
    }
}
