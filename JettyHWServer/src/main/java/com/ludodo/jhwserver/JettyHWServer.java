package com.ludodo.jhwserver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DecimalFormat;

public class JettyHWServer extends AbstractHandler {
    private void buildHTML(HttpServletResponse response)
    {
        Calendar cDodoBirthDay = Calendar.getInstance();
        cDodoBirthDay.clear();

        Calendar cCurrentDate = Calendar.getInstance();

        // Set the date for both of the calendar instance
        cDodoBirthDay.set(2014, 12, 1);
        try {
            cDodoBirthDay.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-12-01 08:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Print out the dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Date 1: " + sdf.format(cDodoBirthDay.getTime()));
        System.out.println("Date 2: " + sdf.format(cCurrentDate.getTime()));

        DecimalFormat decFormat = new DecimalFormat("#,###");

        // Get the represented date in milliseconds
        long time1 = cDodoBirthDay.getTimeInMillis();
        long time2 = cCurrentDate.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = time2 - time1;

        // Difference in seconds
        long diffSec = diff / 1000;
        System.out.println("Difference in seconds " + diffSec);

        // Difference in minutes
        long diffMin = diff / (60 * 1000);
        System.out.println("Difference in minutes " + diffMin);

        // Difference in hours
        long diffHours = diff / (60 * 60 * 1000);
        System.out.println("Difference in hours " + diffHours);

        // Difference in days
        long diffDays = diff / (24 * 60 * 60 * 1000);
        System.out.println("Difference in days " + diffDays);


        try {
            response.getWriter().println("<h1>Hello World！</h1>");
            response.getWriter().println("<h1>Dodo 来到这个世界已经：</h1>");
            response.getWriter().println("<h2>--->  " + decFormat.format(diffSec) + " 秒</h2>");
            response.getWriter().println("<h2>--->  " + decFormat.format(diffMin) + " 分钟</h2>");
             response.getWriter().println("<h2>--->  " + decFormat.format(diffHours) + " 小时</h2>");
            response.getWriter().println("<h2>--->  " + decFormat.format(diffDays) + " 天</h2>");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        System.out.print("handle a request: http request is \n");
        System.out.println(request);

        // 声明response的编码和文件类型
        response.setContentType("text/html; charset=utf-8");

        // 声明返回状态码
        response.setStatus(HttpServletResponse.SC_OK);

//        // 请求的返回值
//        try {
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            System.out.println("sleep failed");
//        }

        buildHTML(response);

        // 通知Jettyrequest使用此处理器处理请求
        baseRequest.setHandled(true);
    }

    public static void main(String[] args) throws Exception {
        System.out.print("main entry\n");
        //创建一个应用服务监听8080端口
        Server server = new Server(8080);
        System.out.print("new a handler as servlet\n");
        server.setHandler(new JettyHWServer());

        //启动应用服务并等待请求
        server.start();
        server.join();
    }
}