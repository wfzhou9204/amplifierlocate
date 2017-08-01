package indi.ninet.amplifierprove.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import indi.ninet.amplifierprove.domain.AmplifierInf;
import indi.ninet.amplifierprove.domain.PortInf;
import indi.ninet.amplifierprove.util.GetBean;
import indi.ninet.amplifierprove.util.IpUtil;
import indi.ninet.amplifierprove.util.SystemConfigure;

/**
 * 
 * @author wfzhou 2017/05/01 放大器定位抽象类，定义模板方法 modify 2017/06/10
 *         端口信息来自数据库、设置缓存更新、增加回复报文个数字段
 *
 */
public abstract class AbstractActiveProve implements ActiveProve {
	protected String ip;
	protected int port;
	protected byte[] requestPacket;
	protected byte[] responsePacket;
	protected AmplifierInf amplifierInf;

	public AbstractActiveProve() {
		amplifierInf = (AmplifierInf) GetBean.getBean("amplifierInf");
	}

	public AbstractActiveProve(String ip, int port) {
		this.ip = ip;
		this.port = port;
		amplifierInf = (AmplifierInf) GetBean.getBean("amplifierInf");
	}

	@Override
	public AmplifierInf getAmplifierInf() {
		return amplifierInf;
	}

	protected void setRequestPacket() {
		PortInf portInf = SystemConfigure.getPortInf(port);
		if (portInf == null) {
			throw new NullPointerException("无法根据端口获取基础报文:" + port);
		}
		String packet = portInf.getPacket();
		String[] string_packets = packet.split(",");
		byte[] packet_byte = new byte[string_packets.length];
		for (int i = 0; i < packet_byte.length; i++)
			packet_byte[i] = (byte) Integer.parseInt(string_packets[i]);
		requestPacket = packet_byte;
	}

	// 定义模板方法
	@Override
	public void prove() {
		// 设置请求报文
		setRequestPacket();
		amplifierInf.setIp(IpUtil.ipToLong(ip));
		amplifierInf.setPort(port);
		// 设置参数,模板方法
		setPar();
		// try_times为尝试次数
		int try_times = Integer
				.parseInt(SystemConfigure.getParValue("try_times"));
		int res_len = 0;
		System.out.println("try_times=" + try_times);
		// 设置请求开始时间
		amplifierInf.setStart_time(System.currentTimeMillis() / 1000);
		while (try_times > 0) {
			setResponsePacket();
			res_len = amplifierInf.getRes_len();
			// res_len>0说明通信成功,不需要再尝试
			if (res_len > 0)
				break;
			else
				try_times--;
		}
		// 设置结束时间
		amplifierInf.setEnd_time(System.currentTimeMillis() / 1000);
	}

	protected void setResponsePacket() {
		int time_out = Integer.parseInt(SystemConfigure.getParValue("time_out"));
		String describe = "定位成功";
		int res_len = 0;
		int packet_num = 0;
		Boolean control = false;
		DatagramSocket defaultSocket = null;
		try {
			defaultSocket = new DatagramSocket(0);
			defaultSocket.setSoTimeout(time_out);
			InetAddress address = InetAddress.getByName(ip);
			DatagramPacket defaultRequestPacket = new DatagramPacket(
					requestPacket, requestPacket.length, address, port);
			defaultSocket.send(defaultRequestPacket);
			// 14字节的物理帧 20字节的IP头 8字节的UDP头
			amplifierInf.setReq_len(requestPacket.length + 14 + 20 + 8);
			while (true) {
				byte reponseBuf[] = new byte[9000];
				DatagramPacket defaultResponsePacket = new DatagramPacket(
						reponseBuf, reponseBuf.length);
				defaultSocket.receive(defaultResponsePacket);
				packet_num++;
				if (packet_num == 1) {
					int rlen = defaultResponsePacket.getLength();
					/**
					 * 当出现UDP分片时,应用层数据部分的长度应当>链路层的MTU(1500)-IP头长度(20)-UDP头长度(8)
					 */
					if (rlen > (1500 - 20 - 8)) {
						/**
						 * 分片报文没有UDP头,IP头后直接是data部分,因此这里1480=1500-20
						 */
						packet_num = rlen / 1480 + 1;
						//分片报文的长度固定为:1514(1500+14(物理帧头))
						res_len=( rlen/1480)*(1500+14)+(14+20+8+rlen-(rlen/1480)*1480);
						// 如果是分片报文,则获取了所有报文长度,无需在超时等待2s的receive,直接跳出循环
						break;
					}
					// 对于控制依赖型端口需要处理控制报文,并且第一个报文为控制报文
					control = dealControlPacket(reponseBuf,
							defaultResponsePacket.getLength(), amplifierInf);
				}
				res_len += defaultResponsePacket.getLength() + 14 + 20 + 8;
			}
		} catch (UnknownHostException e) {
			describe = "未知的主机:UnknownHostException";
			e.printStackTrace();
		} catch (SocketException e) {
			describe = "创建socket出现异常:SocketException";
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			if (res_len == 0)
				describe = "定位超时:SocketTimeoutException";
		} catch (IOException e) {
			describe = "发生IO错误:IOException";
			e.printStackTrace();
		} finally {
			if (!control)
				amplifierInf.setDes_inf(describe);
			amplifierInf.setRes_num(packet_num);
			amplifierInf.setRes_len(res_len);
			amplifierInf.setBaf_value(amplifierInf.getRes_len()
					/ (amplifierInf.getReq_len() + 0.0));
			if (defaultSocket != null)
				defaultSocket.close();
		}
	}

	/**
	 * 
	 * @param reponseBuf
	 *            回复报文字节数组
	 * @param lengtn
	 *            报文长度
	 * @param amplifierInf
	 *            放大器定位信息,因为可能需要修改des_inf信息 对于控制依赖型协议(53、69)需要处理控制报文
	 */
	protected Boolean dealControlPacket(byte[] reponseBuf, int lengtn,
			AmplifierInf amplifierInf) {
		return false;
	}

	/**
	 * 对于参数依赖型协议(53,69)需要设置参数
	 */
	protected void setPar() {

	}
}
