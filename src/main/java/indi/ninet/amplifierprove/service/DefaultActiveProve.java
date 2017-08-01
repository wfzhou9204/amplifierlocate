package indi.ninet.amplifierprove.service;

/**
 * 
 * @author wfzhou 2017/05/01 默认放大器(无参数,返回报文无应用层控制信息)定位,包括19、123、161、1900端口
 *
 */
public class DefaultActiveProve extends AbstractActiveProve {
	public DefaultActiveProve() {
	}

	public DefaultActiveProve(String ip, int port) {
		super(ip, port);
	}
}
