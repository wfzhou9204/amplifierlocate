package indi.ninet.amplifierprove;

import java.io.UnsupportedEncodingException;
import indi.ninet.amplifierprove.service.ActiveProve;
import indi.ninet.amplifierprove.util.GetBean;

public class ActiveProveTest {
	private final Integer a=new Integer(100);
	private final Integer b=new Integer(100);
	public boolean f(){
		return a==b;
	}
	public static void getAmpInf(String ip,int port){
		String str="";//SysConInf.getInitInfMap().get(port+"_rely_id");
		ActiveProve activeProve=(ActiveProve) GetBean.getBean(str,ip,"a.pdf");
		activeProve.prove();
		System.out.println(activeProve.getAmplifierInf());
	}
	public static void main(String args[]) throws UnsupportedEncodingException {
		//getAmpInf("202.193.53.123", 69);
		/*String line="ip,port";
		String[]strs=line.split(",");
		for(int i=0;i<strs.length;i++)
			System.out.println(strs[i]);
		String var="0";
		System.out.println(Integer.parseInt(var));*/
		/*ActiveProveTest apt=new ActiveProveTest();
		System.out.println(apt.f());*/
	}
}
