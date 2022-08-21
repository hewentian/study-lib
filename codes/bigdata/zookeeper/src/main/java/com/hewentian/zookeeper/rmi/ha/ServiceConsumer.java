package com.hewentian.zookeeper.rmi.ha;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import com.hewentian.zookeeper.util.Const;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * <p>
 * <b>ServiceConsumer</b> 是 服务消费者
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2019-02-21 15:45:06
 * @since JDK 1.8
 */
public class ServiceConsumer {
    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> urlList = new ArrayList<>();

    public ServiceConsumer() {
        ZooKeeper zk = connectServer();
        if (zk != null) {
            watchNode(zk);
        }
    }

    public <T extends Remote> T lookup() {
        T service = null;
        int size = urlList.size();

        if (size > 0) {
            String url;
            if (size == 1) {
                url = urlList.get(0);
                System.out.println(String.format("using only url: %s", url));
            } else {
                url = urlList.get(ThreadLocalRandom.current().nextInt(size)); // 随机获取一个元素
                System.out.println(String.format("using random url: %s", url));
            }

            service = lookupService(url);
        }

        return service;
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Const.ZK_HOSTS, Const.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });

            latch.await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return zk;
    }

    // 观察 /registry 节点下所有子节点是否有变化
    private void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Const.ZK_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk);
                    }
                }
            });

            List<String> urlListTmp = new ArrayList<>();
            for (String node : nodeList) {
                byte[] data = zk.getData(Const.ZK_REGISTRY_PATH + "/" + node, false, null);
                urlListTmp.add(new String(data));
            }

            System.out.println(String.format("node data: %s", urlListTmp));
            urlList = urlListTmp;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T lookupService(String url) {
        T remote = null;

        try {
            remote = (T) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            if (e instanceof ConnectException) {
                System.out.println(String.format("ConnectException -> url: %s", url));

                // 若连接中断，则使用 urlList 中第一个 RMI 地址来查找
                if (urlList.size() != 0) {
                    url = urlList.get(0);
                    return lookupService(url);
                }
            }

            e.printStackTrace();
        }

        return remote;
    }
}
