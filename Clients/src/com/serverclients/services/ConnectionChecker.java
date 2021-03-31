
package com.serverclients.services;


import com.serverclients.frames.ClientGUI;
import java.io.IOException;
import java.net.InetAddress;

public class ConnectionChecker extends Thread {
    public static boolean threadStatus;
    private ClientGUI clientGUI;
    private String serverIp;
    
    public ConnectionChecker(ClientGUI clientGUI,String serverIP){
        threadStatus = true;
        this.serverIp = serverIP;
        this.clientGUI = clientGUI;
    }
    public void run(){
        while(threadStatus){
            try {
                boolean result = InetAddress.getByName(this.serverIp).isReachable(5000);
                if(!result){
                    break;
                }
            } catch (IOException ex) {
                //Logger.getLogger(ConnectionChecker.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Data out error");
                break;
            }       
        }
       this.clientGUI.setDisconnectedMessage();
    }
}
