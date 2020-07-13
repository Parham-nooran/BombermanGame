package ir.ac.kntu.map;


import ir.ac.kntu.logic.Direction;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Side {
    UP(getUpImage(), Direction.UP), DOWN(getDownImage(), Direction.DOWN),
    LEFT(getLeftImage(), Direction.LEFT), RIGHT(getRightImage(), Direction.RIGHT);
    private Image image;
    private Direction openDirection;
    Side(Image image, Direction openDirection){
        this.image = image;
        this.openDirection = openDirection;
    }

    public Direction getOpenDirection() {
        return openDirection;
    }

    private static Image getUpImage(){
        try{
            return new Image(new FileInputStream("src/main/resources/assets/map/oneway/oneway_up.png"));
        } catch (FileNotFoundException e){
            System.out.println("Indicated image file does not exist");
        }
        return null;
    }
    private static Image getDownImage(){
        try{
            return new Image(new FileInputStream("src/main/resources/assets/map/oneway/oneway_down.png"));
        } catch (FileNotFoundException e){
            System.out.println("Indicated image file does not exist");
        }
        return null;
    }
    private static Image getLeftImage(){
        try{
            return new Image(new FileInputStream("src/main/resources/assets/map/oneway/oneway_left.png"));
        } catch (FileNotFoundException e){
            System.out.println("Indicated image file does not exist");
        }
        return null;
    }
    private static Image getRightImage(){
        try{
            return new Image(new FileInputStream("src/main/resources/assets/map/oneway/oneway_right.png"));
        } catch (FileNotFoundException e){
            System.out.println("Indicated image file does not exist");
        }
        return null;
    }
    public Image getImage(){
        return image;
    }
}
