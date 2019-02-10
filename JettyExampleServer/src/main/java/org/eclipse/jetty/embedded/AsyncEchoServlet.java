package org.eclipse.jetty.embedded;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AsyncEchoServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("TID = " + Thread.currentThread().getId());
        //名不副实，不是开始什么，只是设置了异步模式
        //reponse不会在servie返回时提交，而是等到AsyncContext.complete() 被调用，或者超时
        AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(0);
        //asyncContext记录了request和response的stream，又记录在listener里，在异步操作时可以拿到
        EchoListener echoListener = new EchoListener(asyncContext);
        request.getInputStream().setReadListener(echoListener);
        response.getOutputStream().setWriteListener(echoListener);
    }

    private class EchoListener implements ReadListener, WriteListener
    {
        private final byte[] buffer = new byte[4096];
        private final AsyncContext asyncContext;
        private final ServletInputStream input;
        private final ServletOutputStream output;
        private final AtomicBoolean ifComplete = new AtomicBoolean(false);

        //注意，宿主类可以访问内部类的私有构造函数
        private EchoListener(AsyncContext asyncContext) throws IOException
        {
            this.asyncContext = asyncContext;
            this.input = asyncContext.getRequest().getInputStream();
            this.output = asyncContext.getResponse().getOutputStream();
        }

        @Override
        public void onDataAvailable() throws IOException
        {
            handleAsyncIO();
        }

        @Override
        public void onAllDataRead() throws IOException
        {
            handleAsyncIO();
        }

        @Override
        public void onWritePossible() throws IOException
        {
            handleAsyncIO();
        }

        private void handleAsyncIO() throws IOException
        {
            // This method is called:
            //   1) after first registering a WriteListener (ready for first write)
            //   2) after first registering a ReadListener iff write is ready
            //   3) when a previous write completes after an output.isReady() returns false
            //   4) from an input callback

            // We should try to read, only if we are able to write!
            while (true)
            {
                if (!output.isReady())
                    // Don't even try to read anything until it is possible to write something,
                    // when onWritePossible will be called
                    break;

                if (!input.isReady())
                    // Nothing available to read, so wait for another call to onDataAvailable
                    break;

                int read = input.read(buffer);
                if (read<0)
                {
                    if (ifComplete.compareAndSet(false,true)) {
//                        try {
//                            int counter=10;
//                            while (counter>0) {
//                                System.out.println(Thread.currentThread().getId() + " : " + counter);
//                                Thread.sleep(5000);
//                                counter-=1;
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                        System.out.println("asyncContext.complete");
                        //提交response
                        asyncContext.complete();
                    }
                    break;
                }
                else if (read>0)
                {
                    output.write(buffer, 0, read);
                }
            }
        }

        @Override
        public void onError(Throwable failure)
        {
            new Throwable("onError",failure).printStackTrace();
            asyncContext.complete();
        }
    }
}
