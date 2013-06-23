package com.thundermoose.bio.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/application-context.xml")
public class TestHibernate {

	@Autowired
	private DataDao dao;
	
	@Test
	public void test(){
		System.out.println(dao.getDataByPlate("HTB1582"));
	}
	
	@Test
	public void testFile() throws FileNotFoundException{
		dao.loadExcel(new FileInputStream(new File("src/test/resources/test_data.xlsx")));
	}
}
