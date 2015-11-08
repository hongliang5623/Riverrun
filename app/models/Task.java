package models;

public class Task {

	public String time;
	public String srcip;
	//public int srcport;
	public String destip;
	//public int destport;
	public String host;
	
	public String sim;
	public String method;
	public boolean ispost;
	public String inserttime;
	public String keyword;
	public String hitdata;
	public String url;
	
	public Task()
	{
		method = "";
		ispost = false;
		sim = "";
		time = "";
		srcip= "";
	//	srcport=0;
		destip="";
	//	destport=0;
		host="";
		inserttime="";
		keyword="";
		hitdata="";
		url="";
	}

}
