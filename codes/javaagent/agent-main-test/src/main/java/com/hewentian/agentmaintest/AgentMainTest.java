package com.hewentian.agentmaintest;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;

public class AgentMainTest {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        System.out.println("list all running JVM");

        List<VirtualMachineDescriptor> list = VirtualMachine.list();

        for (VirtualMachineDescriptor vmd : list) {
            System.out.println(vmd.id() + "\t" + vmd.displayName());

            // 找到我们的目标程序，然后加载 agent-main.jar 发送给该虚拟机
            if (vmd.displayName().endsWith("com.hewentian.hello.HelloWorld") || vmd.displayName().endsWith("hello.jar")) {
                System.out.println("found");

                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("/home/hewentian/ProjectD/gitHub/study-lib/codes/javaagent/agent-main/target/agent-main.jar");
                virtualMachine.detach();
            }
        }
    }

}
