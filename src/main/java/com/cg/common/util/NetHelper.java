package com.cg.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class NetHelper{
	public static String getLocalHostIp(){
		String localHostIp = null;
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			localHostIp = inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
			localHostIp = null;
		}
		return localHostIp;
	}
	/*
	 * ��ȡ����������
	 */
	public static String getLocalHostName() {
		String localHostName = null;
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			localHostName = inetAddress.getHostName();
		} catch (UnknownHostException e) {
			localHostName = null;
		}
		return localHostName;
	}
	/*
	 * ��ȡ��������IP��ַ
	 */
	public ArrayList<String> getAllLocalHostIp() throws UnknownHostException {
		String hostName = getLocalHostName();
		InetAddress[] addresses = InetAddress.getAllByName(hostName);
		ArrayList<String> ips = new ArrayList<String>();
		for (int i = 0; i < addresses.length; i++) {
			if(addresses[i] instanceof Inet4Address){
				ips.add(addresses[i].getHostAddress());
			}
		}
		return ips;
	}
	/*
	 * ��ȡ����������IP��ַ
	 * http://blog.csdn.net/havedream_one/article/details/47071393
	 */
	public static List<String> getIPs()
	{
		List<String> list = new ArrayList<String>();
		Runtime r = Runtime.getRuntime();
		Process p;
		try {
			p = r.exec("arp -a");
			BufferedReader br = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String inline;
			while ((inline = br.readLine()) != null) {
				System.out.println(inline);
				if(inline.contains("�ӿ�") || inline.contains("Internet") || inline.equals("")){
					continue;
				}

				//��ЧIP
				String[] str=inline.split(" {4}");
				list.add(str[0].trim());
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HashSet<String> hashSet = new HashSet<String>(list);
		list.clear();
		list.addAll(hashSet);

		for (int i=0;i<list.size();i++) {
			String string = list.get(i);
			if(string.equals("255.255.255.255")){
				list.remove(string);
				i--;
			}
		}
		System.out.println(list);
		return list;
	}
	/*
	 * ����IP��ȡ������
	 * http://blog.csdn.net/havedream_one/article/details/47071393
	 */
	public static Map<String,String> getHostnames(final List<String> ips) throws InterruptedException{

		final Map<String,String> map = new HashMap<String,String>();
		class MonitorObj{
			private int finishedIpNum = 0;
			public int getFinishedIpNum() {
				return finishedIpNum;
			}
			public void addFinishedIpNum(int value) {
				this.finishedIpNum += value;
			}
			public void doNotify(){
				synchronized (this) {
					this.notify();
				}
			}
		}
		final MonitorObj monitorObj = new MonitorObj();
		class PingIp extends Thread{
			private String ip;
			public PingIp(String threadName,String ip) {
				this.setName(threadName);
				this.ip = ip;
			}
			@Override
			public void run() {
				String hostname = getHostNameByIp(ip);
				if(hostname != null){
					System.out.println(ip + ":" + hostname);
					map.put(ip,hostname);
				}else {
					map.put(ip,""); 		//û�л�ȡ���������������ַ���
				}
				System.out.println("�߳�" + this.getName() + "�ѽ����˳�");
				monitorObj.addFinishedIpNum(1);
				if(monitorObj.getFinishedIpNum() >= ips.size()){
					monitorObj.doNotify();
				}
			}
		}

		System.out.println("������ȡhostname...");
		//       Thread mainThread = Thread.currentThread();
		//       System.out.println(mainThread.getName());
		System.out.println("����" + ips.size() + "���߳�");
		for(int i=0;i<ips.size();i++){
			String ip = ips.get(i);
			PingIp pingIp = new PingIp(String.valueOf(i),ip);
			pingIp.start();
		}
		synchronized (monitorObj) {
			monitorObj.wait();
		}

		System.out.println("��ȡ������");         
		return map;
	}

	/*
	 * ����һ��IP��ȡ��Ӧ������
	 */
	private static String getHostNameByIp(String ip) {
		try {
			InetAddress inetAddress = InetAddress.getByName(ip);
			String hostName = inetAddress.getHostName();
			if(hostName.equals(ip)) return null;
			return hostName;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//��������һ�ַ���
//		String command = "ping -a -n 1 -w 10 " + ip;
//		Runtime r = Runtime.getRuntime();
//		Process p;
//		try {
//			p = r.exec(command);
//			BufferedReader br = new BufferedReader(new InputStreamReader(p
//					.getInputStream()));
//			String inline;
//			while ((inline = br.readLine()) != null) {
//				if(inline.indexOf("[") > -1){
//					int start = inline.indexOf("Ping ");
//					int end = inline.indexOf("[");
//					String hostname = inline.substring(start+"Ping ".length(),end-1);
//					return hostname;
//				}
//			}
//			br.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}
}