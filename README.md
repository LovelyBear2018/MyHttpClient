# <center>CrawlerArtifact爬虫抓取框架使用说明</center>
### <center>qq交流群: 237558423</center>
### <center>Revised by LiuZhiXiong</center>

    CrawlerArtifact,是一种通用爬虫框架,为网络爬虫开发提供了多种简洁实用的框架封装,包括HttpCrawler(底层为HttpClient)、HtmlUnit、SeleniumAppIUM等。并在此基础上,增加了一些特色功能,如缓存,请求重试,监控统计信息等。
##如何使用
    无论打算使用哪种封装框架,请先完成以下步骤1~3:
###1、添加maven依赖
    <dependency>
	    <groupId>com.crawler.artifact</groupId>
    	<artifactId>CrawlerArtifact</artifactId>
    	<version>0.0.1</version>
	</dependency>
###2、添加DTD约束文件
    (1)若为开发环境,将DTD文件拷贝至项目根目录  
    (2)若为测试环境或线上环境tomcat服务,将DTD文件拷贝至tomcat/bin目录
###3、添加全局配置文件至 src/main/resource或src/test/resource下,格式为
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE configuration SYSTEM "crawler-1-config.dtd">
    <configuration>
        <settings>
            <log type="log4j" level="info" />
            <cache timeout="300" />
        </settings>
        <properties>
            <property name="connectionTimeout" value="30000" />
            <property name="socketTimeout" value="30000" />
        </properties>
        <mappers>
            <mapper resource="mappers/JdMapper.xml" />
            <pack value="mappers/" />
        </mappers>
    </configuration>  
      
    标签以及具体含义为:  
    <configuration>(必选):全局配置标签  
    <settings>:系统 日志、缓存、监控 等标签的父标签  
    <log>:日志配置标签
        type:日志类型,目前支持类型为log4j,若选择日志框架为log4j,需要在resource目录下添加log4j.properties,格式为
            log4j.logger.crawlerclient=info,crawlerclient
            log4j.appender.crawlerclient=org.apache.log4j.DailyRollingFileAppender
            log4j.appender.crawlerclient.maxFileSize=300MB
            log4j.appender.crawlerclient.MaxBackupIndex=20
            log4j.appender.crawlerclient.File=/opt/applog/crawlerartifact/invoke_info.log
            log4j.appender.crawlerclient.layout=org.apache.log4j.PatternLayout
            log4j.appender.crawlerclient.Encoding=UTF-8
            log4j.appender.crawlerclient.layout.ConversionPattern=[%p] %d{yyyy-MM-dd HH:mm:ss} [%c]- %m%n
        level:日志级别,共四种:info,debug,warn,error
    <cache>:抓取器上下文缓存配置
        timeout:抓取器缓存超时时间,单位:秒
    <properties>:抓取器连接参数配置,配置项(不区分大小写)为
        timeout:超时时间,包含connectiontimeout和sockettimeout
        connectiontimeout:连接超时时间,单位:毫秒
        sockettimeout:传输超时时间,单位:毫秒
        maxtotalconnections:最大连接数
        maxconnectionperhost:单个ip最大连接数
        maxtrytimestofetch:重试次数
        isfollowredirects:是否支持302跳转
     <mappers>(必选):mapper父标签
     <mapper>:
        location:mapper.xml路径
     <pack>:
        value:mapper.xml所在文件夹路径
###4、按照步骤3的mappers配置的mapper.xml路径,在对应resource路径下建立相应的mapper.xml文件,格式如下:
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE mapper SYSTEM "crawler-1-mapper.dtd" >
    <mapper namespace="com.travelsky.umeckidelay.mappers.JdMapper" crawlertype="httpclient">
    
        <globalheaders id="jdheaders">
            <header name="Host" value="www.jd.com"/>
            <header name="User-Agent"
                    value="Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:56.0) Gecko/20100101 Firefox/56.0"/>
            <header name="Accept" value="text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"/>
            <header name="Accept-Encoding" value="gzip, deflate"/>
            <header name="Accept-Language" value="zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"/>
            <header name="Connection" value="keep-alive"/>
            <header name="Upgrade-Insecure-Requests" value="1"/>
        </globalheaders>
        <charset>utf-8</charset>
        <settings>
            <cache timeout="300"/>
        </settings>
        <get id="login">
            <url value="https://www.jd.com/"/>
            <charset>gbk</charset>
            <headers refid="jdheaders">
                <header name="referer" value="http://www.airchina.com"/>
            <headers>
            <retry>
                <times value="3"/>
                <means>
                    <mean type="code" value="200"/>
                    <mean type="content" value="lzx"/>
                </means>
            </retry>
            <properties resume="true">
                <property name="connection" value="200000"/>
            </properties>
        </get>
    </mapper>
    标签以及具体含义为: 
    <mapper>(必选):包含整个配置信息
        namespace(必选):对应的mapper接口,供SVC调用
        crawlertype:抓取器类型,取值为httpclient,htmlunit,selenium
    <globalheaders>:全局请求头,可以有多个
        id:全局请求头id
    <header>:单个请求头
        name:请求头键
        value:请求头值
    <charset>:全局编码类型,值为utf-8,gb2312,iso-8859-1
    <settings>:参考步骤3讲解,此处setttings配置会覆盖全局settings配置,目前只支持log覆盖
    <proxy>:代理配置
    <schedule>:定时获取验证码
        id:mapper接口方法签名对应id
        location:获取验证码实现类,该实现类需要自己定义并实现AuthCodeProcessor接口,重写buildAuthCodeCacheBean方法,返回  
        AuthCodeCacheBean对象,对象关键值为验证码,参数,抓取器对象
    <get><post><option><head><put><delete>:六大请求方法
        id:mapper接口方法签名对应id
    <url>:请求对应url
    <charset>:编码类型,会在本次请求覆盖全局charset
    <headers>:本次请求的请求头
        refid:值为全局请求头的id
    <header>:单个请求头,若name与全局请求头重复,则覆盖全局请求头
            若value值存在变量,可以通过添加动态参数赋值,格式为{index},index为调用方法时,传入的String数组下标
    <param>:post请求参数
            若value值存在变量,可以通过添加动态参数赋值,格式为{index},index为调用方法时,传入的String数组下标
    <retry>:本次请求添加重试机制
    <times>:最多重试次数
    <mean>:重试判断条件
        type:判断类型,分别为code,content,cookie,location
        value:判断值,code值判断请求返回码是否等于value,content值判断响应页面是否包含value,cookie值判断返回cookie是否包含key为  
        value的cookie,location值判断响应location是否以value开头
    <properties>:连接参数,覆盖步骤3的properties
        resume:请求完毕是否恢复默认全局连接参数
###5、按照mapper标签的namespace配置创建接口,例如按照上面配置,在com.travelsky.umeckidelay.mappers包下建立JdMapper接口    
    public interface JdMapper {
    
    }
###6、根据配置的schedule,get,post,option,head,put,delete标签的id,建立相应的方法,返回值为Response    
    public interface JdMapper {
        Response login();
    }
###7、给mapper添加@Crawler注解,选取抓取器类型,value值为CrawlerType.HTTPCLIENT,CrawlerType.HTMLUNIT,CrawlerType.SELENIUM
    @Crawler(CrawlerType.HTTPCLIENT)
    public interface JdMapper {
        Response login();
    }
    注:若不添加此注解,则采用mapper标签的crawlertype属性配置选取抓取器,若crawlertype属性未配置,则采用CrawlerType.HTTPCLIENT作为抓取器
###8、为对应方法添加@Url,@RequestMethod,@Charset注解
    @Crawler(CrawlerType.HTTPCLIENT)
    public interface JdMapper {    
        @Url("https://www.jd.com/")
        @Charset(CharSetType.UTF8)
        @RequestMethod(HttpMethodType.GET)
        Response login();   
    }
    注:@Url注解值为url字符串
       @Charset:值为CharSetType枚举类型
       @RequestMethod值为HttpMethodType枚举类型
       这部分注解值若与mapper.xml配置内容重复,则覆盖mapper.xml相关内容
###9、添加@Start注解
    @Crawler(CrawlerType.HTTPCLIENT)
    public interface JdMapper {   
        @Start
        @Url("https://www.jd.com/")
        @Charset(CharSetType.UTF8)
        @RequestMethod(HttpMethodType.GET)
        Response login();    
    }
    注:若该方法对应请求为SVC中的首次请求,需添加@Start注解
    
###10、spring svc.xml配置
    <import resource="classpath:applicationContext-CrawlerArtifact-svc.xml" />
    <aop:aspectj-autoproxy />
    <bean id="crawlerSessionFactory" class="com.travelsky.beans.CrawlerSessionFactoryBean">
        <property name="configLocation" value="classpath:crawler-config.xml" />
    </bean>
    <bean id="jdMapper" class="com.travelsky.beans.MapperFactoryBean">
        <property name="mapperInterface" value="com.crawler.artifact.mappers.JdMapper" />
        <property name="crawlerSessionFactory" ref="crawlerSessionFactory" />
    </bean> 
    注:crawlerSessionFactory的value值为全局配置xml路径,根据自己实际路径替换
    jdMapper的value值为创建的Mapper接口全路径,根据自己实际路径替换
    
###11、创建SVC并在spring svc.xml配置
    public class JdSVC implements IDelayWithChangeSVC{  
        @Resource
        JdMapper jdMapper;
    
        @Session
        @Proxy
        @Override
        public CrawlerResult jd_login(String sessionId){
            Response response = jdMapper.login();
            CrawlerResult crawlerResult = new CrawlerResult();
            crawlerResult.setResultCode(1);
            crawlerResult.setProxy(response.getProxy());
    
            return crawlerResult;
        }
    }
    <bean id="jdSVC" class="com.travelsky.umeckidelay.service.impl.JdSVC">
        <property name="jdMapper" ref="jdMapper" />
    </bean>
    注:(1)创建jdMapper并添加@Resource注解,同时添加get和set方法
###12、在SVC方法添加@Session和@Proxy(可选)
    public class JdSVC implements IDelayWithChangeSVC{  
        @Resource
        JdMapper jdMapper;
                        
        @Session
        @Override
        public CrawlerResult getFlightList(String sessionId){
             Response response = jdMapper.getFlightList_login();
             CrawlerResult crawlerResult = new CrawlerResult();
             crawlerResult.setResultCode(1);
             crawlerResult.setProxy(response.getProxy());           
             return crawlerResult;
        }
    }
    注:@Session注解表示可以按照上次抓取环境继续抓取,需遵守相应规约(见文章末尾<几个约束>第(1)条)
###13、Mapper接口的方法内传参
		Response getFlightList_login(@Url String url);
		方法参数注解类型分为五种:
		@Url:对应形参类型为String
		@Headers:对应形参类型为Map或者String[]。若为Map,则与mapper.xml配置合并,若重复,则覆盖;若为String[]则按照{index}格式对mapper.xml文件内容赋值
		@Params:对应形参类型为Map或者String[]或String。若为Map,则与mapper.xml配置合并,若重复,则覆盖;若为String[]则按照{index}格式对mapper.xml文件内容赋值,若为String则忽略mapper.xml配置内容。
		@Cookies:对应形参类型为LoginCrawlerCookie
		@Proxy:对应形参类型为String或HttpHost

##几个约束
    (1)若需要根据上次抓取上下文继续抓取,需要添加@Session注解,同时SVC方法形参格式满足:(1)会话ID的形参名必须为SessionID,不区分大小写,形参位置无要求,形参个数无要求;或者(2)形参个数为1个,形参类型为Java Bean对象,该对象包含成员变量SessionID,不区分大小写,并包含get,set方法
    (2)SVC的Mapper成员变量名称必须以mapper结尾
    (3)Mapper接口内方法名称必须以SVC内方法名作为前缀,后缀根据url自己取名称,中间以_分隔,例如SVC方法名为login(),url为http://www.member.com,则mapper接口内方法名取名为login_member()
    (4)若xml中配置url值包含 & 符号,将 & 改为 &amp;