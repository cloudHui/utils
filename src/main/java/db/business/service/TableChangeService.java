package db.business.service;

import db.business.dao.TableChangeDao;
import db.business.entity.TableChangeEntity;
import db.mysql.DBService;
import db.mysql.DBSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Date;
import java.util.List;

public class TableChangeService extends DBService<TableChangeDao> {
	public TableChangeService() {
		this(DBSourceFactory.INSTANCE.getSqlSessionFactory());
	}

	public TableChangeService(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory, TableChangeDao.class);
	}

	public List<TableChangeEntity> queryChangeTable(Date updateTime, int area) {
		return this.execute((o) -> o.queryChangeTable(updateTime, area));
	}
}
