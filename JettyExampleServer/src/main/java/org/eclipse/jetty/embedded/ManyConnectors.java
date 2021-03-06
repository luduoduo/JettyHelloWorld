package org.eclipse.jetty.embedded;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/* ------------------------------------------------------------ */
/**
 * A Jetty server with multiple connectors.
 */
public class ManyConnectors
{
    public static void main(String[] args) throws Exception
    {
        // Since this example shows off SSL configuration, we need a keystore with the appropriate key.  These two
        // lines are purely a hack to get access to a keystore that we use in many unit tests and should probably be
        // a direct path to your own keystore (used on line 29).
        String pathKeyStore = System.getProperty("path.keystore");
//        System.setProperty("jetty.home", jetty_home);

        // Create a basic jetty server object without declaring the port.  Since we are configuring connectors
        // directly we'll be setting ports on those connectors.
        Server server = new Server();

        // HTTP Configuration
        // HttpConfiguration is a collection of configuration information appropriate for http and https. The default
        // scheme for http is <code>http</code> of course, as the default for secured http is <code>https</code> but
        // we show setting the scheme to show it can be done.  The port for secured communication is also set here.
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(7443);
        http_config.setOutputBufferSize(32768);

        // HTTP connector
        // The first server connector we create is the one for http, passing in the http configuration we configured
        // above so it can get things like the output buffer size, etc. We also set the port (8080) and configure an
        // idle timeout.
        ServerConnector httpConnector = new ServerConnector(server,new HttpConnectionFactory(http_config));
        httpConnector.setPort(9000);
        httpConnector.setIdleTimeout(30000);

        // SSL Context Factory for HTTPS and SPDY
        // SSL requires a certificate so we configure a factory for ssl contents with information pointing to what
        // keystore the ssl connection needs to know about. Much more configuration is available the ssl context,
        // including things like choosing the particular certificate out of a keystore to be used.
        SslContextFactory sslContextFactory = new SslContextFactory();
        String keyStoreString=pathKeyStore + "/keystore";

        System.out.println("keystore is "+keyStoreString);
        sslContextFactory.setKeyStorePath(keyStoreString);
        //keystore的密码
        sslContextFactory.setKeyStorePassword("12345678");
        //pkcs12的密码
        sslContextFactory.setKeyManagerPassword("87654321");

        // HTTPS Configuration
        // A new HttpConfiguration object is needed for the next connector and you can pass the old one as an
        // argument to effectively clone the contents. On this HttpConfiguration object we add a
        // SecureRequestCustomizer which is how a new connector is able to resolve the https connection before
        // handing control over to the Jetty Server.
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // HTTPS connector
        // We create a second ServerConnector, passing in the http configuration we just made along with the
        // previously created ssl context factory. Next we set the port and a longer idle timeout.
        ServerConnector httpsConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory,"http/1.1"),
                new HttpConnectionFactory(https_config));
        httpsConnector.setPort(7443);
        httpsConnector.setIdleTimeout(500000);

        // Here you see the server having multiple connectors registered with it, now requests can flow into the server
        // from both http and https urls to their respective ports and be processed accordingly by jetty. A simple
        // handler is also registered with the server so the example has something to pass requests off to.

        // Set the connectors
        //创建一个server的两个connector，一个用于80端口，一个用于443端口
        server.setConnectors(new Connector[] { httpConnector, httpsConnector });

        // Set a handler
        server.setHandler(new HelloHandler());

        // Start the server
        server.start();
        server.join();
    }
}