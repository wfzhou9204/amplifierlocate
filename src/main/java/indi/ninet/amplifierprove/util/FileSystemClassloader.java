package indi.ninet.amplifierprove.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author wfzhou create 2017/06/04 自定义文件系统类加载器,用来加载以插件形式扩展系统功能的不同类型的放大器检测程序
 *
 */
public class FileSystemClassloader extends ClassLoader {
	// 目录
	private static String rootDir = "";
	private static FileSystemClassloader fileSystemClassloader;
	// 目录上一次修改时间,用于实现热替换
	private static long lastUpdate = 0L;
	static {
		fileSystemClassloader = new FileSystemClassloader();
		// 从配置文件中获取存放定位检测程序的根目录
		rootDir = SystemConfigure.getParValue("pro_path");
		File file = new File(rootDir);
		if (!file.exists()) {
			throw new RuntimeException("自定义文件类加载器:存放检测程序的目录不存在:" + rootDir);
		}
		lastUpdate = file.lastModified();
	}

	public static FileSystemClassloader getInstance() {
		// 由系统参数文件中获取存储检测程序.class文件的目录
		rootDir = SystemConfigure.getParValue("pro_path");
		File file = new File(rootDir);
		if (!file.exists()) {
			throw new RuntimeException("自定义文件类加载器:存放检测程序的目录不存在:" + rootDir);
		}
		long update = file.lastModified();
		/**
		 * synchronized锁住的对象是不能为空的,因为已经在静态块中初始化了fileSystemClassloader
		 * ，因此fileSystemClassloader不为null,其实这里可以写成synchronized
		 * (FileSystemClassloader.class)，单列模式的双检测机制就是这样做的。
		 */
		synchronized (fileSystemClassloader) {
			if (update > lastUpdate) {
				fileSystemClassloader = new FileSystemClassloader();
				lastUpdate = update;
			}
			return fileSystemClassloader;
		}
	}

	private FileSystemClassloader() {

	}

	// 不要破话类加载器的双亲委托模式
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		String path = rootDir + name.substring(name.lastIndexOf(".") + 1)
				+ ".class";
		byte[] classData = getClassData(path);
		if (classData == null) {
			throw new ClassNotFoundException("自定义文件类加载器:获取检测程序的字节码为null");
		} else {
			return defineClass(name, classData, 0, classData.length);
		}
	}

	// 读取class文件并封装成字节数组
	private byte[] getClassData(String path) {
		String error = "自定义文件系统类加载器:";
		InputStream in = null;
		ByteArrayOutputStream bos = null;
		byte[] classData = null;
		try {
			in = new FileInputStream(path);
			bos = new ByteArrayOutputStream();
			int bufferSize = 4096;
			byte[] buffer = new byte[bufferSize];
			int byteNumRead = 0;
			while ((byteNumRead = in.read(buffer)) != -1) {
				bos.write(buffer, 0, byteNumRead);
			}
			classData = bos.toByteArray();
			return classData;
		} catch (FileNotFoundException e) {
			System.err.println(error + "文件未找到:" + path);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(error + "读取class文件出错");
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				System.err.println(error + "文件关闭出现异常");
				e.printStackTrace();
			}
		}
		return null;
	}
}
