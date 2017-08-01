package indi.ninet.amplifierprove.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wfzhou create in 2017/06/20 群组放大器任务信息
 *
 */
public class GroupAmplifierTaskInf {
	// 请求开始时间
	private String start_time;
	// 请求次数
	private int request_degree;
	// 请求的时间间隔,单位为h
	private int space_time;
	// 用户id
	private String userid;
	// 单一放大器任务数组
	private List<SingleAmplifierTaskInf> amplifiertaskinfs = new ArrayList<SingleAmplifierTaskInf>();

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public int getRequest_degree() {
		return request_degree;
	}

	public void setRequest_degree(int request_degree) {
		this.request_degree = request_degree;
	}

	public int getSpace_time() {
		return space_time;
	}

	public void setSpace_time(int space_time) {
		this.space_time = space_time;
	}

	public String getUserid() {
		return userid;
	}

	public void setUser_id(String userid) {
		this.userid = userid;
	}

	public List<SingleAmplifierTaskInf> getAmplifiertaskinfs() {
		return amplifiertaskinfs;
	}

	public void setAmplifiertaskinfs(
			List<SingleAmplifierTaskInf> amplifiertaskinfs) {
		this.amplifiertaskinfs = amplifiertaskinfs;
	}

}
