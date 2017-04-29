package com.cg.common.util;

import java.io.IOException;

import javax.swing.JOptionPane;

public class ExecuteHelper {
	/**
	 * 执行程序
	 * @param path 程序路径
	 * @return 是否执行成功
	 */
	public static boolean Execute(String path){
		try {
			java.awt.Desktop.getDesktop().open(new java.io.File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 执行程序（对jar文件单独讨论）
	 * @param path 程序路径
	 * @param wait 是否等待执行完毕
	 * @return 是否执行成功
	 */
	public static boolean Execute2(String path,boolean wait){
		Runtime runtime=Runtime.getRuntime();  
		Process process;
		try {
			if(path.endsWith(".jar")){
							process = runtime.exec("cmd /c start java -jar " + path);
//				process = runtime.exec("java -jar " + path);
			}else{
				process = runtime.exec(path);
			}
			if(wait){
				int exitcode=process.waitFor();  
				System.out.println("finish:"+exitcode);  
			}
		} catch (IOException | InterruptedException e) {
			JOptionPane.showMessageDialog(null, "任务执行出现异常：" + e.getMessage());
			e.printStackTrace();
			return false;
		} 
		return true;
	}
}
