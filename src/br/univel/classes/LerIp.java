package br.univel.classes;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LerIp {

	public static String ler(){

		InetAddress IP = null;

		try { 
			IP = InetAddress.getLocalHost();
			String IPString = IP.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return IP.getHostAddress();
	}

//	public static void main(String[] args) {
//		new LerIp();
//	}
}
