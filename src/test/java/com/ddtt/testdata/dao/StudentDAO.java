package com.ddtt.testdata.dao;

import com.ddtt.testdata.to.StudentTO;

public interface StudentDAO {
	public StudentTO findStudent(int id);
	
	public void deleteStudent(int id);
}
