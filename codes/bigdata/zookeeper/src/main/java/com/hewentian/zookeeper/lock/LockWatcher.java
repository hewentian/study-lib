package com.hewentian.zookeeper.lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * <p>
 * <b>LockWatcher</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2019-02-22 23:37:07
 * @since JDK 1.8
 */
public class LockWatcher implements Watcher {

    private DistributedLock distributedLock;
    private DoTemplate doTemplate;

    public LockWatcher(DistributedLock distributedLock, DoTemplate doTemplate) {
        this.distributedLock = distributedLock;
        this.doTemplate = doTemplate;
    }

    public void doSomething() {
        System.out.println(Thread.currentThread().getName() + " 获得锁，准备执行指定任务");
        doTemplate.doInvoke();
        TestLock.latch.countDown();
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(distributedLock.getWaitPath())) {
            System.out.println(Thread.currentThread().getName() + " 收到情报，排在我前面的节点已执行完任务");

            try {
                if (distributedLock.checkMinPath()) {
                    doSomething();
                    distributedLock.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
