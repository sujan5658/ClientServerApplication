
package com.serverclients.services;

import com.serverclients.frames.ServerGUI;
import java.io.IOException;
import java.net.InetAddress;

public class ConnectionChecker extends Thread {
    public static boolean threadStatus;
    private ServerGUI serverGUI;
    private String clientIp;
    
    public ConnectionChecker(ServerGUI serverGUI,String clientIP){
        threadStatus = true;
        this.clientIp = clientIP;
        this.serverGUI = serverGUI;
    }
    public void run(){
        while(threadStatus){
            try {
                boolean result = InetAddress.getByName(this.clientIp).isReachable(5000);
                if(!result){
                    break;
                }
            } catch (IOException ex) {
                //Logger.getLogger(ConnectionChecker.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Data out error");
                break;
            }       
        }
        this.serverGUI.disconnectClient(this.clientIp);
    }
}
