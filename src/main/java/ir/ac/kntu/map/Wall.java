package ir.ac.kntu.map;

import ir.ac.kntu.logic.Uncrossable;

public class Wall extends Uncrossable {
    private Type type;
    public Wall(int xCenter, int yCenter, Type type){
        super(type.getImage(), xCenter, yCenter);
        this.type = type;
        this.relocate(xCenter, yCenter);
        this.setFitHeight(50);
        this.setFitWidth(50);
    }


    public Type getType() {
        return type;
    }

}
