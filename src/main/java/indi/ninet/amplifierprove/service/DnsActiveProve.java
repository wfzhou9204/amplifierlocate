package indi.ninet.amplifierprove.service;

import java.util.Random;

import indi.ninet.amplifierprove.domain.AmplifierInf;

/**
 * 
 * @author wfzhou 2017/05/03 用来处理dns协议放大器定位
 *
 */
public class DnsActiveProve extends DefaultActiveProve {
	private String domainName;

	public DnsActiveProve(String ip, String domainName) {
		super(ip, 53);
		this.domainName = domainName;
	}

	public DnsActiveProve() {

	}

	// 获取域名的字节表示,返回域名的字节数组
	private byte[] getDomainBytes() {
		String s[] = domainName.split("\\.");
		int byteLength = s.length;
		for (int i = 0; i < s.length; i++) {
			byteLength += s[i].length();
		}
		byteLength += 1;
		byte[] bytes = new byte[byteLength];
		int pos = 0;
		for (int i = 0; i < s.length; i++) {
			bytes[pos++] = (byte) s[i].length();
			for (int j = 0; j < s[i].length(); j++)
				bytes[pos++] = (byte) s[i].charAt(j);
		}
		byte zero = 0;
		bytes[pos] = zero;
		return bytes;
	}

	@Override
	protected void setRequestPacket() {
		//调用父类方法,设置基础报文
		super.setRequestPacket();
		// 获取dns报文中域名表示的字节数组
		byte[] domainBytes = getDomainBytes();
		// 获取基础报文字节表示
		byte[] configBytes = requestPacket;
		// 2字节的标识 域名 其余基础报文
		int byteLength = 2 + domainBytes.length + configBytes.length;
		requestPacket = new byte[byteLength];
		int pos = 0, cPos = 0;
		// 2字节的标识
		byte[] flags = new byte[2];
		Random random = new Random();
		random.nextBytes(flags);
		for (int i = 0; i < flags.length; i++)
			requestPacket[pos++] = flags[i];
		for (cPos = 0; cPos < 10; cPos++) {
			requestPacket[pos++] = configBytes[cPos];
		}
		for (int i = 0; i < domainBytes.length; i++)
			requestPacket[pos++] = domainBytes[i];
		for (; cPos < configBytes.length; cPos++)
			requestPacket[pos++] = configBytes[cPos];
	}

	// 处理dns控制报文
	@Override
	protected Boolean dealControlPacket(byte[] reponseBuf, int lengtn,
			AmplifierInf amplifierInf) {
		byte res[] = reponseBuf;
		int len = lengtn;
		String describe = "";
		if (len < 3)
			return false;
		else {
			byte flag1 = res[2];
			flag1 = (byte) (flag1 & 128);
			// 是否是DNS回复报文
			if (flag1 == 0)
				return false;
			else {
				byte flag2 = res[3];
				flag2 = (byte) (flag2 & 15);
				// 不是控制报文
				if (flag2 == 0)
					return false;
				switch (flag2) {
				case 1:
					describe = "格式差错";
					break;
				case 2:
					describe = "问题在域名服务器";
					break;
				case 3:
					describe = "域参照问题";
					break;
				case 4:
					describe = "查询类型不支持";
					break;
				case 5:
					describe = "在管理上并禁止";
					break;
				default:
					describe = "保留";
					break;
				}
				amplifierInf.setDes_inf(describe);
			}
		}
		return true;
	}

	@Override
	protected void setPar() {
		amplifierInf.setParameters(domainName);
	}

}
