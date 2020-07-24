package ir.ac.kntu.logic;

import javafx.application.Platform;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AI {
    private Player player;
    public AI(Player player) {
        this.player = player;
    }

    public void load(){
        startThread();
    }
    public void startThread(){
        new Thread(() ->{
            while(player.isAlive()){
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    System.out.println("AI interrupted");
                }
                if(player.isAlive()) {
                    Platform.runLater(this::findAWay);
                }
            }
        }).start();
    }

    public void findAWay(){
        if(!checkDirections().isEmpty()){
            for(Direction direction:checkDirections()){
                if (getDistance(nearestPlayer(), player.getXCenter() + direction.getXValue(),
                        player.getYCenter() + direction.getYValue()) < getDistance(nearestPlayer())) {
                    player.move(direction);
                }
            }
        }
    }
    public ArrayList<Direction> checkDirections(){
        ArrayList<Direction> availableDirections = new ArrayList<>();
        for(Direction direction: Direction.values()){
            if(checkSides(direction)){
                availableDirections.add(direction);
            }
        }
        return availableDirections;
    }
    public boolean checkSides(Direction direction){
        return player.checkDestination(direction);
    }

    public Player nearestPlayer(){
        Player minPlayer = findAPlayer();
        if(minPlayer!=null) {
            double distance = getDistance(minPlayer);
            for (Node player : player.getPane().getChildren().stream().filter(node -> node instanceof Player &&
                    !node.equals(player)).collect(Collectors.toList())) {
                if (getDistance((Player) player) < distance) {
                    distance = getDistance((Player) player);
                    minPlayer = (Player) player;
                }
            }
        }
        return minPlayer;
    }
    private Player findAPlayer(){
        if(player.isAlive()) {
            return (Player) player.getPane().getChildren().stream().filter(node -> node instanceof Player &&
                    !node.equals(player)).collect(Collectors.toList()).get(0);
        }
        return null;
    }
    private double getDistance(Player minPlayer){
        if(player.isAlive()) {
            return Math.sqrt(Math.pow(player.getXCenter() - minPlayer.getXCenter(), 2) +
                    Math.pow(player.getYCenter() - minPlayer.getYCenter(), 2));
        }
        return 0.0;
    }
    private double getDistance(Player minPlayer, int xCenter, int yCenter){
        if(player.isAlive()) {
            return Math.sqrt(Math.pow(xCenter - minPlayer.getXCenter(), 2) +
                    Math.pow(yCenter - minPlayer.getYCenter(), 2));
        }
        return 0.0;
    }
}
