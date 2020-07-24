package ir.ac.kntu.logic;

import ir.ac.kntu.map.Type;
import ir.ac.kntu.map.Wall;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Bomb extends Uncrossable implements Runnable{
    private Pane pane;
    private int radius;
    private Player player;
    public Bomb(Player player){
        this(player.getPane(), player.getXCenter(), player.getYCenter(), player.getBombRadius());
        this.player = player;
    }
    public Bomb(Pane pane, int xCenter, int yCenter, int radius){
        super(xCenter, yCenter);
        this.pane = pane;
        this.radius = radius;
        setImage();
        super.relocate(xCenter, yCenter);
    }
    public void start(){
        pane.getChildren().add(this);
        Thread thread = new Thread(() ->{
            try {
                Thread.sleep(3000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            Platform.runLater(this);
        });
        thread.start();
    }
    private Image findImage(String address){
        try {
            return new Image(new FileInputStream(address));
        } catch (IOException e){
            System.out.println("Something went wrong while trying to read the image file");
        }
        return null;
    }
    private void setImage(){
        super.setImage(findImage("src/main/resources/assets/map/bomb.png"));
        super.setFitHeight(50);
        setFitWidth(50);
    }
    public void run(){
        killPlayers();
        blast();
        remove();
        if(player!=null) {
            player.setActiveBombs(player.getActiveBombs()-1);
        }
    }
    private void killPlayers(){
        deathPlayers().iterator().forEachRemaining(node -> ((Player)node).kill());
    }
    private List<? extends Node> deathPlayers(){
        return pane.getChildren().stream().
                filter(node -> node instanceof Player).
                filter(node -> (filterXAxis2(node)||
                        filterYAxis2(node))).collect(Collectors.toList());
    }
    private void blast(){
        explode();
    }
    private void explode(){
        burnWalls();
        makeFire();
        removeBrickWalls();
    }
    private void burnWalls(){
        for(Node node:getBrickWalls()){
            ((Wall)node).setImage(findImage("src/main/resources/assets/map/block_breaking.png"));
        }
    }
    private void makeFire(){

    }
    private void removeBrickWalls(){
        new Thread(() -> {
            try{
                Thread.sleep(100);

            } catch (InterruptedException e){
                System.out.println("Bomb explosion interrupted");
            }
            Platform.runLater(() -> {
                pane.getChildren().removeAll(getBrickWalls());
            });
        }).start();
    }
    private List<Node> getBrickWalls(){
        return pane.getChildren().stream().
                filter(node -> node instanceof Wall).
                filter(node -> ((Wall) node).getType().isBreakable()).
                filter(node -> (filterXAxis(node)||
                        filterYAxis(node))).collect(Collectors.toList());
    }

    private <T extends Element>boolean filterXAxis(Node node){
        return ((filterPositiveXAxis(node)||
                filterNegativeXAxis(node))&&((T) node).getYCenter()==getYCenter());
    }
    private <T extends Element>boolean filterPositiveXAxis(Node node){
        return ((((T) node).getXCenter()==
                nearestBrickWall(radius, 0,getXCenter(), true, false)&&
                checkForWall(Type.BRICK, ((T) node).getXCenter(), ((T) node).getYCenter(), true)));
    }
    private <T extends Element>boolean filterNegativeXAxis(Node node){
        return (((T) node).getXCenter()==
                nearestBrickWall(radius, 0, getXCenter(),false, false)&&
                checkForWall(Type.BRICK, ((T) node).getXCenter(), ((T) node).getYCenter(), false));
    }
    private boolean filterYAxis(Node node){
        return ((filterPositiveYAxis(node)||filterNegativeYAxis(node))&&
                ((Wall) node).getXCenter()==getXCenter());
    }
    private <T extends Element>boolean filterPositiveYAxis(Node node){
        return (((T) node).getYCenter()==
                nearestBrickWall(0,radius,getYCenter(),true, true)&&
                checkForWall(Type.BRICK, ((T) node).getXCenter(), ((T) node).getYCenter(), true));
    }
    private <T extends Element>boolean filterNegativeYAxis(Node node){
        return (((T) node).getYCenter()==nearestBrickWall(0, radius, getYCenter(), false, true)&&
                checkForWall(Type.BRICK, ((T) node).getXCenter(), ((T) node).getYCenter(), false));
    }


    private boolean filterXAxis2(Node node){
        return ((filterPositiveXAxis(node, nearestBrickWall(radius, 0, getXCenter(), true, false))||
                filterNegativeXAxis(node, nearestBrickWall(radius, 0, getXCenter(), false, false)))&&
                ((Player) node).getYCenter()==getYCenter());
    }

    private <T extends Element>boolean filterPositiveXAxis(Node node, int radius){
        return ((((T) node).getXCenter()<=radius&&((T) node).getXCenter()>=getXCenter()&&
                checkForWall(((T) node).getXCenter(), ((T) node).getYCenter(), true)));
    }
    private <T extends Element>boolean filterNegativeXAxis(Node node, int radius){
        return (((T) node).getXCenter()>=radius&&((T) node).getXCenter()<=getXCenter()&&
                checkForWall(((T) node).getXCenter(), ((T) node).getYCenter(), false));
    }
    private boolean filterYAxis2(Node node){
        return ((filterPositiveYAxis(node, nearestWall(0, radius, getYCenter(), true, true))||
                filterNegativeYAxis(node, nearestWall(0, radius, getYCenter(), false, true)))&&
                ((Player) node).getXCenter()==getXCenter());
    }
    private boolean filterPositiveYAxis(Node node, int radius){
        return (((Player) node).getYCenter()<=radius&&((Player) node).getYCenter()>=getYCenter()&&
                checkForWall(((Player) node).getXCenter(), ((Player) node).getYCenter(), true));
    }


    private boolean filterNegativeYAxis(Node node, int radius){
        return (((Player) node).getYCenter()>=radius&&((Player) node).getYCenter()<=getYCenter()&&
                checkForWall(((Player) node).getXCenter(), ((Player) node).getYCenter(), false));
    }


    private boolean checkForWall(Type type, int xCenter, int yCenter, boolean positive){
        return pane.getChildren().stream().filter(node -> node instanceof Wall).
                filter(node -> !((Wall) node).getType().equals(type)).
                noneMatch(node -> filterWall(node, xCenter, yCenter, positive));
    }
    private boolean checkForWall(int xCenter, int yCenter, boolean positive){
        return pane.getChildren().stream().filter(node -> node instanceof Wall).
                noneMatch(node -> filterWall(node, xCenter, yCenter, positive));
    }
    private boolean filterWall(Node node, int xCenter, int yCenter, boolean positive){
        return (((Wall) node).getXCenter() <= xCenter &&
                ((Wall) node).getXCenter() >= super.getXCenter() &&
                ((Wall) node).getYCenter() <= yCenter && ((Wall) node).getYCenter() >= getYCenter()
                && positive) || (((Wall) node).getXCenter() >= xCenter &&
                ((Wall) node).getXCenter() <= getXCenter() &&
                ((Wall) node).getYCenter() >= yCenter &&
                ((Wall) node).getYCenter() <= getYCenter() && !positive);
    }
    private <T extends Element>boolean filterNearestElement(Node node, int xRadius, int yRadius, boolean positive){
        return (((((T) node).getXCenter()<=getXCenter()+xRadius&&
                ((T)node).getYCenter()<=getYCenter()+yRadius&&
                ((T) node).getXCenter()>=getXCenter()&&((T) node).getYCenter()>=getYCenter())&&positive)||
                ((((T) node).getXCenter()>=getXCenter()-xRadius&&
                        ((T)node).getYCenter()>=getYCenter()-yRadius&&
                        ((T) node).getXCenter()<=getXCenter()&&((T) node).getYCenter()<=getYCenter())&&
                        !positive));
    }
    private List<Node> nearestWalls(int xRadius, int yRadius, boolean positive){
        return pane.getChildren().stream().filter(node -> node instanceof Wall).
                filter(node -> filterNearestElement(node, xRadius, yRadius, positive)).
                collect(Collectors.toList());
    }
    private int nearestWall(int xRadius, int yRadius, int notCommonPoint, boolean positive, boolean yAxis){
        List<Node> nodes = nearestWalls(xRadius, yRadius, positive);
        return nearestElement(nodes, xRadius, yRadius, notCommonPoint, positive, yAxis);
    }
    private List<Node> nearestBrickWalls(int xRadius, int yRadius, boolean positive){
        return pane.getChildren().stream().filter(node -> node instanceof Wall).
                filter(node -> ((Wall)node).getType().equals(Type.BRICK)).
                filter(node -> filterNearestElement(node, xRadius, yRadius, positive)).
                collect(Collectors.toList());
    }
    private int nearestBrickWall(int xRadius, int yRadius, int notCommonPoint, boolean positive, boolean yAxis){
        List<Node> nodes = nearestBrickWalls(xRadius, yRadius, positive);
        return nearestElement(nodes, xRadius, yRadius, notCommonPoint, positive, yAxis);
    }

    private <T extends Element>int nearestElement(
            List<Node> nodes, int xRadius, int yRadius,int notCommonPoint, boolean positive, boolean yAxis){
        if(nodes.isEmpty()){
            return positive?notCommonPoint + Math.max(xRadius, yRadius):notCommonPoint-Math.max(xRadius, yRadius);
        }
        int min = Math.abs(notCommonPoint -
                (yAxis?(((T) nodes.get(0)).getYCenter()):(((T) nodes.get(0)).getXCenter())));
        for (int i = 1; i < nodes.size(); i++) {
            min = yAxis?Math.min(min, Math.abs(notCommonPoint - ((T) nodes.get(i)).getYCenter())):
                    Math.min(min, Math.abs(notCommonPoint - ((T) nodes.get(i)).getXCenter()));
        }
        return positive?min + notCommonPoint:notCommonPoint-min;
    }

    private void remove(){
        pane.getChildren().remove(this);
    }

}
