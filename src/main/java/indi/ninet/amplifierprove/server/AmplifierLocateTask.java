package indi.ninet.amplifierprove.server;

import indi.ninet.amplifierprove.dao.Dao;
import indi.ninet.amplifierprove.daofactory.DaoFactory;
import indi.ninet.amplifierprove.domain.AmplifierInf;
import indi.ninet.amplifierprove.domain.PortInf;
import indi.ninet.amplifierprove.domain.SingleAmplifierTaskInf;
import indi.ninet.amplifierprove.service.ActiveProve;
import indi.ninet.amplifierprove.util.FileSystemClassloader;
import indi.ninet.amplifierprove.util.SystemConfigure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author wfzhou 2017/05/06 放大器定位任务
 *
 */
public class AmplifierLocateTask implements Runnable {
	private SingleAmplifierTaskInf singletask;

	public AmplifierLocateTask() {
		singletask = new SingleAmplifierTaskInf();
	}

	public SingleAmplifierTaskInf getSingletask() {
		return singletask;
	}

	public void setSingletask(SingleAmplifierTaskInf singletask) {
		this.singletask = singletask;
	}

	@Override
	public void run() {
		String pars = "";
		String ip = singletask.getIp();
		int port = singletask.getPort();
		String userid = singletask.getUserid();
		int par_num = singletask.getPar_num();
		if (par_num != 0)
			pars = singletask.getPars();
		PortInf portinf = SystemConfigure.getPortInf(port);
		if (portinf == null) {
			throw new RuntimeException("无法根据端口获取放大器信息:" + port);
		}
		String pro_class = portinf.getPro_class();
		ActiveProve activeProve = null;
		Class<?> c = null;
		Constructor<?> constructor = null;
		/**
		 * 自定义文件类加载器,用来加载各类型放大器定位检测程序
		 */
		FileSystemClassloader fileSystemClassloader = FileSystemClassloader
				.getInstance();
		try {
			c = fileSystemClassloader.loadClass(pro_class);
			/**
			 * par_num==0 非参数依赖型 par_num!=0参数依赖型
			 */
			if (par_num == 0) {
				constructor = c.getConstructor(String.class, int.class);
				constructor.setAccessible(true);
				activeProve = (ActiveProve) constructor.newInstance(ip, port);
			} else {
				constructor = c.getConstructor(String.class, String.class);
				constructor.setAccessible(true);
				activeProve = (ActiveProve) constructor.newInstance(ip, pars);
			}
			System.out.println(c.getClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		activeProve.prove();
		AmplifierInf amplifierInf = activeProve.getAmplifierInf();
		amplifierInf.setUserid(userid);
		System.out.println(amplifierInf);
		Dao dao = DaoFactory
				.getDaoImpl("indi.ninet.amplifierprove.daoimpl.AmplifierInfDaoImpl");
		int result = dao.insert(amplifierInf);
		if (result <= 0) {
			System.err.println("数据插入失败");
		}
	}

}
