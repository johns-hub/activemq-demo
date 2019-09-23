1. MQ学习前言 MQ = 消息中间件		ES 	消息	中间件

    1.1.  消息

   ​	微信，短信，语音		

​	1.2.  中间件

2. MQ如何学，方法讨论

   2.1.  两种老师（上图说话）

   ​	培训

   ​	教育

   2.2.  MQ产品种类

   2.3. 

3. 安装步骤

   3.1 官网下载

   3.2 /opt 目录

   3.3 解压缩 tar -zxvf apache-activemq-5.15.9-bin.tar.gz

   3.4  bin ./activemq star

   3.5 activemq的默认进程端口是61616

   3.6 查看后台进程的方法

```shell
[root@localhost ~]# cd /opt
[root@localhost opt]# wget http://www.apache.org/dyn/closer.cgi?filename=/activemq/5.15.9/apache-activemq-5.15.9-bin.tar.gz&action=download apache-activemq-5.15.9
[root@localhost opt]# mv closer.cgi\?filename=%2Factivemq%2F5.15.9%2Fapache-activemq-5.15.9-bin.tar.gz apache-activemq-5.15.9-bin.tar.gz
[root@localhost opt]# tar -zxvf apache-activemq-5.15.9-bin.tar.gz
[root@localhost opt]# cd /opt/apache-activemq-5.15.9/bin
[root@localhost bin]# ./activemq start
[root@localhost bin]# netstat -anp | grep 61616
bash: netstat: command not found
[root@localhost bin]# yum install net-tools
[root@localhost bin]# netstat -anp | grep 61616
tcp6       0      0 :::61616                :::*                    LISTEN      2954/java
[root@localhost bin]# lsof -i:61616
bash: lsof: command not found
[root@localhost bin]# yum install lsof
[root@localhost bin]# lsof -i:61616
COMMAND  PID USER   FD   TYPE DEVICE SIZE/OFF NODE NAME
java    2954 root  129u  IPv6  23071      0t0  TCP *:61616 (LISTEN)
```

4. ActiveMQ控制台

   后台服务已经启动，前台看不到啊？

   4.1 tomcat 前台出现猫

   ![alt text](http://tomcat.apache.org/res/images/tomcat.png 'tomcat')

   4.2 activemq 前台有图形界面吗？Y

   ```shell
   [root@localhost bin]# firewall-cmd --zone=public --add-port=8161/tcp --permanent
   [root@localhost bin]# firewall-cmd --reload
   ```



   |                                    |                                                          |
   | ---------------------------------- | -------------------------------------------------------- |
   | 第1步:注册驱动(仅仅做一次)         | Class.forName("com.mysql.jdbc.Driver")                   |
   | 第2步:建立连接(Connection)         | conn = DriverManager.getConnection(url, user, password); |
   | 第3步:创建运行SQL的语句(Statement) | stmt = conn.createStatement();                           |
   | 第4步:运行语句                     | ResultSet rs = stmt.executeQuery(sql);                   |
   | 第5步:处理运行结果(ResultSet)      | 省略......                                               |
   | 第6步:释放资源                     | 省略......                                               |



   | Number Of Pending Messages | 等待消费的消息 | 当前未出队的数量。公式=总接收数-总出队列数 |
   | -------------------------- | -------------- | ------------------------------------------ |
   | Number Of Consumers        | 消费者数量     | 消费端的消费者数量                         |
   | Messages Enqueued          | 进队消息数     | 进队总数量，包括出队列的。这个数量只增不减 |
   | Messages Dequeued          | 出队消息数     | 可以理解为消费者消费掉的数量               |

   4.3 消费消息

   ​	4.3.1  先生产，只启动1号消费者，问题：1号消费者能消费消息吗？ Y

   ​	4.3.2  先生产，先启动1号消费者，再启动2号消费者，问题：2号消费者还能消费消息吗？

   ​		a) 1号可以消费		Y

   ​		b) 2号可以消费吗？	N

   ​	4.3.3 先启动2个消费者，再生产6条消息，请问，消费情况如何？

   ​		a) 2个消费者都有6条 

   ​		b) 先到先得，6条全给一个

   ​		c) 一人一半 		Y

5. | 比较项目   | Topic模式队列                                                | Queue模式队列                                                |
   | ---------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
   | 工作模式   | “订阅-发布”模式，如果当前没有订阅者，消息将会被丢弃，如果有多个订阅者，那么这些订阅者都会收到消息 | “负载均衡”模式，如果当前没有消费者，消息也不会丢弃；如果有多个消费者，那么一条消息也只会发送给其中一个消费者，并且要求消费者ack消息。 |
   | 有无状态   | 无状态                                                       | Queue数据默认会在MQ服务器上以文件形式保存，比如ActiveMQ一般保存在$AMQ_HOME/data/kr-store/data下面，也可以配置成DB存储。 |
   | 传递完整性 | 如果没有订阅者，消息会被丢弃                                 | 消息不会丢弃                                                 |
   | 处理效率   | 由于消息要按照订阅者的数量进行复制，所以处理性能会随着订阅者的增加而明显降低，并且还要结合不同消息协议自身的性能差异 | 由于一条消息只发送给一个消费者，所以就算消费者再多，性能也不会有明显降低。当然不同消息协议的具体性能也是有差异的 |

6. 比较

| 特性              | ActiveMQ       | RabbitMQ   | Kafka            | RocketMQ       |
| ----------------- | -------------- | ---------- | ---------------- | -------------- |
| PRODUCER-COMSUMER | 支持           | 支持       | 支持             | 支持           |
| PUBLISH-SUBSCRIBE | 支持           | 支持       | 支持             | 支持           |
| REQUEST-REPLY     | 支持           | 支持       | -                | 支持           |
| API完备性         | 高             | 高         | 高               | 低（静态配置） |
| 多语言支持        | 支持，JAVA优先 | 语言无关   | 支持，JAVA优先   | 支持           |
| 单机吞吐量        | 万级           | 万级       | 十万级           | 单机万级       |
| 消息延迟          | -              | 微秒级     | 毫秒级           | -              |
| 可用性            | 高（主从）     | 高（主从） | 非常高（分布式） | 高             |
| 消息丢失          | -              | 低         | 理论上不丢失     | -              |
| 消息重复          | -              | 可控制     | 理论上会重复     | -              |
| 文档的完备性      | 高             | 高         | 高               | 中             |
| 提供快速入门      | 有             | 有         | 有               | 无             |
| 首次部署难度      | -              | 低         | 中               | 高             |

1. xbean

   ```shell
   [root@localhost conf]# pwd
   /opt/apache-activemq-5.15.9/conf
   [root@localhost conf]# ls -l activemq.xml 
   -rw-r--r--. 1 root root 5911 Mar 15 08:04 activemq.xml
   [root@localhost conf]# cp activemq.xml activemq02.xml 
   
   [root@localhost bin]# pwd
   /opt/apache-activemq-5.15.9/bin
   [root@localhost bin]# ./activemq start xbean:file:/opt/apache-activemq-5.15.9/conf/activemq02.xml
   
   ```

   8. 协议比较

   | 协议    | 描述                                                         |
   | ------- | ------------------------------------------------------------ |
   | TCP     | 默认协议，性能相对可以                                       |
   | NIO     | 基于TCP协议之上的，进行了扩展和优化，具有更好的扩展性        |
   | UDP     | 性能比TCP更好，但是不具有可靠性                              |
   | SSL     | 安全链接                                                     |
   | HTTP(S) | 基于HTTP或者HTTPS                                            |
   | VM      | VM本身不是协议，当客户端和代理在同一个Java虚拟机(vm)中运行时，它们之间需要通信，但不想占有网络通道，而是直接通信，可以使用该方式 |

2. ```shell
   [root@localhost conf]# firewall-cmd --state
   [root@localhost conf]# systemctl stop firewalld
   [root@localhost conf]# firewall-cmd --state
   ```

   9. 安装 mysql5.7

```shell
[root@localhost ~]# yum localinstall https://dev.mysql.com/get/mysql57-community-release-el7-9.noarch.rpm
[root@localhost ~]# yum -y install mysql-community-server
[root@localhost ~]# systemctl start mysqld
[root@localhost ~]# systemctl enable mysqld
[root@localhost ~]# netstat -plntu
[root@localhost ~]# grep 'temporary' /var/log/mysqld.log
[root@localhost ~]# mysql -u root -p

mysql> uninstall plugin validate_password;
Query OK, 0 rows affected (0.01 sec)

mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
Query OK, 0 rows affected (0.00 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.00 sec)

mysql> create user activemq@localhost identified by 'activemq';
Query OK, 0 rows affected (0.00 sec)

mysql> create database activemq;
Query OK, 1 row affected (0.00 sec)

mysql> grant all privileges on activemq.* to activemq@'%' identified by 'activemq';
Query OK, 0 rows affected, 1 warning (0.00 sec)

mysql> SET GLOBAL time_zone = '+8:00';
Query OK, 0 rows affected (0.00 sec)
```

10. activemq jdbc持久化方式

| 修改前KahaDB                                                 | 修改后jdbcPersistenceAdapter                                 |
| :----------------------------------------------------------- | ------------------------------------------------------------ |
| <persistenceAdapter> <kahaDB directory="${activemq.data}/kahadb"/> </persistenceAdapter> | <persistenceAdapter><jdbcPersistenceAdapter dataSource="#my-ds"/>  </persistenceAdapter> |
|                                                              | dataSource指定将要引用的持久化数据库的bean名称，createTablesOnStartup是否在启动的时候创建数据表，默认值是true，这样每次启动都会去创建数据表，一般是第一次启动的时候设置为true之后改成false |

```xml
<persistenceAdapter> 
    <jdbcPersistenceAdapter dataSource="#mysql-ds"/> 
</persistenceAdapter>
<bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close"> 
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/> 
	<property name="url" value="jdbc:mysql://192.168.1.205:3306/activemq?relaxAutoCommit=true"/> 
    <property name="username" value="activemq"/> 
	<property name="password" value="activemq"/> 
	<property name="poolPreparedStatements" value="true"/> 
</bean> 
```



| 修改前persistenceAdapter                                     | 修改后persistenceFactory                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| <persistenceAdapter>   <br/>    <jdbcPersistenceAdapter dataSource="#my-ds"/>  </persistenceAdapter> | <persistenceFactory>         <journalPersistenceAdapterFactory 
    	journalLogFiles="4" 
        journalLogFileSize="32768"
	useJournal="true"
	useQuickJournal="true"
        dataSource="#mysql-ds" dataDirectory="${activemq.base}/data" />    			
</persistenceFactory> |

activemq 启动不起来

```shell
[root@localhost ~]# /opt/apache-activemq-5.15.9/bin/activemq console
[root@localhost ~]# systemctl stop firewalld
```

安装 oh zsh

```bash
[root@localhost ~]# yum install zsh
[root@localhost ~]# yum install git
[root@localhost ~]# sh -c "$(wget https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh -O -)"
```

activemq 集群部署规划列表

| 主机          | ZK集群端口 | AMQ集群bind端口            | AMQ消息TCP端口 | 管理控制台端口 | AMQ节点安装目录             |
| ------------- | ---------- | -------------------------- | -------------- | -------------- | --------------------------- |
| 192.168.1.204 | 2181       | bind="tcp://0.0.0.0:61619" | 61616          | 8161           | /opt/apache-activemq-5.15.9 |
| 192.168.1.205 | 2181       | bind="tcp://0.0.0.0:61619" | 61616          | 8161           | /opt/apache-activemq-5.15.9 |
| 192.168.1.206 | 2181       | bind="tcp://0.0.0.0:61619" | 61616          | 8161           | /opt/apache-activemq-5.15.9 |

zookeeper 集群搭建

```shell
[root@localhost ~]# cd /opt/
[root@localhost opt]# wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.6/zookeeper-3.4.6.tar.gz
[root@localhost opt]# tar cvzf zookeeper-3.4.6.tar.gz
[root@localhost opt]# cd zookeeper-3.4.6/
[root@localhost zookeeper-3.4.6]# mkdir data
[root@localhost zookeeper-3.4.6]# cd data
[root@localhost data]# vi myid 
1
~
"myid" 1L, 2C
[root@localhost data]# cd ../conf/
[root@localhost conf]# cp zoo_sample.cfg zoo.cfg
[root@localhost conf]# vi zoo.cfg
dataDir=/opt/zookeeper-3.4.6/data
server.1=192.168.1.204:2888:3888
server.2=192.168.1.205:2888:3888
server.3=192.168.1.206:2888:3888
~
[root@localhost conf]# cd ../bin/
[root@localhost bin]# ./zkServer.sh start
[root@localhost opt]# tar cvzf zookeeper-3.4.6.tar.gz zookeeper-3.4.6
[root@localhost opt]# scp zookeeper-3.4.6.tar.gz root@192.168.1.205:/opt/
[root@localhost opt]# scp zookeeper-3.4.6.tar.gz root@192.168.1.206:/opt/
[root@localhost bin]# ./zkCli.sh -server 192.168.1.204
[root@localhost bin]# ./zkServer.sh status
```

activemq 高可用集群配置

```xml
<persistenceAdapter>
    <replicatedLevelDB
      directory="${activemq.data}/leveldb"
      replicas="3"
      bind="tcp://0.0.0.0:61619"
      zkAddress="192.168.1.204:2181,192.168.1.205:2181,192.168.1.206:2181"
      zkPath="/activemq/leveldb-stores"
      sync="local_disk"
      />
  </persistenceAdapter>
```

延迟投递和定时投递

| Property name        | type   | description        |
| -------------------- | ------ | ------------------ |
| AMQ_SCHEDULED_DELAY  | long   | 延迟投递时间       |
| AMQ_SCHEDULED_PERIOD | long   | 重复投递的时间间隔 |
| AMQ_SCHEDULED_REPEAT | int    | 重复投递次数       |
| AMQ_SCHEDULED_CRON   | String | Cron表达式         |
|                      |        |                    |

