/**
 * Ian Dimayuga
 * EECS293 HW02
 */
package icd3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ian
 *
 */
public class TankTest {
    private double[] m_bottomLeft;
    private double[] m_topRight;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        double[] bottomLeft = { -0.5, 1, -4.5 };
        double[] topRight = { 0.5, 2, 3.5 };
        m_bottomLeft = bottomLeft;
        m_topRight = topRight;
    }

    /**
     * Test method for {@link icd3.Tank#Tank()}.
     */
    @Test
    public void testTank() {
        Tank empty = new Tank();
        assertTrue(empty.getBottom() == 0.0);
        assertTrue(empty.getTop() == 0.0);
        assertTrue(empty.baseArea() == 0.0);
    }

    /**
     * Test method for Illegal State behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected=IllegalStateException.class)
    public void testSetCoordinatesIllegalState() {
        Tank tank = new Tank();
        tank.setCoordinates(new double[3], new double[3]);
        tank.setCoordinates(new double[3], new double[3]);
    }

    /**
     * Test method for negative coordinate behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetCoordinatesNegative() {
        Tank tank = new Tank();
        tank.setCoordinates(m_topRight, m_bottomLeft);
    }

    /**
     * Test method for not enough min dimensions behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetCoordinatesWrongMinDimension() {
        Tank tank = new Tank();
        tank.setCoordinates(new double[2], new double[3]);
    }

    /**
     * Test method for not enough max dimensions behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetCoordinatesWrongMaxDimension() {
        Tank tank = new Tank();
        tank.setCoordinates(new double[3], new double[2]);
    }

    /**
     * Test method for {@link icd3.Tank}.
     */
    @Test
    public void testTankBehavior() {
        Tank tank = new Tank();
        tank.setCoordinates(m_bottomLeft, m_topRight);
        assertTrue(tank.getBottom() == -4.5);
        assertTrue(tank.getTop() == 3.5);
        assertTrue(tank.baseArea() == 1.0);
    }

}
