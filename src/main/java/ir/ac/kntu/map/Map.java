package ir.ac.kntu.map;

import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.Element;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

public class Map implements Serializable {
    private Director director;

    public Map(Director director) {
        this.director = director;
        director.setMap(this);
    }
    public void load()  {
        try {
            Image block = new Image(new FileInputStream("src/main/resources/assets/map/normal.png"));
            Image ironWall = new Image(new FileInputStream("src/main/resources/assets/map/wall.png"));
            Image brickWall = new Image(new FileInputStream("src/main/resources/assets/map/block.png"));
            loadBackGround(block);
            loadIronWalls(ironWall);
            loadRows(ironWall);
            loadColumns(ironWall);
            loadBrickWalls(brickWall);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    private void loadRows(Image image){
        loadARow(0,0,16, image);
        loadARow(0, 11*50, 16, image);
    }
    private void loadColumns(Image image){
        loadAColumn(0, 50,10, image);
        loadAColumn(15*50, 50, 10, image);
    }
    private void loadARow(int xBegin, int yCenter, int number, Image image){
        for(int i=0;i<number;i++){
            director.getPane().getChildren().add(new Wall(50*i+xBegin, yCenter, Type.IRON, image));
        }
    }
    private void loadAColumn(int xCenter, int yBegin, int number, Image image){
        for(int i=0;i<number;i++){
            director.getPane().getChildren().add(new Wall(xCenter, 50*i+yBegin, Type.IRON, image));
        }
    }
    private void loadBackGround(Image image){
        for(int i=0;i<16;i++){
            for(int j=0;j<12;j++){
                director.getPane().getChildren().add(new Block(50*i,50*j,image));
            }
        }
    }
    private void loadBrickWalls(Image image){
        for (int i = 0; i < 60;) {
            int xCenter = (int) ((16 * Math.random())) * 50, yCenter =(int) ((12 * Math.random())) * 50;
            if(director.getPane().getChildren().stream().filter(node -> node instanceof Element).
                    noneMatch(node -> ((Element)node).getXCenter()==xCenter&&
                            ((Element) node).getYCenter()==yCenter)) {
                director.getPane().getChildren().add(new Wall(xCenter, yCenter, Type.BRICK, image));
                i++;
            }
        }
    }
    private void loadIronWalls(Image image){
        for (int i = 1; i < 12; i += 2) {
            for (int j = 1; j < 8; j += 2) {
                director.getPane().getChildren().
                        add(new Wall(50 * i + 50, 50 * j + 50, Type.IRON, image));
            }
        }
    }
    public void loadOneWayBlock(Image image){

    }
}