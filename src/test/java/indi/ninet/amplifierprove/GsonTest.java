package indi.ninet.amplifierprove;

import com.google.gson.Gson;

public class GsonTest {
	public static void main(String args[]){
		Gson gson=new Gson();
		Domain domain=new Domain();
		domain.setUserid("wfzhou");
		domain.setIp("1.1.1.1");
		domain.setPort(19);
		domain.setPar_num(0);
		String jsonData="{\"userid\":\"wfzhou\", \"ip\":\"1.1.1.1\",\"port\":\"19\"}";
		Domain d=gson.fromJson(jsonData, Domain.class);
		System.out.println(d.getPort());
	
	}
}
