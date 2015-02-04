package com.ddtt.testdata.service;

import com.ddtt.testdata.to.StudentTO;

public interface StudentService {
	public StudentTO findStudent(int id);

	public void deleteStudent(int id);
}
