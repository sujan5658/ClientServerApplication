package com.serverclients.services;

import com.serverclients.frames.ClientGUI;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnOffTimer extends Thread {
    public static boolean threadStatus;
    private ClientGUI clientGUI;
    private int minute;
    private int second;
    
    public TurnOffTimer(ClientGUI clientGUI){
        this.clientGUI = clientGUI;
        threadStatus = true;
        this.minute = 4;
        this.second = 0;
    }
    public void run(){
        while(threadStatus){
            try {
                String secondsValue = "00";
                if(this.second > 9){
                    secondsValue = Integer.toString(this.second);
                }else{
                    secondsValue = "0"+Integer.toString(this.second);
                }
                this.clientGUI.setTimerValue(this.minute+" : "+secondsValue);
                
                if(this.minute == 0 && this.second == 0){
                    Runtime runTime = Runtime.getRuntime();
                    try {
                        TurnOffTimer.threadStatus = false;
                        ConnectionChecker.threadStatus = false;
                        IdleTimeService.threadStatus = false;
                        runTime.exec("shutdown -s -t 0");
                    } catch (IOException ex) {
                        Logger.getLogger(TurnOffTimer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                
                if(this.second == 0){
                    this.minute -= 1;
                    this.second = 60;
                }
                Thread.sleep(1000);
                this.second--;
            } catch (InterruptedException ex) {
                Logger.getLogger(TurnOffTimer.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }
}
