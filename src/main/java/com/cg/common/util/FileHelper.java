package com.cg.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.naming.ldap.UnsolicitedNotificationEvent;

public class FileHelper {
	public static String getProjRoot(){
		return System.getProperty("user.dir");
	}
	/**
	 * 将text写入文件中
	 */
	public static void WriteToFile(File file,String text){
		WriteToFile(file, text,"utf-8");
	}
	public static void WriteToFile(File file,String text,String encode){
		if(file == null || text == null) return;
		try(BufferedWriter out = 
				new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),encode))) {
			out.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void WriteToFile(String path,String text){
		if(path == null || text == null) return;
		WriteToFile(new File(path), text);
	}
	/**
	 * 从文件中读取全部内容
	 */
	public static String ReadAllFromFile(File file){
		if(file == null || !file.exists()) return null;
		StringBuilder text = new StringBuilder();
		String line;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			while((line = in.readLine()) != null){
				text.append(line);
				text.append("\n");
			}
			text.deleteCharAt(text.length() - 1);
			return text.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static File getDir(String dirName){
		File dir = new File(dirName);
		if(!dir.exists()) dir.mkdir();
		if(!dir.isDirectory()) return null;
		return dir;
	}
	
	public static File getDir(File dirBase,String dirName){
		return getDir(dirBase,dirName,true);
	}
	
	public static File getDir(File dirBase,String dirName,boolean createNew){
		if(dirBase == null || dirBase.exists() == false) throw new RuntimeException("基目录不存在");
		File dir = new File(dirBase,dirName);
		if(createNew && !dir.exists()) dir.mkdir();
		return dir;
	}
	/**
	 * 获取指定目录下的文件，createNew为true时，则当文件不存在时创建新文件
	 */
	public static File getFile(File dirBase, String fileName, boolean createNew){
		if(dirBase == null || dirBase.exists() == false) throw new RuntimeException("基目录不存在");
		File file = new File(dirBase,fileName);
		try {
			if(!file.exists() && createNew)
				file.createNewFile();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
//	public static File getFile(File dirBase, String fileName){
//		return getFile(dirBase,fileName,true);
//	}
	public static void deleteDir(File dir) {
		if(dir == null || !dir.exists()) return;
		for(File file:dir.listFiles()){
			if(file.isFile()) 
				file.delete();
			else if(file.isDirectory()) 
				deleteDir(file);
		}
		dir.delete();
	}
}
