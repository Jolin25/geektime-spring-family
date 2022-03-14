package com.woniuxy.myspringmvc.core;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.woniuxy.myspringmvc.annotation.Controller;
import com.woniuxy.myspringmvc.annotation.RequestMapping;

/**
 * 处理器适配器
 * 
 * @author 小虫子的小日常
 *
 */
public class HandlerMapper {
	// Controller实例
	private Map<String, Object> ioc = new Hashtable<String, Object>();

	// Controller实例和URL请求对应的映射
	private Map<String, Object> controllerMapping = new Hashtable<String, Object>();
	// Method实例和URL请求对应的映射
	private Map<String, Method> methodMapping = new Hashtable<String, Method>();
	// 所有的Controller的类全名
	private List<String> classNames = null;

	public HandlerMapper(List<String> classNames) {
		this.classNames = classNames;
		// 1.根据获取的类名们实例化Controller
		doInstance(classNames);

		// 2.建立Controller实例和URL请求的映射以及Method实例和URL请求的映射

		initHandlerMapping();
	}

	/**
	 * 根据获取的类名们（className）实例化Controller
	 * (给有Controller注解的类实例化，因为我们假设用Controller注解来标志是否要实例化该类)
	 * 
	 * @param classNames
	 */
	private void doInstance(List<String> classNames) {
		/*
		 * 判断classNames是否为空
		 */
		if (classNames == null) {
			return;
		}
		/*
		 * 遍历classNames，获取每一个类名对应的Class实例，判断该类上是否有Controller注解，若有，
		 * 则实例化该类，获取该类的类名，改为小驼峰（因为习惯吧），并将该实例和新的名字存在在ioc（用于存放Controller实例）中。
		 */
		for (String className : classNames) {
			try {
				// 获取每一个类全名对应的Class实例
				Class<?> cl = Class.forName(className);
				// 判断该类上是否有Controller注解
				if (cl.isAnnotationPresent(Controller.class)) {
					// 若有，则实例化该类，获取该类的类名,改为小驼峰,并将该实例和新的名字存在在ioc中
					try {
						Object controller = cl.newInstance();
						String simpleName = cl.getSimpleName();
						String newName = (simpleName.charAt(0) + "").toLowerCase() + simpleName.substring(1);
						ioc.put(newName, controller);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 建立Controller实例和URL请求的映射以及Method实例和URL请求的映射
	 * (设置注解RequestMapping为储存URL请求的注解，该注解中存储了作为请求的名字。 通过获取该注解的值，来建立对应的映射)
	 */
	private void initHandlerMapping() {
		if (ioc.values() == null) {
			return;
		}
		/*
		 * 遍历Controller实例（存储在ioc中），检查是否有RequestMapping注解，及其值，建立映射
		 */
		for (Object controller : ioc.values()) {
			// 获取controller的Class实例
			Class<?> cl = controller.getClass();
			// 检查该Class实例是否有RequestMapping注解
			if (!cl.isAnnotationPresent(RequestMapping.class)) {
				continue;
			}
			// 获取该Class实例的RequestMapping注解的值
			String controllerMappingKey = cl.getDeclaredAnnotation(RequestMapping.class).value();
			// 遍历该Controller实例对应的类下面的方法
			// 获取该Class实例的Methods
			Method[] methods = cl.getDeclaredMethods();
			// 遍历
			for (Method method : methods) {
				if (!method.isAnnotationPresent(RequestMapping.class)) {
					continue;
				}
				// 获取该方法上的RequestMapping的值
				String methodMappingKey = method.getDeclaredAnnotation(RequestMapping.class).value();
				// 设置映射，将请求URL和对应的Controller实例以及Method实例放入对应的map中
				String key = controllerMappingKey + methodMappingKey;
				controllerMapping.put(key, controller);
				methodMapping.put(key, method);
			}
		}
	}

	public Map<String, Object> getControllerMapping() {
		return controllerMapping;
	}

	public void setControllerMapping(Map<String, Object> controllerMapping) {
		this.controllerMapping = controllerMapping;
	}

	public Map<String, Method> getMethodMapping() {
		return methodMapping;
	}

	public void setMethodMapping(Map<String, Method> methodMapping) {
		this.methodMapping = methodMapping;
	}

}
