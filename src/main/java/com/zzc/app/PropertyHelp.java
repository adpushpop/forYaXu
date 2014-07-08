package com.zzc.app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * property读写
 * @author zhengzhichao
 *
 */
public class PropertyHelp {
	private final static Logger logger = LoggerFactory.getLogger(PropertyHelp.class);
    //属性文件的路径
	private static URL classPath = ClassLoader.getSystemResource("/");
    private static String profilepath = classPath.getPath()+File.separatorChar+"config.properties";
    private static Properties props = new Properties();
    
    static {
        try {
            props.load(new FileInputStream(profilepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {        
            System.exit(-1);
        }
    }
   
    /**
     * 读取
     * @param key
     * @return
     */
    public static String getKeyValue(String key) {
        return props.getProperty(key);
    }
    
    /**
     * 读取
     * @param filePath 读取文件的路径
     * @param key
     * @return
     */
    public static String readValue(String filePath, String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 写入
     * @param keyname
     * @param keyvalue
     */
    public static void writeProperties(String keyname,String keyvalue) {        
        try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(profilepath);
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            logger.error("----->属性文件更新错误");
        }
    }
     
    /**
     * 更新
     * @param keyname
     * @param keyvalue
     */
    public static void updateProperties(String keyname,String keyvalue) {
        try {
            props.load(new FileInputStream(profilepath));
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(profilepath);            
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
           logger.error("------>属性文件更新错误");
        }
    }
}