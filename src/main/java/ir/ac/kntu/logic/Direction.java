package ir.ac.kntu.logic;

public enum Direction {
    RIGHT(50,0, 0), LEFT(-50,0, 2),
    UP(0,-50, 4),DOWN(0,+50, 6);
    private int imageCode;
    private int xValue, yValue;
    Direction(int xValue, int yValue, int imageCode){
        this.imageCode = imageCode;
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public int getImageCode() {
        return imageCode;
    }

    public int getXValue() {
        return xValue;
    }

    public int getYValue() {
        return yValue;
    }

}
