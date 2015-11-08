package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.Rule;
import models.Task;
import play.Play;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;



/**
 * @author HongLiang
 *
 */
public class MongoDB {
	/*
	 * mongo_db:数据库
	   collection_table:集合表
	*/
	Mongo mongodb = null;
	DB mongo_colls = null;
	DBCollection mongo_tab = null;
	
	String DB_NAME__VISITLOG = "visitlog";
	
	String COLLECTION_HTTPLOG ="httplog";
	String COLLECTION_RULE ="rule";
	RuleDao ruledao = new RuleDao();
	
	public MongoDB(){  
		  boolean local  = false;
		  String localHostName = Play.configuration.getProperty("mongo.local.hostname");
		  Integer  localPort =Integer.parseInt(Play.configuration.getProperty("mongo.local.port"));

		  String remoteHostName = Play.configuration.getProperty("mongo.remote.hostname");
		  Integer remotePort = Integer.parseInt(Play.configuration.getProperty("mongo.remote.port"));
		  
		  String hostname ="";
		  int port ;
		  if(local){
			  hostname = localHostName;
			  port = localPort;
			  try {
				  mongodb = new Mongo( hostname, port);
				System.out.println("connect mongodb success");
			  }catch(Exception e) {
				 System.out.println("connect mongodb failure");
			  }
		  }else {
			hostname = remoteHostName;
		    port = remotePort;
		    try {
		    	mongodb = new Mongo( hostname , port);
		        }catch(Exception e) {
		        	 System.out.println(e);
		        }
		    }
	}
	public Map getHttplogList(String st, String et, String ip,String rulecontent,String scope,String simid,String currpage){
		Map resultmap=new HashMap();
		int totalpage=0;
		List<Task> httplist = new ArrayList<Task>();
		mongo_colls= mongodb.getDB(DB_NAME__VISITLOG);
		mongo_tab = mongo_colls.getCollection(COLLECTION_HTTPLOG);
		JDBCUtils jdbc=JDBCUtils.getJdbcInstance();
		
		BasicDBObject query = new BasicDBObject();
		/*queryCondition.put("age", new BasicDBObject("$gt", 27));  */
		if("rulelog".equals(scope)){
//			query.put("data",new BasicDBObject("$exists", "true"));
			/*	name: {$exists: true}*/
			query.put("data",new BasicDBObject("$ne", ""));
			String str[]= new String[0];
			query.put("keyword",new BasicDBObject("$ne",str));
			query.put("proto","POST");
		}
		if(null!=st&&!"".equals(st)&&null!=et&&!"".equals(et)){
			 BasicDBObject andquery = new BasicDBObject();
			 andquery.put("$gte", st);
			 andquery.append("$lte", et);
			 query.put("time", andquery);
		}
		else if(null!=st&&!"".equals(st)){
			query.put("time",new BasicDBObject("$gte", st));
		}
		else if(null!=et&&!"".equals(et)){
			query.put("time",new BasicDBObject("$lte", et));
		}
		if(null!=simid&&!"".equals(simid)){
			query.put("simid",simid);
		}
		if(null!=ip&&!"".equals(ip)){
			/*$or:[{age:11},{age:22}]
			srcip  destip
			{"$or":[{"srcip":"159.226.43.99"},{"destip":"159.226.43.99"}]}
			{ "simid" : "vtk7mil2a2g6crjztofsr4ay2", "$or" : [{ "srcip" : "159.226.43.99" }, { "destip" : "159.226.43.99" }] }*/
			BasicDBList values = new BasicDBList();
			values.add(new BasicDBObject("srcip",ip)); 
			values.add(new BasicDBObject("destip",ip)); 
			query.put("$or",values);
		}
		if(null!=rulecontent&&!"".equals(rulecontent)){
			/*db.httplog.find({ "keyword" : { "$all" : [3292]}}).limit(50);*/
			/*Pattern pattern = Pattern.compile(rulecontent, Pattern.CASE_INSENSITIVE);
		    query.put("keyword", pattern);*/
			List<String> idlst=new ArrayList<String>();
			idlst=ruledao.getRuleIdByName(rulecontent);
		/*	db.httplog.find({ "$or" : [{ "keyword" : { "$all" : [3292] } }, { "keyword" : { "$all" : [3287] } }] }).limit(50);*/
			BasicDBList values = new BasicDBList();
		/*	{ "$or" : [ { "$all" : "[3290]"} , { "$all" : "[3292]"}]}*/
			if(idlst.size()>0){
				for(int i=0;i<idlst.size();i++){
					BasicDBObject val = new BasicDBObject();
					int[] str=new int[1];
					str[0]=Integer.parseInt(idlst.get(i));
					val.put("keyword",new BasicDBObject("$all",str)); 
					values.add(val);
				}
				query.put("$or",values);
			}
		}
		System.out.println(query);
		/*分页一页10条数据*/
		int count_every_page=10;
		totalpage=mongo_tab.find(query).count()/count_every_page;
		int begin=(Integer.parseInt(currpage)-1)*count_every_page;
		DBCursor cur = mongo_tab.find(query).skip(begin).limit(count_every_page);
		
		System.out.println("mongo query success....");
		jdbc.getConnection();
		while (cur.hasNext()) {
			BasicDBObject curnext = (BasicDBObject) cur.next();
			if (curnext != null) {
				Task task = new Task();
				task.time = curnext.getString("time");
				task.srcip = curnext.getString("srcip");
				task.destip = curnext.getString("destip");
				task.host = curnext.getString("host");
				task.sim = curnext.getString("simid");
				task.method = curnext.getString("proto");
				if("post".equalsIgnoreCase(curnext.getString("proto"))){
					task.ispost=true;
				}
				task.hitdata = curnext.getString("data");
				String keyword="";
				
				String rule_id= curnext.getString("keyword");
				rule_id=rule_id.replaceAll("\\[","");
				rule_id=rule_id.replaceAll("\\]","");
				rule_id=rule_id.replaceAll("\"","");
				rule_id=rule_id.trim();
				if(!"".equals(rule_id.trim())){
					System.out.println("命中内容"+curnext.getString("data"));
					String rule[]=rule_id.split(",");
					if(rule.length>0){
						for(int i=0;i<rule.length;i++){
							if(rule[i].startsWith("sg")||rule[i].contains("sg")){
								rule[i]=rule[i].split("sg")[1];
							}
							if(i==0){
								keyword=keyword+ruledao.getRulenameById(rule[i]);
							}else{
								keyword = keyword+","+ruledao.getRulenameById(rule[i]);
							}
						}
					}
				}
				task.keyword =keyword;
//				task.keyword =rule_id;
				task.url = curnext.getString("url");
				httplist.add(task);
			}
		}
		jdbc.closeconn();
		resultmap.put("count", totalpage);
		resultmap.put("list", httplist);
		return resultmap;
	}
	
	public List<Rule> getRuleList(String sim, String host, String ip,String rulecontent){
		
		List<Rule> rulelist = new ArrayList<Rule>();
		mongo_colls= mongodb.getDB(DB_NAME__VISITLOG);
		mongo_tab = mongo_colls.getCollection(COLLECTION_RULE);
		BasicDBObject query = new BasicDBObject();
		DBObject order = null;
		if (!(ip == null || "".equals(ip))) {
			query.put("ip",ip);
		}
		if (!(sim == null || "".equals(sim))) {
			query.put("sim",sim);
		}
		if (!(host == null || "".equals(host))) {
			query.put("host",host);
		}
		if (!(rulecontent == null || "".equals(rulecontent))) {
			query.put("rulecontent",rulecontent);
		}
		order=new BasicDBObject().append("intime",1);
		try{
			DBCursor cur = mongo_tab.find(query).sort(order);
			while (cur.hasNext()) {
				BasicDBObject curnext = (BasicDBObject) cur.next();
				if (curnext != null) {
					Rule rule = new Rule();
					rule.setHost(curnext.getString("host")) ;
					rule.setIp(curnext.getString("ip"));
//					rule.setRulecontent(curnext.getString("rulecontent"));
					rule.setSimid(curnext.getString("simid"));
					String keywords =curnext.getString("keyword");
					keywords = formatKeyword(keywords);
					rule.setKeyword(keywords);
//						rule.host=curnext.getString("host");
//						rule.ip=curnext.getString("ip");
//						rule.rulecontent=curnext.getString("rulecontent");
//						rule.simid=curnext.getString("simid");
					rulelist.add(rule);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rulelist;
			
	}
	public String formatKeyword(String keywords) {
		
		keywords=keywords.replaceAll("\"", "");
		keywords=keywords.replaceAll("\\s","");
		keywords=keywords.split("\\[")[1];
		keywords=keywords.split("\\]")[0];
		
		return keywords;
	}
	
	public boolean addOrder2Rule(BasicDBObject r){
		mongo_colls= mongodb.getDB(DB_NAME__VISITLOG);
		mongo_tab = mongo_colls.getCollection(COLLECTION_RULE);
		boolean flag = false;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String dd=df.format(new Date());// new Date()为获取当前系统时间
		long nt=DateFormatUtil.StringDate2Long(dd);
		try{
			if(null!=r){
				r.put("intime",String.valueOf(nt));
				mongo_tab.insert(r);
			flag = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean deleteRule(BasicDBObject r){
		
		mongo_colls= mongodb.getDB(DB_NAME__VISITLOG);
		mongo_tab = mongo_colls.getCollection(COLLECTION_RULE);
		boolean flag = false;
		
		try{
			if(null!=r){
				mongo_tab.remove(r);
				flag = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
public JSONArray getRuleJsonArray(String sim){
		
		JSONArray rulearray = new JSONArray();
		mongo_colls= mongodb.getDB(DB_NAME__VISITLOG);
		mongo_tab = mongo_colls.getCollection(COLLECTION_RULE);
		
		DBObject query = null;
		DBObject order = null;
		
		if (!(sim == null || "".equals(sim))) {
			query = new BasicDBObject().append("simid",sim);
		}
		order=new BasicDBObject().append("intime",1);
		try{
			DBCursor cur = mongo_tab.find(query).sort(order);
			while (cur.hasNext()) {
				BasicDBObject curnext = (BasicDBObject) cur.next();
				if (curnext != null) {
					JSONObject json = new JSONObject();
					json.put("host", curnext.getString("host"));
					json.put("ip", curnext.getString("ip"));
					json.put("sim", sim);
//					System.out.println(curnext.getString("rule_content"));
//					json.put("rule", curnext.getString("rule_content","null"));
//					json.put("hehe","null");
					String strkey=formatKeyword(curnext.getString("keyword"));
					json.put("keyword",strkey);
					rulearray.put(json);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rulearray;
	}
public boolean updateOrder4Rule(BasicDBObject info) {
	mongo_colls= mongodb.getDB(DB_NAME__VISITLOG);
	mongo_tab = mongo_colls.getCollection(COLLECTION_RULE);
	boolean flag = false;
	try{
		if(null!=info){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String dd=df.format(new Date());// new Date()为获取当前系统时间
			long nt=DateFormatUtil.StringDate2Long(dd);
			BasicDBObject infotemp=new BasicDBObject();
			infotemp.put("simid", info.getString("simid"));
			infotemp.put("ip", info.getString("ip"));
			infotemp.put("host", info.getString("host"));
			infotemp.put("rulecontent", info.getString("rulecontent"));
			info.put("intime",nt);
//			String keyword =info.getString("keyword");
//			info.put("keyword",null);
//			infotemp.put("keyword", keyword);
			mongo_tab.update(infotemp,info);
			flag = true;
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return flag;
}
}
