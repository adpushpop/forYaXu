package com.zzc.app;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
	private final static String commandFilePath = System.getProperty("user.dir")+File.separatorChar+"command.txt";
	
	public static void main(String[] args) {
		if(args.length != 1){
			logger.error("----->miss args！");
			return;
		}
		if("exeCmd".equals(args[0])){//执行命令
			exeCmd();
		}else if("autoInput".equals(args[0])){//自动输入
			autoInput();
		}else{
			logger.error("----->wrong args!");
		}
	}
	
	/**
	 * 执行命令
	 */
	public static void exeCmd(){
		Runtime runtime = Runtime.getRuntime();
		
		//解析文件并执行命令
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(commandFilePath);
			isr = new InputStreamReader(fis, CHARSET);
			br = new BufferedReader(isr);
			String line = "";
			
			int sleepTime = Integer.valueOf(PropertyHelp.getKeyValue("commandSleepTime"));//当前lineNum
			String commandPath = PropertyHelp.getKeyValue("commandPath");//执行命令的路径
			while ((line=br.readLine())!=null) {
				logger.info("----->execute command :{}",line);
				Process process = runtime.exec(line, null, new File(commandPath));
				int ret = process.waitFor();
				if(ret == 0){
					logger.info("----->execute result is :{}",ret);
					printStream(new BufferedInputStream(process.getInputStream()));
				}else{
					logger.error("----->execute result is :{}",ret);
					printStream(new BufferedInputStream(process.getErrorStream()));
				}
				Thread.sleep(sleepTime);
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
	 * 打印输出
	 * @param is
	 */
	public static void printStream(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				logger.info(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 自动输入，读取配置文件，自动输入，类似按键精灵功能
	 */
	public static void autoInput(){
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
