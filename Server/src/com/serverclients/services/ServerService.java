
package com.serverclients.services;

import com.serverclients.frames.ServerGUI;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerService extends Thread {
    public static ServerSocket serverSocket;
    private Socket clientSocket;
    private ServerGUI serverGui;
    public static boolean threadStatus;
    
    public ServerService(ServerGUI serverGui,int port) throws IOException{
        this.serverGui = serverGui;
        serverSocket = new ServerSocket(port);
        threadStatus = true;
    }
    public void run(){
        try{
            this.serverGui.setServerStartMessage("Connection open for clients.");
            while(threadStatus){
                this.clientSocket = serverSocket.accept();
                Thread serverWorker = new ServerWorker(this.serverGui,this.clientSocket);
                serverWorker.start();
            }
        }catch(IOException er){
            System.out.println("error");
        }finally{
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
