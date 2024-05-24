package com.michelle.smartstudy.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SubmissionConsumerManager {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private AmqpAdmin amqpAdmin;

    private final Map<String, SimpleMessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();

    private static Integer msgNum = 0;

    // 动态生成对应队列的Consumer（每个课程都有一个对应的submissionqueue和与其对应的consumer）
    public void addConsumer(String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);  // 设置连接工厂
        container.setQueueNames(queueName); // 指定监听的队列名称
        container.setMessageListener(new MessageListenerAdapter(new Object() {
            // 在收到对应队列中的消息时会执行handleMessage方法
            public void handleMessage(byte[] body) {
                msgNum++;
                // 根据队列名称进行操作(只是为了说明可以获取到队列名称)
                log.info("Received homework for " + queueName + ", msgNum: {}", msgNum);
                // 检查收到的内容
                if (body == null || body.length == 0) {
                    log.error("null msg");
                } else {    // 信息不为空
                    String msg = new String(body);
                    log.info("receive message {}", msg);
                }
                // 这里可以添加更多基于队列名称的逻辑
            }
            /*
            // 在 handleMessage 方法内部，可以显式调用底下这些方法这些辅助方法来处理消息。
            // 定义其他方法
            private void processHomework(String homework) {
                // 解析作业内容或其他处理逻辑
                System.out.println("Processing homework: " + homework);
                // 假设进行一些处理
                String result = analyzeHomework(homework);
                System.out.println("Homework analysis result: " + result);
            }

            private String analyzeHomework(String homework) {
                // 模拟作业分析
                return "Analysis of " + homework;
            } */
        }));
        container.start();  // 启动监听容器
        // 将Container保存到map
        listenerContainers.put(queueName, container);
    }

    // 在教师删除课程时，删除rabbitMQ上对应的submission队列，并删除监听该队列的消费者
    public void removeConsumer(String queueName) {
        SimpleMessageListenerContainer container = listenerContainers.remove(queueName);
        // 删除consumer
        if (container != null) {
            container.stop();
            System.out.println("Consumer for queue " + queueName + " stopped.");
        }

        // 删除队列
        amqpAdmin.deleteQueue(queueName);
        System.out.println("Queue " + queueName + " deleted.");
    }

}
