
package com.serverclients.services;

import com.serverclients.frames.ServerGUI;
import com.serverclients.pojos.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ServerWorker extends Thread {
    private Socket mySocket;
    private ServerGUI serverGui;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private ObjectInputStream objectIn;
    private Client client;
    
    public ServerWorker(ServerGUI serverGui,Socket mySocket){
        this.serverGui = serverGui;
        this.mySocket = mySocket;
        this.client = new Client();
    }
    public void run(){
        try {
            this.dataIn = new DataInputStream(this.mySocket.getInputStream());
            this.dataOut = new DataOutputStream(this.mySocket.getOutputStream());
            this.objectIn = new ObjectInputStream(this.mySocket.getInputStream());
            this.client = (Client)this.objectIn.readObject();
            client.setSocket(this.mySocket);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            this.client.setConnectedTime(dtf.format(now));
            this.serverGui.addDataInTable(this.client);
            this.dataOut.writeUTF("Welcome "+this.client.getUserName());
            this.client.setMsgFromServer("From Server : Welcome "+this.client.getUserName());
            Thread connection = new ConnectionChecker(this.serverGui,this.client.getIpAddress());
            connection.start();
            String msg = "";
            while(!this.mySocket.isClosed()){
                msg = this.dataIn.readUTF();
                switch(msg){
                    case "DisconnectMe":
                        System.out.println("Reached there");
                        this.serverGui.disconnectClient(this.client.getIpAddress());
                        break;
                    default:
                        this.client.setMsgFromServer(this.client.getMsgFromServer()+"\n From Client : "+msg);
                        this.serverGui.setMessageInClientDetails(msg,this.client.getIpAddress());
                }
            }
            this.dataIn.close();
            this.dataOut.close();
            this.objectIn.close();
            this.mySocket.close();
        } catch (IOException ex) {
            //Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception IOException from serverworker");
            this.serverGui.disconnectClient(this.client.getIpAddress());
        } catch (ClassNotFoundException ex) {
            System.out.println("Error : "+ex.getMessage());
            System.out.println("Exception Class not found from serverworker");
        }
    }
}
