package com.ddtt.testdata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ddtt.testdata.to.StudentTO;

public class StudentDAOImpl extends JdbcDaoSupport implements StudentDAO {
	public StudentTO findStudent(int id) {
		String sql = "select ID, NAME, DOB, CREATED_TM, SCHOOL_ID from DDTT_STUDENT where id = ?";
		StudentTO to = getJdbcTemplate().query(sql, new StudentResultSetExtractor(), id);
		return to;
	}
	
	public void deleteStudent(int id){
		String sql = "delete from DDTT_STUDENT_CLASS where student_id=?";
		getJdbcTemplate().update(sql, id);
		
		sql = "delete from DDTT_STUDENT where id=?";
		getJdbcTemplate().update(sql, id);
	}

	private StudentTO populateStudentTO(ResultSet rs) throws SQLException{
		StudentTO to = new StudentTO();
		to.setId(rs.getInt("ID"));
		to.setName(rs.getString("NAME"));
		to.setDob(rs.getDate("DOB"));
		to.setCreatedTm(rs.getTimestamp("CREATED_TM"));
		to.setSchoolId(rs.getInt("SCHOOL_ID"));
		return to;
	}
	
	class StudentResultSetExtractor implements ResultSetExtractor<StudentTO> {
		public StudentTO extractData(ResultSet rs) throws SQLException, DataAccessException {
			if (rs.next()) {
				return populateStudentTO(rs);
			}
			return null;
		}
	}

	class StudentMapper implements RowMapper<StudentTO> {
		public StudentTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			return populateStudentTO(rs);
		}
	}
}

