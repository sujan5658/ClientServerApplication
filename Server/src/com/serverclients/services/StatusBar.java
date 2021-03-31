/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serverclients.services;

import com.serverclients.frames.ServerGUI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sujan Koju
 */
public class StatusBar extends Thread {
    public static boolean threadStatus;
    private ServerGUI serverGUI;
    public StatusBar(ServerGUI serverGUI){
        this.serverGUI = serverGUI;
        threadStatus = true;
    }
    public void run(){
        int i=1;
        while(threadStatus){
            if(i>100){
                i=1;
            }
            this.serverGUI.fillStatusBar(i);
            i++;
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(StatusBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
