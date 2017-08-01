package indi.ninet.amplifierprove.util;
/**
 * 
 * @author wfzhou
 * @since 2017/5/31
 * 工具类,将点分十进制的IP转为long类型
 *
 */
public class IpUtil {
	//将ip地址为long
	public static long ipToLong(String strIp) {  
        String[]ip = strIp.split("\\."); 
        return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);  
    }
	//将long转为点分10进制字符串的ip
	public static String longtoIp(long ip){
		StringBuilder str=new StringBuilder();
		int bit=24;
		long value=0L;
		for(int i=0;i<=3;i++){
			value=ip>>bit;
		    if(i==3){
		    	str.append(value);
		    }else{
		    	str.append(value+".");
		    }
			ip=ip-(value<<bit);
			bit=bit-8;
		}
		return str.toString();
	}
	public static void main(String args[]){
		long value=ipToLong("219.141.161.34");
		System.out.println(value);
		System.out.println(longtoIp(value));
	}
}
