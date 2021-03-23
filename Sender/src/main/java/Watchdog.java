import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.*;

/**
 * @author Ruzhen Zhang
 */
public class Watchdog {

    private ScheduledExecutorService scheduler= Executors.newScheduledThreadPool((int) 0.3);

    public void startSenderWatchdog(Session session, MessageProducer producer){

        Runnable runnable = () -> {
            TextMessage textMessage = null;
            try {
                textMessage = session.createTextMessage("hello");
            } catch (JMSException e) {
                e.printStackTrace();
            }
            // 5.3 向目的地写入消息
            try {
                producer.send(textMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis()+"...hello");
        };
        //延迟执行时间（秒）
        long delay = 0;
        //执行的时间间隔（秒）
        long period = 1;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        ScheduledExecutorService scheduleExec = new ScheduledThreadPoolExecutor(1,namedThreadFactory);
        scheduleExec.scheduleAtFixedRate(runnable, delay, period, TimeUnit.SECONDS);


    }


}
