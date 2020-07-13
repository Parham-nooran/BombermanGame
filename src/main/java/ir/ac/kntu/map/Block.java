package ir.ac.kntu.map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Block extends ImageView {
    public Block(int x, int y,Image image){
        super(image);
        super.relocate(x,y);
        super.setFitHeight(50);
        super.setFitWidth(50);
    }

}
