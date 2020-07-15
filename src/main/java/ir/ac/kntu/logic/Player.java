package ir.ac.kntu.logic;

import ir.ac.kntu.file.FileManager;
import ir.ac.kntu.map.OneWay;
import ir.ac.kntu.map.Type;
import ir.ac.kntu.map.Wall;
import ir.ac.kntu.status.Timer;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class Player extends Element implements Movable,Bomber{
    private String name;
    private SerializedImage [] images;
    private boolean isAlive;
    private Pane pane;
    private int bombRadius;
    private boolean activeBomb;
    private FileManager fileManager;
    public Player(String name, int xCenter, int yCenter, String[]addresses){
        super(xCenter, yCenter);
        this.name = name;
        this.bombRadius = 150;
        this.fileManager = new FileManager();
        this.images = new SerializedImage[8];
        loadImages(addresses);
        setImageStatus(xCenter, yCenter, images);
    }
    private void loadImages(String[]addresses){
        try {
            for (int i = 0; i < 8; i++) {
                this.images[i] = new SerializedImage(new FileInputStream(addresses[i]));
            }
        } catch(FileNotFoundException e){
            System.out.println();;
        }
    }
    private void setImageStatus(int xCenter, int yCenter, Image[]images){
        this.setImage(images[0]);
        this.relocate(xCenter,yCenter);
        this.setFitHeight(50);
        this.setFitWidth(50);
    }
    @Override
    public void move(Direction direction){
        checkForPowerUp(direction);
        if(checkDestination(direction)) {
            //if (checkHorizontalMovement(direction)) {
            setXCenter(getXCenter()+ direction.getXValue());
            //}
            //if (checkVerticalMovement(direction)) {
            setYCenter(getYCenter()+direction.getYValue());
            //}
        }
        /*try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
        }*/
        //this.setImage(images[direction.getImageCode()+1]);
        this.setImage(images[direction.getImageCode()]);
        this.relocate(getXCenter(), getYCenter());
    }
    public void stop(Direction direction){
        this.setImage(images[direction.getImageCode()]);
        this.relocate(getXCenter(), getYCenter());
    }
    /*private boolean checkHorizontalMovement(Direction direction){
        return getXCenter() + 50 + direction.getXValue() > 0 &&
                getXCenter() + 50 + direction.getXValue() < pane.getWidth();
    }
    private boolean checkVerticalMovement(Direction direction){
        return getYCenter() + 50 + direction.getYValue() > 0 &&
                getYCenter() + 50 + direction.getYValue() < pane.getHeight();
    }*/
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
        Timer<Integer> timer = new Timer<>(0, 0, 15, true);
        timer.setThread();
        bombRadius = 250;
        timer.setBeginAndEnd(this.bombRadius, 150);
    }
    public void kill(){
        this.isAlive = false;
        fileManager.storeInFile(this, "players.txt");
        pane.getChildren().remove(this);
    }
    public void setBomb(){
        if(!activeBomb) {
            activeBomb = true;
            new Bomb(this).start();
        }
    }

    public int getBombRadius() {
        return bombRadius;
    }


    public Pane getPane() {
        return pane;
    }



    public void setActiveBomb(boolean activeBomb) {
        this.activeBomb = activeBomb;
    }

    public void setDirector(Director director) {
        this.pane = director.getPane();
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

    /*@Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return isAlive == player.isAlive &&
                bombRadius == player.bombRadius &&
                Objects.equals(name, player.name) &&
                director.equals(player.director) &&
                Arrays.equals(images, player.images) &&
                pane.equals(player.pane);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, director, isAlive, pane, bombRadius);
        result = 31 * result + Arrays.hashCode(images);
        return result;
    }*/
}
