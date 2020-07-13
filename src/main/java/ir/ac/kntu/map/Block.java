package ir.ac.kntu.map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Block extends ImageView {
    private int xCenter, yCenter;
    public Block(int xCenter, int yCenter,Image image){
        super(image);
        super.relocate(xCenter,yCenter);
        super.setFitHeight(50);
        super.setFitWidth(50);
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }

    public int getXCenter() {
        return xCenter;
    }

    public int getYCenter() {
        return yCenter;
    }
}
