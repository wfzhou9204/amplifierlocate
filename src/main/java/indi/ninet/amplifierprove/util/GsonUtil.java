package indi.ninet.amplifierprove.util;

import com.google.gson.Gson;

/**
 * 
 * @author wfzhou create in 2017/06/20 json解析工具类,封装gson,将json字符串转换为bean对象
 *
 */
public class GsonUtil {
	public static <T> T gsonToBean(String jsonData, Class<T> c) {
		Gson gson = new Gson();
		T result = gson.fromJson(jsonData, c);
		return result;
	}
}
