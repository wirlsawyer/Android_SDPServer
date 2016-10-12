package com.example.sdpserver;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class SDPServer extends Thread {
	private Handler mHandler;
	private BluetoothServerSocket serverSocket;
	public BluetoothSocket localBluetoothSocket;
	private static final UUID ServiceDiscoveryServerServiceClassID_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");	
					
	// "PWAccessP"	
	public SDPServer(BluetoothAdapter bluetoothAdapter,	String ServerName, Handler handler) {
		try {
			
			this.mHandler = handler;
			this.serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(ServerName,
							ServiceDiscoveryServerServiceClassID_UUID);
			Log.e("SYBluetoothSDPServerThread",
					"Success:listenUsingRfcommWithServiceRecord");
		} catch (IOException e) {
			// TODO Auto-generated catch blocks
			Log.e("SYBluetoothSDPServerThread",
					"Error:listenUsingRfcommWithServiceRecord");
		}

	}

	public void run() {
		try {

			Log.e("SYBluetoothSDPServerThread", "Listen....");
			this.localBluetoothSocket = this.serverSocket.accept();
			
		} catch (IOException localIOException) {
			// AndBloodPressureMonitor.access$102(AndBloodPressureMonitor.this,
			// false);
			Log.e("SYBluetoothSDPServerThread", "Error:serverSocket.accept()");
			try {
				Log.e("SYBluetoothSDPServerThread", "Error:serverSocket.accept()");
				this.serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("SYBluetoothSDPServerThread", "Error:serverSocket.accept()");
			}
			Log.e("SYBluetoothSDPServerThread", "Error:serverSocket.accept()");
		}

		Log.e("SYBluetoothSDPServerThread", "Success:serverSocket.accept()"); 
		
		
		byte[] arrayOfByte = new byte[70];	
		
		while(true)
		{
			try {
				
				this.localBluetoothSocket.getInputStream().read(arrayOfByte);
				
				String hexNumber = new String();
				for (int i = 0; i < arrayOfByte.length; i++) {
					hexNumber += " 0x"
							+ Integer.toHexString(0xff & arrayOfByte[i]);
				}
				
				Log.v("Recv:", hexNumber + " length:" + arrayOfByte.length);
				
				Message msg = new Message();
				msg.obj = hexNumber;
				mHandler.sendMessage(msg);

				
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} 	
			
		}//end while
		
		
		
		try {
			this.serverSocket.close();
			this.localBluetoothSocket.close();
			return;
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	public void cancel() {
		try {
			this.serverSocket.close();
			return;
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}
}
