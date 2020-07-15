package ir.ac.kntu.map;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Type {
    BRICK(true, getBrickWallImage()), IRON(false, getIronWallImage()),
    ONE_WAY(false);
    private boolean breakable;
    private Image image;
    Type(boolean breakable, Image image){
        this.breakable=breakable;
        this.image = image;
    }
    Type(boolean breakable){
        this.breakable = breakable;
    }


    public boolean isBreakable() {
        return breakable;
    }
    private static Image getIronWallImage(){
        try {
            return new Image(new FileInputStream("src/main/resources/assets/map/wall.png"));
        } catch (FileNotFoundException e){
            System.out.println("");
        }
        return null;
    }
    private static Image getBrickWallImage(){
        try {
            return new Image(new FileInputStream("src/main/resources/assets/map/block.png"));
        } catch (FileNotFoundException e){
            System.out.println("");
        }
        return null;
    }

    public Image  getImage(){
        return image;
    }
}