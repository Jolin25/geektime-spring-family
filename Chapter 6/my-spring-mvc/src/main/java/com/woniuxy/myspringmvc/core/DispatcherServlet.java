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
import com.woniuxy.myspringmvc.annotation.ResponseBody;
import com.woniuxy.myspringmvc.annotation.ResponseForward;
import com.woniuxy.myspringmvc.annotation.ResponseRedirect;

/**
 * 1.本质就是一个Servlet
 * 2.DispatcherServlet ： MVC 框架之下的前端控制器
 * Web容器（tomcat）启动后就加载该Servlet（web.xml配置的），
 * 并调用init函数： 负责对映射器的初始化
 * 3.tomcat工作流程：
 * 当客户请求某个资源时，
 * HTTP 服务器会用一个 ServletRequest 对象把客户的请求信息封装起来，
 * 然后调用 Servlet 容器的 service 方法，
 * Servlet 容器拿到请求后，根据请求的 URL 和 Servlet 的映射关系，找到相应的 Servlet，
 * 如果 Servlet 还没有被加载，就用反射机制创建这个 Servlet，
 * 并调用 Servlet 的 init 方法来完成初始化，接着调用 Servlet 的 service 方法来处理请求，
 * 把 ServletResponse 对象返回给 HTTP 服务器，HTTP 服务器会把响应发送给客户端。
 *
 * @author 小虫子的小日常
 */
public class DispatcherServlet extends HttpServlet {

    // 配置文件实例
    Properties properties = new Properties();

    // Controller类名
    List<String> classNames = new ArrayList<String>();
    HandlerAdapter adapter = null;
    HandlerMapper mapper = null;

    /**
     * DispatcherServlet初始化函数（通过web.xml配置为服务器一开始就会对该Servlet进行初始化，而不是等到有前端请求该Servlet才初始化）
     * 该方法内负责对映射器的初始化，即建立Controller实例和URL请求的映射，Method实例和URL请求的映射
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
        // DONE_Joly:Method实例是什么 ---> 拿来调的方法啊，url指明了要调用哪个类的哪个方法
        // 创建HandlerMapper实例（实例化Controller，建立Controller实例和Method实例与URL请求之间里映射）
        mapper = new HandlerMapper(classNames);
        // 创建HandlerAdapter实例(用于service方法里，adapter根据URL请求在映射中找到对应的实例，然后执行方法)
        adapter = new HandlerAdapter(mapper);
    }

    /**
     * 重写的HttpServlet的service方法：这样的话tomcat接收到请求了之后，tomcat会调用这个方法
     * service()
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // url与（Contoller以及Method）进行匹配，匹配成功后执行该Method实例，并返回结果
        // 这里的result相当于是Spring MVC 中的Controller的返回值（ModelAndView：相当于是View的初始版以及Model）
        Object result = adapter.doAdapter(request, response);
        /*
         * 这里类似于执行了ViewResolver和视图的作用：
         * 判断执行的controller包里的类的方法是否有ResponseBody注解，若有则将方法的返回值传给客户
         */
        // 获取Method（这里没有做非空判断，默认是可以找到的，实际的话是不一定的）
        // Method method = mapper.getMethodMapping().get(request.getPathInfo());
        Method method = mapper.getMethodMapping().get(request.getServletPath() + request.getPathInfo());
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
            // 设置响应内容类型 。 这里应该是ViewreSolver的作用
            response.setContentType("application/json;charset=utf-8");
            ObjectMapper om = new ObjectMapper();
            // 将指定对象转为json，并利用指定输出流输出json。这里是视图的作用
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
     * @param packageName Controller的包名
     */
    private void doScanner(String packageName) {
        // 利用包名转成路径名
        // TODO_Joly: 看一下正则表达式
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
                // 不够严谨，应该不仅根据配置文件中设置的要扫描的package，还要根据该package下面的类上有没有注解来决定 ---> 后面实例化Controller的时候，判断Controller注解了
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
