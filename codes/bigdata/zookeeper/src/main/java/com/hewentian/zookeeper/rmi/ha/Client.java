package com.hewentian.zookeeper.rmi.ha;

import com.hewentian.zookeeper.rmi.HelloService;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * <p>
 * <b>Client</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2019-02-21 16:04:33
 * @since JDK 1.8
 */
public class Client {
    public static void main(String[] args) throws Exception {
        ServiceConsumer consumer = new ServiceConsumer();

        while (true) {
            HelloService helloService = consumer.lookup();
            String now = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            String result = helloService.sayHello("Tim");

            System.out.println(now + " " + result);
            Thread.sleep(3000);
        }
    }
}
