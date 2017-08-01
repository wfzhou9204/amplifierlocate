package indi.ninet.amplifierprove.daofactory;

import indi.ninet.amplifierprove.daoimpl.DaoImpl;

/**
 * 
 * @author wfzhou
 * DAO工厂类
 *
 */
public class DaoFactory {
	//利用反射获取对应DAO的实现类  静态工厂模式
	public static DaoImpl getDaoImpl(String className){
		checkClassName(className);
		DaoImpl daoImpl=null;
		try {
			daoImpl=(DaoImpl) Class.forName(className).newInstance();
			return daoImpl;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static void checkClassName(String className){
		if(className==null||className==""){
			throw new NullPointerException("DAO工厂创建DAOImpl实例：className=null");
		}
	}
}
