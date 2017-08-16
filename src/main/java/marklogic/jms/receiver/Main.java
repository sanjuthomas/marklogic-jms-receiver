package marklogic.jms.receiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class Main {

    public static void main(final String[] args) throws NamingException, JMSException, FileNotFoundException, IOException {

        if (args.length != 1) {
            System.err.println("######################################");
            System.err.println("marklogic.jms.Main connection.properties");
            System.err.println("######################################");
            throw new IllegalArgumentException("Missing property file!");
        }

        final Properties cProperties = new Properties();
        cProperties.load(new FileInputStream(new File(args[0])));
        final String INITIAL_CONTEXT_FACTORY = cProperties.getProperty(ConnectionProperties.INITIAL_CONTEXT_FACTORY);
        final String CONTEXT_PROVIDER_URL = cProperties.getProperty(ConnectionProperties.CONTEXT_PROVIDER_URL);
        final String QUEUE_NAME = cProperties.getProperty(ConnectionProperties.QUEUE_NAME);
        final String TYPE = cProperties.getProperty(ConnectionProperties.TYPE);

        final Properties connection = new Properties();
        connection.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        connection.put(Context.PROVIDER_URL, CONTEXT_PROVIDER_URL);
        connection.put(TYPE + "." + QUEUE_NAME, QUEUE_NAME);

        final InitialContext initialContext = new InitialContext(connection);
        final Queue queue = (Queue) initialContext.lookup(QUEUE_NAME);
        final QueueConnectionFactory connFactory = (QueueConnectionFactory) initialContext.lookup(QueueConnectionFactory.class.getSimpleName());

        final QueueConnection queueConn = connFactory.createQueueConnection();
        final QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        final QueueReceiver queueReceiver = queueSession.createReceiver(queue);
        
        final AsyncReceiver asyncReceiver = new AsyncReceiver(cProperties);
        queueReceiver.setMessageListener(asyncReceiver);

        queueConn.setExceptionListener(asyncReceiver);
        queueConn.start();
    }

}
