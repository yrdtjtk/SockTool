package com.tcps.yrd;

public class Buff {
	public static String toString(byte[] b,String fmt){
		StringBuilder sb = new StringBuilder("[");
		for(int i=0; i<b.length; i++){
			sb.append(String.format(fmt, b[i]));
			sb.append(", ");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.replace(sb.length()-1, sb.length(), "]");
		return sb.toString();
	}
	public static String toHexString(byte[] b){
		return toString(b,"%02X");
	}
	public static String toStr(byte[] b,String fmt,int len){
		int rLen = b.length;
		if(len < rLen){
			rLen = len;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<rLen; i++){
			sb.append(String.format(fmt, b[i]));
			sb.append(" ");
		}
		return sb.toString();
	}
	public static String toStr(byte[] b,String fmt){
		return toStr(b,fmt,b.length);
	}
	public static String toHexStr(byte[] b,int len){
		return toStr(b,"%02X",len);
	}
	public static String toHexStr(byte[] b){
		return toStr(b,"%02X");
	}
	
}
