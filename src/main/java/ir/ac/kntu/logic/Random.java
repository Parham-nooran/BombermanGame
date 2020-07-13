package ir.ac.kntu.logic;

import javafx.application.Platform;

public class Random implements Runnable{
    private Director director;
    public Random(Director director){
        this.director = director;
    }
    @Override
    public void run(){
        int random = (int)(Math.random()*6);
    }
    public void start(){
        new Thread(() ->{
            while(!director.isFinished()){
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    System.out.println("Interrupted");
                }
                Platform.runLater(this);
            }
        }).start();
    }
    public void setBomb(){

    }
    public void setOneWayBlock(){

    }
    public void setEmpower(){

    }
}
