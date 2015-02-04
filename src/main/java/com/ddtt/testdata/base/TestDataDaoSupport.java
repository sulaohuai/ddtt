package com.ddtt.testdata.base;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class TestDataDaoSupport extends JdbcDaoSupport {
	public int insert(String sql, Object[] objs) {
		return update(sql, objs);
	}

	public int delete(String sql, Object[] objs) {
		return update(sql, objs);
	}

	public int update(String sql, Object[] objs) {
		return getJdbcTemplate().update(sql, objs);
	}

	public void execute(String sql) {
		getJdbcTemplate().execute(sql);
	}

	public void batchUpdate(String[] sqls) {
		getJdbcTemplate().batchUpdate(sqls);
	}
}
