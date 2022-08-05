package db.business.dao;

import db.business.entity.TableChangeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TableChangeDao {
	List<TableChangeEntity> queryChangeTable(@Param("updatetime") Date var1, @Param("area") int var2);
}
