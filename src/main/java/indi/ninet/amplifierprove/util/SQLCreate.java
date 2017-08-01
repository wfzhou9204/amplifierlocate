package indi.ninet.amplifierprove.util;

/**
 * 
 * @author wfzhou create 2017/06/05 根据给定参数生成SQL语句的工具类
 *
 */
public class SQLCreate {
	/**
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param attribute
	 *            列数组
	 * @return 生成的sql语句
	 */
	public static String insert(String tableName, String[] attribute) {
		checkTableName(tableName);
		checkNull(attribute);
		StringBuilder value = new StringBuilder();
		value.append("insert into " + tableName + " ( ");
		for (int i = 0; i < attribute.length - 1; i++) {
			value.append(attribute[i] + ",");
		}
		value.append(attribute[attribute.length - 1] + " ) ");
		value.append("values ( ");
		for (int i = 0; i < attribute.length - 1; i++) {
			value.append("?,");
		}
		value.append("? )");
		return value.toString();
	}

	// 插入表里面所有列值可以不给出列名
	public static String insert(String tableName, int len) {
		checkTableName(tableName);
		if (len <= 0) {
			throw new NullPointerException("SQL创建(插入):属性个数需要为正数");
		}
		StringBuilder value = new StringBuilder();
		value.append("insert into " + tableName + " values ( ");
		for (int i = 0; i < len - 1; i++) {
			value.append("?,");
		}
		value.append("? )");
		return value.toString();
	}

	/**
	 * 
	 * @param tableName
	 *            表名
	 * @param attribute
	 *            列数组
	 * @param condition
	 *            条件数组
	 * @return 生成的sql语句
	 */
	public static String search(String tableName, String[] attribute,
			String[] condition) {
		checkTableName(tableName);
		checkNull(attribute);
		StringBuilder value = new StringBuilder();
		value.append("select ");
		for (int i = 0; i < attribute.length - 1; i++) {
			value.append(attribute[i] + ",");
		}
		value.append(attribute[attribute.length - 1] + " ");
		value.append("from " + tableName + " ");
		if (condition != null && condition.length != 0) {
			value.append("where ");
			for (int i = 0; i < condition.length - 1; i++) {
				value.append(condition[i] + "=? and ");
			}
			value.append(condition[condition.length - 1] + "=?");
		}
		return value.toString();
	}

	/**
	 * 
	 * @param tableName
	 *            表名
	 * @param attribute
	 *            列数组
	 * @param condition
	 *            条件数组
	 * @return
	 */
	public static String update(String tableName, String[] attribute,
			String[] condition) {
		checkTableName(tableName);
		checkNull(attribute);
		StringBuilder value = new StringBuilder();
		value.append("update " + tableName + " set "+attribute[0]+"=? ");
		for (int i = 1; i < attribute.length ; i++) {
			value.append( ","+attribute[i] + "=? ");
		}
		if (condition != null && condition.length != 0) {
			value.append("where ");
			for (int i = 0; i < condition.length - 1; i++) {
				value.append(condition[i] + "=? and ");
			}
			value.append(condition[condition.length - 1] + "=?");
		}
		return value.toString();
	}

	/**
	 * 
	 * @param tableName
	 *            表名
	 * @param condition
	 *            条件数组
	 * @return 生成的sql语句
	 */
	public static String delete(String tableName, String[] condition) {
		checkTableName(tableName);
		StringBuilder value = new StringBuilder();
		value.append("delete from " + tableName + " ");
		if (condition != null && condition.length != 0) {
			value.append("where ");
			for (int i = 0; i < condition.length - 1; i++) {
				value.append(condition[i] + "=? and ");
			}
			value.append(condition[condition.length - 1] + "=?");
		}
		return value.toString();
	}

	private static void checkTableName(String tableName) {
		if (tableName == null || tableName.length() == 0) {
			throw new NullPointerException("SQL创建:表名不能为空值");
		}
	}

	private static void checkNull(String[] str) {
		if (str == null || str.length == 0) {
			throw new NullPointerException("SQL创建:属性数组不能为空值");
		}
	}

	public static void main(String args[]) {
		Reflect reflect = new Reflect(
				"indi.ninet.amplifierprove.domain.PortInf");
		System.out.println(SQLCreate.insert("amplifier_inf",
				reflect.getAttributeName()));
		String condition[] = { "port"};
		System.out.println(SQLCreate.search("port_inf",
				reflect.getAttributeName(), condition));
		System.out.println(SQLCreate.delete("port_inf", condition));
		System.out.println(SQLCreate.update("port_inf", reflect.getAttributeName(), condition));
	}
}
