package ir.ac.kntu.logic;

import ir.ac.kntu.file.FileManager;
import ir.ac.kntu.map.Map;
import ir.ac.kntu.menu.Control;
import ir.ac.kntu.menu.Main;
import ir.ac.kntu.menu.Ranking;
import ir.ac.kntu.status.Timer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Director implements Runnable {
    private ArrayList<Player> players;
    private SerializedPane pane;
    private Timer timer;
    private Map map;
    private boolean finished;
    private boolean closed;
    private Main main;
    private String mapFile;
    private Stage stage;
    private Scene scene;
    public  Director(SerializedPane pane, Stage stage, Scene scene, String mapFile) {
        this.mapFile = mapFile;
        this.players = new ArrayList<>();
        this.scene = scene;
        this.timer = new Timer(pane, 0, 3,0,true, false);
        this.stage = stage;
        this.pane = pane;
        this.closed = false;
        this.map = new Map(this, mapFile, 50);
        this.finished = false;
        setStageStatus();
    }
    private void setStageStatus(){
        stage.setOnCloseRequest(windowEvent -> {
            setClosed(true);
            stage.close();
        });
    }
    public void addAll(ArrayList<Player> players){
        this.players.addAll(players.stream().filter(player -> !this.players.contains(player)).
                collect(Collectors.toList()));
    }
    public void removeAll(ArrayList<Player> players){
        this.players.removeAll(players);
    }

    public Main getMain() {
        return main;
    }

    public void setMapFile(String mapFile){
        this.mapFile = mapFile;
        this.map = new Map(this, mapFile, 50);
    }
    public SerializedPane getPane() {
        return pane;
    }

    public void actionOnKeyPress(KeyEvent keyEvent){
        if(!players.isEmpty()) {
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
    public void start(){
        load();
        startCountDown();
    }
    private void startCountDown(){
        Timer newTimer = new Timer(pane, 0, 0, 5, true, true);
        newTimer.load(340, 250, 20);
        startTheGame();
    }
    public void startTheGame(){
        new Thread(() -> {
            try{
                Thread.sleep(6000);
            } catch (InterruptedException e){
                System.out.println("Timer was interrupted");
            }
            Platform.runLater(() -> {
                startThread();
                players.iterator().forEachRemaining(Player::load);
                new Random(this).start();
                timer.load();
            });
        }).start();
    }
    public boolean isClosed() {
        return closed;
    }
    public void addPlayer(Player player){
        players.add(player);
    }
    private void startThread(){
        Thread thread = new Thread(() ->{
            while(!finished){
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    System.out.println("Director has been interrupted");
                    e.printStackTrace();
                }
                if(!finished) {
                    Platform.runLater(this);
                }
            }
        });
        thread.start();
    }
    private void load(){
        map.load();
        relocatePlayers();
        players.iterator().forEachRemaining(player -> {
            player.setPane(pane);
            player.setTimer(timer);
        });

    }
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    private void relocatePlayers(){
        ArrayList<Integer> availableCoordinates = map.getPlayersCoordinates(false);
        players.iterator().forEachRemaining(player -> {
            int random = Random.getRandomInt(0,availableCoordinates.size());
            player.relocate(availableCoordinates.get(random)/1000, availableCoordinates.get(random)%1000);
            availableCoordinates.remove(random);
        });
    }
    @Override
    public void run(){
        finished = closed || timer.isFinished()|| pane.getChildren().stream().filter(node ->
                node instanceof Player).count()<2;
        timer.setFinished(finished);
        if(finished&&!closed){
            checkPlayers();
            new FileManager().storeInFile(players, "players.txt");
            pane.getChildren().removeAll(pane.getChildren());
            new Ranking(this).load();
            this.main = new Main(pane, stage, scene);
        }
    }
    private void checkPlayers(){
        Collections.sort(players);
        players.iterator().forEachRemaining(player -> {
            if(player.getTimer().equals(timer)&&player.getTime()==players.get(0).getTime()){
                player.setWins(player.getWins()+1);
            }
            if(player.isAlive()){
                player.kill();
                player.setTime(0);
            }
        });
    }
    public Map getMap() {
        return map;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public void setClosed(boolean closed) {
        this.closed = closed;
        timer.setFinished(closed);
        if(closed) {
            players.iterator().forEachRemaining(Player::kill);
        }
    }
    private void movePlayer1(KeyCode keyCode){
        Player player = players.get(0);
        if(player.isAlive()&&player.getControl().equals(Control.USER)){
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
        if(player.isAlive()&&player.getControl().equals(Control.USER)){
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
        if(player.isAlive()&&player.getControl().equals(Control.USER)){
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
        if(player.isAlive()&&player.getControl().equals(Control.USER)){
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
    public Scene getScene() {
        return scene;
    }
    public boolean isFinished() {
        return finished;
    }
    public Timer getTimer() {
        return timer;
    }
    public String[] getPlayerImages(String color){
        String [] playerImages = new String[8];
        playerImages[0] = "src/main/resources/assets/player"+color+"/player"+color+"_right_standing.png";
        playerImages[1] = "src/main/resources/assets/player"+color+"/player"+color+"_right_moving.png";
        playerImages[2] = "src/main/resources/assets/player"+color+"/player"+color+"_left_standing.png";
        playerImages[3] = "src/main/resources/assets/player"+color+"/player"+color+"_left_moving.png";
        playerImages[4] = "src/main/resources/assets/player"+color+"/player"+color+"_up_standing.png";
        playerImages[5] = "src/main/resources/assets/player"+color+"/player"+color+"_up_moving.png";
        playerImages[6] = "src/main/resources/assets/player"+color+"/player"+color+"_down_standing.png";
        playerImages[7] = "src/main/resources/assets/player"+color+"/player"+color+"_down_moving.png";
        return playerImages;
    }
}
