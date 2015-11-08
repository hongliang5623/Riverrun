package controllers;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.MulitRule;
import models.Rule;
import models.Rule4IpHost;
import models.Rule4Keyword;
import models.Ruledomain;


public class RuleDao {
	/**
	 * @param args
	 */
	JDBCUtils jdbc=JDBCUtils.getJdbcInstance();
	RuleUtil ruleutil=new RuleUtil();
	 
	public boolean addRuleip(Rule4IpHost iphost){
		jdbc.getConnection();
		String sql = "insert into singlegw_rules_iphost(simid,ip,host,status,insert_time,proto,macid)values('"
                + iphost.getSimid() + "','" + iphost.getIp()+ "','" +iphost.getHost()+"','"+
				iphost.getStatus()+"','"+iphost.getInsert_time()+"','"+iphost.getProto()+"','"+iphost.getMacid()+"');";
		System.out.println(sql);
		boolean flag=jdbc.execute(sql);
		
		if(flag){
			return true;
		}else{
			return false;
		}
		
	}
	public boolean addRulekeyword(Rule4Keyword keyword){
		/**先检查mac是否已经插上别的sim卡
		 * 如果没有就不用管直接添加，如果有就把原sim卡信息更新，让其对应的mac为空
		 * 在检查本次添加是更新操作还是插入操作
		 * */
		checkMac(keyword);
		jdbc.getConnection();
		boolean flag=false;
		String check_sql="select id from singlegw_rules_keyword where simid = '"+keyword.getSimid()+"'";
		ResultSet result_ruleid= jdbc.query(check_sql);
		try {
			if(result_ruleid.next()){
				/*UPDATE users SET age = 24, name = 'Mike' WHERE id = 123;*/
				String sql_up="";
				if(null!=keyword.getMacid()&&!"".equals(keyword.getMacid())){
				 sql_up = "update  singlegw_rules_keyword Set rule = '"+keyword.getRule()+
							"',rule_name = '"+keyword.getRule_name()+"',macid = '"+keyword.getMacid()+
							"',update_time = '"+keyword.getUpdata_time()+
			                "'where id = '"+result_ruleid.getString("id")+"';";
				}else{
				 sql_up = "update  singlegw_rules_keyword Set rule = '"+keyword.getRule()+"',rule_name = '"+keyword.getRule_name()+"',update_time = '"+keyword.getUpdata_time()+
                "'where id = '"+result_ruleid.getString("id")+"';";
				}
				System.out.println(sql_up);
				flag=jdbc.execute(sql_up);
			}else{
				String sql_in = "insert into singlegw_rules_keyword(simid,rule,rule_name,status,update_time,macid)values('"
		                + keyword.getSimid() + "','" + keyword.getRule()+ "','" +keyword.getRule_name()+
		                "','"+keyword.getStatus()+"','"+keyword.getUpdata_time()+"','"+keyword.getMacid()+"');";
				System.out.println(sql_in);
				flag=jdbc.execute(sql_in);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jdbc.closeconn();
		if(flag){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean addRuleMulit(MulitRule keyword){
		String sql = "insert into multigw_filter_rules(rule,rule_name,status,update_time,update_flag )values('"
                + keyword.getRule() + "','" + keyword.getRule_name()+ "','" +keyword.getStatus()+"','"
				+ keyword.getUpdata_time()+"','"+keyword.getUpdata_flag()+"');";
		System.out.println(sql);
		boolean flag=jdbc.execute(sql);
		if(flag){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteRuleip(String iphost,String simid){
		boolean end_flag=false;
		String sql_delip = "delete from singlegw_rules_iphost where id = '"+iphost+"'";
		if(null!=simid&&!"".equals(simid)){
			sql_delip=sql_delip+"and simid = '"+simid+"'";
		}
		System.out.println(sql_delip);
		end_flag=jdbc.execute(sql_delip);
		if(null!=simid&&!"".equals(simid)){
			String sql_checkip = "select *  from singlegw_rules_iphost where simid = '"+simid+"'";
			ResultSet result_ip=jdbc.query(sql_checkip);
			try {
				if(result_ip.next()){
				}
				else{
					deleteRulekeywordBysim(simid);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return end_flag;
		/*检测该条是不是最后一个，如果是烧掉*/
		
	}
	
	public boolean deleteRulekeywordBysim(String simid){
		String sql = "delete from singlegw_rules_keyword where simid = '"+simid+"'";
		System.out.println(sql);
		return jdbc.execute(sql);
	}
	
	public boolean deleteRulekeywordByid(String keyword,String simid){
		String sql = "delete from singlegw_rules_keyword where id = '"+keyword+"'";
		System.out.println(sql);
		return jdbc.execute(sql);
	}
	
	public boolean deleteRulemulti(String ruleid){
		String sql = "delete from multigw_filter_rules where rule_id = '"+ruleid+"'";
		System.out.println(sql);
		return jdbc.execute(sql);
	}
	
	
	/*public void updateRuleip(Rule4IpHost iphost){
		String sql = "delete from singlegw_rules_iphost where id="+iphost.getId();
		System.out.println(sql);
		jdbc.execute(sql);
	}*/
	public boolean updata_rulekey(Rule4Keyword keyword){
		jdbc.getConnection();
		boolean flag=false;
		String sql="";
		if(null!=keyword.getMacid()&&!"".equals(keyword.getMacid())){
			sql = "update singlegw_rules_keyword set rule = '"+keyword.getRule()+
					"',rule_name = '"+keyword.getRule_name()+"',macid = '"+keyword.getMacid()+
					"',update_time = '"+keyword.getUpdata_time()+
	                "' where simid = '"+keyword.getSimid()+"'";
			}else{
				sql = "update singlegw_rules_keyword set rule = '"+keyword.getRule()+
						"',rule_name = '"+keyword.getRule_name()+
						"',update_time = '"+keyword.getUpdata_time()+
		                "' where simid = '"+keyword.getSimid()+"'";
			}
		System.out.println(sql);
		flag=jdbc.execute(sql);
		jdbc.closeconn();
		return flag;
	}
	
	
	public boolean updata_multi(MulitRule mulit) {
		// TODO Auto-generated method stub
		/*multigw_filter_rules*/
		jdbc.getConnection();
		boolean flag=false;
		String sql = "update multigw_filter_rules set rule='"+mulit.getRule()+
				"',rule_name = '"+mulit.getRule_name()+"',update_time = '"+mulit.getUpdata_time()+
                "' where rule_id = '"+mulit.getRule_id()+"';";
		System.out.println(sql);
		flag=jdbc.execute(sql);
		jdbc.closeconn();
		return flag;
	}
	
	public JSONArray getRule(String simid,String mac){
		/**最终格式 
		 * 单机：
		 * [{"rule":"@text(袭击|反恐|暗杀)",
			"ruleid":"10001",
			"iplist":["101.159.89.211","101.159.89.211","101.159.89.211"],
			"hostlist":["k.youku.com1","k.youku.com2","k.youku.com3"],
			}]
		  *多机：
		  *[{"rule":"@text(袭击|反恐|暗杀)","ruleid":"10001"}
		  * {"rule":"@text(袭击|反恐|暗杀)","ruleid":"10001"}
		  * {"rule":"@text(袭击|反恐|暗杀)","ruleid":"10001"}]
		  */
		jdbc.getConnection();
		String sql_key = "";
		if(null!=simid&&!"".equals(simid)){
			sql_key = "select * from  singlegw_rules_keyword where simid='"+simid+"'";
		}else if(null!=mac&&!"".equals(mac)){
			sql_key = "select * from  singlegw_rules_keyword where macid='"+mac+"'";
		}
		System.out.println(sql_key);
		ResultSet result_key=jdbc.query(sql_key);
		JSONArray rulejsonarray=new JSONArray();
		try {
			if(result_key.next()){
				JSONObject rulejson= new JSONObject();
				Ruledomain ruletemp=new Ruledomain();
				String simtemp=result_key.getString("simid");
				List<String> iplist_temp=new ArrayList<String>();
				List<String> hostlist_temp=new ArrayList<String>();
				
				ruletemp.setRule(result_key.getString("rule"));
				ruletemp.setRule_name(result_key.getString("rule_name"));
				ruletemp.setRuleid(result_key.getString("id"));
				
				String sql_iphost = "select * from  singlegw_rules_iphost where simid='"+simtemp+"'";
				System.out.println(sql_iphost);
				ResultSet result_iphost=jdbc.query(sql_iphost);
				
				while(result_iphost.next()){
					String iptemp=result_iphost.getString("ip");
					String hosttemp=result_iphost.getString("host");
					if(null!=iptemp&&!"".equals(iptemp)){
						iplist_temp.add(iptemp);
					}
					if(null!=hosttemp&&!"".equals(hosttemp)){
						hostlist_temp.add(hosttemp);
					}
				}
				try {
					/*rulejson.put("rule", ruletemp.getRule());*/
					/*@text(地方 风格和) @text(是的|的方法) -@text(水电费) 
					 * @text(天安门|长安街|紫禁城)
					 * */
					String ruletext=ruletemp.getRule();
					//String rule_hex_str = ruleutil.ruleEncrypt(ruletext);
				/* * 这是逗号分隔
				 **/ 	
//					String rulett = ruleutil.rule4Test(ruletext);
//					System.out.println(rulett);
//					rulejson.put("rule", rulett);
					rulejson.put("rule", ruletext);
					rulejson.put("ruleid", "sg"+ruletemp.getRuleid());
//					rulejson.put("rule_name", ruletemp.getRule_name());
					rulejson.put("iplist", iplist_temp);
					rulejson.put("hostlist", hostlist_temp);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rulejsonarray.put(rulejson);
			}else{
				String sql_multi = "select * from  multigw_filter_rules";
				System.out.println(sql_multi);
				ResultSet result_multi=jdbc.query(sql_multi);
				while(result_multi.next()){
					Ruledomain ruletemp=new Ruledomain();
					JSONObject rulejson= new JSONObject();
					ruletemp.setRule(result_multi.getString("rule"));
					ruletemp.setRuleid(result_multi.getString("rule_id"));
					ruletemp.setRule_name(result_multi.getString("rule_name"));
					try {
						String ruletext=ruletemp.getRule();
						//String rule_hex_str = ruleutil.ruleEncrypt(ruletext);
//						String rulett = ruleutil.rule4Test(ruletext);
//						System.out.println(rulett);
//						rulejson.put("rule", rulett);
						rulejson.put("rule", ruletext);
						rulejson.put("ruleid", ruletemp.getRuleid());
//						rulejson.put("rule_name", ruletemp.getRule_name());
						rulejsonarray.put(rulejson);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jdbc.closeconn();
		return rulejsonarray;
		
	}
	
	public String getRulenameById(String ruleid){
		String sql_key = "select rule_name from singlegw_rules_keyword where id = '"+ruleid+"'";
		System.out.println(sql_key);
		ResultSet result_key=jdbc.query(sql_key);
		String rulename="";
		try {
			if(result_key.next()){
				rulename=result_key.getString("rule_name");
			}else{
				String sql_multi = "select rule_name from multigw_filter_rules where rule_id  = '"+ruleid+"'";
				System.out.println(sql_multi);
				ResultSet result_multi=jdbc.query(sql_multi);
				if(result_multi.next()){
					rulename=result_multi.getString("rule_name");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rulename;
	}
	public List<Ruledomain> getRuleList4Single(String sim ,String rulename, String host, String ip) {
		
		String sql_key = "select * from  singlegw_rules_keyword";
		List<Ruledomain> rulelist=new ArrayList<Ruledomain>();
		if(null!=sim&&!"".equals(sim)){
			sql_key=sql_key+" where simid = '"+sim+"'";
			
			if(null!=rulename&&!"".equals(rulename)){
				
				sql_key=sql_key+" and rule_name like '%"+rulename+"%'";
				
			}
			
		}
		else if(null!=rulename&&!"".equals(rulename)){
			
			sql_key=sql_key+" where rule_name like '%"+rulename+"%'";
		}
		
		sql_key=sql_key+";";
		
		rulelist=queryFromKeyiphost(sql_key,ip,host);
		
		return rulelist;
	}
	public List<Ruledomain> queryFromKeyiphost(String sql_key,String ip,String host) {
		
		List<Ruledomain> rulelist=new ArrayList<Ruledomain>();
		jdbc.getConnection();
		System.out.println(sql_key);
		ResultSet result_keyword=jdbc.query(sql_key);
		try {
			while(result_keyword.next()){
				JSONArray ruleja=ruleutil.splitRules(result_keyword.getString("rule"));
				String ruletext="";
				String ruletextcomma="";
				 for (int i = 0; i < ruleja.length(); i++) {
			            JSONObject jo;
						try {
							jo = (JSONObject) ruleja.get(i);
							ruletext= ruletext+jo.getString("rule")+":  "+jo.getString("word")+" ";
							ruletextcomma= ruletextcomma+jo.getString("rule")+":  "+jo.getString("word")+",";
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
			     }
				 ruletextcomma.substring(0,ruletextcomma.length()-2);
				 String simid=result_keyword.getString("simid");
				 String macid=result_keyword.getString("macid");
				 String sql_iphost ="";
				 if(null!=macid&&!"".equals(macid)){
					 sql_iphost = "select * from  singlegw_rules_iphost where simid='"+simid+"'and macid = '"+macid+" '";
				 }else{
					 sql_iphost = "select * from  singlegw_rules_iphost where simid='"+simid+"'";
				 }
				 
				 if(null!=host&&!"".equals(host)){
						sql_key=sql_iphost+" and host = '"+host+"'";
				 }
				 if(null!=ip&&!"".equals(ip)){
					 sql_iphost=sql_iphost+" and ip = '"+ip+"'";
				 }
				  
				 sql_iphost=sql_iphost+"order by insert_time DESC";
				 System.out.println(sql_iphost);
				 ResultSet result_iphost=jdbc.query(sql_iphost);
				if(result_iphost.next()){
					Ruledomain keyword=new Ruledomain();
					keyword.setRule(ruletext);
					keyword.setRulecomma(ruletextcomma);
					keyword.setRule_name(result_keyword.getString("rule_name"));
					keyword.setMacid(result_keyword.getString("macid"));
					keyword.setId("iphost_"+result_iphost.getString("id"));
					keyword.setSimid(simid);
					keyword.setIp(result_iphost.getString("ip"));
					keyword.setHost(result_iphost.getString("host"));
					rulelist.add(keyword);
					while(result_iphost.next()){
						Ruledomain keyword2=new Ruledomain();
						keyword2.setRule(ruletext);
						keyword2.setRulecomma(ruletextcomma);
						keyword2.setRule_name(result_keyword.getString("rule_name"));
						keyword2.setMacid(result_keyword.getString("macid"));
						keyword2.setId("iphost_"+result_iphost.getString("id"));
						keyword2.setSimid(simid);
						keyword2.setIp(result_iphost.getString("ip"));
						keyword2.setHost(result_iphost.getString("host"));
						rulelist.add(keyword2);
					}
				}
				/**这种情况要考虑如果sim卡只配了规则没有配ip以及host
				 * 
				**/else if(("".equals(host)&&"".equals(ip))||null==ip&&null==host){
					Ruledomain keyword=new Ruledomain();
					keyword.setRule(ruletext);
					keyword.setRule_name(result_keyword.getString("rule_name"));
					keyword.setId("keyword_"+result_keyword.getString("id"));
					keyword.setIp("");
					keyword.setHost("");
					keyword.setSimid(simid);
					rulelist.add(keyword);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		jdbc.closeconn();
		return rulelist;
	}
	public List<Ruledomain> getRuleList4Multi(String rulename) {
		// TODO Auto-generated method stub
		String sql_multi = "select * from  multigw_filter_rules";
		List<Ruledomain> rulelist=new ArrayList<Ruledomain>();
		if(null!=rulename&&!"".equals(rulename)){
			sql_multi=sql_multi+" where rule_name like '%"+rulename+"%'";
		}
//		sql_multi=sql_multi+"order by update_time DESC";
		jdbc.getConnection();	
		System.out.println(sql_multi);
		ResultSet result_multi=jdbc.query(sql_multi);
		try {	
			while(result_multi.next())
			{
			Ruledomain rule=new Ruledomain();
			rule.setId("multi_"+result_multi.getString("rule_id"));
			JSONArray ruleja=ruleutil.splitRules(result_multi.getString("rule"));
			String ruletext="";
			String ruletextcomma="";
			 for (int i = 0; i < ruleja.length(); i++) {
		            JSONObject jo;
					try {
						jo = (JSONObject) ruleja.get(i);
						ruletext= ruletext+jo.getString("rule")+":  "+jo.getString("word")+" ";
						ruletextcomma= ruletextcomma+jo.getString("rule")+":  "+jo.getString("word")+",";
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
		     }
			ruletextcomma.substring(0,ruletextcomma.length()-2);
			rule.setRule(ruletext);
			rule.setRulecomma(ruletextcomma);
			rule.setRule_name(result_multi.getString("rule_name"));
			rulelist.add(rule);
			} 
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		jdbc.closeconn();
		return rulelist;
	}
	public boolean saveSingleRule(Rule4IpHost iphost11, Rule4Keyword rulekey,String ruleflag) {
		// TODO Auto-generated method stub
		boolean ipflag=	addRuleip(iphost11);
		boolean keyflag=true;
		if("lam".equals(ruleflag)){
			keyflag=addRulekeyword(rulekey);
		}
		boolean flag=false;
		if(ipflag&&keyflag){
			flag=true;
		}
		
		return flag;
	}
	
	public boolean checkMac(Rule4Keyword keyword){
		/**更换mac地址*/
		boolean flag=true;
		if(null!=keyword.getMacid()&&!"".equals(keyword.getMacid())){
			jdbc.getConnection();
			System.out.println("begin to check mac is employed or not");
			String check_sql="select simid from singlegw_rules_keyword where macid = '"+keyword.getMacid()+"'";
			ResultSet result_ruleid= jdbc.query(check_sql);
			try {
				if(result_ruleid.next()){
					String oldsim=result_ruleid.getString("simid");
					if(!oldsim.equals(keyword.getSimid())){
						/*UPDATE users SET age = 24, name = 'Mike' WHERE id = 123;*/
						String sql_up = "update  singlegw_rules_keyword Set macid = '' where simid = '"+oldsim+"';";
						System.out.println(sql_up);
						flag=jdbc.execute(sql_up);
						
						String sql_upiphost = "update  singlegw_rules_iphost Set macid = '' where simid = '"+oldsim+"';";
						System.out.println(sql_upiphost);
						flag=jdbc.execute(sql_upiphost);
						
					}
				}else{
					System.out.println("mac is not employed,Continue configuration");
				}
				/*if(flag){
					String sql_new = "insert into singlegw_rules_keyword(simid,rule,rule_name,status,update_time,macid)values('"
			                + keyword.getSimid() + "','" + keyword.getRule()+ "','" +keyword.getRule_name()+
			                "','"+keyword.getStatus()+"','"+keyword.getUpdata_time()+"','"+keyword.getMacid()+"');";
					System.out.println(sql_new);
					flag=jdbc.execute(sql_new);
					System.out.println("new mac configure is ok");
				}*/
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jdbc.closeconn();
		}
		if(flag){
			return true;
		}else{
			return false;
		}
	}
	public boolean saveMulitRule(MulitRule mulit) {
		// TODO Auto-generated method stub
		jdbc.getConnection();
		boolean flag=false;
		flag=addRuleMulit(mulit);
		jdbc.closeconn();
		return flag;
	}
	public boolean deleteRule(String ruleid,String simid) {
		// TODO Auto-generated method stub
		jdbc.getConnection();
		String[] idarry=ruleid.split("_");
		String ruleflag=idarry[0];
		String ru_id=idarry[1];
		boolean flag= false;
		/*iphost_   keyword_*/
		if("iphost".equals(ruleflag)){
			flag=deleteRuleip(ru_id,simid);
		}else if("keyword".equals(ruleflag)){
			flag=deleteRulekeywordByid(ru_id,simid);
		}else if("multi".equals(ruleflag)){
			flag=deleteRulemulti(ru_id);
		}
		jdbc.closeconn();
		return flag;
	}
	public boolean checkRuleName(String rulename) {
		// TODO Auto-generated method stub
		String sql ="select * from multigw_filter_rules where rule_name = '"+rulename+"'";
		jdbc.getConnection();	
		boolean flag=true;//默认时可用的
		System.out.println(sql);
		ResultSet multi_rulename=jdbc.query(sql);
		try {	
			while(multi_rulename.next())
			{
				flag=false;
			}
		}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		jdbc.closeconn();
		return flag;
	}
/*	public boolean updata_rulekey(Rule4Keyword rulekey) {
		// TODO Auto-generated method stub
		return false;
	}*/
	public List getRuleIdByName(String rulecontent) {
		// TODO Auto-generated method stub
		List<String> idlst=new ArrayList<String>();
		String mu_sql ="select rule_id  from multigw_filter_rules where rule_name like '%"+rulecontent+"%'";
		String single_sql ="select id from singlegw_rules_keyword where rule_name like '%"+rulecontent+"%'";
		jdbc.getConnection();	
		System.out.println(mu_sql);
		System.out.println(single_sql);
		ResultSet multi_ruleid=jdbc.query(mu_sql);
		ResultSet single_ruleid=jdbc.query(single_sql);
		try {	
			while(multi_ruleid.next())
			{
				idlst.add(multi_ruleid.getString("rule_id"));
			}
			while(single_ruleid.next())
			{
				idlst.add(single_ruleid.getString("id"));
			}
		}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		jdbc.closeconn();
		
		return idlst;
	}
}