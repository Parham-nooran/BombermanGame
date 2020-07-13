package ir.ac.kntu.logic;

import ir.ac.kntu.map.Type;
import ir.ac.kntu.map.Wall;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Bomb extends ImageView implements Runnable{
    private int xCenter;
    private int yCenter;
    private Pane pane;
    private int radius;
    public Bomb(Pane pane, int xCenter, int yCenter, int radius){
        super();
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.pane = pane;
        this.radius = radius;
        setImage();
        super.relocate(xCenter, yCenter);
    }
    private Image findImage(){
        try {
            return new Image(new FileInputStream("src/main/resources/assets/map/bomb.png"));
        } catch (IOException e){
            System.out.println("Something went wrong while trying to read the bomb image file");
        }
        return null;
    }
    private void setImage(){
        super.setImage(findImage());
        super.setFitHeight(50);
        setFitWidth(50);
    }
    public void run(){
        killPlayers();
        removeBrickWalls();
        remove();
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
    private void removeBrickWalls(){
        pane.getChildren().removeAll(pane.getChildren().stream().
                filter(node -> node instanceof Wall).
                filter(node -> ((Wall) node).getType().isBreakable()).
                filter(node -> (filterXAxis(node)||
                        filterYAxis(node))).collect(Collectors.toList()));
    }
    private <T extends Element>boolean filterXAxis(Node node){
        return ((filterPositiveXAxis(node)||
                filterNegativeXAxis(node))&&((T) node).getYCenter()==yCenter);
    }
    private <T extends Element>boolean filterPositiveXAxis(Node node){
        return ((((T) node).getXCenter()==
                nearestBrickWall(radius, 0,xCenter, true, false)&&
                checkForWall(Type.BRICK, ((T) node).getXCenter(), ((T) node).getYCenter(), true)));
    }
    private <T extends Element>boolean filterNegativeXAxis(Node node){
        return (((T) node).getXCenter()==
                nearestBrickWall(radius, 0, xCenter,false, false)&&
                checkForWall(Type.BRICK, ((T) node).getXCenter(), ((T) node).getYCenter(), false));
    }
    private boolean filterYAxis(Node node){
        return ((filterPositiveYAxis(node)||filterNegativeYAxis(node))&&
                ((Wall) node).getXCenter()==xCenter);
    }
    private <T extends Element>boolean filterPositiveYAxis(Node node){
        return (((T) node).getYCenter()==
                nearestBrickWall(0,radius,yCenter,true, true)&&
                checkForWall(Type.BRICK, ((T) node).getXCenter(), ((T) node).getYCenter(), true));
    }
    private <T extends Element>boolean filterNegativeYAxis(Node node){
        return (((T) node).getYCenter()==nearestBrickWall(0, radius, yCenter, false, true)&&
                checkForWall(Type.BRICK, ((T) node).getXCenter(), ((T) node).getYCenter(), false));
    }


    private boolean filterXAxis2(Node node){
        return ((filterPositiveXAxis(node, nearestBrickWall(radius, 0, xCenter, true, false))||
                filterNegativeXAxis(node, nearestBrickWall(radius, 0, xCenter, false, false)))&&
                ((Player) node).getYCenter()==yCenter);
    }

    private <T extends Element>boolean filterPositiveXAxis(Node node, int radius){
        return ((((T) node).getXCenter()<=radius&&((T) node).getXCenter()>=xCenter&&
                checkForWall(((T) node).getXCenter(), ((T) node).getYCenter(), true)));
    }
    private <T extends Element>boolean filterNegativeXAxis(Node node, int radius){
        return (((T) node).getXCenter()>=radius&&((T) node).getXCenter()<=xCenter&&
                checkForWall(((T) node).getXCenter(), ((T) node).getYCenter(), false));
    }
    private boolean filterYAxis2(Node node){
        return ((filterPositiveYAxis(node, nearestWall(0, radius, yCenter, true, true))||
                filterNegativeYAxis(node, nearestWall(0, radius, yCenter, false, true)))&&
                ((Player) node).getXCenter()==xCenter);
    }
    private boolean filterPositiveYAxis(Node node, int radius){
        return (((Player) node).getYCenter()<=radius&&((Player) node).getYCenter()>=yCenter&&
                checkForWall(((Player) node).getXCenter(), ((Player) node).getYCenter(), true));
    }


    private boolean filterNegativeYAxis(Node node, int radius){
        return (((Player) node).getYCenter()>=radius&&((Player) node).getYCenter()<=yCenter&&
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
                ((Wall) node).getXCenter() >= this.xCenter &&
                ((Wall) node).getYCenter() <= yCenter && ((Wall) node).getYCenter() >= this.yCenter
                && positive) || (((Wall) node).getXCenter() >= xCenter &&
                ((Wall) node).getXCenter() <= this.xCenter &&
                ((Wall) node).getYCenter() >= yCenter &&
                ((Wall) node).getYCenter() <= this.yCenter && !positive);
    }
    private <T extends Element>boolean filterNearestElement(Node node, int xRadius, int yRadius, boolean positive){
        return (((((T) node).getXCenter()<=xCenter+xRadius&&
                ((T)node).getYCenter()<=yCenter+yRadius&&
                ((T) node).getXCenter()>=xCenter&&((T) node).getYCenter()>=yCenter)&&positive)||
                ((((T) node).getXCenter()>=xCenter-xRadius&&
                        ((T)node).getYCenter()>=yCenter-yRadius&&
                        ((T) node).getXCenter()<=xCenter&&((T) node).getYCenter()<=yCenter)&&
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
