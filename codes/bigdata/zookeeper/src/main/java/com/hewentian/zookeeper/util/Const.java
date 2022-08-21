package com.hewentian.zookeeper.util;

public interface Const {
    String ZK_HOSTS = "zookeeper1.hewentian.com:2181,zookeeper2.hewentian.com:2181,zookeeper3.hewentian.com:2181";
    int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/registry";
    String ZK_PROVIDER_PATH = ZK_REGISTRY_PATH + "/provider";

}
