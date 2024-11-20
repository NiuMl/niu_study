package com.niuml.niu_study_canal;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;
import java.util.List;


import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;




/***
 * @author niumengliang
 * Date:2024/9/15
 * Time:17:46
 */
public class TTTest {

    private final static int BATCH_SIZE = 1000;
    public static void main(String[] args) {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.1.4", 11111), "example", "", "");
        try {
            //打开连接
            connector.connect();
            //订阅数据库表,全部表
            connector.subscribe(".*\\..*");
            //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();
            while (true) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(BATCH_SIZE);
                //获取批量ID
                long batchId = message.getId();
                //获取批量的数量
                int size = message.getEntries().size();
                //如果没有数据
                if (batchId == -1 || size == 0) {
                    try {
                        //线程休眠2秒
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //如果有数据,处理数据
                    printEntry(message.getEntries());
                }
                //进行 batch id 的确认。确认之后，小于等于此 batchId 的 Message 都会被确认。
//                connector.ack(batchId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }
    /**
     * 打印canal server解析binlog获得的实体类信息
     */
    private static void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                //开启/关闭事务的实体类型，跳过
                continue;
            }
            //RowChange对象，包含了一行数据变化的所有特征
            //比如isDdl 是否是ddl变更操作 sql 具体的ddl sql beforeColumns afterColumns 变更前后的数据字段等等
            RowChange rowChage;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }
            //获取操作类型：insert/update/delete类型
            EventType eventType = rowChage.getEventType();
            //打印Header信息
            System.out.println(String.format("================》; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));
            //判断是否是DDL语句
            if (rowChage.getIsDdl()) {
                System.out.println("================》;isDdl: true,sql:" + rowChage.getSql());
            }
            //获取RowChange对象里的每一行数据，打印出来
            for (RowData rowData : rowChage.getRowDatasList()) {
                //如果是删除语句
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                    rowData.getBeforeColumnsList().stream().filter(Column::getIsKey).findFirst().ifPresent(a->{
                        System.out.println("delete from `"+entry.getHeader().getSchemaName()+"`.`"+
                                entry.getHeader().getTableName()+"` where "+a.getName()+"="+a.getValue());
                    });
                    //如果是新增语句
                } else if (eventType == EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                    String key = rowData.getAfterColumnsList().stream().map(a-> "`"+a.getName()+"`").collect(Collectors.joining(","));
                    String value = rowData.getAfterColumnsList().stream().map(a->{
                        if(a.getMysqlType().equals("int")){
                            return a.getValue();
                        }else{
                            return "'"+a.getValue()+"'";
                        }
                    }).collect(Collectors.joining(","));
                    System.out.println("insert into `"+entry.getHeader().getSchemaName()+"`.`"+ entry.getHeader().getTableName()+"`("+key+") values("+value+")");
                    //如果是更新的语句
                } else {
                    //变更前的数据
                    System.out.println("------->; before");
                    printColumn(rowData.getBeforeColumnsList());
                    //变更后的数据
                    System.out.println("------->; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() +
                    "    update=" + column.getUpdated()+"      column_type="+column.getMysqlType()+"  主键="+column.getIsKey());
        }
    }









//    //Canal服务地址
//    private static final String SERVER_ADDRESS = "192.168.1.2";
//
//    //Canal Server 服务端口号
//    private static final Integer PORT = 11111;
//
//    //目的地，其实Canal Service内部有一个队列,和配置文件中一致即可，参考【修改instance.properties】图中
//    private static final String DESTINATION = "example";
//
//    //用户名和密码，但是目前不支持，只能为空
//    private static final String USERNAME = "";
//
//    //用户名和密码，但是目前不支持，只能为空
//    private static final String PASSWORD= "";
//
//    public static void main(String[] args){
//        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress(SERVER_ADDRESS, PORT), DESTINATION, USERNAME, PASSWORD);
//        canalConnector.connect();
//        //订阅所有消息
//        canalConnector.subscribe(".*\\..*");
//        // canalConnector.subscribe("test1.*"); 只订阅test1数据库下的所有表
//        //恢复到之前同步的那个位置
//        canalConnector.rollback();
//
//        for(;;){
//            //获取指定数量的数据，但是不做确认标记，下一次取还会取到这些信息。 注：不会阻塞，若不够100，则有多少返回多少
//            Message message = canalConnector.getWithoutAck(100);
//            //获取消息id
//            long batchId = message.getId();
//            if(batchId != -1){
//                System.out.println("msgId -> " + batchId);
//                printEnity(message.getEntries());
//                //提交确认
////                canalConnector.ack(batchId);
//                //处理失败，回滚数据
//                //canalConnector.rollback(batchId);
//            }
//        }
//    }
//
//    private static void printEnity(List<CanalEntry.Entry> entries) {
//        for (CanalEntry.Entry entry : entries) {
//            if(entry.getEntryType() != CanalEntry.EntryType.ROWDATA){
//                continue;
//            }
//            try{
//                // 序列化数据
//                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
//                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
//                    System.out.println(rowChange.getEventType());
//                    switch (rowChange.getEventType()){
//                        //如果希望监听多种事件，可以手动增加case
//                        case INSERT:
//                            // 表名
//                            String tableName = entry.getHeader().getTableName();
//                            //System.out.println("表名："+tableName);
//                            //测试users表进行映射处
//                            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
//                            //for(CanalEntry.Column c:afterColumnsList){
//                            //	System.out.println("字段："+c.getName()+"值："+c.getValue());
//                            //}
//
//                            System.out.println(afterColumnsList);
//                            break;
//                        case UPDATE:
//                            List<CanalEntry.Column> afterColumnsList2 = rowData.getAfterColumnsList();
//                            System.out.println("新插入的数据是：" + afterColumnsList2);
//                            break;
//                        case DELETE:
//                            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
//                            System.out.println("被删除的数据是：" + beforeColumnsList);
//                            break;
//                        default:
//                    }
//                }
//            } catch (InvalidProtocolBufferException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
