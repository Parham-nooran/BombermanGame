package ir.ac.kntu.logic;

import ir.ac.kntu.map.Map;
import ir.ac.kntu.menu.Main;
import ir.ac.kntu.status.Timer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Director implements Runnable {
    private ArrayList<Player> players;
    private Pane pane;
    private Timer<Integer> timer;
    private Map map;
    private boolean finished;
    private boolean closed;
    private Integer numberOfPlayers;
    private String mapFile;
    private Stage stage;
    private Scene scene;
    public  Director(Pane pane, Stage stage, Scene scene, Integer numberOfPlayers, String mapFile) {
        this.mapFile = mapFile;
        this.players = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        this.scene = scene;
        makePlayers(new String[]{"Normal","Red", "Yellow", "Black"}, new Integer[]{50, 100, 200, 400},
                new Integer[]{250, 350, 350,  250},
                new String[]{"", "_red", "_yellow", "_black"});
        this.stage = stage;
        this.pane = pane;
        this.closed = false;
        this.timer = new Timer<>((SerializedPane) pane, 0, 3,0,true);;
        this.map = new Map(this);
        this.finished = false;
        setPlayersDirector();
        stage.setOnCloseRequest(windowEvent -> {
            finished = closed =true;
            stage.close();
        });
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
            while(!finished){
                try{
                    Thread.sleep(17);
                }catch (InterruptedException e){
                    System.out.println("Director has been interrupted");
                    e.printStackTrace();
                }
                Platform.runLater(this);
            }
        });
        thread.start();
    }


    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    private void load(){
        map.load();
        players.iterator().forEachRemaining(Player::load);
        new Random(this).start();
        timer.load();
    }
    @Override
    public void run(){
        finished = timer.isFinished()|| pane.getChildren().stream().filter(node -> node instanceof Player).count()<2;
        timer.setFinished(finished);
        if(finished&&!closed){
            pane.getChildren().removeAll(pane.getChildren());
            new Main(pane, stage, scene).load();
        }
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
        players.iterator().forEachRemaining(Player::kill);
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

    public void makePlayers(String[]names, Integer[]xCenters, Integer[]yCenters, String[]colors) {
        for(int i=0;i<numberOfPlayers;i++){
            players.add(new Player(names[i], xCenters[i], yCenters[i], initializePlayer(colors[i])));
        }
    }
    private String[] initializePlayer(String color){
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
