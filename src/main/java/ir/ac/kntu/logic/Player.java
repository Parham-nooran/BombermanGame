package ir.ac.kntu.logic;

import ir.ac.kntu.map.OneWay;
import ir.ac.kntu.map.Type;
import ir.ac.kntu.map.Wall;
import ir.ac.kntu.status.Timer;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Player extends Element implements Movable, Bomber, Serializable, Comparable {
    private String name;
    private int time;
    private int numberOfGames;
    private int wins;
    private String[] addresses;
    private boolean isAlive;
    private SerializedPane pane;
    private int bombRadius;
    private int activeBombs;
    private Timer timer;
    public Player(String name, int xCenter, int yCenter, String[]addresses, Timer timer){
        super(xCenter, yCenter);
        this.name = name;
        this.bombRadius = 150;
        this.addresses = addresses;
        this.setImage(loadImage(addresses[0]));
        setImageStatus();
        this.timer = timer;
        //this.images = new SerializedImage[8];
        //loadImages(addresses);
        //setImageStatus(xCenter, yCenter, ));
    }
    /*private void loadImages(String[]addresses){
        try {
            for (int i = 0; i < 8; i++) {
                this.images[i] = new SerializedImage(new FileInputStream(addresses[i]));
            }
        } catch(FileNotFoundException e){
            System.out.println();
        }
    }*/
    public Image loadImage(String address){
        try{

            return new Image(new FileInputStream(address));
        } catch (FileNotFoundException e){
            System.out.println();
        }
        return null;
    }
    private void setImageStatus(){
        this.relocate(getXCenter(),getYCenter());
        this.setFitHeight(50);
        this.setFitWidth(50);
    }
    /*private void setImagesStatus(int xCenter, int yCenter, Image[]images){
        this.setImage(images[0]);
        this.relocate(xCenter,yCenter);
        this.setFitHeight(50);
        this.setFitWidth(50);
    }*/
    @Override
    public void move(Direction direction){
        checkForPowerUp(direction);
        changeImage(direction);
        if(checkDestination(direction)) {
            setXCenter(getXCenter()+ direction.getXValue());
            setYCenter(getYCenter()+direction.getYValue());
        }
        this.setImage(loadImage(addresses[direction.getImageCode()+1]));
        this.relocate(getXCenter(), getYCenter());
    }
    private void changeImage(Direction direction){
        new Thread(() -> {
            try{
                Thread.sleep(100);

            } catch (InterruptedException e){
                System.out.println("Image changing was interrupted");
            }
            Platform.runLater(() -> this.setImage(loadImage(addresses[direction.getImageCode()])));
        }).start();
    }

    public int getActiveBombs() {
        return activeBombs;
    }

    public void setActiveBombs(int activeBombs) {
        this.activeBombs = activeBombs;
    }

    private boolean checkDestination(Direction direction){
        return pane.getChildren().stream().filter(node -> node instanceof Wall &&
                (!((Wall) node).getType().equals(Type.ONE_WAY)||
                        (((Wall) node).getType().equals(Type.ONE_WAY)&&
                                !((OneWay) node).getSide().getOpenDirection().equals(direction)))).
                noneMatch(node -> ((Wall) node).getXCenter() == getXCenter() + direction.getXValue() &&
                        ((Wall) node).getYCenter() == getYCenter() + direction.getYValue());
    }
    private void checkForPowerUp(Direction direction){
        if(!getPowerUps(direction).isEmpty()){
            pane.getChildren().removeAll(getPowerUps(direction).get(0));
            addBombRadius();
        }
    }
    private List<Node> getPowerUps(Direction direction){
        return pane.getChildren().stream().filter(node -> node instanceof PowerUp&&((PowerUp) node).getXCenter()==
                getXCenter()+direction.getXValue()&&((PowerUp) node).
                getYCenter()==getYCenter()+direction.getYValue()).collect(Collectors.toList());
    }
    private void addBombRadius(){
        Timer timer = new Timer(0, 0, 15, true);
        timer.setThread();
        bombRadius = 250;
        timer.setBeginAndEnd(this.bombRadius, 150);
    }
    public void kill(){
        this.isAlive = false;
        if(!timer.isFinished()) {
            this.time = timer.getMinute() * 100 + timer.getSecond();
        }
        this.numberOfGames++;
        pane.getChildren().remove(this);
    }

    public void setBomb(){
        if(activeBombs<1) {
            activeBombs ++;
            new Bomb(this).start();
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public int getBombRadius() {
        return bombRadius;
    }


    public Pane getPane() {
        return pane;
    }


    public void setPane(SerializedPane pane) {
        this.pane = pane;
    }

    public void load(){
        pane.getChildren().add(this);
        this.isAlive = true;
        removeBrickWalls();
    }
    private void removeBrickWalls(){
        pane.getChildren().removeAll(pane.getChildren().stream().filter(this::filterWalls).
                collect(Collectors.toList()));
    }
    private boolean filterWalls(Node node){
        return node instanceof Wall&&((Wall) node).getType().equals(Type.BRICK)&&
                ((Wall) node).getXCenter()==getXCenter()&&((Wall) node).getYCenter()==getYCenter();
    }
    @Override
    public void bomb() {
        setBomb();
    }

    public boolean isAlive() {
        return isAlive;
    }



    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return name.equals(player.name);
    }

    public Image[] getRankingImages(){
        return new Image[]{loadImage(addresses[6]),loadImage(addresses[7])};
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", numberOfGames=" + numberOfGames +
                ", wins=" + wins +
                ", addresses=" + Arrays.toString(addresses) +
                ", isAlive=" + isAlive +
                ", pane=" + pane +
                ", bombRadius=" + bombRadius +
                ", activeBombs=" + activeBombs +
                ", timer=" + timer +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return this.time-((Player)o).getTime();
    }
}
