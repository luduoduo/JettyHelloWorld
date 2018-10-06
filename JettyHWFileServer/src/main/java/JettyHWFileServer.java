import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.ResourceService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DecimalFormat;

public class JettyHWFileServer {

    public static void main(String[] args) throws Exception {
        System.out.print("main entry\n");
        //创建一个应用服务监听8080端口
        Server server = new Server(8082);
        System.out.print("new a handler as servlet\n");

        final Logger theLogger=Log.getLogger(ResourceService.class);
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