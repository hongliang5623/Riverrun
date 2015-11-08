package models;

import java.util.Date;

public class MulitRule {

	private int rule_id;
	private String rule;
	private String rule_name;
	private int status;
	private String updata_time;
	private int updata_flag;
	
	public int getRule_id() {
		return rule_id;
	}
	public void setRule_id(int rule_id) {
		this.rule_id = rule_id;
	}
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUpdata_flag() {
		return updata_flag;
	}
	public void setUpdata_flag(int updata_flag) {
		this.updata_flag = updata_flag;
	}
	public String getUpdata_time() {
		return updata_time;
	}
	public void setUpdata_time(String updata_time) {
		this.updata_time = updata_time;
	}
}
