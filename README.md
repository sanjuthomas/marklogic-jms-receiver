# Overview
marklogic-jms-receiver is standalone java application to receive the message(s) from a Java Messaging Service (JMS) based Message Oriented Middleware (MOM) and to push those messages into MarkLogic as JSON, XML or Text documents.

# High-Level Architecture Diagram
![MarkLogic JMS Receiver](arch.png)

# Configuration
Please refer to configuration file [here.](https://github.com/sanjuthomas/marklogic-jms-receiver/blob/master/config/connection.properties)
The receiver is tested against Apache Active MQ. (apache-activemq-5.13.3) If you are using a different MOM, please change the initial_context_factory property. You can change the queue/topic name(s) and content provider URL in the same property file.

# How to run the receiver?
Run the Main class with connection.properties file as the first argument.

`java marklogic.jms.receiver.Main connection.properties`

# [Demo Video](https://www.youtube.com/watch?v=SaDUsSHXoQg&feature=youtu.be)
