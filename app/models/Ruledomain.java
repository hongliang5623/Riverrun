package models;

import java.util.Date;
import java.util.List;

public class Ruledomain {

	private String id;
	private String ruleid;
	private String macid;
	private String rule;
	private String rulecomma;
	private String rule_name;
	private String simid;
	private List<String> iplist;
	private List<String> hostlist;
	private String ip;
	private String host;
	private String proto;
	/**调接口的时候用两个list,规则查看的时候就不用
	 * 可用作单机或多机
	 * */
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	public List<String> getIplist() {
		return iplist;
	}
	public void setIplist(List<String> iplist) {
		this.iplist = iplist;
	}
	public List<String> getHostlist() {
		return hostlist;
	}
	public void setHostlist(List<String> hostlist) {
		this.hostlist = hostlist;
	}
	public String getRuleid() {
		return ruleid;
	}
	public void setRuleid(String ruleid) {
		this.ruleid = ruleid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSimid() {
		return simid;
	}
	public void setSimid(String simid) {
		this.simid = simid;
	}
	public String getMacid() {
		return macid;
	}
	public void setMacid(String macid) {
		this.macid = macid;
	}
	public String getRulecomma() {
		return rulecomma;
	}
	public void setRulecomma(String rulecomma) {
		this.rulecomma = rulecomma;
	}
	public String getProto() {
		return proto;
	}
	public void setProto(String proto) {
		this.proto = proto;
	}
}
