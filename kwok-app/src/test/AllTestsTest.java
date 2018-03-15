import static org.junit.Assert.*;

import org.apache.struts.action.ActionMessages;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;

/**
 * 
 */

/**
 * @author Admin
 *
 */
public class AllTestsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link java.lang.Object#Object()}.
	 */
	@Test
	public final void testObject() {
	//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#getClass()}.
	 */
	@Test
	public final void testGetClass() {
		
	}

	/**
	 * Test method for {@link java.lang.Object#hashCode()}.
	 */
	@Test
	public final void testHashCode() {
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEquals() {
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#clone()}.
	 */
	@Test
	public final void testClone() {
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 */
	@Test
	public final void testToString() {
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#notify()}.
	 */
	@Test
	public final void testNotify() {
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#notifyAll()}.
	 */
	@Test
	public final void testNotifyAll() {
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long)}.
	 */
	@Test
	public final void testWaitLong() {
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long, int)}.
	 */
	@Test
	public final void testWaitLongInt() {
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait()}.
	 */
	@Test
	public final void testWait() {
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#finalize()}.
	 */
	@Test
	public final void testFinalize() {
	//	testGetCompanyCount();
   //     testAddCompany();
		
	//	fail("Not yet implemented"); // TODO
	}
	public void testGetCompanyCount() throws DatabaseException {
 //       ContactService contactService = ServiceProvider.getContactService(requestContext);
        int count;
        try {
//            count = contactService.getCompanyCount(new QueryCriteria());
        } catch (Exception e) {
            count = -1;
        }
  //      System.out.printf("Number of companies:" + count, (count != -1));
        
	
    }

	public void testAddCompany() throws DatabaseException {
        Company company = new Company();
        company.setName("Test Company " + System.currentTimeMillis());
        company.setDescription("This is company description.");

 //       ContactService contactService = ServiceProvider.getContactService(requestContext);
 //       ActionMessages errors = contactService.addCompany(company, null);
//        System.out.printf("Adding company... " + company.getName() + ", " + errors, errors.isEmpty());
    }
}
