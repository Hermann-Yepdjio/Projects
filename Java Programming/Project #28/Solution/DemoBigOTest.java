
package project1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hermann
 * @Since 1-10-2017
 * DemoBigOTest.java
 * 
 * Description: Junit test for methods in DemoBigO
 */
public class DemoBigOTest {

    public DemoBigOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of throwDivideException method, of class DemoBigO.
     */
    @Test
    public void testThrowDivideException() {
        System.out.println("throwDivideException");
        DemoBigO instance = new DemoBigO();
        boolean expResult = true;
        boolean result = instance.throwDivideException();
        assertEquals(expResult, result);
    }

    /**
     * Test of throwAnException method, of class DemoBigO.
     */
    @Test
    public void testThrowAnException() {
        System.out.println("throwAnException");
        DemoBigO instance = new DemoBigO();
        instance.throwAnException();
    }

    /**
     * Test of demoBigO1 method, of class DemoBigO.
     */
    @Test
    public void testDemoBigO1() {
        System.out.println("demoBigO1");
        long n = 0L;
        DemoBigO instance = new DemoBigO();
        instance.demoBigO1(n);
    }

    /**
     * Test of demoBigOLogN method, of class DemoBigO.
     */
    @Test
    public void testDemoBigOLogN() {
        System.out.println("demoBigOLogN");
        long n = 0L;
        DemoBigO instance = new DemoBigO();
        instance.demoBigOLogN(n);
    }

    /**
     * Test of demoBigOn method, of class DemoBigO.
     */
    @Test
    public void testDemoBigOn() {
        System.out.println("demoBigOn");
        long n = 0L;
        DemoBigO instance = new DemoBigO();
        instance.demoBigOn(n);
    }

    /**
     * Test of demoBigONLogNInit method, of class DemoBigO.
     */
    @Test
    public void testDemoBigONLogNInit() {
        System.out.println("demoBigONLogNInit");
        long n = 0L;
        DemoBigO instance = new DemoBigO();
        instance.demoBigONLogNInit(n);
    }

    /**
     * Test of demoBigONLogN method, of class DemoBigO.
     */
    @Test
    public void testDemoBigONLogN() {
        System.out.println("demoBigONLogN");
        long n = 0L;
        DemoBigO instance = new DemoBigO();
        instance.demoBigONLogN(n);
    }

    /**
     * Test of demoBigOnn method, of class DemoBigO.
     */
    @Test
    public void testDemoBigOnn() {
        System.out.println("demoBigOnn");
        long n = 0L;
        DemoBigO instance = new DemoBigO();
        instance.demoBigOnn(n);

    }

}
