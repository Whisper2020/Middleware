import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileTransferReceiver {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factoryA = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD, "tcp://127.0.0.1:61616");

        ActiveMQConnection conn = (ActiveMQConnection) factoryA.createConnection();

        conn.start();
        ActiveMQSession session = (ActiveMQSession) conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = new ActiveMQQueue("FileTransfer");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(message -> {
            if (message instanceof BlobMessage) {
                BlobMessage blobMessage = (BlobMessage) message;
                try {
                    String fileName = message.getStringProperty("FILE.NAME");
                    System.out.println();
                    message.getLongProperty("FILE.SIZE" + "");
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("请指定文件保存地址");
                    fileChooser.setSelectedFile(new File(fileName));
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        OutputStream os = new FileOutputStream(file);
                        System.out.println("" + fileName);
                        InputStream inputStream = blobMessage.getInputStream();
                        byte[] buff = new byte[256];
                        int len;
                        while ((len = inputStream.read(buff)) > 0)
                            os.write(buff, 0, len);
                        os.close();
                        System.out.println("" + fileName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
