package com.hewentian.premain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class PreMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("agentArgs : " + agentArgs);

        // 一次只加一个 addTransformer
//        inst.addTransformer(new BaseClassFileTransformer(), true);
        inst.addTransformer(new CustomClassFileTransformer(), true);
    }

    /**
     * 简单
     *
     * @return a well-formed class file buffer (the result of the transform),
     * or <code>null</code> if no transform is performed.
     */
    static class BaseClassFileTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            System.out.println("BaseClassFileTransformer premain load Class: " + className);

            return null;
        }
    }

    /**
     * 自定义
     */
    static class CustomClassFileTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            System.out.println("CustomClassFileTransformer premain load Class: " + className);

            // 操作 HelloWorld 类
            if ("com/hewentian/hello/HelloWorld".equals(className)) {
                try {
                    // 从ClassPool获得CtClass对象
                    final ClassPool classPool = ClassPool.getDefault();
                    final CtClass clazz = classPool.get("com.hewentian.hello.HelloWorld");

                    CtMethod travelWorld = clazz.getDeclaredMethod("travelWorld");

                    // 对 com.hewentian.hello.HelloWorld.travelWorld() 方法进行改写
                    // $0代表的是this，$1代表方法的第一个参数、$2代表方法的第二个参数，以此类推
                    String methodBody = "{"
                            + "System.out.println($1 + \" Ho travelWorld() ---------- \" + new java.util.Date());"
                            + "}";

                    travelWorld.setBody(methodBody);

                    // 修改后的字节码
                    byte[] byteCode = clazz.toBytecode();

                    // Removes this <code>CtClass</code> object from the <code>ClassPool</code>.
                    // If <code>get()</code> in <code>ClassPool</code> is called
                    // with the name of the removed method,
                    // the <code>ClassPool</code> will read the class file again
                    // and constructs another <code>CtClass</code> object representing
                    // the same class.
                    clazz.detach();

                    return byteCode;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
