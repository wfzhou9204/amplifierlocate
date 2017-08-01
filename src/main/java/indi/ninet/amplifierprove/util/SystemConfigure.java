package indi.ninet.amplifierprove.util;

import indi.ninet.amplifierprove.dao.Dao;
import indi.ninet.amplifierprove.daofactory.DaoFactory;
import indi.ninet.amplifierprove.domain.PortInf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author wfzhou create 2017/06/15 系统配置信息
 *
 */
public class SystemConfigure {
	// 系统参数文件路径
	private static final String confPath = "/etc/amplifierlocate_conf/system_arguments.txt";
	// 缓存系统配置信息的map
	private static final Map<String, String> parameters = new ConcurrentHashMap<String, String>();
	// 缓存放大器信息
	private static final Map<Integer, PortInf> portinfs = new ConcurrentHashMap<Integer, PortInf>();
	// 系统配置文件最后修改时间
	private static long lastUpdate = 0;
	// 更新系统参数缓存锁
	private static ReentrantLock parameters_lock = new ReentrantLock();
	// 更新放大器信息缓存锁
	private static ReentrantLock portinfs_lock = new ReentrantLock();
	static {
		initPar();
		initPortinf();
	}

	// 将文件中的系统参数缓存到parameters中
	private static void initPar() {
		String error = "系统配置信息初始化";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			File file = new File(confPath);
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			String str = "";
			while ((str = br.readLine()) != null) {
				if (str.equals("")) {
					continue;
				}
				String[] values = str.split("	");
				if (values.length < 2)
					continue;
				parameters.put(values[0], values[1]);
			}
			lastUpdate = file.lastModified();
		} catch (FileNotFoundException e) {
			System.err.println(error + ":配置文件不存在:" + confPath);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.err.println(error + ":编码出错");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(error + "读取配置文件出错");
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (isr != null)
					isr.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
				System.err.println(error + "关闭配置文件出错");
				e.printStackTrace();
			}
		}
	}

	private static void initPortinf() {
		PortInf portInf = new PortInf();
		Reflect reflect = new Reflect(portInf);
		Dao dao = DaoFactory
				.getDaoImpl("indi.ninet.amplifierprove.daoimpl.PortInfDaoImpl");
		String[] attributeName = reflect.getAttributeName();
		String[] condition = null;
		int[] type = null;
		String value[] = null;
		List<Object> list = dao.search(portInf, attributeName, condition, type,
				value);
		if (list == null) {
			throw new RuntimeException("从数据库中获取放大器信息失败:list=null");
		}
		Iterator<Object> iterator = list.iterator();
		while (iterator.hasNext()) {
			portInf = (PortInf) iterator.next();
			portinfs.put(portInf.getPort(), portInf);
		}
	}

	/**
	 * 
	 * @param key
	 * @return synchronized实现同步,需要锁住
	 *         parameters,读操作也会被锁定,而这里使用的是ConcurrentHashMap
	 *         ,确保了get与put操作的线程安全,因此这里使用reentrantlock,只需保证多线程修改
	 *         parameters时加锁,而不需要
	 *         锁住parameters,保证其余的get操作(貌似本系统中好像也没有,但思想是对的,放大器信息更新就是如此
	 *         )不会锁住,类似于读写分离,这也是可重入锁相当于synchronized的优点之一
	 */
	public static String getParValue(String key) {
		File file = new File(confPath);
		// 如果系统参数文件不存在,直接抛出异常
		if (!file.exists()) {
			throw new RuntimeException("获取系统配置参数值:系统参数文件不存在!");
		}
		// 获取文件最后修改时间
		long update = file.lastModified();
		// 存在多线程操作lastUpdated和parameters,这里需要同步
		parameters_lock.lock();
		try {
			// 如果文件修改时间大于上次修改时间,说明参数文件修改了,需要重新加载
			if (update > lastUpdate) {
				initPar();
				lastUpdate = update;
			}
			return parameters.get(key);
		} finally {
			parameters_lock.unlock();
		}
		/*
		 * synchronized (parameters) { // 如果文件修改时间大于上次修改时间,说明参数文件修改了,需要重新加载 if
		 * (update > lastUpdate) { initPar(); lastUpdate = update; } } return
		 * parameters.get(key);
		 */
	}

	// 根据端口获取对应的放大器信息
	public static PortInf getPortInf(int port) {
		return portinfs.get(port);
	}

	/**
	 * 更新放大器信息缓存,存在多线程访问,需要同步
	 * 
	 * @see getParValue(String key)
	 */

	public static void updatePortInf() {
		portinfs_lock.lock();
		try {
			initPortinf();
		} finally {
			portinfs_lock.unlock();
		}
		/*
		 * synchronized (portinfs) { initPortinf(); }
		 */
	}

	public static void main(String args[]) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + ":"
							+ SystemConfigure.getParValue("server_port"));
				}
			}
		};
		Thread thread1 = new Thread(r);
		Thread thread2 = new Thread(r);
		thread1.start();
		thread2.start();
	}
}
