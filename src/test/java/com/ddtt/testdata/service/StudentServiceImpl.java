package com.ddtt.testdata.service;

import com.ddtt.testdata.dao.StudentDAO;
import com.ddtt.testdata.to.StudentTO;

public class StudentServiceImpl implements StudentService{
	private StudentDAO studentDAO;

	public void setStudentDAO(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
	}
	
	public StudentTO findStudent(int id){
		return studentDAO.findStudent(id);
	}

	public void deleteStudent(int id){
		studentDAO.deleteStudent(id);
	}
}
