package db.mysql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class DruidFactory implements DataSourceFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(DruidFactory.class);
	private DataSource dataSource;

	public DruidFactory() {
	}

	public void setProperties(Properties properties) {
		try {
			this.dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception var3) {
			LOGGER.error("[ERROR] failed for create data source ({})", properties.toString(), var3);
		}

	}

	public DataSource getDataSource() {
		return this.dataSource;
	}
}
