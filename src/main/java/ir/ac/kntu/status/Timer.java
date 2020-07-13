package ir.ac.kntu.status;

import ir.ac.kntu.logic.SerializedPane;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Timer implements Runnable, Serializable {
    private int second;
    private int minute;
    private int hour;
    private boolean notClosed=true;
    private SerializedLabel label;
    private SerializedPane pane;
    private boolean countDown;
    private boolean finished;
    private Timer(SerializedPane pane){
        this.label = new SerializedLabel();
        label.setLayoutX(200);
        label.setLayoutY(5);
        this.pane = pane;
        label.setTextFill(Color.WHITE);
        label.setScaleX(3);
        label.setScaleY(2);
    }
    public Timer(SerializedPane pane, int second, int minute, int hour, boolean countDown){
        this(pane);
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.countDown = countDown;
        if(second+minute+hour>0){
            finished = false;
        }
    }
    @Override
    public void run(){
        if(countDown){
            countDown();
        } else{
            countUp();
            System.out.println(second);
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void countDown(){
        if(60*minute+second+3600*hour<=59){
            label.setTextFill(Color.RED);
        }
        if(minute+hour+second<0){
            finished=true;
        } else {
            if(minute ==0 && hour>0){
                hour--;
                minute=59;
            }
            if(second == 0 && minute>0){
                minute--;
                second = 59;
            }
            this.upDataLabel();
            second--;
        }
    }
    public void countUp(){
        if(second>=60){
            minute++;
            second=0;
        }
        if(minute>=60){
            hour++;
            minute=0;
        }
        this.upDataLabel();
        second++;
    }
    public void load(){
        pane.getChildren().add(label);
        setThread();
    }
    private void upDataLabel(){
        String current = String.format("%02d : %02d : %02d",hour,minute,second);
        this.label.setText(current);
    }
    public void setNotClosed(boolean notClosed) {
        this.notClosed = notClosed;
    }
    public void setThread(){
        Thread thread = new Thread(() ->{
            while(notClosed){
                Platform.runLater(this);
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
