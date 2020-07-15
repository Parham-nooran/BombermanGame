package ir.ac.kntu.map;

public class OneWay extends Wall {
    private Side side;
    public OneWay(int xCenter, int yCenter, Side side){
        super(xCenter, yCenter, Type.ONE_WAY);
        super.setImage(side.getImage());
        super.setFitHeight(50);
        super.setFitWidth(50);
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
