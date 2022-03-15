package com.woniuxy.myspringmvc.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理器适配器
 *
 * @author 小虫子的小日常
 */
public class HandlerAdapter {
    private HandlerMapper mapper = null;

    public HandlerAdapter(HandlerMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 当请求进来时，根据URL请求在映射中找到对应的Controller实例和Method实例 ，执行方法。
     *
     * @param request
     * @param response
     */
    public Object doAdapter(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        // 获取URL请求中我们需要的部分
        // String path = request.getPathInfo();
        String path = request.getServletPath() + request.getPathInfo();
        // 根据path找到对应的实例
        Object controller = mapper.getControllerMapping().get(path);
        Method method = mapper.getMethodMapping().get(path);
        try {
            // 要判断能不能找到对应的实例哟，否则invoke的时候是可能出现空指针异常的
            if (method != null && controller != null) {
                /*
                 * 参数处理： 根据该method的形参列表，决定执行该method的时候传进去的实参列表
                 */
                // 创建为该method准备的实参列表,长度根据形参的长度来确定
                Object[] methodParamValues = new Object[method.getParameterCount()];
                // 获取该method的形参列表
                Parameter[] methodParams = method.getParameters();
                // 获取页面参数(该方法获取的是页面参数的键值对形式，键为String类型的name，值为String[]类型的value)
                Map<String, String[]> pageParams = request.getParameterMap();
                // 遍历形参列表，对形参列表的数据类型进行判断，根据形参列表向实参列表填充数据
                for (int i = 0; i < methodParams.length; i++) {
                    Parameter methodParam = methodParams[i];
                    if (methodParam.getType().getSimpleName().equals("HttpServletRequest")) {
                        methodParamValues[i] = request;
                    } else if (methodParam.getType().getSimpleName().equals("HttpServletResponse")) {
                        methodParamValues[i] = response;
                    } else if (methodParam.getType().getClassLoader() != null) {// 这个方法用来确定这个形参的数据类型是不是自定义的
                        // 如果是自定义类型
                        /*
                         * 那么需要根据页面参数来封装出一个对应的对象，根据PO确定该对象需要哪些属性，
                         * 在页面参数中根据属性查找是否有对应的参数，因为对象的属性可能和方法的其他参数
                         * 重复，所以我们用的是对象名加属性来作为key来寻找。
                         */
                        // 遍历该参数（对象）的属性
                        // 获取该形式参数的Class实例
                        Class<?> cl = methodParam.getType();
                        // 创建该形式参数对应的数据类型的对象
                        Object po = null;
                        try {
                            po = cl.newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        // 获取属性们
                        Field[] fields = cl.getDeclaredFields();
                        // 遍历，赋值
                        for (int j = 0; j < fields.length; j++) {
                            Field field = fields[j];
                            // 获取属性的名称
                            String fieldName = field.getName();
                            // 创建key,形参名.属性名
                            String key = methodParam.getName() + "." + fieldName;
                            // 根据key，到页面参数中查找对应的参数的值
                            String[] pageParamValue = pageParams.get(key);
                            // 若没有找到形参的属性对应的实参，则提示用户
                            if (pageParamValue == null) {
                                try {
                                    response.sendError(404, "参数：" + key + "未传");

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                            // 获取属性的数据类型
                            Class fieldType = field.getType();
                            // 将页面参数中的对应的该属性的参数值和形参的该属性的数据类型传下去，以此获得真正的实参
                            Object realFieldValue = getValue(pageParamValue, fieldType);
                            // 调用setter方法，对该对象设置该属性
                            // 拼接setter方法名
                            String setter = "set" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1);
                            // 获取setter方法
                            try {
                                Method setterMethod = cl.getMethod(setter, fieldType);
                                // 调用setter方法
                                setterMethod.invoke(po, realFieldValue);
                            } catch (NoSuchMethodException | SecurityException e) {
                                e.printStackTrace();
                            }
                            methodParamValues[i] = po;
                        }
                    } else {
                        // 如果是jdk自带的数据类型
                        /*
                         * 根据形参的名称去找页面参数中是否有对应的参数，如果找到了，则将页面参数转换为该形参的数据类型（
                         * 因为从页面上取过来的参数都是String类型）。
                         */
                        String[] pageParamValue = pageParams.get(methodParam.getName());
                        // 如果未找到需要的形参对应的实参，则提示用户
                        if (pageParamValue == null) {
                            try {
                                response.sendError(404, "参数：" + methodParam.getName() + "未传");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                        // 获取形参的数据类型
                        Class methodParamType = methodParam.getType();
                        // 根据形参类型进行数据类型转换，获得真正的实参(将页面参数中对应的参数和形参类型传下去)
                        methodParamValues[i] = getValue(pageParamValue, methodParamType);
                    }

                }
                /*
                 * 利用该对象，执行该方法 。可变长参数的本质就是数组哇
                 */
                result = method.invoke(controller, methodParamValues);

            } else {
                // 如果URL请求输入有误，无法在映射中找到对应的Controller实例和Method实例，则提示用户
                try {
                    response.sendError(404, "映射输入错误啦，你输入的映射是：" + path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将页面参数转换为该形参的数据类型
     *
     * @param pageParamValue
     * @param methodParamType
     * @return
     */
    private Object getValue(String[] pageParamValue, Class methodParamType) {
        Object val = null;
        boolean isArray = false;
        String paramTypeName = null;
        // 判断形参的数据类型是否是数组
        if (methodParamType.isArray()) {
            isArray = true;
        }
        // 获取形参的数据类型的名称
        if (isArray) {
            paramTypeName = methodParamType.getComponentType().getSimpleName();
        } else {
            paramTypeName = methodParamType.getSimpleName();
        }
        // 根据参数的数据类型的名称进行匹配，然后把页面参数进行对应的数据类型转换
        // String
        if (paramTypeName.equals("String")) {
            if (isArray) {
                val = pageParamValue;
            } else {
                val = pageParamValue[0];
            }
        }
        // Integer||int
        if (paramTypeName.equals("Integer") || paramTypeName.equals("int")) {
            if (isArray) {
                if (paramTypeName.equals("int")) {
                    int[] paramVal = new int[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Integer.parseInt(pageParamValue[i]);
                    }
                    val = paramVal;
                } else {
                    Integer[] paramVal = new Integer[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Integer.parseInt(pageParamValue[i]);
                    }
                    val = paramVal;
                }
            } else {
                if (paramTypeName.equals("int")) {
                    val = Integer.parseInt(pageParamValue[0]);
                } else {
                    val = (Integer) Integer.parseInt(pageParamValue[0]);
                }
            }
        }
        // Byte||byte
        if (paramTypeName.equals("Byte") || paramTypeName.equals("byte")) {
            if (isArray) {
                if (paramTypeName.equals("byte")) {
                    byte[] paramVal = new byte[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Byte.parseByte(pageParamValue[i]);
                    }
                    val = paramVal;
                } else {
                    Byte[] paramVal = new Byte[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Byte.parseByte(pageParamValue[i]);
                    }
                    val = paramVal;
                }
            } else {
                if (paramTypeName.equals("byte")) {
                    val = Byte.parseByte(pageParamValue[0]);
                } else {
                    val = (Byte) Byte.parseByte(pageParamValue[0]);
                }
            }
        }
        // short||Short
        if (paramTypeName.equals("Short") || paramTypeName.equals("short")) {
            if (isArray) {
                if (paramTypeName.equals("short")) {
                    short[] paramVal = new short[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Short.parseShort(pageParamValue[i]);
                    }
                    val = paramVal;
                } else {
                    Short[] paramVal = new Short[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Short.parseShort(pageParamValue[i]);
                    }
                    val = paramVal;
                }
            } else {
                if (paramTypeName.equals("short")) {
                    val = Short.parseShort(pageParamValue[0]);
                } else {
                    val = (Short) Short.parseShort(pageParamValue[0]);
                }
            }
        }
        // Long||long
        if (paramTypeName.equals("Long") || paramTypeName.equals("long")) {
            if (isArray) {
                if (paramTypeName.equals("long")) {
                    long[] paramVal = new long[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Long.parseLong(pageParamValue[i]);
                    }
                    val = paramVal;
                } else {
                    Long[] paramVal = new Long[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Long.parseLong(pageParamValue[i]);
                    }
                    val = paramVal;
                }
            } else {
                if (paramTypeName.equals("long")) {
                    val = Long.parseLong(pageParamValue[0]);
                } else {
                    val = (Long) Long.parseLong(pageParamValue[0]);
                }
            }
        }
        // Float||float
        if (paramTypeName.equals("Float") || paramTypeName.equals("float")) {
            if (isArray) {
                if (paramTypeName.equals("float")) {
                    float[] paramVal = new float[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Float.parseFloat(pageParamValue[i]);
                    }
                    val = paramVal;
                } else {
                    Float[] paramVal = new Float[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Float.parseFloat(pageParamValue[i]);
                    }
                    val = paramVal;
                }
            } else {
                if (paramTypeName.equals("float")) {
                    val = Float.parseFloat(pageParamValue[0]);
                } else {
                    val = (Float) Float.parseFloat(pageParamValue[0]);
                }
            }
        }
        // Double||double
        if (paramTypeName.equals("Double") || paramTypeName.equals("double")) {
            if (isArray) {
                if (paramTypeName.equals("double")) {
                    double[] paramVal = new double[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Double.parseDouble(pageParamValue[i]);
                    }
                    val = paramVal;
                } else {
                    Double[] paramVal = new Double[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Double.parseDouble(pageParamValue[i]);
                    }
                    val = paramVal;
                }
            } else {
                if (paramTypeName.equals("double")) {
                    val = Double.parseDouble(pageParamValue[0]);
                } else {
                    val = (Double) Double.parseDouble(pageParamValue[0]);
                }
            }
        }
        // Character||char
        if (paramTypeName.equals("Character") || paramTypeName.equals("char")) {
            if (isArray) {
                if (paramTypeName.equals("char")) {
                    char[] paramVal = new char[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = pageParamValue[i].charAt(0);
                    }
                    val = paramVal;
                } else {
                    Character[] paramVal = new Character[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = pageParamValue[i].charAt(0);
                    }
                    val = paramVal;
                }
            } else {
                if (paramTypeName.equals("char")) {
                    val = pageParamValue[0].charAt(0);
                } else {
                    val = (Character) pageParamValue[0].charAt(0);
                }
            }
        }
        // Boolean||boolean
        if (paramTypeName.equals("Boolean") || paramTypeName.equals("boolean")) {
            if (isArray) {
                if (paramTypeName.equals("boolean")) {
                    boolean[] paramVal = new boolean[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Boolean.parseBoolean(pageParamValue[i]);
                    }
                    val = paramVal;
                } else {
                    Boolean[] paramVal = new Boolean[pageParamValue.length];
                    for (int i = 0; i < paramVal.length; i++) {
                        paramVal[i] = Boolean.parseBoolean(pageParamValue[i]);
                    }
                    val = paramVal;
                }
            } else {
                if (paramTypeName.equals("boolean")) {
                    val = Boolean.parseBoolean(pageParamValue[0]);
                } else {
                    val = (Boolean) Boolean.parseBoolean(pageParamValue[0]);
                }
            }
        }
        // BigDecimal
        if (paramTypeName.equals("BigDecimal")) {
            if (isArray) {
                BigDecimal[] paramVal = new BigDecimal[pageParamValue.length];
                for (int i = 0; i < paramVal.length; i++) {
                    paramVal[i] = new BigDecimal(pageParamValue[i]);
                }
                val = paramVal;
            } else {
                val = new BigDecimal(pageParamValue[0]);
            }
        }
        return val;
    }

}
