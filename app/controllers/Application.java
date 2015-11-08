package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import models.MulitRule;
import models.Rule;
import models.Rule4IpHost;
import models.Rule4Keyword;
import models.Ruledomain;
import models.Task;

import play.mvc.*;
import play.mvc.results.RenderText;


public class Application extends Controller {

	public static void index() {
		render();
	}

	public static  void checkRever() {
		render();
	}
	
	static MongoDB mongo_db = new MongoDB();
	static RuleDao ruledao = new RuleDao();
	
	public static void reverLogSearch(String st, String et, String ip,
			String rulename,String scope,String simid,String currpage) {

		Map result_map = mongo_db.getHttplogList(st, et, ip,rulename,scope,simid,currpage);
		List<Task> getTask_list = (List<Task>) result_map.get("list");
		int totalpage=(Integer) result_map.get("count");
//		task.time = curnext.getString("time");
//		task.srcip = curnext.getString("srcip");
//		task.destip = curnext.getString("destip");
//		task.host = curnext.getString("host");
//		task.sim = curnext.getString("simid");
//		task.method = curnext.getString("proto");
//		if("post".equalsIgnoreCase(curnext.getString("proto"))){
//			task.ispost=true;
//		}
//		task.hitdata = curnext.getString("data");
//		task.keyword =keyword;
//		task.url = curnext.getString("url");
		List<Map> list = new ArrayList<Map>();
		for(Task task:getTask_list){
			Map map=new HashMap<String,String>();
			map.put("time", task.time);
			map.put("srcip", task.srcip);
			map.put("destip", task.destip);
			map.put("host", task.host);
			map.put("sim", task.sim);
			map.put("method", task.method);
			map.put("ispost", task.ispost);
			map.put("hitdata", task.hitdata);
			map.put("keyword", task.keyword);
			map.put("url", task.url);
			list.add(map);
		}
		
//		int count=getTask_list.size();
		render("/Application/logresult.html",list, totalpage,currpage);
	}

	public static void domainManage() {
		
		renderTemplate("/Application/domain.html");
	}
	
	public static void addRule() {
			
			renderTemplate("/Application/addrule.html");
	}
	
	public static void checkRule() {
		
		renderTemplate("/Application/checkrule.html");
	}
	
	public static void checkRulename(String rulename) {
		
		boolean flag=true;
		
		try{
			if(null!=rulename && !"".equals(rulename)){
				flag=ruledao.checkRuleName(rulename);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(flag){
			renderText("succ");
		}else{
			renderText("fal");
		}
	}
	
	public static void domain_Del() {
		
		renderTemplate("/Application/domainsearch.html");
		
	}
	
	/*public static void domain_delete(String sim, String host, String ip,String rulecontent) {
		boolean flag=false;
		BasicDBObject info = new BasicDBObject();
		try{
			if(null!=sim && !"".equals(sim)){
				info.put("simid", sim);
			}
			if(null!=ip && !"".equals(ip)){
				info.put("ip", ip);
			}
			if(null!=host && !"".equals(host)){
				info.put("host", host);
			}
			if(null!=rulecontent && !"".equals(rulecontent)){
				info.put("rulecontent", rulecontent);	
			}
			
			flag=mongo_db.deleteRule(info);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(flag){
			renderText("succ");
		}else{
			renderText("fal");
		}
		}*/
	
	public static void domain_delete(String ruleid,String simid) {
		boolean flag=false;
		
		try{
			if(null!=ruleid && !"".equals(ruleid)){
				flag=ruledao.deleteRule(ruleid,simid);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(flag){
			renderText("succ");
		}else{
			renderText("fal");
		}
		}
	
	public static void domainSearch(String sim,String rulename, String host, String ip,String flag) {
		
		/*List<Rule> rulelist = mongo_db.getRuleList(sim, host, ip,rulecontent);*/
		boolean ss=false;
		List<Ruledomain> rulelist=new ArrayList<Ruledomain>();
		if(null!=flag&&flag.equals("multi")){
			rulelist=ruledao.getRuleList4Multi(rulename);
		}else{
			rulelist=ruledao.getRuleList4Single(sim,rulename,host, ip);
			ss=true;
		}
		render("/Application/result.html",rulelist,ss);
	}
	
	public static void save_rule(String sim, String host, String ip,String keyword,String mark,
									String rulename,String ruleflag,String network,String proto) {
		RuleUtil ruleutil = new RuleUtil(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag=false;
		if("single".equals(mark)){
			Rule4IpHost iphost11 = new Rule4IpHost();
			Rule4Keyword rulekey = new Rule4Keyword();
			if(null!=sim && !"".equals(sim)){
				iphost11.setSimid(sim);
				rulekey.setSimid(sim);
			}
			if(null!=host){
				iphost11.setHost(host);
			}else{
				iphost11.setHost("");
			}
			if(null!=ip){
				iphost11.setIp(ip);
			}else{
				iphost11.setIp("");
			}
			if(null!=proto){
				iphost11.setProto(proto);
			}else{
				iphost11.setProto("");
			}
			if(null!=network){
				iphost11.setMacid(network);
				rulekey.setMacid(network);
			}else{
				rulekey.setMacid("");
				iphost11.setMacid("");
			}
			iphost11.setInsert_time(sdf.format(new Date()));
			iphost11.setStatus(1);
			rulekey.setUpdata_time(sdf.format(new Date()));
			rulekey.setStatus(1);
			if(null!=rulename && !"".equals(rulename)){
				rulekey.setRule_name(rulename);
			}
			String ruletext=ruleutil.formatRule(keyword);
			System.out.println(ruletext);
			if(null!=ruletext && !"".equals(ruletext)){
				rulekey.setRule(ruletext);
			}
			flag=ruledao.saveSingleRule(iphost11,rulekey,ruleflag);
		}
		else if("multi".equals(mark)){
			MulitRule mulit = new MulitRule();
			if(null!=rulename && !"".equals(rulename)){
				mulit.setRule_name(rulename);
			}
			String ruletext=ruleutil.formatRule(keyword);
			if(null!=ruletext && !"".equals(ruletext)){
				mulit.setRule(ruletext);
			}
			mulit.setStatus(1);
			mulit.setUpdata_flag(0);
			mulit.setUpdata_time(sdf.format(new Date()));
			flag=ruledao.saveMulitRule(mulit);
		}
		if(flag){
			renderText("succ");
		}
		else{
			renderText("falure");
		}
	}

	

	
/*	
 *mongodb
 * public static void save_rule(String sim, String host, String ip,String rulecontent,String keyword) {
		boolean flag=false;
		BasicDBObject info = new BasicDBObject();
		//rulecontent="4qu0dlsir05s5jfuz1tjn8v74vko07pcd74dwu85y4bt12m8zi";
		sim="abcdefghijklmnopqrstuvwxyz0123456789";
		try{
			if(null!=sim && !"".equals(sim)){
				info.put("simid", sim);
			}
			if(null!=ip && !"".equals(ip)){
				info.put("ip", ip);
			}
			if(null!=host && !"".equals(host)){
				info.put("host", host);
			}
//			if(null!=rulecontent && !"".equals(rulecontent)){
//				info.put("rulecontent", rulecontent);	
//			}
			System.out.println(keyword);
			if(null!=keyword && !"".equals(keyword)){
				JSONObject aaa = new JSONObject(keyword);
				keyword={"orkeyword":orkeyword,"addkeyword":addkeyword,"nokeyword":nokeyword}
				String keyor[]=aaa.getString("orkeyword").split(",");
				String keyadd[]=aaa.getString("addkeyword").split(",");
				String keyno[]=aaa.getString("nokeyword").split(",");
				String[] bbb = new String[keyor.length + keyadd.length+keyno.length];
				for (int j = 0; j < keyor.length; ++j) {
					  bbb[j] = keyor[j];
				  }
				for (int j = 0; j < keyadd.length; ++j) {
					bbb[keyor.length + j] ="+"+keyadd[j];
				  }
				for (int j = 0; j < keyno.length; ++j) {
					bbb[keyor.length + keyadd.length +j] ="-"+keyno[j];
				  }
				info.put("keyword", keyor);
			}
			System.out.println(info);
			flag=mongo_db.addOrder2Rule(info);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(flag){
			renderText("succ");
//			List<Rule> rulelist = mongo_db.getRuleList(sim, host, ip,rulecontent);
//			render("/Application/result.html",rulelist);
		}else{
			renderText("fal");
		}
		}*/

	/*public static void save_rule(String sim, String host, String ip,String keyword,String mark) {
		boolean flag=false;
		//sim="vtk7mil2a2g6crjztofsr4ay2";
		if("iphost".equals(mark)){
			
			flag = save_rule4ip(sim, host, ip);
			
		}else if("keyword".equals("mark")){
			
			flag = save_rule4keyword(sim,keyword);
		}
		if(flag){
			renderText("succ");
		}else{
			renderText("fal");
		}
	}*/

	/*public static boolean save_rule4ip(String sim, String host, String ip){
		boolean flag = false;
		Rule4IpHost iphost=new Rule4IpHost();
		try{
			if(null!=sim && !"".equals(sim)){
				iphost.setSimid(sim);
			}
			if(null!=ip && !"".equals(ip)){
				iphost.setIp(ip);
			}
			if(null!=host && !"".equals(host)){
				iphost.setHost(host);
			}
			iphost.setInsert_time(new Date());
			iphost.setStatus(1);
			flag=ruledao.addRuleip(iphost);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}*/
	
//	public static boolean save_rule4keyword(String sim,String keyword){
//		boolean flag = false;
//		Rule4Keyword key=new Rule4Keyword();
//		try{
//			if(null!=sim && !"".equals(sim)){
//				key.setSimid(sim);
//			}
//			if(null!=keyword && !"".equals(keyword)){
//				key.setKeyword(keyword);
//			}
//			key.setStatus(1);
//			key.setUpdata_time(new Date());
//			flag=ruledao.addRulekeyword(key);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return flag;
//	}
	
	/*
	 * mongo 版
	 * public static void update_rule(String sim, String host, String ip,String rulecontent,String keyword) {
		
		
		boolean flag=false;
		BasicDBObject info = new BasicDBObject();
		try{
			if(null!=sim && !"".equals(sim)){
				info.put("simid", sim);
			}
			if(null!=ip && !"".equals(ip)){
				info.put("ip", ip);
			}
			if(null!=host && !"".equals(host)){
				info.put("host", host);
			}
			if(null!=rulecontent && !"".equals(rulecontent)){
				info.put("rulecontent", rulecontent);	
			}
			if(null!=keyword && !"".equals(keyword)){
				String keywords[]=keyword.split(",");
				info.put("keyword", keywords);	
			}
			flag=mongo_db.updateOrder4Rule(info);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(flag){
			renderText("succ");
		}else{
			renderText("fal");
		}
		}*/
	

public static void update_rule(String sim, String ruleid, String mac,String rulename,String keyword) {
		
		//{'ruleid':ruleid,'sim':sim,'mac':macid,'rulename':rulename,'keyword':keyword}
		RuleUtil ruleutil = new RuleUtil(); 
		boolean flag=false;
		String ruleflag="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null!=ruleid&&!"".equals(ruleid)){
			String[] idarry=ruleid.split("_");
			ruleflag=idarry[0];
			String ru_id=idarry[1];
			String ruletext=ruleutil.formatRule(keyword);
			System.out.println(ruletext);
			
			try{
				if("multi".equals(ruleflag)){
					MulitRule mulit = new MulitRule();
					mulit.setRule_id(Integer.parseInt(ru_id));
					mulit.setRule_name(rulename);
					mulit.setRule(ruletext);
					mulit.setUpdata_time(sdf.format(new Date()));
					flag=ruledao.updata_multi(mulit);
				}else{
					Rule4Keyword rulekey = new Rule4Keyword();
					rulekey.setRule_id(Integer.parseInt(ru_id));
					if(null!=mac){
						rulekey.setMacid(mac);
					}else{
						rulekey.setMacid("");
					}
					rulekey.setSimid(sim);
					rulekey.setRule_name(rulename);
					rulekey.setRule(ruletext);
					rulekey.setUpdata_time(sdf.format(new Date()));
					flag=ruledao.updata_rulekey(rulekey);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(flag){
			/*renderText("succ");*/
			boolean ss=false;
			String host="";
			String ip="";
			String rule_name="";
			List<Ruledomain> rulelist=new ArrayList<Ruledomain>();
			if(null!=ruleflag&&"multi".equals(ruleflag)){
				rulelist=ruledao.getRuleList4Multi(rulename);
			}else{
				rulelist=ruledao.getRuleList4Single(sim,rule_name,host, ip);
				ss=true;
			}
			render("/Application/result.html",rulelist,ss);
		}else{
			renderText("fal");
		}
		}

	public static void getReverRule(String sim,String mac) throws JSONException {

		RuleUtil ruleutil=new RuleUtil();
		JSONArray rulerray = new JSONArray();
		JSONObject root = new JSONObject();
		String rule_enc="";
		/**mongodb版本
		 * if(null!=sim&&!"".equals(sim)){
			rulerray = mongo_db.getRuleJsonArray(sim);
		}*/
		if((null!=sim&&!"".equals(sim))||(null!=mac&&!"".equals(mac))){
			rulerray = ruledao.getRule(sim,mac);
		}
		root.put("root", rulerray);
		System.out.println(root.toString());
		int length=root.toString().getBytes().length;
		rule_enc = ruleutil.ruleEncrypt(root.toString());
		rule_enc=length+","+rule_enc;
//		renderJSON(root.toString());
		renderText(rule_enc);
	}
	
//	public static void config(String sim) throws JSONException {
//
//		JSONArray keywordArray = new JSONArray();
//		JSONObject root = new JSONObject();
//
//		if(DB_Config.m==null || DB_Config.db ==null)
//		{
//			 DB_Config.getCollection_visitlog();
//		}
//		DBCollection rule = DB_Config.db.getCollection("rule");
//		DBObject query;
//
//		if (sim!=null &&!sim.equals(null) && !"".equals(sim)) {
//			query = new BasicDBObject().append("simid", sim);
//			DBCursor cur = rule.find(query);
//			if (cur != null) {
//				while (cur.hasNext()) {
//
//					BasicDBObject curnext = (BasicDBObject) cur.next();
//					if (curnext != null) {
//						JSONObject json = new JSONObject();
//						json.put("host", curnext.getString("host"));
//						json.put("ip", curnext.getString("ip"));
//						json.put("sim", sim);
//						json.put("rule", curnext.getString("rule_content"));
//						keywordArray.put(json);
//					}
//				}
//			}
//		}
//		
//		
//		//DB_Config.closeDb();
//		root.put("root", keywordArray);
//		renderJSON(root.toString());
//	}

//	public static List<Task> getTask(String st, String et, String ip) {
//		List<Task> task_list = new ArrayList<Task>();
//
//		if(DB_Config.m==null || DB_Config.db ==null)
//		{
//			 DB_Config.getCollection_visitlog();
//		}
//	
//		DBCollection httplog = DB_Config.db.getCollection("httplog");
//		DBObject query;
//
//		if (!(ip == null || ip.equals(""))) {
//			query = new BasicDBObject().append("time",
//					new BasicDBObject().append("$gt", st).append("$lt", et))
//					.append("destip", ip);
//		} else {
//			query = new BasicDBObject().append("time", new BasicDBObject()
//					.append("$gt", st).append("$lt", et));
//		}
//
//		DBCursor cur = httplog.find(query);
//
//		while (cur.hasNext()) {
//			BasicDBObject curnext = (BasicDBObject) cur.next();
//			if (curnext != null) {
//
//				Task task = new Task();
//				task.time = curnext.getString("time");
//				task.srcip = curnext.getString("srcip");
//				task.destip = curnext.getString("destip");
//				task.host = curnext.getString("host");
//				task.sim = curnext.getString("simid");
//				task.method = curnext.getString("proto");
//
//				task.inserttime = "2014-07-11 09:09:10";
//				task.url = "http://www.tvmao.com/program/AHTV-AHTV1-w4.html";
//				task_list.add(task);
//			}
//		}
//
//		//DB_Config.closeDb();
//		return task_list;
//
//	}
}