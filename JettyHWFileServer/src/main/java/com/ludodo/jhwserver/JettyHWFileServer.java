package com.ludodo.jhwserver;

import java.io.IOException;

//servlet
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//jetty
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.ResourceService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;


//util
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

//log4j2
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;


//utils
import com.ludodo.jhw.JettyHWUtils;

public class JettyHWFileServer {

    public static void main(String[] args) throws Exception {
        System.out.print("main entry\n");

        //创建一个应用服务监听8080端口
        Server server = new Server(8082);
        System.out.print("new a handler as servlet\n");


        System.out.println("class name is "+ JettyHWFileServer.class.getName());

        //设置Log4j2
        Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        Logger logger1 = LogManager.getLogger(JettyHWFileServer.class);

        JettyHWUtils.configureLog4j2();

//        logger.trace("this is trace level");
//        logger.debug("this is debug level");
//        logger.info("this is info level");
//        logger.warn("this is warn level");
//        logger.error("this is error level");
//        logger.fatal("this is fatal level");

        logger.error("log4j2 ready 1!");
        logger1.error("log4j2 ready 2!");

//
//        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
//        Map<String, LoggerConfig> map = loggerContext.getConfiguration().getLoggers();
//
//
//        for (LoggerConfig aLoggerConfig:map.values()) {
//            System.out.println(aLoggerConfig.getName() + " : " + aLoggerConfig.getLevel());
//        }


        //设置JETTY库里的一些log，需要使用完整名字空间，因为与Log4j2冲突
        final org.eclipse.jetty.util.log.Logger theLogger=org.eclipse.jetty.util.log.Log.getLogger(ResourceService.class);
        theLogger.setDebugEnabled(true);

        //使用ResourceHandler很容易做成一个文件服务器
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);    //允许访问文件夹

        String osType = System.getProperty("os.name").toLowerCase();
        System.out.println("Current OS is " + osType);

        if (osType.indexOf("mac")>=100) {
            resource_handler.setWelcomeFiles(new String[]{ "null" });
            resource_handler.setResourceBase("/Users/lufei/Pictures/baobao");
        }else if (osType.indexOf("linux")>=0) {
            resource_handler.setWelcomeFiles(new String[]{ "null" });
            resource_handler.setResourceBase("/home/lufei/Pictures/baobao");
        }else
        {
            resource_handler.setWelcomeFiles(new String[]{ "index.html" });
            resource_handler.setResourceBase("./JettyHWFileServer/src/main/resources");
        }

        System.out.println("dir=" + resource_handler.getResourceBase());
        System.out.println("WelcomeFiles=" + resource_handler.getWelcomeFiles()[0]);

        HandlerList handlers = new HandlerList();
        //利用系统提供的两个handler类，装进HandlerList，它们被顺序执行
        handlers.setHandlers(new Handler[] {
                resource_handler,   //HandlerList先尝试执行这个，如果找到文件，则响应；否则进入下一个
                new DefaultHandler() });    //DefaultHandler用于兜底，对找不到的文件给出404
        server.setHandler(handlers);

        //启动应用服务并等待请求
        server.start();
        server.join();
    }
}