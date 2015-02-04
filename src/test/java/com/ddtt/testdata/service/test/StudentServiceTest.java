package com.ddtt.testdata.service.test;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ddtt.testdata.service.StudentService;
import com.ddtt.testdata.to.StudentTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bean-ddtt-test.xml")
public class StudentServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Resource
	private StudentService studentService;

	@Test
	public void findStudent_notFound() {
		//this case will find a issue: RowMapper doesn't work for no record found case
		StudentTO to = studentService.findStudent(1234567890);
		Assert.assertNull(to);
	}

	@Test
	//DDTT v0.001
	public void findStudent() {
		StudentTO to = studentService.findStudent(1);
		Assert.assertNotNull(to);
		Assert.assertEquals(to.getName(), "Jason");
	}
	
	@Test
	//DDTT v0.002
	public void deleteStudent() {
		studentService.deleteStudent(2);
		
		StudentTO to = studentService.findStudent(2);
		Assert.assertNull(to);
	}

}
