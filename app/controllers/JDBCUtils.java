package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import play.Play;


public class JDBCUtils {
	
		Logutils util=new Logutils();
		static Connection conn = null;
		private static JDBCUtils jdbc;
		
		 public static JDBCUtils getJdbcInstance() {  
		       if (jdbc == null) {  
		    	   jdbc = new JDBCUtils();  
		       }  
		       return jdbc;  
		    }  

//		public JDBCUtils() {
//			if(null!=conn){
//				try {
//					if(conn.isClosed()){
//						conn=getConnection();
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//        	}

//		}
		public  Connection getConnection() {
			try {
//				Properties prop = null;
//				try {
//					prop = util.getPropConfig();
//				} catch (IOException e1) {
					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				String driver = prop.getProperty("driver").trim();
//				String url = prop.getProperty("url").trim();
//				String username = prop.getProperty("username").trim();
//				String password = prop.getProperty("password").trim();
				String driver = Play.configuration.getProperty("driver").trim();
				String url = Play.configuration.getProperty("url").trim();
				String username = Play.configuration.getProperty("username").trim();
				String password = Play.configuration.getProperty("password").trim();
				Class.forName(driver);
				
//				Connection conn = DriverManager.getConnection(url,username,password);
				if(conn==null||conn.isClosed()){
					conn = DriverManager.getConnection(url,username,password);
					System.out.println("mysql connect success....");
				}
				
//				return conn;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				 e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				 e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				 e.printStackTrace();
			}
			return null;

		}
		 public  boolean execute(String sql) {
			 		
		        try {
		        	if(null!=conn){
			        	if(conn.isClosed()){
			        		conn=getConnection();
			        	}
		        	}else if(null==conn){
		        		conn=getConnection();
		        	}
		            Statement st = conn.createStatement();
		            
		            int count = st.executeUpdate(sql);

		            st.close();
		           // closeconn();
		            if(count>0){
		            	return true;
		            }
		            else{
		            	System.out.println("executeUpdate failure!");
		            	return true;
		            }
		            
		        } catch (SQLException e) {

		            throw new IllegalArgumentException(e);

		        }
		    }
		 public  ResultSet query(String sql) {
		 		
		        try {
		        	if(null!=conn){
			        	if(conn.isClosed()){
			        		conn=getConnection();
			        	}
		        	}else if(null==conn){
		        		conn=getConnection();
		        	}
		            Statement st = conn.createStatement();
		            
		            ResultSet result = st.executeQuery(sql);

//		            st.close();
		            if(result!=null){
		            	return result;
		            }
		            else{
		            	System.out.println("executeQuery failure!");
		            	return null;
		            }
//		            conn.close();
		        } catch (SQLException e) {

		            throw new IllegalArgumentException(e);

		        }
		    }
		 
		 public  void closeconn() {
		 		
		        	if(null!=conn){
		        		try {
							conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
		    }


		/*public Connection getConn() {
			return conn;
		}
		public void setConn(Connection conn) {
			this.conn = conn;
		}
*/
	}
