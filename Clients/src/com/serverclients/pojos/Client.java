
package com.serverclients.pojos;

import java.io.Serializable;
import java.net.Socket;

public class Client implements Serializable{
    private String ipAddress;
    private int portNo;
    private String operatingSystem;
    private String osArch;
    private String osVersion;
    private String userName;
    private String connectedTime;
    private String startTime;
    private String endTime;
    private Socket socket;
    private String msgFromServer;

    public Client() {
        this.ipAddress = "";
        this.portNo = 8085;
        this.operatingSystem = "";
        this.osArch = "";
        this.osVersion = "";
        this.userName = "";
        this.connectedTime = "";
        this.startTime = "----";
        this.endTime = "----";
        this.socket = null;
        this.msgFromServer = "";
    }
    
    public Client(Client client) {
        this.ipAddress = client.ipAddress;
        this.portNo = client.portNo;
        this.operatingSystem = client.operatingSystem;
        this.osArch = client.osArch;
        this.osVersion = client.osVersion;
        this.userName = client.userName;
        this.connectedTime = client.connectedTime;
        this.startTime = client.startTime;
        this.endTime = client.endTime;
        this.socket = client.socket;
        this.msgFromServer = client.msgFromServer;
    }
    
    public Client(String ipAddress, int portNo, String operatingSystem, String osArch, String osVersion, String userName, String connectedTime, String startTime, String endTime,Socket socket,String msgFromServer) {
        this.ipAddress = ipAddress;
        this.portNo = portNo;
        this.operatingSystem = operatingSystem;
        this.osArch = osArch;
        this.osVersion = osVersion;
        this.userName = userName;
        this.connectedTime = connectedTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.socket = socket;
        this.msgFromServer = msgFromServer;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNo() {
        return portNo;
    }

    public void setPortNo(int portNo) {
        this.portNo = portNo;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getConnectedTime() {
        return connectedTime;
    }

    public void setConnectedTime(String connectedTime) {
        this.connectedTime = connectedTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getMsgFromServer() {
        return msgFromServer;
    }

    public void setMsgFromServer(String msgFromServer) {
        this.msgFromServer = msgFromServer;
    }
    
    @Override
    public String toString() {
        return "Client{" + "ipAddress=" + ipAddress + ", portNo=" + portNo + ", operatingSystem=" + operatingSystem + ", osArch=" + osArch + ", osVersion=" + osVersion + ", userName=" + userName + ", connectedTime=" + connectedTime + ", startTime=" + startTime + ", endTime=" + endTime + ", socket=" + socket + '}';
    }
}
