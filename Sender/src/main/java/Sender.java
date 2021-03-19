import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

/**
 * @program: activemq_01
 * @ClassName Sender
 * @description: 消息发送
 * @author: muxiaonong
 * @create: 2020-10-02 13:01
 * @Version 1.0
 **/
public class Sender {

    public static void main(String[] args) throws Exception{
        // 1. 获取连接工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616"
        );

        // 2. 获取一个向activeMq的连接
        Connection connection = factory.createConnection();
        // 3. 获取session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4.找目的地，获取destination，消费端，也会从这个目的地取消息
        Queue queue = session.createQueue("user");

        // 5.1 消息创建者
        MessageProducer producer = session.createProducer(queue);

        // consumer --> 消费者
        // producer --> 创建者
        // 5.2. 创建消息
        for (int i = 0; i < 100; i++) {
            TextMessage textMessage = session.createTextMessage("hi："+i);
            // 5.3 向目的地写入消息
            producer.send(textMessage);
            Thread.sleep(1000);
        }

        // 6.关闭连接
        connection.close();

        System.out.println("结束。。。。。");

    }
}
