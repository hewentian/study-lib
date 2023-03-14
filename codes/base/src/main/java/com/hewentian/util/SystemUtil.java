package com.hewentian.util;

import sun.management.VMManagement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>
 * <b>SystemUtil</b> 是
 * </p>
 *
 * @author Tim Ho
 * @since 2022-12-07 14:12:44
 */
public class SystemUtil {
    /**
     * 获取当前进程 pid
     *
     * @return 当前进程 pid
     */
    public static int getCurrentProcessId() {
        int pid = 0;

        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);

            VMManagement management = (VMManagement) jvm.get(runtime);
            Method method = management.getClass().getDeclaredMethod("getProcessId");
            method.setAccessible(true);

            pid = (Integer) method.invoke(management);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pid;
    }
}
