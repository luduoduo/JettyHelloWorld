package com.ludodo.jhw;

import java.io.File;
import java.io.IOException;
import java.util.Map;

//log4j2
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class JettyHWUtils {

    public static void configureLog4j2()
    {
        String log4jConfigFile =  "log4j2.xml";
        Configurator.initialize(null, "log4j2.xml");
        System.out.println("log4j conf file is " + log4jConfigFile);

        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Map<String, LoggerConfig> map = loggerContext.getConfiguration().getLoggers();

        Logger loggerRoot = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

        //show log4j2 conf
        for (LoggerConfig aLoggerConfig:map.values()) {
            String name=aLoggerConfig.getName();
            if (name==null || name.length()==0)
                name="root";
            loggerRoot.info("<"+name+">" + " : " + aLoggerConfig.getLevel());
        }
    }
}