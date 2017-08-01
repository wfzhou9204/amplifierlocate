package indi.ninet.amplifierprove.util;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
/**
 * 
 * @author wfzhou
 * 2017/05/02
 * 获取bean应用类
 *
 */
public class GetBean {
	//获取对象,调用默认构造函数
	public static Object getBean(String src){
		ClassPathResource resource=new ClassPathResource("beans.xml");
		DefaultListableBeanFactory factory=new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(resource);
		 Object bean=factory.getBean(src);
		 if(bean==null)
			 throw new NullPointerException("获取bean时出现null:"+src);
		 return bean;
	}
	//获取bean，调用特定的构造函数
	public static Object getBean(String src,Object... obj){
		//System.out.println("src="+src);
		ClassPathResource resource=new ClassPathResource("beans.xml");
		DefaultListableBeanFactory factory=new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(resource);
		 Object bean=factory.getBean(src,obj);
		 if(bean==null)
			 throw new NullPointerException("获取bean时出现null:"+src+":"+obj);
		 return bean;
	}
}
