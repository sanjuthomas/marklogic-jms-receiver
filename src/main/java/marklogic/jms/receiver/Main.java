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
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class Main {
    
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

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
        final String TYPES = cProperties.getProperty(ConnectionProperties.TYPES);
        final Properties connection = new Properties();
        connection.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        connection.put(Context.PROVIDER_URL, CONTEXT_PROVIDER_URL);
        
        for(final String type : TYPES.split(",")){
            final String names = cProperties.getProperty(type.trim());
            for(final String name : names.split(",")){
                if(Queue.class.getSimpleName().toLowerCase().equals(type)){
                    logger.info("Type :", type);
                    logger.info("Name :", name);
                    connection.put(type + "." + name, name);
                    final InitialContext initialContext = new InitialContext(connection);
                    final Queue queue = (Queue) initialContext.lookup(name);
                    final QueueConnectionFactory connFactory = (QueueConnectionFactory) initialContext.lookup(QueueConnectionFactory.class.getSimpleName());

                    final QueueConnection queueConn = connFactory.createQueueConnection();
                    final QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                    final QueueReceiver queueReceiver = queueSession.createReceiver(queue);
                    
                    final AsyncReceiver asyncReceiver = new AsyncReceiver(cProperties);
                    queueReceiver.setMessageListener(asyncReceiver);

                    queueConn.setExceptionListener(asyncReceiver);
                    queueConn.start();
                }else if(Topic.class.getSimpleName().toLowerCase().equals(type)){
                    logger.info("Type :", type);
                    logger.info("Name :", name);
                    connection.put(type + "." + name, name);
                    final InitialContext initialContext = new InitialContext(connection);
                    final Topic topic = (Topic) initialContext.lookup(name);
                    final TopicConnectionFactory topicFactory = (TopicConnectionFactory) initialContext.lookup(TopicConnectionFactory.class.getSimpleName());

                    final TopicConnection topicConn = topicFactory.createTopicConnection();
                    final TopicSession topicSession = topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
                    final TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
                    
                    final AsyncReceiver asyncReceiver = new AsyncReceiver(cProperties);
                    topicSubscriber.setMessageListener(asyncReceiver);

                    topicConn.setExceptionListener(asyncReceiver);
                    topicConn.start();
                }else{
                    logger.error("Unknown type: ", type);
                    throw new RuntimeException("Unknown type: "+ type);
                }
            }
        }
    }
}
