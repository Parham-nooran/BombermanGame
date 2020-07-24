package ir.ac.kntu.status;

import ir.ac.kntu.logic.SerializedPane;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Timer implements Runnable, Serializable {
    private int second;
    private int minute;
    private int hour;
    private SerializedLabel label;
    private SerializedPane pane;
    private boolean countDown;
    private boolean finished;
    private Integer object;
    private Integer end;
    private boolean onlySecond;
    public Timer(SerializedPane pane, int hour, int minute, int second, boolean countDown, boolean onlySecond){
        this(hour, minute, second, countDown);
        this.onlySecond = onlySecond;
        this.label = new SerializedLabel();
        this.pane = pane;
        setLabelStatus();
        checkTime();
    }
    private void setLabelStatus(){
        label.setLayoutX(200);
        label.setLayoutY(5);
        label.setTextFill(Color.WHITE);
        label.setScaleX(3);
        label.setScaleY(2);
    }
    private void checkTime(){
        if(countDown&&second+minute+hour<=0){
            finished = true;
        }
    }
    public Timer(int hour, int minute, int second, boolean countDown){
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.countDown = countDown;
        this.finished = false;
    }
    @Override
    public void run(){
        if(countDown){
            countDown(onlySecond);
        } else{
            countUp();
            System.out.println(second);
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void countDown(boolean onlySecond){
        if(label!=null&&60*minute+second+3600*hour<=59&&!onlySecond){
            label.setTextFill(Color.RED);
        }
        if(minute+hour+second<0){
            finished=true;
            if(label!=null) {
                pane.getChildren().remove(this.label);
            }
            this.object = end;
        } else {
            if(minute ==0 && hour>0){
                hour--;
                minute=59;
            }
            if(second == 0 && minute>0){
                minute--;
                second = 59;
            }
            if(label!=null) {
                this.upDataLabel(onlySecond);
            }
            second--;
        }
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setBeginAndEnd(Integer target, Integer end) {
        this.object = target;
        this.end = end;
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
        this.upDataLabel(onlySecond);
        second++;
    }
    public void load(){
        pane.getChildren().add(label);
        startThread();
    }
    public void load(int xCenter, int yCenter, int size){
        this.label.setLayoutX(xCenter);
        this.label.setLayoutY(yCenter);
        label.setScaleX(size);
        label.setScaleY(size);
        label.setOpacity(label.getOpacity()/1.2);
        pane.getChildren().add(label);
        startThread();
    }
    private void upDataLabel(boolean second){
        this.label.setText(second?String.format("%2d",this.second):String.format("%02d : %02d",minute,this.second));
    }
    public void startThread(){
        new Thread(() ->{
            while(!finished){
                Platform.runLater(this);
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public int getSecond() {
        return second;
    }

    public int getMinute() {
        return minute;
    }

}
