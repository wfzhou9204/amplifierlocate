package indi.ninet.amplifierprove.server;

import indi.ninet.amplifierprove.domain.GroupAmplifierTaskInf;
import indi.ninet.amplifierprove.domain.SingleAmplifierTaskInf;
import indi.ninet.amplifierprove.util.GsonUtil;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * @author wfzhou 2017/05/02 处理接受php页面post的数据的handle modify in 2017/06/21 add
 *         grouptaskhandler
 *
 */
@SuppressWarnings("restriction")
public class AmplifierHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String httpMethod = httpExchange.getRequestMethod();
		String requestURI = httpExchange.getRequestURI().toString();
		System.out.println("method=" + httpMethod);
		System.out.println("requestURI=" + requestURI);
		// 输出httpRequestheaders信息
		Set<Entry<String, List<String>>> headers = (httpExchange
				.getRequestHeaders()).entrySet();
		Iterator<Entry<String, List<String>>> iterator = headers.iterator();
		while (iterator.hasNext()) {
			Entry<String, List<String>> entry = iterator.next();
			System.out.println(entry.getKey() + "  " + entry.getValue());
		}
		/**
		 * 允许任何域发送请求
		 */
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin",
				"*");
		/**
		 * 对于cors请求,一般会首先发送HTTP OPTIONS请求，以确认针对某个目标地址的请求必须具有怎样的约束
		 * （比如应该采用怎样的HTTP方法以及自定义的请求报头），然后根据其约束发送真正的请求。
		 * 比如针对“跨域资源”的预检（Preflight）请求采用的HTTP方法就是OPTIONS。
		 */
		if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
			httpExchange.getResponseHeaders().add(
					"Access-Control-Allow-Methods", "POST,OPTIONS");
			/**
			 * nbos前端使用的是mootools,发送异步ajax请求时，对于cros会在http头中加入X-Request,X-
			 * Requested-With,需要将其加入到"Access-Control-Allow-Headers"
			 */
			httpExchange.getResponseHeaders().add(
					"Access-Control-Allow-Headers",
					"X-Request,X-Requested-With,Content-Type,Authorization");
			/**
			 * 状态码204表示请求成功了,但没有数据返回,浏览器不用刷新页面.也不用导向新的页面. 当有一些服务, 只是返回成功与否的时候,
			 * 可以尝试使用HTTP的状态码来作为返回信息, 而省掉多余的数据传输, 比如这里的预检请求(http opitions).
			 */
			httpExchange.sendResponseHeaders(204, -1);
			return;
		}
		if (httpMethod.equalsIgnoreCase("POST")
				&& requestURI.equals("/nbos/amplifier-locate/simple-task")) {
			// 单ip定位检测处理回调类
			new SimpleTaskHandler().handle(httpExchange);
		} else if (httpMethod.equalsIgnoreCase("POST")
				&& requestURI.equals("/nbos/amplifier-locate/super-task")) {
			// 群组ip处理回调类
			new SuperTaskHandle().handle(httpExchange);
		} else {
			// 默认处理回调类
			new DefaultHandler().handle(httpExchange);
		}
	}

	// 默认的请求处理的回调函数类
	class DefaultHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {
			String response = "oop you get it";
			/**
			 * 这里的长度需是取字节后的长度,因为如果存在中文,一个中文需要两个字节来表示
			 */
			httpExchange.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
			httpExchange.close();
		}
	}

	class TaskHandler implements HttpHandler {
		private String receiveData;

		private void setReceiveData(HttpExchange httpExchange)
				throws IOException {
			InputStream is = httpExchange.getRequestBody();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line).append('\n');
			}
			receiveData = sb.toString();
			bufferedReader.close();
		}

		// 子类实现
		public void doTask() {

		}

		public final String getReceiveData() {
			return receiveData;
		}

		private void setResponseData(HttpExchange httpExchange,
				String responseData) throws IOException {
			httpExchange.getResponseHeaders().add("Content-Type",
					"application/json; charset=UTF-8");
			/**
			 * 这里的长度需是取字节后的长度,因为如果存在中文,一个中文需要两个字节来表示
			 */
			httpExchange.sendResponseHeaders(200,
					responseData.getBytes().length);
			OutputStream os = httpExchange.getResponseBody();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os, "UTF-8"));
			writer.write(responseData);
			writer.flush();
			writer.close();
			httpExchange.close();
		}

		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			setReceiveData(httpExchange);
			String responseData = "{";
			responseData += "\"inf\":\"请求成功\"}";
			setResponseData(httpExchange, responseData);
			doTask();
		}

	}

	// 单IP放大器定位检测请求回调函数类
	final class SimpleTaskHandler extends TaskHandler implements HttpHandler {
		@Override
		// 创建后台线程执行单ip定位检测任务
		public void doTask() {
			String receiveData = getReceiveData();
			System.out.println(receiveData);
			SingleAmplifierTaskInf singletaskinf = GsonUtil.gsonToBean(
					receiveData, SingleAmplifierTaskInf.class);
			AmplifierLocateTask task = new AmplifierLocateTask();
			task.setSingletask(singletaskinf);
			Thread thread = new Thread(task);
			thread.start();
		}
	}

	// 群组放大器处理类
	final class SuperTaskHandle extends TaskHandler implements HttpHandler {
		@Override
		public void doTask() {
			// 获取前台发送的数据
			String receiveData = getReceiveData();
			// 数量太长不调试输出
			if (receiveData.length() <= 200) {
				System.out.println(receiveData);
			}
			// 将json字符串数据转为javabean对象

			GroupAmplifierTaskInf grouptaskinf = GsonUtil.gsonToBean(
					receiveData, GroupAmplifierTaskInf.class);
			System.out.println(grouptaskinf.getUserid());
			
		}

	}

}