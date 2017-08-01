package indi.ninet.amplifierprove.domain;
/**
 * 
 * @author wfzhou
 * create 2017/06/03
 * 定位放大器类型信息
 */
public class PortInf {
	//端口
	private int port;
	//参数依赖信息,par_rely=0,无参数依赖;par_rely>0,参数依赖的个数
	private int par_rely;
	//参数描述信息
	private String par_describe;
	//定位检测程序主类的完整包名
	private String pro_class;
	//该端口对应的基础检测报文
	private String packet;
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getPar_rely() {
		return par_rely;
	}
	public void setPar_rely(int par_rely) {
		this.par_rely = par_rely;
	}
	public String getPar_describe() {
		return par_describe;
	}
	public void setPar_describe(String par_describe) {
		this.par_describe = par_describe;
	}
	public String getPro_class() {
		return pro_class;
	}
	public void setPro_class(String pro_class) {
		this.pro_class = pro_class;
	}
	public String getPacket() {
		return packet;
	}
	public void setPacket(String packet) {
		this.packet = packet;
	}
	@Override
	public String toString() {
		StringBuilder str=new StringBuilder();
		str.append(port+"  ");
		str.append(par_rely+"  ");
		str.append(par_describe+"  ");
		str.append(pro_class+"  ");
		str.append(packet+"  ");
		return str.toString();
	}
	
}
