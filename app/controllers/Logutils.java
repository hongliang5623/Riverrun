package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Logutils {

	public Properties getPropConfig() throws IOException {
		
		String path = this.getClass().getResource("/").getPath();
		path=path.split("classes")[0]+"mysql.cfg";
		path=path.replace("/D:", "D:");
		InputStream inputStream = null;
		System.out.println("config path:"+path);
		try {
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
		}
		Properties p = new Properties();    
		 try {    
		  p.load(inputStream);    
		  } catch (IOException e1) {    
		   e1.printStackTrace();    
		 }    
		
		 inputStream.close();
		return p;
	}
	public static void main(String args[]){
//		Logutils util=new Logutils();
//		Properties prop=null;
//		try {
//			prop=util.getPropConfig();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(prop.get("username"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date11 =null;
		date11= df.format(new Date());
		System.out.println(date11);
	}
}
