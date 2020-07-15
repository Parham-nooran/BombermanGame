package ir.ac.kntu.menu;

import javafx.scene.layout.Pane;

public abstract class Menu {
    private Pane pane;
    public Menu(Pane pane){
        this.pane = pane;
    }

    public Pane getPane() {
        return pane;
    }
}
