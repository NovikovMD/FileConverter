package logger;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

/**
 * Базовый логгер для FineConverter
 */
public class Logger {
    private static Logger instance;
    private final org.apache.log4j.Logger logger;
    private static final String PATH_TO_PROPERTIES = "src/main/java/logger/logger.properties";
    private static final Level DEFAULT_LEVEL = Level.ALL;

    private Logger(){
        logger = LogManager.getLogger(Logger.class);
        PropertyConfigurator.configure(PATH_TO_PROPERTIES);
        logger.setLevel(DEFAULT_LEVEL);
    }

    public static Logger getInstance(){
        if (instance == null){
            instance = new Logger();
        }

        return instance;
    }

    public void info(Object message, Throwable t){
        logger.info(message,t);
    }
    public void info(Object message){
        logger.info(message);
    }

    public void warn(Object message, Throwable t){
        logger.warn(message,t);
    }
    public void warn(Object message){
        logger.warn(message);
    }

    public void trace(Object message, Throwable t){
        logger.trace(message,t);
    }

    public void trace(Object message){
        logger.trace(message);
    }

    public void debug(Object message, Throwable t){
        logger.debug(message,t);
    }

    public void debug(Object message){
        logger.debug(message);
    }

    public void error(Object message, Throwable t){
        logger.error(message,t);
    }

    public void error(Object message){
        logger.error(message);
    }

    public void fatal(Object message, Throwable t){
        logger.fatal(message,t);
    }

    public void fatal(Object message){
        logger.fatal(message);
    }
}
