package com.chen.proxy;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Java 文件编译工具类
 */
public class JavaCompilerUtil {

    /**
     * 编译单个 Java 文件
     * @param javaFilePath Java 文件路径
     * @param outputDir 编译输出目录
     * @return 编译是否成功
     */
    public static boolean compile(String javaFilePath, String outputDir) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("JDK 未安装或未配置正确 (找不到 javac)");
            return false;
        }

        // 确保输出目录存在
        new File(outputDir).mkdirs();

        // 使用标准文件路径而不是 Path.toString()
        File javaFile = new File(javaFilePath);

        // 准备编译参数
        String[] options = new String[] {
                "-d",
                outputDir,
                javaFile.getAbsolutePath()  // 使用绝对路径
        };

        int compilationResult = compiler.run(
                null, null, null, options);

        return compilationResult == 0;
    }

}