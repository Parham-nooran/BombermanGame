package ir.ac.kntu.map;


public enum Type {
    BRICK(true), IRON(false), ONE_WAY(false);
    private boolean breakable;
    Type(boolean breakable){
        this.breakable=breakable;
    }
    public boolean isBreakable() {
        return breakable;
    }
}
