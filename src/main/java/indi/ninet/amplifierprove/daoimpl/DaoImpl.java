package indi.ninet.amplifierprove.daoimpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import indi.ninet.amplifierprove.dao.Dao;
import indi.ninet.amplifierprove.domain.PortInf;
import indi.ninet.amplifierprove.util.Connect;
import indi.ninet.amplifierprove.util.Prest;
import indi.ninet.amplifierprove.util.Reflect;
import indi.ninet.amplifierprove.util.SQLCreate;

public class DaoImpl implements Dao {

	@Override
	public int insert(Object obj) {
		int result = 0;
		// 反射工具类
		Reflect reflect = new Reflect(obj);
		// 动态生成sql语句
		String sql = SQLCreate.insert(reflect.getTableName(),
				reflect.getAttributeNum());
		System.out.println(sql);
		// 获取对象属性值
		String attributeValue[] = reflect.getAttributeValue();
		// 获取对象属性类型
		int attributeType[] = reflect.getAttributeType();
		// 获取数据库连接对象
		Connection connect = Connect.getConnect();
		Prest prest = new Prest(connect, sql, attributeValue, attributeType,
				false);
		result = prest.executeUpdate();
		// 关闭数据库
		prest.closeDB();
		return result;
	}

	@Override
	public int update(String tableName, String[] attributeName,
			String[] condition, int[] type, String[] value) {
		int result = 0;
		String sql = SQLCreate.update(tableName, attributeName, condition);
		System.out.println(sql);
		Connection connection = Connect.getConnect();
		Prest prest = new Prest(connection, sql, value, type, false);
		result = prest.executeUpdate();
		prest.closeDB();
		return result;
	}

	@Override
	public int delete(String tableName, String[] condition, int[] type,
			String[] value) {
		int result = 0;
		String sql = SQLCreate.delete(tableName, condition);
		System.out.println(sql);
		Connection connection = Connect.getConnect();
		Prest prest = new Prest(connection, sql, value, type, false);
		result = prest.executeUpdate();
		prest.closeDB();
		return result;
	}

	@Override
	public List<Object> search(Object obj, String[] attributeName,
			String[] condition, int[] type, String[] value) {
		Reflect reflect = new Reflect(obj);
		String tableName = reflect.getTableName();
		String sql = SQLCreate.search(tableName, attributeName, condition);
		System.out.println(sql);
		Connection connection = Connect.getConnect();
		Prest prest = new Prest(connection, sql, value, type, false);
		ResultSet resultSet = prest.executeQuery();
		List<Object> list = new ArrayList<Object>();
		if (resultSet == null) {
			return null;
		}
		try {
			while (resultSet.next()) {
				Object objects[] = new Object[attributeName.length];
				// System.out.println(obj.getClass().getName());
				Reflect r = new Reflect(obj.getClass().getName());
				for (int i = 0; i < attributeName.length; i++) {
					// System.out.println(attributeName[i]);
					objects[i] = resultSet.getObject(attributeName[i]);
				}
				/*
				 * for(int i=0;i<objects.length;i++){
				 * System.out.println(objects[i]); }
				 */
				r.setValues(objects, attributeName);
				list.add(r.getObject());
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				prest.closeDB();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String args[]) {
		// System.out.println(SystemConf.get("server_ip"));
		DaoImpl daoImpl = new DaoImpl();
		PortInf portInf = new PortInf();
		portInf.setPort(111);
		portInf.setPar_rely(0);
		daoImpl.insert(portInf);
	}
}
