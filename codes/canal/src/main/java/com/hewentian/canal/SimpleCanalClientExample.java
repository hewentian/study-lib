package com.hewentian.canal;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;

public class SimpleCanalClientExample {

    public static void main(String args[]) {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("192.168.56.113", 11111),
                "example", "canal", "canal");

        int batchSize = 1000;
        int emptyCount = 0;

        try {
            connector.connect();

            //订阅 监控的 数据库.表
//            connector.subscribe("test.t_user");
            connector.subscribe(".*\\..*");
            connector.rollback();
            int totalEmptyCount = 100;

            while (emptyCount < totalEmptyCount) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                System.out.println("batchId: " + batchId);

                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    System.out.println("empty count: " + emptyCount);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
                }

                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }

            System.out.println("empty too many times, exit");
        } finally {
            connector.disconnect();
        }
    }

    private static void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error, data:" + entry.toString(), e);
            }

            EventType eventType = rowChage.getEventType();
            long delayTime = new Date().getTime() - entry.getHeader().getExecuteTime();
            System.out.println(String.format("================ binlog[%s:%s], name[%s,%s], eventType: %s, delayTime: %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType, delayTime));

            // DDL数据，打印SQL
            if (eventType == EventType.QUERY || rowChage.getIsDdl()) {
                System.out.println("sql -----> " + rowChage.getSql());
            }

            // DML数据，打印字段信息
            for (RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    System.out.println("---------- before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("---------- after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + ", update = " + column.getUpdated());
        }
    }

}
