package ir.ac.kntu.logic;

import ir.ac.kntu.map.*;
import javafx.application.Platform;
import javafx.scene.Node;

import java.util.List;
import java.util.stream.Collectors;

public class Random implements Runnable{
    private Director director;
    public Random(Director director){
        this.director = director;
    }
    @Override
    public void run(){
        action();
    }
    private void action(){
        switch (getRandomInt(4)){
            case 0:
                setBrickWall();
                break;
            case 1:
                setBomb();
                break;
            case 2:
                setOneWayBlock();
                break;
            case 3:
                setPowerUp();
                break;
            default:
                break;
        }
    }
    public void start(){
        new Thread(() ->{
            while(!director.isFinished()){
                try {
                    Thread.sleep(5000);
                } catch(InterruptedException e){
                    System.out.println("Random generator interrupted");
                }
                if(!director.isFinished()) {
                    Platform.runLater(this);
                }
            }
        }).start();
    }
    public void setBomb(){
        Block block = findRandomFreePoint();
        new Bomb(director.getPane(), block.getXCenter(), block.getYCenter(), 150).start();
    }
    public void setBrickWall(){
        Block block = findRandomFreePoint();
        director.getPane().getChildren().add(new Wall(block.getXCenter(), block.getYCenter(), Type.BRICK));
    }
    public void setOneWayBlock(){
        Block block = findRandomFreePoint();
        int random = getRandomInt(4);
        Side side = random<2?random<1?Side.DOWN:Side.UP:random<3?Side.LEFT:Side.RIGHT;
        director.getPane().getChildren().add(new OneWay(block.getXCenter(), block.getYCenter(), side));
    }
    public void setPowerUp(){
        Block block = findRandomFreePoint();
        director.getPane().getChildren().add(new PowerUp(director.getPane(), block.getXCenter(), block.getYCenter()));
    }
    private Block findRandomFreePoint(){
        List<Node> list = director.getPane().getChildren().stream().filter(this::filterNodes).
                collect(Collectors.toList());
        return (Block)list.get(getRandomInt(list.size()));
    }

    private boolean filterNodes(Node node){
        return node instanceof Block&&!isThereAnElement(((Block) node).getXCenter(), ((Block) node).getYCenter());
    }
    public int getRandomInt(int maximum){
        return (int)(Math.random()*maximum);
    }
    public boolean isThereAnElement(int xCenter, int yCenter){
        return director.getPane().getChildren().stream().filter(node -> node instanceof Element).
                anyMatch(node -> ((Element) node).getXCenter() == xCenter &&
                        ((Element) node).getYCenter() == yCenter);
    }
}