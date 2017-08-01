package indi.ninet.amplifierprove.util;

import indi.ninet.amplifierprove.domain.PortInf;

import java.lang.reflect.Field;

/**
 * 
 * @author wfzhou create 2017/06/05 反射工具类,根据完整类名获取javabean的属性名称和属性类型并封装到数组中
 *         主要是方便数据库的操作，相当于一个简单的对象关系映射,目前只支持简单的数据类型 后序版本可要hibernate来实现复杂对象关系映射
 */
public class Reflect {
	private String classFullName;
	private Class<?> c;
	private Field[] fields;
	private Object object;

	public Reflect(String classFullName) {
		this.classFullName = classFullName;
		init();
	}

	public Reflect(Object object) {
		if (object == null) {
			throw new NullPointerException("反射工具类：object=null");
		}
		this.object = object;
		classFullName = object.getClass().getName();
		c = object.getClass();
		fields = c.getDeclaredFields();

	}

	private void init() {
		try {
			c = Class.forName(classFullName);
			fields = c.getDeclaredFields();
			object = c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// 获取类中各个属性名字
	public String[] getAttributeName() {
		int len = getAttributeNum();
		String value[] = new String[len];
		for (int i = 0; i < len; i++) {
			value[i] = fields[i].getName();
		}
		return value;
	}

	// 获取类中属性的个数
	public int getAttributeNum() {
		return fields.length;
	}

	/**
	 * 获取类中各个属性的类型 1:String 2:int 3:double 4:float 5:long
	 */
	public int[] getAttributeType() {
		int len = getAttributeNum();
		int[] value = new int[len];
		for (int i = 0; i < len; i++) {
			String type = fields[i].getType().getName();
			switch (type) {
			case "java.lang.String":
				value[i] = 1;
				break;
			case "int":
				value[i] = 2;
				break;
			case "double":
				value[i] = 3;
				break;
			case "float":
				value[i] = 4;
				break;
			case "long":
				value[i] = 5;
				break;
			case "java.lang.Integer":
				value[i] = 2;
				break;
			case "java.lang.Double":
				value[i] = 3;
				break;
			case "java.lang.Float":
				value[i] = 4;
				break;
			case "java.lang.Long":
				value[i] = 5;
				break;
			default:
				try {
					throw new Exception(type + ":不支持的数据类型");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		return value;
	}

	// 获取对象的属性值
	public String[] getAttributeValue() {
		int len = getAttributeNum();
		String value[] = new String[len];
		for (int i = 0; i < len; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				Object obj = field.get(object);
				if (obj == null)
					value[i] = null;
				else
					value[i] = obj.toString();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	// 获取操作的数据库表名 ,这里有个规定:表名需与类名相同
	public String getTableName() {
		String tableName = classFullName.substring(classFullName
				.lastIndexOf(".") + 1);
		// Java规范类名首字母需大写,这里需将大写转为小写
		tableName = tableName.toLowerCase();
		return tableName;
	}

	/**
	 * 
	 * @param objects
	 *            属性值数组
	 * @param attributeName
	 *            要赋值的属性名数组
	 */
	public void setValues(Object[] objects, String[] attributeName) {
		// 重新初始化
		init();
		if (objects == null || attributeName == null) {
			throw new NullPointerException("反射工具类:setValues参数存在NULL");
		}
		if (!(objects.length == attributeName.length)) {
			throw new RuntimeException("反射工具类:setValues数组参数长度不一致");
		}
		for (int i = 0; i < objects.length; i++) {
			Object obj = objects[i];
			// 当值为null时,直接跳过
			if (obj == null)
				continue;
			String aName = attributeName[i];
			for (int j = 0; j < fields.length; j++) {
				if (aName.equals(fields[j].getName())) {
					Field field = fields[j];
					field.setAccessible(true);
					String type = field.getType().getName();
					try {
						switch (type) {

						case "java.lang.String":
							field.set(object, obj.toString());
							break;
						case "int":
							field.set(object, Integer.parseInt(obj.toString()));
							break;
						case "double":
							field.set(object,
									Double.parseDouble(obj.toString()));
							break;
						case "float":
							field.set(object, Float.parseFloat(obj.toString()));
							break;
						case "long":
							field.set(object, Long.parseLong(obj.toString()));
							break;
						case "java.lang.Integer":
							field.set(object, new Integer(obj.toString()));
							break;
						case "java.lang.Double":
							field.set(object, new Double(obj.toString()));
							break;
						case "java.lang.Float":
							field.set(object, new Float(obj.toString()));
							break;
						case "java.lang.Long":
							field.set(object, new Long(obj.toString()));
							break;
						default:
							try {
								throw new Exception(type + ":不支持的数据类型");
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();

					} catch (IllegalAccessException e) {
						e.printStackTrace();

					}
				}
			}
		}
	}

	public Object getObject() {
		return object;
	}

	public static void main(String args[]) {
		Reflect reflect = new Reflect(
				"indi.ninet.amplifierprove.domain.PortInf");
		System.out.println(reflect.getTableName());
		String name[] = reflect.getAttributeName();
		for (int i = 0; i < name.length; i++) {
			System.out.print(name[i] + " ");
		}
		System.out.println();
		int type[] = reflect.getAttributeType();
		for (int i = 0; i < type.length; i++) {
			System.out.print(type[i] + " ");
		}
		System.out.println();
		int size = reflect.getAttributeNum();
		System.out.println("size=" + size);
		PortInf portInf = new PortInf();
		portInf.setPort(53);
		portInf.setPar_rely(1);
		portInf.setPar_describe("par_describe");
		portInf.setPro_class("pro_class");
		portInf.setPacket("packet");
		reflect = new Reflect(portInf);
		String value[] = reflect.getAttributeValue();
		for (int i = 0; i < value.length; i++) {
			System.out.print(value[i] + " ");
		}
		System.out.println();
	}

}
