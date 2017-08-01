package indi.ninet.amplifierprove.dao;

import java.util.List;

/**
 * 
 * @author wfzhou create 2017/06/08 数据访问接口 ,定义了访问数据库的公共方法
 *
 */
public interface Dao {
	/**
	 * 
	 * @param 插入的对象
	 * @return
	 */
	public int insert(Object obj);

	/**
	 * 
	 * @param tableName
	 *            要操作的数据库表名
	 * @param attributeName
	 *            列名数组
	 * @param condition
	 *            条件数组
	 * @param type
	 *            数据类型数组
	 * @param value
	 *            值数据数组
	 * @return
	 */
	public int update(String tableName, String[] attributeName,
			String[] condition, int[] type, String[] value);

	/**
	 * 
	 * @param tableName
	 *            表名
	 * @param condition
	 *            条件数组
	 * @param type
	 *            值类型数组
	 * @param value
	 *            值数组
	 * @return
	 */
	public int delete(String tableName, String[] condition, int[] type,
			String[] value);

	/**
	 * 
	 * @param obj
	 *            操作对象
	 * @param attributeName
	 *            属性数组
	 * @param condition
	 *            条件数组
	 * @param type
	 *            值类型数组
	 * @param value
	 *            值数组
	 * @return
	 */
	public List<Object> search(Object obj, String[] attributeName,
			String[] condition, int[] type, String[] value);
}
