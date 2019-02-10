package org.eclipse.jetty.embedded;

import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.servlet.*;
//import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.EnumSet;

public class OneServletContext
{
    public static void main( String[] args ) throws Exception
    {
        QueuedThreadPool threadPool = new QueuedThreadPool(6, 3, 120);
        Server server = new Server(threadPool);

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[] {connector});


        ServletContextHandler contextHandler = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.setResourceBase("./");

        // Add dump servlet
        contextHandler.addServlet(contextHandler.addServlet(DumpServlet.class, "/dump/*"), "*.dump");
        contextHandler.addServlet(HelloServlet.class, "/hello/*");
        contextHandler.addServlet(DefaultServlet.class, "/");
        contextHandler.addServlet(AsyncEchoServlet.class, "/async");

        contextHandler.addFilter(TestFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        contextHandler.addFilter(TestFilter.class,"/test", EnumSet.of(DispatcherType.REQUEST,DispatcherType.ASYNC));
        contextHandler.addFilter(TestFilter.class,"*.test", EnumSet.of(DispatcherType.REQUEST,DispatcherType.INCLUDE,DispatcherType.FORWARD));

        //监控 ServletContext 生命周期的变化
        contextHandler.addEventListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent servletContextEvent) {
                System.out.println("ServletContextEvent: " + "contextInitialized");
            }

            @Override
            public void contextDestroyed(ServletContextEvent servletContextEvent) {
                System.out.println("ServletContextEvent: " + "contextDestroyed");
            }
        });

        //监控 Request 生命周期的变化
        contextHandler.addEventListener(new ServletRequestListener() {
            @Override
            public void requestInitialized(ServletRequestEvent servletRequestEvent) {
//                System.out.println("ServletRequestListener: " + "requestInitialized >>>>>");
            }

            @Override
            public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
//                System.out.println("ServletRequestListener: " + "requestDestroyed <<<<<");
            }


        });

        //监控 ServletContextAttribute 生命周期的变化
        contextHandler.addEventListener(new ServletContextAttributeListener() {
            @Override
            public void attributeAdded(ServletContextAttributeEvent var1) {
                System.out.println("ServletContextAttributeListener: " + "attributeAdded");
            }
            @Override
            public void attributeRemoved(ServletContextAttributeEvent var1) {
                System.out.println("ServletContextAttributeListener: " + "attributeRemoved");
            }

            @Override
            public void attributeReplaced(ServletContextAttributeEvent var1) {
                System.out.println("ServletContextAttributeListener: " + "attributeReplaced");
            }
        });

        //监控 ServletContextAttribute 生命周期的变化
        contextHandler.addEventListener(new ContextHandler.ContextScopeListener() {
            @Override
            public void enterScope(ContextHandler.Context context, Request request, Object reason) {
//                System.out.println("ContextScopeListener: " + "enterScope");
            }

            @Override
            public void exitScope(ContextHandler.Context context, Request request) {
//                System.out.println("ContextScopeListener: " + "exitScope");
            }
        });

        server.setHandler(contextHandler);

        server.start();
//        server.dumpStdErr();
        server.join();
    }

    public static class TestFilter implements Filter
    {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException
        {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {
//            System.out.println("TestFilter: preprocess");
//            System.out.println("getServletPath "+ ((Request)request).getServletPath());
//            System.out.println("getPathInfo is "+ ((Request)request).getPathInfo());
//            System.out.println("getRequestURI is "+ ((Request)request).getRequestURI());
//            System.out.println("getRequestURL is "+ ((Request)request).getRequestURL());
//
//            System.out.println("peer ip --> " + request.getRemoteAddr() + " : "+request.getRemotePort());

            chain.doFilter(request, response);
//            System.out.println("TestFilter: postprocess");
            HttpFields fields=((Response) response).getHttpFields();
            for (HttpField httpField:fields) {
                System.out.println(httpField.getName()+ " : " + httpField.getValue());
            }

        }

        @Override
        public void destroy()
        {

        }
    }

}