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

//自定义一个handler
public class CustomHandler extends AbstractHandler {
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // 设置自定义Header头，建议使用X开头
        response.addHeader("X-Author", "ABC");
        /* 设置字符集 */
        response.setContentType("text/html;charset=utf-8");
        // 输出一段文本，这里为了简单，我们直接使用最原始的方式打印信息
        Logger loggerFS = LogManager.getLogger(JettyHWFileServer.class);
        loggerFS.info("CustomHandler.handle");
    }
}
