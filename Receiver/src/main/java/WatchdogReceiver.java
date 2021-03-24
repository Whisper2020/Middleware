import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;



public class WatchdogReceiver {

    public static void main(String[] args) throws Exception {
        // 1. 获取连接工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616"
        );

        // 2. 获取一个向activeMq的连接
        Connection connection = factory.createConnection();
        connection.start();

        // 3. 获取session
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Destination queue = session.createQueue("Watchdog");

        // 5 获取消息
        MessageConsumer consumer = session.createConsumer(queue);
        //设定时间为三秒
        long check=System.currentTimeMillis();
        while(System.currentTimeMillis()-check>3) {
            TextMessage message = (TextMessage) consumer.receive(3000);
            if (message == null) {
                System.out.println("[INFO]Target Disconnected.");
            }
        }

    }
}
