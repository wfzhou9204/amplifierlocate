package indi.ninet.amplifierprove.domain;

/**
 * 
 * @author wfzhou create in 2017/06/20 单一放大器定位信息
 *
 */
public class SingleAmplifierTaskInf {
	// 用户id
	private String userid;
	// 放大器IP
	private String ip;
	// 放大器端口
	private int port;
	// 参数数目
	private int par_num;
	// 参数值,多个参数值以","分割
	private String pars;

	public String getUserid() {
		return userid;
	}

	public void setUser_id(String userid) {
		this.userid = userid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPar_num() {
		return par_num;
	}

	public void setPar_num(int par_num) {
		this.par_num = par_num;
	}

	public String getPars() {
		return pars;
	}

	public void setPars(String pars) {
		this.pars = pars;
	}
}
