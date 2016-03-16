package com.tcps.yrd;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class SockClient {
	private String serverIP = "";
	private int serverPort = 0;
	private boolean bConnected = false;
	Socket socket = null;
	BufferedReader inStr = null;
	PrintWriter outStr = null;
	BufferedInputStream inBuf = null;
	BufferedOutputStream outBuf = null;
	
	public boolean isConnected(){
		return bConnected;
	}
	
	public boolean connectToServer(String serIp, int serPort){
		InetAddress addr;
		try {
			addr = InetAddress.getByName(serIp);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Please check the server's IP address!");
			return false;
		}
		try {
			socket = new Socket(addr,serPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try {
			inStr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			inBuf = new BufferedInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
		try {
			outStr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			outBuf = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
		bConnected = true;
		return true;
	}
	public void disconnectFromServer(){
		try {
			bConnected = false;
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendStrToServer(String strData){
		outStr.write(strData);
		outStr.flush();
	}
	public void sendBufToServer(byte[] bufData,int off,int len){
		try {
			outBuf.write(bufData,off,len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		outStr.flush();
	}
	public void sendBufToServer(byte[] bufData,int len){
		sendBufToServer(bufData,0,len);
	}
	public void sendBufToServer(byte[] bufData){
		sendBufToServer(bufData,0,bufData.length);
	}
	public int recvStrFromServer(char[] buf){
		int ret;
		try {
			ret = inStr.read(buf);
			if(ret < 0){
				bConnected = false;
			}
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bConnected = false;
			return -2;
		}
	}
	public int recvBufFromServer(byte[] buf){
		int ret;
		try{
			ret = inBuf.read(buf);
			if(ret < 0){
				bConnected = false;
			}
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bConnected = false;
			return -2;
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char[] buf = new char[10];
		SockClient client = new SockClient();
		client.connectToServer("192.168.20.168", 1024);
		client.sendStrToServer("hello hello duanxinlaile!");
		int num = client.recvStrFromServer(buf);
		System.out.println("rcv[" + num + "]=" + new String(buf));
		client.disconnectFromServer();
	}

}
