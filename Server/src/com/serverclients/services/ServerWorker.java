
package com.serverclients.services;

import com.serverclients.frames.ServerGUI;
import com.serverclients.pojos.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    
    public void updateEndTimeForCustomer(){
        ArrayList<Client> clients = new ArrayList<Client>();
        String path = "Files"+File.separator+"clientsList.txt";
        File file = new File(path);
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            clients = (ArrayList<Client>)objectIn.readObject();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
           
            for(Client client : clients){
                if(client.getClientUniqueNumber().equals(this.client.getClientUniqueNumber())){
                    client.setEndTime(dtf.format(now));
                }
            }
            objectIn.close();
            fileIn.close();
            
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(clients);
            objectOut.close();
            fileOut.close();
        } catch (Exception ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
                        this.updateEndTimeForCustomer();
                        this.serverGui.disconnectClient(this.client.getIpAddress());
                        break;
                    case "$Going$To$Idle$":
                        this.serverGui.setClientStatus("IDLE",this.client.getIpAddress());
                        break;
                    case "$Being$Online$":
                        this.serverGui.setClientStatus("ONLINE",this.client.getIpAddress());
                        break;
                    case "$Being$Idle$":
                        this.dataOut.writeUTF("#shutdown@yourself#");
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
            System.out.println("Exception IOException from serverworker : "+ex.getMessage());
            this.serverGui.disconnectClient(this.client.getIpAddress());
        } catch (ClassNotFoundException ex) {
            System.out.println("Error : "+ex.getMessage());
            System.out.println("Exception Class not found from serverworker");
        }
    }
}
