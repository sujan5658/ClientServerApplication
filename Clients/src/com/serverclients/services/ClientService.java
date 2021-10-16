
package com.serverclients.services;

import com.serverclients.frames.ClientGUI;
import com.serverclients.pojos.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientService extends Thread {
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private ObjectOutputStream objectOut;
    public static Socket mysocket;
    private int port;
    private String serverIp;
    private ClientGUI clientGUI;
    private Client client;
    private String userUniqueId;
    private String customerUserName;
    
    private void getClientDetails(){
        this.client = new Client();
        String ip="";
        try {
            ip = InetAddress.getLocalHost().getHostAddress().toString();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.client.setIpAddress(ip);
        this.client.setUserName(System.getProperty("user.name"));
        this.client.setPortNo(this.port);
        this.client.setOperatingSystem(System.getProperty("os.name"));
        this.client.setOsVersion(System.getProperty("os.version"));
        this.client.setOsArch(System.getProperty("os.arch"));
        this.client.setCustomerUserName(this.customerUserName);
        this.client.setUserUniqueId(this.userUniqueId);
        SimpleDateFormat formatter = new SimpleDateFormat("dd:mm:YYYY:HH:mm:ss");
        String date = formatter.format(new Date());
        String key = this.customerUserName+this.userUniqueId+date;
        this.client.setClientUniqueNumber(key);
    }
    public ClientService(String serverIp,int port,ClientGUI clientGUI,String customerUserName,String userUniqueId) throws IOException{
        this.serverIp = serverIp;
        this.clientGUI = clientGUI;
        this.port = port;
        mysocket = new Socket(serverIp,port);
        clientGUI.setConnectionMessage();
        this.customerUserName = customerUserName;
        this.userUniqueId = userUniqueId;
    }
  
    public void run(){
        this.getClientDetails();
        Thread connection = new ConnectionChecker(this.clientGUI,this.serverIp);
        connection.start();
        
        try {
            this.objectOut = new ObjectOutputStream(mysocket.getOutputStream());
            this.dataIn = new DataInputStream(mysocket.getInputStream());
            this.dataOut = new DataOutputStream(mysocket.getOutputStream());
            this.objectOut.writeObject(this.client);
            
            Thread idleTime = new IdleTimeService(this.clientGUI,this.dataOut);
            idleTime.start();
            
            String msg = "";
            boolean isAlert=false;
            Runtime runTime = Runtime.getRuntime();
            while(mysocket.isConnected()){
                msg = this.dataIn.readUTF().toString();
                switch(msg){
                    case "ServerStopped":
                        this.clientGUI.setDisconnectedMessage();
                        msg = "From Server : Server Stopped. Connection loss.!!!";
                        break;
                    case "#shutdown@yourself#":
                        this.mysocket.close();
                        //This code is for window
                        msg = "From Server :  ";
                        runTime.exec("shutdown -s -t 0");
                        //This code is for linux and mac.
                        //runTime.exec("shutdown -h now");
                        break;
                    case "#logoff@yourself#":
                        this.mysocket.close();
                        //This code is for window
                        //runTime.exec("shutdown -L");
                        //This code is for linux and mac
                        runTime.exec("osascript -e 'tell app \"System Events\" to  «event aevtrlgo»'");
                        //If above code doesnot work for mac you can try this also.
                        //runTime.exec("osascript -e 'tell app \"System Events\" to log out'");
                        break;
                    default:
                        if(msg.charAt(0)=='A'){
                            isAlert = true;
                            msg = msg.substring(1);
                        }
                        if(msg.charAt(0)=='N'){
                            isAlert = false;
                            msg = msg.substring(1);
                        }
                        if(msg.indexOf("$Set$Idle$Time$")!=-1){
                            String [] values = msg.split(":");
                            int minutes = Integer.parseInt(values[1]);
                            IdleTimeService.idleTimeout = minutes;
                        }else{
                            msg = "From Server : "+msg;
                        }
                        
                }
                if(!msg.equals("From Server :  ")){
                    this.clientGUI.setServerMessageInMessageBox(isAlert,msg);
                }
                isAlert=false;
            }
        } catch (IOException ex) {
            //Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Port closed");
        }
    }
}
