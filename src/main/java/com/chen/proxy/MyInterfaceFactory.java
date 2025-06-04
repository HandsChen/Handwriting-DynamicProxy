package com.chen.proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;


public class MyInterfaceFactory {

    private static final AtomicInteger count = new AtomicInteger();

    private static File createJavaFile(String classname, MyHandler myHandler) throws IOException {
        String context = " package com.chen.proxy;\n" +
                "\n" +
                "public class " + classname + " implements MyInterface{\n" +
                "MyInterface myInterface;\n" +
                "    @Override\n" +
                "    public void func1() {\n" +
                "        " + myHandler.functionBody("func1") + "\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void func2() {\n" +
                "       " + myHandler.functionBody("func2") + "\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void func3() {\n" +
                "       " + myHandler.functionBody("func3") + "\n" +
                "    }\n" +
                "}";
        File javaFile = new File(classname + ".java");
        Files.writeString(javaFile.toPath(), context);
        return javaFile;
    }

    //创建一个获取类名的方法
    private static String getClassName() {
        return "MyInterface$proxy" + count.incrementAndGet(); //为了不重复
    }

/*    //创建一个获取方法块内代码的函数
    private static String functionBody(String methodName) {
        return "System.out.println(\"" + methodName + "\");";
    }*/

    //创建一个可以将class文件加载的函数
    private static MyInterface newInstance(String className, MyHandler handler) throws Exception {
        Class<?> aClass = MyInterfaceFactory.class.getClassLoader().loadClass(className);
        Constructor<?> constructor = aClass.getConstructor();
        MyInterface proxyObject =(MyInterface)  constructor.newInstance(); //生成代理对象
        handler.setProxy(proxyObject); //给代理对象赋值同类类型的属性
        return proxyObject;
    }

    //编写一个创建代理对象的函数
    public static MyInterface createProxyObject(MyHandler myHandler) throws Exception {
        String className = getClassName();
        File javaFile = createJavaFile(className, myHandler);  //1.创建 java字符串文件
        System.out.println(JavaCompilerUtil.compile( //2.将字符串文件编译为class文件
                javaFile.getAbsolutePath(),  // 使用绝对路径
                "out/production/Handwriting-DynamicProxy") ? "编译成功" : "编译失败");
        return newInstance("com.chen.proxy." + className, myHandler); //这类要给全类名，即类名前面要补包名
    }

    public static void main(String[] args) throws IOException {
//        File javaFile = createJavaFile(); // 假设这个方法创建了有效的Java文件
//        boolean success = JavaCompilerUtil.compile(
//                javaFile.getAbsolutePath(),  // 使用绝对路径
//                "out/classes");
//        System.out.println(success ? "编译成功" : "编译失败");
    }
}
