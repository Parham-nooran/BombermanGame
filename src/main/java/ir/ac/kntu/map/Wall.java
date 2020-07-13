package ir.ac.kntu.map;

import ir.ac.kntu.logic.Element;
import javafx.scene.image.Image;

public class Wall extends Element {
    private Type type;
    public Wall(int xCenter, int yCenter, Type type, Image image){
        super(image, xCenter, yCenter);
        this.type = type;
        this.relocate(xCenter, yCenter);
        this.setFitHeight(50);
        this.setFitWidth(50);
    }


    public Type getType() {
        return type;
    }

}
