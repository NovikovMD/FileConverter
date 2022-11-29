package logger;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

/**
 * Базовый логгер для FileConverter
 */
public class Logger {
    private static Logger instance;
    private final org.apache.log4j.Logger logger;
    private static final String PATH_TO_PROPERTIES = "src/main/java/logger/logger.properties";
    private static final Level DEFAULT_LEVEL = Level.INFO;

    private Logger() {
        logger = LogManager.getLogger(Logger.class);
        PropertyConfigurator.configure(PATH_TO_PROPERTIES);
        logger.setLevel(DEFAULT_LEVEL);
    }

    public void setLevel(Level lvl) {
        logger.setLevel(lvl);
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }

        return instance;
    }

    public void info(Object message, Throwable throwable) {
        logger.info(message, throwable);
    }

    public void info(Object message) {
        logger.info(message);
    }

    public void warn(Object message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    public void warn(Object message) {
        logger.warn(message);
    }

    public void trace(Object message, Throwable throwable) {
        logger.trace(message, throwable);
    }

    public void trace(Object message) {
        logger.trace(message);
    }

    public void debug(Object message, Throwable throwable) {
        logger.debug(message, throwable);
    }

    public void debug(Object message) {
        logger.debug(message);
    }

    public void error(Object message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void error(Object message) {
        logger.error(message);
    }

    public void fatal(Object message, Throwable throwable) {
        logger.fatal(message, throwable);
    }

    public void fatal(Object message) {
        logger.fatal(message);
    }
}
