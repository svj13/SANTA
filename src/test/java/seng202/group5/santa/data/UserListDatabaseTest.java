package seng202.group5.santa.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class UserListDatabaseTest {

	private UserListDatabase database;

	@Before
	public void setUp() throws Exception {
		String i =  System.getProperty("user.dir");
		new File(i + File.separator + "Crime.db").delete();
		database = new UserListDatabase();
	}

	@After
	public void tearDown() throws Exception {
		String i =  System.getProperty("user.dir");
		new File(i + File.separator + "Crime.db").delete();
	}

	@Test
	public void testMakeTable() throws Exception {
		database.makeTable();
		assertEquals(0, database.getDistinct("LIST_ID").size());
		assertEquals(0, database.getDistinct("DISPLAYTEXT").size());
	}

	@Test
	public void testMakeTableTwice() throws Exception {
		database.makeTable();
		assertEquals(0, database.getDistinct("LIST_ID").size());
		assertEquals(0, database.getDistinct("DISPLAYTEXT").size());
	}

	@Test
	public  void  addCrimeListId() throws Exception {
		database.makeTable();
		database.addCrimeListId("list1");
		Integer correct = new Integer(1);
		assertEquals(database.getListNumber("list1"), correct);
	}

	@Test
	public  void  addListTwoId() throws Exception {
		database.makeTable();
		database.addCrimeListId("list1");
		database.addCrimeListId("list2");
		Integer correct = new Integer(2);
		assertEquals(database.getListNumber("list2"), correct);
	}

	@Test
	public  void  getDistnctTest () {
		UserListDatabase listDatabase = new UserListDatabase();
		listDatabase.makeTable();
		listDatabase.addCrimeListId("list1");
		listDatabase.addCrimeListId("list2");
		listDatabase.addCrimeListId("list3");
		listDatabase.addCrimeListId("list5");
		assertEquals(4, listDatabase.getDistinct("LIST_ID").size());
	}

	@Test
	public  void  getDistnctTestDisplayText () {
		UserListDatabase listDatabase = new UserListDatabase();
		listDatabase.makeTable();
		listDatabase.addCrimeListId("list1");
		listDatabase.addCrimeListId("list2");
		listDatabase.addCrimeListId("list3");
		listDatabase.addCrimeListId("list5");
		assertEquals(4, listDatabase.getDistinct("DISPLAYTEXT").size());
	}

	@Test
	public  void  deleteDataSetTest () {
		UserListDatabase listDatabase = new UserListDatabase();
		listDatabase.makeTable();
		listDatabase.addCrimeListId("list1");
		System.out.println(listDatabase.getListNumber("list1"));
		listDatabase.deleteUserList(1); // there should be 1 left as if there is one list and you delete it add a default list
		assertEquals(1, listDatabase.getDistinct("LIST_ID").size());
		assertEquals("Default Data List", listDatabase.getDistinct("DISPLAYTEXT").get(0));
		assertEquals("2",listDatabase.getDistinct("LIST_ID").get(0));
	}

}
