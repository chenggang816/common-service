package com.cg.common.util;

import java.io.IOException;

import javax.swing.JOptionPane;

public class ExecuteHelper {
	/**
	 * ִ�г���
	 * @param path ����·��
	 * @return �Ƿ�ִ�гɹ�
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
	 * ִ�г��򣨶�jar�ļ��������ۣ�
	 * @param path ����·��
	 * @param wait �Ƿ�ȴ�ִ�����
	 * @return �Ƿ�ִ�гɹ�
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
			JOptionPane.showMessageDialog(null, "����ִ�г����쳣��" + e.getMessage());
			e.printStackTrace();
			return false;
		} 
		return true;
	}
}
