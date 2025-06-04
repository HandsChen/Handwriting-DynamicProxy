package com.chen.proxy;

import java.lang.reflect.Field;

//动态代理的三要素 类 加载器 handler
public class Main {

    public static void main(String[] args) throws Exception {
        MyInterface proxyObject = MyInterfaceFactory.createProxyObject(new PrintFunctionName());
        proxyObject.func1();
        proxyObject.func2();
        proxyObject.func3();
        System.out.println("___________________");
        MyInterface proxyObject2 = MyInterfaceFactory.createProxyObject(new PrintFunctionName2());
        proxyObject2.func1();
        proxyObject2.func2();
        proxyObject2.func3();
        System.out.println("___________________");
        MyInterface proxyObject3 = MyInterfaceFactory.createProxyObject(new LogHandler(proxyObject));
        proxyObject3.func1();
        proxyObject3.func2();
        proxyObject3.func3();
    }

    static class PrintFunctionName implements MyHandler {

        @Override
        public String functionBody(String methodName) {
            return "System.out.println(\"" + methodName + "\");";
        }
    }

    //如果我们想要额外打印一点东西呢？记住我们永远是在写类而不是逻辑，即字符串
    static class PrintFunctionName2 implements MyHandler {

        @Override
        public String functionBody(String methodName) {
            StringBuilder sb = new StringBuilder();
            return sb.append("System.out.println(\"1\");").append("System.out.println(\"").append(methodName).append("\");").toString();
        }
    }

    static class LogHandler implements MyHandler {
        MyInterface myInterface;
        LogHandler(MyInterface myInterface) {
            this.myInterface = myInterface;
        }

        @Override
        public void setProxy(MyInterface proxy) {
            Class<? extends MyInterface> aClass = proxy.getClass();
            try {
                Field field = aClass.getDeclaredField("myInterface"); //拿到代理对象的属性值
                field.setAccessible(true);
                field.set(proxy,myInterface); //给代理对象中的MyInterface属性赋值
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public String functionBody(String methodName) {
            return "System.out.println(\"before\");\n" +
                    "           myInterface." + methodName + "();\n" +
                    "            System.out.println(\"after\");";
        }
    }
}
