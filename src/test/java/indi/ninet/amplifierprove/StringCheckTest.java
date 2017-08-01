package indi.ninet.amplifierprove;

public class StringCheckTest {
	private static String data="user_id=wfzhou&ip=8.8.8.8&port=53&par_num=1&pars=dk";
	//对字符串合法性检查
	public static void checkStr(){
		if(data==null||data==""){
			System.err.println("接受数据为空");
			return ;
		}
		String datas[] = data.split("&");
		for(int i=0;i<datas.length;i++){
			
		}
	}
	public static void main(String args[]){
		
	}
}
