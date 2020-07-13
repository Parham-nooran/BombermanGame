package ir.ac.kntu.logic;

import ir.ac.kntu.file.FileManager;
import ir.ac.kntu.map.Map;
import ir.ac.kntu.status.Timer;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.Serializable;
import java.util.ArrayList;

public class Director implements Serializable, Runnable {
    private ArrayList<Player> players;
    private Pane pane;
    private Timer timer;
    private Map map;
    private boolean finished;
    private FileManager fileManager;
    private String playersFile;
    private String mapFile;
    public  Director(Pane pane, ArrayList<Player> players, Timer timer, String playersFile, String mapFile) {
        this.mapFile = mapFile;
        this.playersFile = playersFile;
        this.fileManager = new FileManager();
        this.players = players;
        this.pane = pane;
        this.timer = timer;
        this.map = new Map(this);
        this.finished = false;
        setPlayersDirector();
    }

    public void setMap(Map map) {
        this.map = map;
    }
    private void setPlayersDirector(){
        players.iterator().forEachRemaining(player -> player.setDirector(this));
    }
    public Pane getPane(){
        return pane;
    }

    public void actionOnKeyPress(KeyEvent keyEvent){
        if(!players.isEmpty()) {
            if(players.size()>0) {
                movePlayer1(keyEvent.getCode());
                if(players.size()>1) {
                    movePlayer2(keyEvent.getCode());
                }
                if(players.size()>2) {
                    movePlayer3(keyEvent.getCode());
                }
                if(players.size()>3) {
                    movePlayer4(keyEvent.getCode());
                }
            }

        }
    }
    public void start(){
        load();
        Thread thread = new Thread(() ->{
            while(finished){
                try{
                    Thread.sleep(17);
                }catch (InterruptedException e){
                    System.out.println("Director has been interrupted");
                    e.printStackTrace();
                }
            }
            Platform.runLater(this);
        });
        thread.start();
    }
    private void load(){
        map.load();
        players.iterator().forEachRemaining(Player::load);
        new Random(this).start();
        timer.load();
    }
    @Override
    public void run(){
        players.iterator().forEachRemaining(player -> finished=finished||!player.isAlive());
        finished = finished||timer.isFinished();
        if(finished){
            pane.getChildren().removeAll(pane.getChildren());
        }
    }
    public void writeToFile(Player player){
        fileManager.storeInFile(player, playersFile);
    }
    private void movePlayer1(KeyCode keyCode){
        Player player = players.get(0);
        if(player.isAlive()){
            switch (keyCode){
                case UP:
                    player.move(Direction.UP);
                    break;
                case DOWN:
                    player.move(Direction.DOWN);
                    break;
                case LEFT:
                    player.move(Direction.LEFT);
                    break;
                case RIGHT:
                    player.move(Direction.RIGHT);
                    break;
                case SHIFT:
                    player.bomb();
                default:
                    break;
            }
        }
    }
    private void movePlayer2(KeyCode keyCode){
        Player player = players.get(1);
        if(player.isAlive()){
            switch (keyCode){
                case W:
                    player.move(Direction.UP);
                    break;
                case S:
                    player.move(Direction.DOWN);
                    break;
                case A:
                    player.move(Direction.LEFT);
                    break;
                case D:
                    player.move(Direction.RIGHT);
                    break;
                case Q:
                    player.bomb();
                default:
                    break;
            }
        }
    }
    private void movePlayer3(KeyCode keyCode){
        Player player = players.get(2);
        if(player.isAlive()){
            switch (keyCode){
                case T:
                    player.move(Direction.UP);
                    break;
                case G:
                    player.move(Direction.DOWN);
                    break;
                case F:
                    player.move(Direction.LEFT);
                    break;
                case H:
                    player.move(Direction.RIGHT);
                    break;
                case R:
                    player.bomb();
                default:
                    break;
            }
        }
    }
    private void movePlayer4(KeyCode keyCode){
        Player player = players.get(3);
        if(player.isAlive()){
            switch (keyCode){
                case I:
                    player.move(Direction.UP);
                    break;
                case K:
                    player.move(Direction.DOWN);
                    break;
                case J:
                    player.move(Direction.LEFT);
                    break;
                case L:
                    player.move(Direction.RIGHT);
                    break;
                case U:
                    player.bomb();
                default:
                    break;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public Timer getTimer() {
        return timer;
    }
}