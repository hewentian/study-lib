package com.hewentian.elasticsearch.util;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <b>ElasticsearchUtil</b> 是 https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/introduction.html
 * </p>
 *
 * @since JDK 1.8
 */
public class ElasticsearchUtil {
    private static final String elasticsearchClusterUsername;
    private static final String elasticsearchClusterPassword;
    private static final String elasticsearchClusterHosts;
    private static RestClient restClient;
    private static ElasticsearchTransport elasticsearchTransport;
    private static ElasticsearchClient elasticsearchClient;
    private static ElasticsearchAsyncClient elasticsearchAsyncClient;

    private ElasticsearchUtil() {
    }

    static {
        elasticsearchClusterUsername = Config.get("elasticsearch.cluster.username", null);
        elasticsearchClusterPassword = Config.get("elasticsearch.cluster.password", null);
        elasticsearchClusterHosts = Config.get("elasticsearch.cluster.hosts", null);

        if (StringUtils.isAnyBlank(elasticsearchClusterUsername, elasticsearchClusterPassword, elasticsearchClusterHosts)) {
            System.err.println("elasticsearch.cluster.username or elasticsearch.cluster.password or elasticsearch.cluster.hosts is blank");
            System.exit(1);
        }
    }

    public static ElasticsearchClient getElasticsearchClient() throws Exception {
        System.out.println("start to init ElasticsearchClient");

        if (null == elasticsearchTransport) {
            initElasticsearchTransport();
        }

        // And create the API client
        elasticsearchClient = new ElasticsearchClient(elasticsearchTransport);

        System.out.println("init ElasticsearchClient successfully");

        return elasticsearchClient;
    }

    public static ElasticsearchAsyncClient getElasticsearchAsyncClient() throws Exception {
        System.out.println("start to init ElasticsearchAsyncClient");

        if (null == elasticsearchTransport) {
            initElasticsearchTransport();
        }

        // And create the API client
        elasticsearchAsyncClient = new ElasticsearchAsyncClient(elasticsearchTransport);

        System.out.println("init ElasticsearchAsyncClient successfully");

        return elasticsearchAsyncClient;
    }

    private static void initElasticsearchTransport() throws Exception {
        List<HttpHost> hostList = new ArrayList<>();

        // Create the low-level client
        for (String host : StringUtils.split(elasticsearchClusterHosts, ",")) {
            String[] schemaHostnamePort = StringUtils.split(host, ":");

            String hostname = schemaHostnamePort[1].replace("//", "");
            int port = Integer.parseInt(schemaHostnamePort[2]);
            String schema = schemaHostnamePort[0];

            hostList.add(new HttpHost(hostname, port, schema));
        }

//        restClient = usernamePasswordAuthentication(hostList);
        restClient = httpCaCrtAuthentication(hostList);

        // Create the transport with a Jackson mapper
        ObjectMapper om = new ObjectMapper();
        JsonpMapper mapper = new JacksonJsonpMapper(om);
        elasticsearchTransport = new RestClientTransport(restClient, mapper);
    }

    private static RestClient usernamePasswordAuthentication(List<HttpHost> hostList) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticsearchClusterUsername, elasticsearchClusterPassword));

        RestClientBuilder builder = RestClient.builder(
                hostList.toArray(new HttpHost[0]))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                });

        return builder.build();
    }

    private static RestClient httpCaCrtAuthentication(List<HttpHost> hostList) throws Exception {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticsearchClusterUsername, elasticsearchClusterPassword));

        URL httpCaCrtResource = ElasticsearchUtil.class.getClassLoader().getResource("certs/http_ca.crt");
        Path caCertificatePath = Paths.get(httpCaCrtResource.toURI());
//        Path caCertificatePath = Paths.get("/path/to/ca.crt");

        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");
        Certificate trustedCa;
        try (InputStream is = Files.newInputStream(caCertificatePath)) {
            trustedCa = factory.generateCertificate(is);
        }
        KeyStore trustStore = KeyStore.getInstance("pkcs12");
        trustStore.load(null, null);
        trustStore.setCertificateEntry("ca", trustedCa);
        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                .loadTrustMaterial(trustStore, null);
        final SSLContext sslContext = sslContextBuilder.build();

        RestClientBuilder builder = RestClient.builder(
                hostList.toArray(new HttpHost[0]))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credentialsProvider));

        return builder.build();
    }

    public static void close() {
        try {
            elasticsearchTransport.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
