/**
* @since 2014-9-15 下午8:26:21
* @author hanyao
*/
package controllers;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RuleUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RuleUtil util=new RuleUtil();
//		JSONArray ja = util.splitRules("@text(安徽 禽流感 H7N9) @text(袭击|反恐|暗杀) -@text(天安门 长安街)");
		JSONArray ja = util.splitRules("@text(你好) @text(我好) @text(大家好)");//这种语法是不对的
//		JSONArray ja = util.splitRules("@text(地方 风格和) @text(是的|的方法) -@text(水电费)");
		
		System.out.println(ja);
	}

	// 将组好的规则拆分成配置的值 @title(安徽 禽流感) @text(安徽 禽流感 H7N9)
	public JSONArray splitRules(String rules) {
		JSONObject result = new JSONObject();
		JSONArray newArr = new JSONArray();
		
		String r = rules;
		int s = 0;
		int e = -1;
		try {
			if (r.indexOf(")") < r.length() - 1) {// 判断组间关系是AND还是OR 0=AND 1=OR
				String split = r.substring(r.indexOf(")") + 1, r.indexOf(")") + 2);
				if (split.equals("|")) {
					result.put("rela", 1);
				} else {
					result.put("rela", 0);
				}
			} else {
				result.put("rela", 0);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = e; i < r.length() - 1; i++) {
			JSONObject newRule = new JSONObject();
			s = e + 1;
			e = r.indexOf(")", s);
			if (e == -1 || e >= r.length()) {
				break;
			}
			String rule = r.substring(s, e + 1);
			if ("|".equals(rule.substring(0, 1)) || " ".equals(rule.substring(0, 1))) {
				rule = rule.substring(1);
			}
			
			try {
				String word = rule.substring(rule.indexOf("(") + 1, rule.lastIndexOf(")"));
				String type = rule.substring(rule.indexOf("@") + 1, rule.indexOf("("));
				if (rule.substring(0, 1).equals("-")) {
					newRule.put("rule", "NOT");
				} else if (rule.substring(0, 1).equals("@")) {
					if (word.contains("|")) {
						newRule.put("rule", "OR");
						word = word.replace("|", " ");
					} else {
						newRule.put("rule", "AND");
					}
				}
					newRule.put("type", type);
					newRule.put("word", word);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			newArr.put(newRule);
		}
		return newArr;
	}
	
	
	public String formatRuleold(String keyword) {
		String ruletext="";
		System.out.println(keyword);
		if(null!=keyword && !"".equals(keyword)){
			try {
				JSONObject aaa = new JSONObject(keyword);
				/*@text(袭击|反恐|暗杀)
				 *@text((袭击|反恐|暗杀) (你好 中国) -(河南))
				 * */
				/*{"addkeyword":"你好，呵呵","orkeyword":"打发","nokeyword":"辅导费"}*/
				String addkey = aaa.optString("addkeyword","");
				String orkey = aaa.optString("orkeyword","");
				String nokey = aaa.optString("nokeyword","");
				String addtemp="";
				String ortemp="";
				String notemp ="";
				String addstr[]=null;
				String orstr[]=null;
				String nostr[]=null;
//				int flag = 0;
				
				if(!"".equals(addkey)){
					if(addkey.contains("，")){
						addstr=addkey.split("，");
					}else{
						addstr=addkey.split(",");
					}
				}
				if(!"".equals(orkey)){
					if(orkey.contains("，")){
						orstr=orkey.split("，");
					}else{
						orstr=orkey.split(",");
					}
				}
				if(!"".equals(nokey)){
					if(nokey.contains("，")){
						nostr=nokey.split("，");
					}else{
						nostr=nokey.split(",");
					}
				}
				if(null!=addstr&&!"".equals(addstr)&&addstr.length>0){
//					flag=flag+1;
					for(int i=0;i<addstr.length;i++){
						addtemp = addtemp + addstr[i] +"\u0020";
					}
					addtemp=addtemp.substring(0,addtemp.length()-1);
					addtemp="@text("+addtemp+") ";
				}
				if(null!=orstr&&!"".equals(orstr)&&orstr.length>0){
//					flag=flag+1;
					for(int i=0;i<orstr.length;i++){
						ortemp = ortemp + orstr[i] +"|";
					}
					ortemp=ortemp.substring(0,ortemp.length()-1);
					ortemp="@text("+ortemp+") ";
				}
				if(null!=nostr&&!"".equals(nostr)&&nostr.length>0){
//					flag=flag+1;
					for(int i=0;i<nostr.length;i++){
						notemp = notemp + nostr[i] +"\u0020";
					}
					notemp=notemp.substring(0,notemp.length()-1);
					notemp="-@text("+notemp+")";
				}
				ruletext=addtemp+ortemp+notemp;
//				if(flag>1){
//					ruletext="@text("+addtemp+ortemp+notemp+")";
//				}else{
//					ruletext="@text"+addtemp+ortemp+notemp;
//				}
						System.out.println(ruletext);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ruletext;
	}
	public String formatRule(String keyword) {
		String ruleresult="";
	/*	
	 * {'innerrel':"or",'keytext':"OR:风格和,日历_OR:风格和_AND:电饭锅_OR:风格和_NOT:风格和"} 
	 * NOT:从v锦湖_OR:吧就分隔_NOT:规划_AND:徐斌
	 * 	NOT:从v锦湖_OR
	 * */
		if(null!=keyword && !"".equals(keyword)){
			/*@text(袭击|反恐|暗杀)
			 *@text((袭击|反恐|暗杀) (你好 中国) -(河南))
			 * */
			try {
				JSONObject aaa = new JSONObject(keyword);
				String innnertype = aaa.optString("innerrel");
				String keytext = aaa.optString("keytext","");
				String rulearray[]=null;
				if(!"".equals(keytext)){
					rulearray=keytext.split("_");
				}
			
				if("or".equalsIgnoreCase(innnertype)){
					for(int i=0;i<rulearray.length;i++){
						String rule_1=format1rule(rulearray[i]);
						ruleresult=ruleresult+rule_1+"|";
					}
				}else{
					for(int i=0;i<rulearray.length;i++){
						String rule_1=format1rule(rulearray[i]);
						ruleresult=ruleresult+rule_1+"\u0020";
					}
				}
				ruleresult=ruleresult.substring(0, ruleresult.length()-1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ruleresult;
	}

	public String format1rule(String ruletext) {
		String ruletemp[]=ruletext.split(":");
		String resulttemp="";
		String[] rule_word=null;
		if(ruletemp[1].contains("，")){
			ruletemp[1]=ruletemp[1].replace("，",",");
		}
		rule_word=ruletemp[1].split(",");
		if("or".equalsIgnoreCase(ruletemp[0])){
			for(int j=0;j<rule_word.length;j++){
				resulttemp = resulttemp + rule_word[j] +"|";
			}
		}else{
			for(int j=0;j<rule_word.length;j++){
				resulttemp = resulttemp + rule_word[j] +"\u0020";
			}
		}
		resulttemp=resulttemp.substring(0, resulttemp.length()-1);
		if("not".equalsIgnoreCase(ruletemp[0])){
			resulttemp="-@text("+resulttemp+")";
		}else{
			resulttemp="@text("+resulttemp+")";
		}
	
		return resulttemp;
	}
	
	public String rule4Test(String ruletext)
			throws JSONException {
		/*	
		 * 这种方法太笨
		 * ruletext=ruletext.replace("@text","");
			ruletext=ruletext.replace("(","");
			ruletext=ruletext.replace(")","");
			ruletext=ruletext.trim();
			if(ruletext.contains("|")){
				//ruletext.replace("|", ",");
				String  str[] = ruletext.split("\\|");
				for(int i=0;i<str.length;i++){
					rulett=rulett+str[i]+",";
				}
				rulett=rulett.substring(0, rulett.length()-1);
			}else if(ruletext.indexOf(" ")!=-1){
				//ruletext.replace(" ", ",");
				String  str[] = ruletext.split(" ");
				for(int i=0;i<str.length;i++){
					rulett=rulett+str[i]+",";
				}
				rulett=rulett.substring(0, rulett.length()-1);
			}*/
		String rulett="";
		JSONArray ja=splitRules(ruletext);
		for(int i=0;i<ja.length();i++){
			String singlerule="";
			
			singlerule=ja.getJSONObject(i).getString("word");
			String rulestr[]=singlerule.split("\\s");
			for(int j=0;j<rulestr.length;j++){
				rulett=rulett+rulestr[j]+",";
			}
		}
		if(rulett.length()>1){
			rulett=rulett.substring(0, rulett.length()-1);
		}
		return rulett;
	}

	public String ruleEncrypt(String plain){
		String outString="";
		AES aes = new AES();
		byte[] key = null;
		byte[] iv  = null;
		try {
			key = "0123456789abcdef".getBytes("UTF-8");
			iv= "fedcba9876543210".getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		aes.init(key, iv);
		
		byte[] indata;
		byte[] outdata = null;
		try {
			indata = plain.getBytes("UTF-8");
			outdata = aes.encrypt(indata);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		outString = Base16.encode(outdata);
		
		return outString;
	}
	
	
	
	
	
}
