package ir.ac.kntu.logic;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PowerUp extends Element {
    private Pane pane;
    public PowerUp(Pane pane, int xCenter, int yCenter){
        super(xCenter, yCenter);
        this.pane = pane;
        super.setImage(loadImage());
        super.setFitHeight(50);
        super.setFitWidth(50);
        super.relocate(xCenter, yCenter);
    }
    public void remove(){
        pane.getChildren().removeAll(this);
    }
    private Image loadImage(){
        try{
            return new Image(new FileInputStream("src/main/resources/assets/map/powerup.png"));
        } catch (FileNotFoundException e){
            System.out.println("Power up's image file does not exists");
        }
        return null;
    }
}
