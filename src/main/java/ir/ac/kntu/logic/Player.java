package ir.ac.kntu.logic;

import ir.ac.kntu.map.OneWay;
import ir.ac.kntu.map.Type;
import ir.ac.kntu.map.Wall;
import ir.ac.kntu.menu.Control;
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

public class Player extends Element implements Movable, Bomber, Serializable, Comparable<Object> {
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
    private Control control;
    public Player(String name, int xCenter, int yCenter, SerializedPane pane, String[]addresses, Timer timer){
        super(xCenter, yCenter);
        this.name = name;
        this.bombRadius = 150;
        this.wins = 0;
        this.addresses = addresses;
        this.pane = pane;
        this.timer = timer;
        this.control = Control.USER;
    }
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

    public boolean checkDestination(Direction direction){
        return pane.getChildren().stream().filter(node -> node instanceof Bomb || node instanceof Wall &&
                (!((Wall) node).getType().equals(Type.ONE_WAY)||
                        (((Wall) node).getType().equals(Type.ONE_WAY)&&
                                !((OneWay) node).getSide().getOpenDirection().equals(direction)))).
                noneMatch(node -> ((Uncrossable) node).getXCenter() == getXCenter() + direction.getXValue() &&
                        ((Uncrossable) node).getYCenter() == getYCenter() + direction.getYValue());
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
        timer.startThread();
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

    public int getBombRadius() {
        return bombRadius;
    }


    public Pane getPane() {
        return pane;
    }

    public void load(){
        this.setImage(loadImage(addresses[getXCenter()>270?2:0]));
        setImageStatus();
        pane.getChildren().add(this);
        this.isAlive = true;
        checkAI();
        removeBrickWall();
    }
    private void checkAI(){
        if(control.equals(Control.AI)){
            new AI(this).load();
        }
    }
    private void removeBrickWall(){
        pane.getChildren().removeAll(pane.getChildren().stream().filter(this::filterWalls).
                collect(Collectors.toList()));
    }
    private boolean filterWalls(Node node){
        return node instanceof Wall&&((Wall) node).getType().equals(Type.BRICK)&&
                ((Wall) node).getXCenter()==getXCenter()&&((Wall) node).getYCenter()==getYCenter();
    }

    public void setPane(SerializedPane pane) {
        this.pane = pane;
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

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }
    public void relocate(int xCenter, int yCenter){
        super.relocate(xCenter, yCenter);
        this.setXCenter(xCenter);
        this.setYCenter(yCenter);
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

    public Timer getTimer() {
        return timer;
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

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    @Override
    public int compareTo(Object o) {
        return this.time-((Player)o).getTime();
    }
}
