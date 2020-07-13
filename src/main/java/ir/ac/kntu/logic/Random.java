package ir.ac.kntu.logic;

import ir.ac.kntu.map.Block;
import ir.ac.kntu.map.Side;
import ir.ac.kntu.map.Type;
import ir.ac.kntu.map.Wall;
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
        setOneWayBlock();
    }
    public void start(){
        new Thread(() ->{
            while(!director.isClosed()){
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException e){
                    System.out.println("Interrupted");
                }
                Platform.runLater(this);
            }
        }).start();
    }
    public void setBomb(){

    }
    public void setOneWayBlock(){
        Block block = findRandomFreePoint();
        Type type = Type.ONE_WAY;
        type.setSide(Side.DOWN);
        director.getPane().getChildren().add(new Wall(block.getXCenter(), block.getYCenter(), type));
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
    private int getRandomInt(int maximum){
        return (int)(Math.random()*maximum);
    }
    public boolean isThereAnElement(int xCenter, int yCenter){
        return director.getPane().getChildren().stream().filter(node -> node instanceof Element).
                anyMatch(node -> ((Element) node).getXCenter() == xCenter &&
                        ((Element) node).getYCenter() == yCenter);
    }
}