package ir.ac.kntu.logic;

import javafx.application.Platform;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AI {
    private Player player;
    private boolean search;
    public AI(Player player) {
        this.player = player;
        this.search = true;
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
                    Platform.runLater(() -> {
                        if(search) {
                            findAWay();
                        }
                        checkArea();
                        setBomb();
                    });
                }
            }
        }).start();
    }
    private void setBomb(){
        if(getDistance(nearestPlayer())<50){
            player.setBomb();
        }
    }
    private void checkArea(){
        List<Node> bombs =  player.getPane().getChildren().stream().filter(node -> node instanceof Bomb&&
                getDistance(player, ((Bomb) node).getXCenter(), ((Bomb) node).getYCenter())<150).
                collect(Collectors.toList());
        if(!bombs.isEmpty()){
            for(Direction direction:checkDirections()){
                if (getDistance(nearestPlayer(), player.getXCenter() + direction.getXValue(),
                        player.getYCenter() + direction.getYValue()) > getDistance(player,
                        ((Bomb)bombs.get(0)).getXCenter(), ((Bomb)bombs.get(0)).getYCenter())) {
                    player.move(direction);
                }
            }
        }
        search = bombs.isEmpty();
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
