package utils.config;

import java.net.URL;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

public class LogbackConfig {
	public static void configureLogback() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			URL url = LogbackConfig.class.getClassLoader().getResource("logback.xml");
			if (url == null) {
				ClassLoader classloader = Thread.currentThread().getContextClassLoader();
				url = classloader.getResource("logback.xml");
			}
			if (url == null) {
				throw new Exception(" logback.xml can not find ");
			}
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(loggerContext);
			loggerContext.reset();
			configurator.doConfigure(url);
			StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}