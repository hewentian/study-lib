package com.hewentian.mongodb.util;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>
 * <b>Config.java</b> 是读取配置信息的类
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2015-11-12 11:21:54 AM
 * @since JDK 1.8
 */
public final class Config {
    private Config() {
    }

    private static Properties p = null;

    static {
        // default config
        init("config.properties");
    }

    private static void init(String configName) {
        try {
            p = new Properties();
            p.load(Config.class.getClassLoader().getResourceAsStream(configName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key, String defaultValue) {
        if (null == key) {
            return defaultValue;
        }

        String retValue = null;
        if (null != p) {
            retValue = p.getProperty(key, defaultValue);
        }

        return retValue;
    }
}
