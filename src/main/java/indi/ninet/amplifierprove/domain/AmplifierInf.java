package indi.ninet.amplifierprove.domain;

import indi.ninet.amplifierprove.util.IpUtil;

/**
 * 
 * @author wfzhou 2017/05/01 一次定位中放大器的相关信息 modify 2017/06/03 add res_num
 */
public class AmplifierInf {
	private long amplifier_id;
	private long job_id;// 作业id
	private long ip;
	private String location;
	private int port;
	private String userid;
	private String parameters;
	private long start_time;
	private long end_time;
	private int req_len;
	private int res_num;// 回复报文数量
	private int res_len;
	private double baf_value;
	private String des_inf;

	public AmplifierInf() {

	}

	public long getAmplifier_id() {
		return amplifier_id;
	}

	public void setAmplifier_id(long amplifier_id) {
		this.amplifier_id = amplifier_id;
	}

	public long getJob_id() {
		return job_id;
	}

	public void setJob_id(long job_id) {
		this.job_id = job_id;
	}

	public long getIp() {
		return ip;
	}

	public void setIp(long ip) {
		this.ip = ip;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public int getReq_len() {
		return req_len;
	}

	public void setReq_len(int req_len) {
		this.req_len = req_len;
	}

	public int getRes_num() {
		return res_num;
	}

	public void setRes_num(int res_num) {
		this.res_num = res_num;
	}

	public int getRes_len() {
		return res_len;
	}

	public void setRes_len(int res_len) {
		this.res_len = res_len;
	}

	public double getBaf_value() {
		return baf_value;
	}

	public void setBaf_value(double baf_value) {
		this.baf_value = baf_value;
	}

	public String getDes_inf() {
		return des_inf;
	}

	public void setDes_inf(String des_inf) {
		this.des_inf = des_inf;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(IpUtil.longtoIp(ip) + "  ");
		str.append(port + "  ");
		str.append(parameters + "  ");
		str.append(start_time + "  ");
		str.append(end_time + "  ");
		str.append(req_len + "   ");
		str.append(res_len + "   ");
		str.append(res_num+"  ");
		str.append(baf_value + "  ");
		str.append(des_inf + "  ");
		return str.toString();

	}
}
