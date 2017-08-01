package indi.ninet.amplifierprove.server;

import indi.ninet.amplifierprove.util.SystemConfigure;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

/**
 * 
 * @author wfzhou 2017/05/02 模拟http服务端,接受php页面post的数据 modify 2017/06/03 add
 *         ThreadPoolExecutor
 */
@SuppressWarnings("restriction")
public class AmplifierHttpServer extends Thread {
	private ExecutorService conectPool = Executors.newFixedThreadPool(200);

	@Override
	public void run() {
		String error = "后台服务启动出错:";
		String server_ip = SystemConfigure.getParValue("server_ip");
		int port = Integer.parseInt(SystemConfigure.getParValue("server_port"));
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(
					server_ip, port), 200);
			server.createContext("/nbos/amplifier-locate",
					new AmplifierHttpHandler());
			// server.setExecutor(null);
			server.setExecutor(conectPool);
			server.start();
			System.out.println("后台服务成功开启!");
		} catch (IOException e) {
			System.err.println(error + "发生IO错误");
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new AmplifierHttpServer().start();
	}
}