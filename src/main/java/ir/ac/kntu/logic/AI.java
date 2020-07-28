package ir.ac.kntu.logic;
import ir.ac.kntu.map.Type;
import ir.ac.kntu.map.Wall;
import javafx.application.Platform;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AI {
    private Player player;
    private boolean wait;
    public AI(Player player) {
        this.player = player;
    }
    public void load(){
        startThread();
    }
    public void startThread(){
        Thread thread = new Thread(() ->{
            while(player.isAlive()){
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    System.out.println("AI interrupted");
                }
                if(player.isAlive()) {
                    try {
                        Platform.runLater(() -> {
                            findAWay();
                            checkArea();
                            setBomb();
                            removeWalls();
                        });
                    } catch (NullPointerException e){
                        System.out.print("");
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
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
    }
    private void releaseWait(int seconds){
        new Thread(() ->{
            try{
                Thread.sleep( 1000*seconds);
            } catch (InterruptedException e){
                wait = false;
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
    public void diagonalMovement(){
        for(int i=4;i<8;i++){
            if(player.checkDestination(Direction.values()[i])){
                player.move(Direction.values()[i]);
                wait = true;
                releaseWait(3);
                break;
            }
        }
    }
    public ArrayList<Direction> checkDirections(){
        ArrayList<Direction> availableDirections = new ArrayList<>();
        for(Direction direction: Direction.values()){
            if(checkSides(direction, true)){
                availableDirections.add(direction);
            }
        }
        return availableDirections;
    }
    public boolean checkSides(Direction direction, boolean positive){
        return positive?player.checkDestination(direction):player.getPane().getChildren().stream().anyMatch(node ->
                node instanceof Wall&&((Wall) node).getType().
                equals(Type.BRICK)&&((Wall) node).getXCenter()==player.getXCenter()+direction.getXValue()&&
                ((Wall) node).getYCenter()==player.getYCenter()+direction.getYValue());
    }

    private void removeWalls(){
        for(Direction direction:notAvailAvailableDirections()){
            if(player.getPane().getChildren().stream().anyMatch(node -> node instanceof Wall&& ((Wall)node).
                    getType().equals(Type.BRICK)&&((Wall) node).getXCenter()==player.getXCenter()+direction.getXValue()
                    && ((Wall) node).getYCenter()==player.getYCenter()+direction.getYValue())){
                player.setBomb();
                diagonalMovement();
            }
        }
    }
    private ArrayList<Direction> notAvailAvailableDirections(){
        ArrayList<Direction> notAvailAvailableDirections = new ArrayList<>();
        for(Direction direction: Direction.values()){
            if(!checkSides(direction, false)){
                notAvailAvailableDirections.add(direction);
            }
        }
        return notAvailAvailableDirections;
    }
    private void checkDiagonal(Direction first, Direction second){

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
            try {
                return (Player) player.getPane().getChildren().stream().filter(node -> node instanceof Player &&
                        !node.equals(player)).collect(Collectors.toList()).get(0);
            } catch (IndexOutOfBoundsException e){
                System.out.print("");
            }
        }
        return null;
    }
    private double getDistance(Player minPlayer){
        if(player.isAlive()&&minPlayer!=null) {
            return Math.sqrt(Math.pow(player.getXCenter() - minPlayer.getXCenter(), 2) +
                    Math.pow(player.getYCenter() - minPlayer.getYCenter(), 2));
        }
        return 0.0;
    }
    private double getDistance(Player minPlayer, int xCenter, int yCenter){
        if(player.isAlive()&&minPlayer!=null) {
            return Math.sqrt(Math.pow(xCenter - minPlayer.getXCenter(), 2) +
                    Math.pow(yCenter - minPlayer.getYCenter(), 2));
        }
        return 0.0;
    }
}
