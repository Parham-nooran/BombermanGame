package ir.ac.kntu.logic;

import javafx.scene.layout.Pane;

public class PowerUp extends Element {
    private Pane pane;
    public PowerUp(Pane pane, int xCenter, int yCenter){
        super(xCenter, yCenter);
        this.pane = pane;
    }
    public void remove(){
        pane.getChildren().removeAll(this);
    }
}
