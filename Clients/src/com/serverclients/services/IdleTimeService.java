
package com.serverclients.services;
import com.serverclients.frames.ClientGUI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.sun.jna.*;
import com.sun.jna.win32.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class IdleTimeService extends Thread {
    public static boolean threadStatus;
    private ClientGUI clientGUI;
    private DataOutputStream dataOut;
    public static int idleTimeout=10;
    
    IdleTimeService(ClientGUI clientGUI,DataOutputStream dataOut){
        this.clientGUI = clientGUI;
        threadStatus = true;
        this.dataOut = dataOut;
    }

    public interface Kernel32 extends StdCallLibrary {
		Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

		/**
		 * Retrieves the number of milliseconds that have elapsed since the system was
		 * started.
		 * 
		 * @see http://msdn2.microsoft.com/en-us/library/ms724408.aspx
		 * @return number of milliseconds that have elapsed since the system was
		 *         started.
		 */
		public int GetTickCount();
	};

	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

		/**
		 * Contains the time of the last input.
		 * 
		 * @see http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputstructures/lastinputinfo.asp
		 */
		public static class LASTINPUTINFO extends Structure {
			public int cbSize = 8;

			/// Tick count of when the last input event was received.
			public int dwTime;

			@SuppressWarnings("rawtypes")
			@Override
			protected List getFieldOrder() {
				return Arrays.asList(new String[] { "cbSize", "dwTime" });
			}
		}

		/**
		 * Retrieves the time of the last input event.
		 * 
		 * @see http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputfunctions/getlastinputinfo.asp
		 * @return time of the last input event, in milliseconds
		 */
		public boolean GetLastInputInfo(LASTINPUTINFO result);
	};

	/**
	 * Get the amount of milliseconds that have elapsed since the last input event
	 * (mouse or keyboard)
	 * 
	 * @return idle time in milliseconds
	 */
	public static int getIdleTimeMillisWin32() {
		User32.LASTINPUTINFO lastInputInfo = new User32.LASTINPUTINFO();
		User32.INSTANCE.GetLastInputInfo(lastInputInfo);
		return Kernel32.INSTANCE.GetTickCount() - lastInputInfo.dwTime;
	}

	enum State {
		UNKNOWN, ONLINE, IDLE, AWAY
	};
        
        public void run(){
            if (!System.getProperty("os.name").contains("Windows")) {
                System.err.println("ERROR: Only implemented on Windows");
                System.exit(1);
            }
            State state = State.UNKNOWN;
            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

            while (threadStatus) {
                int idleSec = getIdleTimeMillisWin32() / 1000;
                State newState = idleSec < 2*60 ? State.ONLINE : idleSec >= idleTimeout*60 ? State.AWAY : State.IDLE;
               
                if (newState != state) {
                        state = newState;
                        System.out.println(dateFormat.format(new Date()) + " # " + state);
                        if(state == state.IDLE){
                            try {
                                this.dataOut.writeUTF("$Going$To$Idle$");
                            } catch (IOException ex) {
                                Logger.getLogger(IdleTimeService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (state == State.AWAY) {
                            try {
                                this.dataOut.writeUTF("$Being$Idle$");
                            } catch (IOException ex) {
                                Logger.getLogger(IdleTimeService.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                        if (state == State.ONLINE) {
                            try {
                                this.dataOut.writeUTF("$Being$Online$");
                            } catch (IOException ex) {
                                Logger.getLogger(IdleTimeService.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                }
                try {
                        Thread.sleep(1000);
                } catch (Exception ex) {
                }
            }
        }
}
