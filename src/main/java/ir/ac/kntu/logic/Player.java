package ir.ac.kntu.logic;

import ir.ac.kntu.map.Type;
import ir.ac.kntu.map.Wall;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.stream.Collectors;

public class Player extends Element implements Movable,Bomber {
    private String name;
    private Director director;
    private SerializedImageView [] images;
    private boolean isAlive;
    private Pane pane;
    private int bombRadius;
    public Player(String name, int xCenter, int yCenter, Image[]images){
        super(images[0], xCenter, yCenter);
        this.isAlive = true;
        this.images = new SerializedImageView[images.length];
        this.bombRadius = 150;
        for(int i=0;i<images.length;i++){
            this.images[i]=new SerializedImageView(images[i]);
        }
        this.setImage(images[0]);
        this.relocate(xCenter,yCenter);
        this.setFitHeight(50);
        this.setFitWidth(50);
    }
    @Override
    public void move(Direction direction){
        if(pane.getChildren().stream().filter(node -> node instanceof Wall).
                noneMatch(node -> ((Wall) node).getXCenter() == getXCenter() + direction.getXValue() &&
                        ((Wall) node).getYCenter() == getYCenter() + direction.getYValue())) {
            if (getXCenter() + 50 + direction.getXValue() > 0 &&
                    getXCenter() + 50 + direction.getXValue() < pane.getWidth()) {
                setXCenter( getXCenter()+ direction.getXValue());
            }
            if (getYCenter() + 50 + direction.getYValue() > 0 &&
                    getYCenter() + 50 + direction.getYValue() < pane.getHeight()) {
                setYCenter(getYCenter()+direction.getYValue());
            }
        }
        this.relocate(super.getXCenter(), super.getYCenter());
    }
    public void kill(){
        this.isAlive = false;
        director.writeToFile(this);
        pane.getChildren().remove(this);
    }
    public void setBomb(int xCenter, int yCenter){
        Bomb bomb = new Bomb(pane, xCenter, yCenter, bombRadius);
        pane.getChildren().add(bomb);
        Thread thread = new Thread(() ->{
            try {
                Thread.sleep(3000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            Platform.runLater(bomb);
        });
        thread.start();
    }

    public void setBombRadius(int bombRadius) {
        this.bombRadius = bombRadius;
    }

    public void setDirector(Director director) {
        this.director = director;
        this.pane = director.getPane();
    }

    public void load(){
        pane.getChildren().add(this);
        removeBrickWalls();
    }
    private void removeBrickWalls(){
        pane.getChildren().removeAll(pane.getChildren().stream().filter(node -> filterWalls(node)).
                collect(Collectors.toList()));
    }
    private boolean filterWalls(Node node){
        return node instanceof Wall&&((Wall) node).getType().equals(Type.BRICK)&&
                ((Wall) node).getXCenter()==getXCenter()&&((Wall) node).getYCenter()==getYCenter();
    }
    @Override
    public void bomb() {
        setBomb(super.getXCenter(), super.getYCenter());
    }

    public boolean isAlive() {
        return isAlive;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return isAlive == player.isAlive &&
                bombRadius == player.bombRadius &&
                Objects.equals(name, player.name) &&
                director.equals(player.director) &&
                Arrays.equals(images, player.images) &&
                pane.equals(player.pane);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, director, isAlive, pane, bombRadius);
        result = 31 * result + Arrays.hashCode(images);
        return result;
    }*/
}
