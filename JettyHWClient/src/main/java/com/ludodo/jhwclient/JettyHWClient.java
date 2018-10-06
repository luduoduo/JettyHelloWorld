package com.ludodo.jhwclient;

import java.io.IOException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.Callback;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class JettyHWClient {
    public void syncGet() {
        System.out.print("同步方式GET请求\n");

        // Instantiate HttpClient
        HttpClient httpClient = new HttpClient();

        // Configure HttpClient, for example:
        httpClient.setFollowRedirects(false);

        // Start HttpClient
        try {
            httpClient.start();
        } catch (Exception e) {
            System.out.println("httpClient.start异常，Cause is : " + e.getMessage());
        }

        //GET
        try {
            //            ContentResponse response = httpClient.GET("https://www.sina.com.cn");
            //            ContentResponse response = httpClient.GET("http://127.0.0.1:8080");
            System.out.println("发送请求....");

            //同步GET
            ContentResponse response = httpClient.newRequest("http://127.0.0.1:8080")
                    .method(HttpMethod.GET)
                    .agent("JettyHWClient")
                    .timeout(4, TimeUnit.SECONDS)
                    .send();
            //下面代码与上面一句等价
            //            Request request = httpClient.newRequest("http://127.0.0.1:8080");
            //            request.method(HttpMethod.GET);
            //            request.agent("MyJettyClient");
            //            ContentResponse response = request.send();

            System.out.println("请求 path = " + response.getRequest().getPath());
            System.out.println("请求 method = " + response.getRequest().getMethod());
            System.out.println("请求 URI = " + response.getRequest().getURI());
            System.out.println("请求 Agent = " + response.getRequest().getAgent());

            //            System.out.println("repsonse = " + response);
            //            System.out.println("repsonse header = " + response.getHeaders());
            System.out.println("响应 status = " + response.getStatus());
            System.out.println("响应 header Content-Type = " + response.getHeaders().getValues("Content-Type"));
            System.out.println("响应 header Content-Length = " + response.getHeaders().getValues("Content-Length"));
            System.out.println("响应 Server = " + response.getHeaders().getValues("Server"));

            System.out.println("响应 content = \n" + response.getContentAsString());
        } catch (Exception e) {
            System.out.println("异常，Cause is : " + e.getMessage());
        }
    }

    //用Response.Listener，了解所有通信细节
    public void asyncGet() {
        System.out.print("异步方式GET请求\n");

        // Instantiate HttpClient
        HttpClient httpClient = new HttpClient();

        // Configure HttpClient, for example:
        httpClient.setFollowRedirects(false);

        // Start HttpClient
        try {
            httpClient.start();
        } catch (Exception e) {
            System.out.println("httpClient.start异常，Cause is : " + e.getMessage());
        }

        //GET
        try {
            System.out.println("发送请求....");

            //异步GET，send没有返回值，不像同步那样直接返回response
            httpClient.newRequest("http://127.0.0.1:8080")
                    .method(HttpMethod.GET)
                    .agent("JettyHWClient")
                    .timeout(4, TimeUnit.SECONDS)
                    .send(new Response.Listener() {
                        //Response.Listener继承了response里所有的listener
                        @Override
                        public void onBegin(Response response) {
                            System.out.println("----onBegin");
                        }

                        @Override
                        public void onContent(Response response, ByteBuffer byteBuffer, Callback callback) {
                            System.out.println("----onContent1");
                            System.out.println("响应 content = \n" + byteBuffer);
                        }


                        @Override
                        public void onComplete(Result result) {
                            System.out.println("----onComplete");

                            System.out.println("GET 完成");
                            Request request = result.getRequest();
                            Response response = result.getResponse();

                            System.out.println("请求 path = " + request.getPath());
                            System.out.println("请求 method = " + request.getMethod());
                            System.out.println("请求 URI = " + request.getURI());
                            System.out.println("请求 Agent = " + request.getAgent());
                            System.out.println("repsonse = " + response);
                            System.out.println("repsonse header = " + response.getHeaders());
                            System.out.println("响应 status = " + response.getStatus());
                            System.out.println("响应 header Content-Type = " + response.getHeaders().getValues("Content-Type"));
                            System.out.println("响应 header Content-Length = " + response.getHeaders().getValues("Content-Length"));
                            System.out.println("响应 Server = " + response.getHeaders().getValues("Server"));
                        }

                        @Override
                        public void onContent(Response response, ByteBuffer byteBuffer) {
                            System.out.println("----onContent2");
                        }

                        @Override
                        public void onFailure(Response response, Throwable throwable) {
                            System.out.println("----onFailure");
                            System.out.println("失败：" + response);
                        }

                        @Override
                        public boolean onHeader(Response response, HttpField httpField) {
                            System.out.println("----onHeader1");
                            System.out.println(httpField.getName() + " : " + httpField.getValue());
                            return false;
                        }

                        @Override
                        public void onHeaders(Response response) {
                            System.out.println("----onHeader2");
                        }

                        @Override
                        public void onSuccess(Response response) {
                            System.out.println("----onSuccess");
                        }
                    });
        } catch (Exception e) {
            System.out.println("异常，Cause is : " + e.getMessage());
        }
        ;
    }


    //用Response.Listener，了解所有通信细节
    public void asyncGet2() {
        System.out.print("异步方式GET请求\n");

        // Instantiate HttpClient
        HttpClient httpClient = new HttpClient();

        // Configure HttpClient, for example:
        httpClient.setFollowRedirects(false);

        // Start HttpClient
        try {
            httpClient.start();
        } catch (Exception e) {
            System.out.println("httpClient.start异常，Cause is : " + e.getMessage());
        }

        //GET
        try {
            System.out.println("发送请求....");

            //异步GET，send没有返回值，不像同步那样直接返回response
            httpClient.newRequest("http://127.0.0.1:8080")
                    .method(HttpMethod.GET)
                    .agent("JettyHWClient")
                    .timeout(4, TimeUnit.SECONDS)
                    .onResponseContent(new Response.ContentListener() {
                        @Override
                        public void onContent(Response response, ByteBuffer byteBuffer) {   //这里有问题，拿到的不是html返回页面，似乎有泄露
//                            String stringBuffer = new String(byteBuffer.array());
//                            System.out.println("响应 content = \n" + stringBuffer);
                        }
                    })
                    .send(new Response.CompleteListener() {
                        @Override
                        public void onComplete(Result result) {
                            System.out.println("----onComplete");

                            System.out.println("GET 完成");
                            Request request = result.getRequest();
                            Response response = result.getResponse();

                            System.out.println("请求 path = " + request.getPath());
                            System.out.println("请求 method = " + request.getMethod());
                            System.out.println("请求 URI = " + request.getURI());
                            System.out.println("请求 Agent = " + request.getAgent());

                            System.out.println("repsonse = " + response);
                            System.out.println("repsonse header = " + response.getHeaders());
                            System.out.println("响应 status = " + response.getStatus());
                            System.out.println("响应 header Content-Type = " + response.getHeaders().getValues("Content-Type"));
                            System.out.println("响应 header Content-Length = " + response.getHeaders().getValues("Content-Length"));
                            System.out.println("响应 Server = " + response.getHeaders().getValues("Server"));
                        }
                    });
        } catch (Exception e) {
            System.out.println("异常，Cause is : " + e.getMessage());
        }
        ;
    }


    public static void main(String[] args) throws Exception {
        System.out.print("Hello world!\n");
        JettyHWClient client = new JettyHWClient();
//        client.syncGet();
//        client.asyncGet();
        client.asyncGet2();
        int timeDS = 5000;    //等待5s
        while (timeDS > 0)
            try {
                Thread.sleep(100);
                timeDS -= 100;
            } catch (InterruptedException e) {
                e.printStackTrace();
                timeDS = 0;
            }
        System.out.println("The end");
    }
}