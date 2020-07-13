package ir.ac.kntu.logic;

public enum Direction {
    UP(0,-50),DOWN(0,+50),LEFT(-50,0),RIGHT(50,0);
    private int xValue, yValue;
    Direction(int xValue, int yValue){
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public int getXValue() {
        return xValue;
    }

    public int getYValue() {
        return yValue;
    }

}
