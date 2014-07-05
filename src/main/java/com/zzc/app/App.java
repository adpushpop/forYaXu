package com.zzc.app;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 读取文件并调用按键命令
 */
public class App {
	private final static Logger logger = LoggerFactory.getLogger(App.class);
	private final static String CHARSET = "UTF-8";
	/**
	 * imei文件路径
	 */
	private final static String imeiFilePath = System.getProperty("user.dir")+File.separatorChar+"imei.txt";
	
	
	public static void main(String[] args) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(imeiFilePath);
			isr = new InputStreamReader(fis, CHARSET);
			br = new BufferedReader(isr);
			String line="";
			
			Thread.sleep(5000);//先休眠一会 给时间放鼠标位置
			int lineNum = Integer.valueOf(PropertyHelp.getKeyValue("lineNum"));//当前lineNum
			int currentLineNum = 1;
			while ((line=br.readLine())!=null) {
				if(lineNum == currentLineNum){//当前行数等于需要行数
					logger.info("----->当前imei:{}",line);
					char[] imei = line.toUpperCase().toCharArray();
					for(char c : imei){
						Thread.sleep(500);
						pressKey(c);
					}
					lineNum++;
					PropertyHelp.updateProperties("lineNum", String.valueOf(lineNum));
					break;
				}
				currentLineNum++;
			}
		}catch(FileNotFoundException e){
			logger.error("----->{}{}","找不到文件:",imeiFilePath);
		}catch (UnsupportedEncodingException e) {
			logger.error("----->{}{}","编码类型错误，支持的编码类型为：",CHARSET);
		}catch (IOException e) {
			logger.error("----->{}",e.getMessage());
		}catch (Exception e) {
			logger.error("----->{}",e.getMessage());
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(isr != null){
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 根据传入的字符，执行按键动作
	 * @param str
	 */
	public static void pressKey(char c){
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			logger.error("----->{}",e.getMessage());
		}
		int key = (int)c;
		robot.keyPress(key);
		robot.keyRelease(key);
	}
}
