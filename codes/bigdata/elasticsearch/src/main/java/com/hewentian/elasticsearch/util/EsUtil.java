package com.hewentian.elasticsearch.util;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.List;

/**
 * <p>
 * <b>EsUtil</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2019-02-27 16:40:37
 * @since JDK 1.8
 */
public class EsUtil {
    private static String esClusterName;
    private static String esClusterTransportHosts;
    private static TransportClient transportClient;

    private EsUtil() {
    }

    static {
        esClusterName = Config.get("elasticsearch.cluster.name", null);
        esClusterTransportHosts = Config.get("elasticsearch.cluster.transport.hosts", null);

        if (StringUtils.isBlank(esClusterName) || StringUtils.isBlank(esClusterTransportHosts)) {
            System.err.println("请设置 elasticsearch.cluster.name 或者 elasticsearch.cluster.transport.hosts");
            System.exit(1);
        }
    }

    public static TransportClient getClient() throws Exception {
        if (null == transportClient) {
            System.out.println("start to init TransportClient");

            Settings esSettings = Settings.builder()
                    .put("cluster.name", esClusterName) // 设置ES实例的名称
                    .put("client.transport.sniff", true) // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                    .build();

            transportClient = new PreBuiltTransportClient(esSettings);

            for (String hostPort : esClusterTransportHosts.split(",")) {
                String host = hostPort.split(":")[0];
                String port = hostPort.split(":")[1]; // http.port default is 9200, tcp.port default is 9300

                transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(host), Integer.valueOf(port)));
            }

            System.out.println("init TransportClient successfully");
        }

        return transportClient;
    }

    public static void connectInfo() throws Exception {
        List<DiscoveryNode> discoveryNodes = getClient().connectedNodes();
        for (DiscoveryNode node : discoveryNodes) {
            System.out.println(node.getHostAddress());
        }
    }

    public static void close() {
        transportClient.close();
    }
}
