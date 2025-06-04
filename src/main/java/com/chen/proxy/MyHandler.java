package com.chen.proxy;
//创建MyHandler的意义是为了让类中的方法抽出来，灵活可变
public interface MyHandler {
    String functionBody(String methodName); //我给你一个方法名，我给你一个方法体
    //proxy为代理对象
   default void setProxy(MyInterface proxy){ //能够对MyInterface类型的代理对象中MyInterface属性进行赋值
   };
}
