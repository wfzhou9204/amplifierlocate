package indi.ninet.amplifierprove.domain;

/**
 * 
 * @author wfzhou create in 2017/06/22 作业信息
 *
 */
public class JobInf {
	// 作业ID
	private long job_id;
	// 作业开始时间
	private long start_time;
	// 作业结束时间
	private long end_time;
	/**
	 * 作业状态 0:待执行 1:正在执行 2:已完成 3:撤销 4:异常终止
	 */
	private int job_state;
	// 基本协议IP数
	private int basicprotocol_num;
	// 非基本协议IP数
	private int no_basicprotocol_num;
	// 用户ID
	private String userid;

	public long getJob_id() {
		return job_id;
	}

	public void setJob_id(long job_id) {
		this.job_id = job_id;
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

	public int getJob_state() {
		return job_state;
	}

	public void setJob_state(int job_state) {
		this.job_state = job_state;
	}

	public int getBasicprotocol_num() {
		return basicprotocol_num;
	}

	public void setBasicprotocol_num(int basicprotocol_num) {
		this.basicprotocol_num = basicprotocol_num;
	}

	public int getNo_basicprotocol_num() {
		return no_basicprotocol_num;
	}

	public void setNo_basicprotocol_num(int no_basicprotocol_num) {
		this.no_basicprotocol_num = no_basicprotocol_num;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
