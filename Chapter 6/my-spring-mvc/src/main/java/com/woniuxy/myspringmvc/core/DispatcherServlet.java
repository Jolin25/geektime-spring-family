package com.woniuxy.myspringmvc.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woniuxy.myspringmvc.annotation.Controller;
import com.woniuxy.myspringmvc.annotation.RequestMapping;
import com.woniuxy.myspringmvc.annotation.ResponseBody;
import com.woniuxy.myspringmvc.annotation.ResponseForward;
import com.woniuxy.myspringmvc.annotation.ResponseRedirect;

/**
 * DispatcherServlet 前端控制器？？？中英文对应否？ 服务器一开始就加载该Servlet，并调用init函数， 负责对映射器的初始化
 * 
 * @author 小虫子的小日常
 *
 */
public class DispatcherServlet extends HttpServlet {

	// 配置文件实例
	Properties properties = new Properties();

	// Controller类名
	List<String> classNames = new ArrayList<String>();
	HandlerAdapter adapter = null;
	HandlerMapper mapper = null;

	/**
	 * DispatcherServlet初始化函数（通过web.xml配置为服务器一开始就会对该Servlet进行初始化）
	 * 该方法内负责对映射器的初始化，即建立Controller实例和URL请求的映射，Method实例和URL请求的映射 (non-Javadoc)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("===============================================");
		/*
		 * 1.首先需要找到所有的Controller，获取其类全名
		 */
		// 1.1找到配置文件（properties）内的存储了存放Controller的包的路径的信息
		// 1.1.1找到配置文件（因为配置文件的名字可能会被修改，所以我们把配置文件的名字通常放在了web.xml中，则当配置文件
		// 的名字修改了的时候，我们只需要更改web.xml中的存储该配置文件的名字的值即可。）这样比较灵活吧
		// 1.1.1.1获取配置文件名
		String propertiesName = config.getInitParameter("config");
		// 1.1.1.2加载Properties文件
		loadProperties(propertiesName);
		// 1.2 获取所有Controller的类名（将Controller包名作为参数放进去）
		doScanner(properties.getProperty("package"));

		// 创建HandlerMapper实例（实例化Controller，建立Controller实例和Method实例与URL请求之间里映射）
		mapper = new HandlerMapper(classNames);
		// 创建HandlerAdapter实例(用于service方法里，adapter根据URL请求在映射中找到对应的实例，然后执行方法)
		adapter = new HandlerAdapter(mapper);
	}

	/**
	 * service()
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object result = adapter.doAdapter(request, response);
		/*
		 * 判断执行的controller包里的类的方法是否有ResponseBody注解，若有则将方法的返回值传给客户
		 */
		// 获取Method
		Method method = mapper.getMethodMapping().get(request.getPathInfo());
		/*
		 * Returns any extra path information associated with the URL the client
		 * sent when it made this request. The extra path information follows
		 * the servlet path but precedes the query string and will start with a
		 * "/" character.
		 * 遵循的是servlet的路径
		 */
		// System.out.println(request.getPathInfo());
		// 判断是否有ResponseBody注解
		boolean hasResponseBody = method.isAnnotationPresent(ResponseBody.class);
		if (hasResponseBody) {
			// 如果有ResponseBody，则将返回值输出到客户端
			// 利用json哈
			// 设置响应内容类型
			response.setContentType("application/json;charset=utf-8");
			ObjectMapper om = new ObjectMapper();
			// 将指定对象转为json，并利用指定输出流输出json
			om.writeValue(response.getOutputStream(), result);
			return;
		}
		/*
		 * 判断执行的controller包里的类的方法是否有ResponseRedirect注解，若有则进行重定向
		 */
		boolean hasResponseRedirect = method.isAnnotationPresent(ResponseRedirect.class);
		if (hasResponseRedirect) {
			// 将返回值内容作为重定向目标
			if (result.getClass().getSimpleName().equals("String")) {
				String url = (String) result;
				response.sendRedirect(url);
				return;
			}
		}
		/*
		 * 判断执行的controller包里的类的方法是否有PesponseForward注解，若有则进行请求转发
		 */
		boolean hasResponseForward = method.isAnnotationPresent(ResponseForward.class);
		if (hasResponseForward) {
			if (result.getClass().getSimpleName().equals("String")) {
				String url = (String) result;
				request.getRequestDispatcher(url).forward(request, response);
				return;
			}
		}
	}

	/**
	 * 扫描配置文件中指定的包，获取该包下的所有Controller的类名
	 * 
	 * @param object
	 */
	private void doScanner(String packageName) {
		// 利用包名转成路径名
		String packagePath = packageName.replaceAll("\\.", "/");
		/*
		 * 获取File对象
		 */
		// 获取new FIle（）中的参数，这里用URI
		// ！！！这里的实现大概看一下
		// System.out.println(DispatcherServlet.class.getClassLoader());
		URL url = DispatcherServlet.class.getClassLoader().getResource(packagePath);
		// 得到包的对应File实例
		// url.getFile().replace("%20", " ")
		// 示例：/ D:/Program/
		// Files/Tomecat/webapps/MySpringMVC56/WEB-INF/classes/com/woniuxy/myspringmvc/controller/
		File file = new File(url.getFile().replace("%20", " "));
		// 遍历该file，如果是文件夹则继续遍历该文件夹，如果是文件则放入Controller类名集合中
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				doScanner(packageName + "." + f.getName());
			} else {
				classNames.add(packageName + "." + f.getName().replace(".class", ""));
			}
		}
	}

	/**
	 * 加载配置文件（Properties）
	 * 
	 * @param propertiesName
	 */
	private void loadProperties(String propertiesName) {
		// 获取文件输入流
		InputStream is = DispatcherServlet.class.getClassLoader().getResourceAsStream(propertiesName);
		// 加载配置文件
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {// 释放资源
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
