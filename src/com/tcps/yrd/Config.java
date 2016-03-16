package com.tcps.yrd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class Config {
	private static final String fileName = "SockTool.ini";
	Properties prop = new Properties();
	
	public Config(){
		InputStream is;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return;
		}
//		InputStream is = Config.class.getResourceAsStream(fileName);
		try {
			prop.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void save(){
		OutputStream os;
		try {
			os = new FileOutputStream(fileName);
			try {
				prop.store(os, "Write SockTool's Config to File.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getServerIP(){
		return prop.getProperty("ServerIP");
	}
	public void setServerIP(String sip){
		prop.setProperty("ServerIP", sip);
	}
	public String getServerPort(){
		return prop.getProperty("ServerPort");
	}
	public void setServerPort(int sp){
		prop.setProperty("ServerPort", Integer.toString(sp));
	}
	public static void main(String[] args){
		Config c = new Config();
		String s;
		s = c.getServerIP();
		System.out.println("ServerIp=" + s);
		s = c.getServerPort();
		System.out.println("ServerPort=" + s);
		c.setServerIP("192.168.20.168");
		c.setServerPort(1024);
		c.save();
	}
}
