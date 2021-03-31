/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serverclients.services;

import com.serverclients.frames.PleaseWait;

/**
 *
 * @author Sujan Koju
 */
public class PleaseWaitService extends Thread{
    public static PleaseWait pleaseWait;
    public void run(){
        pleaseWait = new PleaseWait();
        pleaseWait.setVisible(true);
    }
}
