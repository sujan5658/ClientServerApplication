
package com.serverclients.services;

import com.serverclients.frames.ClientDetails;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimerThread extends Thread {
    public static boolean threadStatus;
    private ClientDetails clientDetail;
    
    public TimerThread(ClientDetails clientDetail){
        this.clientDetail = clientDetail;
        threadStatus = true;
    }
    public void run(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss");
        while(threadStatus){
            LocalDateTime now = LocalDateTime.now();
            this.clientDetail.setCurrentTime(dtf.format(now));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
