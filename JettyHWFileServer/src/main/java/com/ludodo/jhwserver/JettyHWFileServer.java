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
import org.eclipse.jetty.server.handler.*;

import org.eclipse.jetty.server.Handler;


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


//utils
import com.ludodo.jhw.JettyHWUtils;

public class JettyHWFileServer {

    public static void main(String[] args) throws Exception {
        System.out.print("main entry\n");

        //设置Log4j2
        JettyHWUtils.configureLog4j2();
        Logger loggerRoot = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        Logger loggerFS = LogManager.getLogger(JettyHWFileServer.class);
        loggerFS.trace("visible @ trace level");
        loggerFS.debug("visible @ debug level");
        loggerFS.info("visible @ info level");
        loggerFS.warn("visible @ warn level");
        loggerFS.error("visible @ error level");
        loggerFS.fatal("visible @ fatal level");


        //创建一个应用服务监听端口
        int port=8082;
        Server server = new Server(port);
        System.out.println("new a handler as servlet @"+port);

        //设置JETTY库里的一些log，需要使用完整名字空间，因为与Log4j2冲突。不过这里是基于jetty使用了java logger的前提。
        final org.eclipse.jetty.util.log.Logger theLogger=org.eclipse.jetty.util.log.Log.getLogger(ResourceService.class);
        theLogger.setDebugEnabled(true);

        //使用ResourceHandler很容易做成一个文件服务器
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);    //允许访问文件夹

        String osType = System.getProperty("os.name").toLowerCase();
        loggerFS.info("Current OS is " + osType);

        if (osType.contains("mac")) {
            resource_handler.setWelcomeFiles(new String[]{ "null" });
            resource_handler.setResourceBase("/Users/lufei/Pictures/baobao");
        }else if (osType.contains("linux")) {
            resource_handler.setWelcomeFiles(new String[]{ "null" });
            resource_handler.setResourceBase("/home/lufei/Pictures/baobao");
        }else
        {
            resource_handler.setWelcomeFiles(new String[]{ "index.html" });
            resource_handler.setResourceBase("./JettyHWFileServer/src/main/resources");
        }

        loggerFS.info("accessible dir = " + resource_handler.getResourceBase());
        loggerFS.info("WelcomeFiles = " + resource_handler.getWelcomeFiles()[0]);

//        handlerList 顺序执行，成功则停
//        HandlerList handlers = new HandlerList();
//        //利用系统提供的两个handler类，装进HandlerList，它们被顺序执行
//        handlers.setHandlers(new Handler[] {
//                resource_handler,   //HandlerList先尝试执行这个，如果找到文件，则响应；否则进入下一个
//                new DefaultHandler()
//        });    //DefaultHandler用于兜底，对找不到的文件给出404
//        server.setHandler(handlers);

//        handlerCollection 顺序执行，全部执行
        HandlerCollection handlerCollect = new HandlerCollection();
        CustomHandler customHandler = new CustomHandler();
        handlerCollect.addHandler(customHandler);
        handlerCollect.addHandler(resource_handler);
        server.setHandler(handlerCollect);

        //启动应用服务并等待请求
        server.start();
        server.join();
    }
}