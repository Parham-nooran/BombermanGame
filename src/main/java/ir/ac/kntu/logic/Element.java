package ir.ac.kntu.logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Element extends ImageView {
    private int xCenter;
    private int yCenter;
    public Element(Image image, int xCenter, int yCenter){
        super(image);
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
