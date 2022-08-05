package db.mysql;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBService<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DBService.class);
	private SqlSessionFactory sqlSessionFactory;
	private Class<T> clazz;

	public DBService(SqlSessionFactory sqlSessionFactory, Class<T> clazz) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.clazz = clazz;
	}

	public <R> R execute(Execute<T, R> execute) {
		SqlSession sqlSession = null;

		Object var4;
		try {
			sqlSession = this.sqlSessionFactory.openSession(true);
			Object o;
			if (null == sqlSession) {
				LOGGER.error("Error! failed for get sql session");
				o = null;
				return (R) o;
			}

			o = sqlSession.getMapper(this.clazz);
			if (null == o) {
				LOGGER.error("Error! failed for get mapper ({})", this.clazz.getSimpleName());
				var4 = null;
				return (R) var4;
			}

			var4 = execute.execute((T) o);
		} catch (Exception var8) {
			LOGGER.error("Error! failed for execute:{}", execute.toString(), var8);
			return null;
		} finally {
			if (null != sqlSession) {
				sqlSession.close();
			}

		}

		return (R) var4;
	}
}
