package indi.ninet.amplifierprove.service;

import java.io.UnsupportedEncodingException;

import indi.ninet.amplifierprove.domain.AmplifierInf;

/**
 * 
 * @author wfzhou 2017/05/03 用来处理tftp协议放大器定位
 *
 */
public class TftpActiveProve extends DefaultActiveProve {
	private static final String MODE = "octet";// 读取文件模式
	private static final byte DAT = 3; // 数据报文标志
	private static final byte ERROR = 5; // 差错报文标志
	private String fileName;// 文件名

	public TftpActiveProve(String ip, String fileName) {
		super(ip, 69);
		this.fileName = fileName;
	}

	public TftpActiveProve() {
	}

	@Override
	protected void setRequestPacket() {
		// 调用父类方法,设置基础报文
		super.setRequestPacket();
		byte[] configBytes = requestPacket;
		int byteLength = configBytes.length + fileName.length() + 1
				+ MODE.length() + 1;
		requestPacket = new byte[byteLength];
		byte zeroByte = 0;
		int pos = 0;
		for (int i = 0; i < configBytes.length; i++)
			requestPacket[pos++] = configBytes[i];
		for (int i = 0; i < fileName.length(); i++) {
			requestPacket[pos++] = (byte) fileName.charAt(i);
		}
		requestPacket[pos++] = zeroByte;
		for (int i = 0; i < MODE.length(); i++) {
			requestPacket[pos++] = (byte) MODE.charAt(i);
		}
		requestPacket[pos] = zeroByte;
	}

	@Override
	protected Boolean dealControlPacket(byte[] reponseBuf, int lengtn,
			AmplifierInf amplifierInf) {
		byte res[] = reponseBuf;
		int len = lengtn;
		String describe = "";
		if (len < 4)
			return false;
		byte[] opCode = { res[0], res[1] }; // 差错码
		if (opCode[1] == ERROR) {
			try {
				String errorText = new String(res, 4, len - 4 - 1, "UTF-8");
				describe = errorText;
			} catch (UnsupportedEncodingException e) {
				describe = "定位TFTP放大器:" + ip + ":编码出错";
				System.err.println("定位TFTP放大器:" + ip + ":编码出错");
				e.printStackTrace();
			}
		} else if (opCode[1] == DAT) {
			describe = "定位成功:" + "返回了数据信息";
		} else {
			return false;
		}
		amplifierInf.setDes_inf(describe);
		return true;
	}

	@Override
	protected void setPar() {
		amplifierInf.setParameters(fileName);
	}
}
