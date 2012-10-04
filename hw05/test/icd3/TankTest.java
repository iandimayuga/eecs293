/**
 * Ian Dimayuga
 * EECS293 HW05
 */
package icd3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ian
 *
 */
public class TankTest
{
    private double[] m_bottomLeft;
    private double[] m_topRight;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        double[] bottomLeft = { -0.5, 1, -4.5 };
        double[] topRight = { 0.5, 2, 3.5 };
        m_bottomLeft = bottomLeft;
        m_topRight = topRight;
    }

    /**
     * Test method for {@link icd3.Tank#Tank()}.
     */
    @Test
    public void testTank()
    {
        Tank defaultTank = new Tank();
        assertTrue(defaultTank.getBottom() == 0.0);
        assertTrue(defaultTank.getTop() == 0.0);
        assertTrue(defaultTank.baseArea() == 0.0);
    }

    /**
     * Test method for Illegal State behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetCoordinatesIllegalState()
    {
        Tank tank = new Tank();
        tank.setCoordinates(new double[3], new double[] {1, 1, 1});
        tank.setCoordinates(new double[3], new double[] {2, 2, 2});
    }

    /**
     * Test method for zero coordinate behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCoordinatesZero()
    {
        Tank tank = new Tank();
        tank.setCoordinates(new double[3], new double[3]);
    }

    /**
     * Test method for negative coordinate behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCoordinatesNegative()
    {
        Tank tank = new Tank();
        tank.setCoordinates(m_topRight, m_bottomLeft);
    }

    /**
     * Test method for not enough min dimensions behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCoordinatesWrongMinDimension()
    {
        Tank tank = new Tank();
        tank.setCoordinates(new double[2], new double[3]);
    }

    /**
     * Test method for not enough max dimensions behavior of {@link icd3.Tank#setCoordinates(double[], double[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCoordinatesWrongMaxDimension()
    {
        Tank tank = new Tank();
        tank.setCoordinates(new double[3], new double[2]);
    }

    /**
     * Test method for {@link icd3.Tank}.
     */
    @Test
    public void testTankBehavior()
    {
        Tank tank = new Tank();
        tank.setCoordinates(m_bottomLeft, m_topRight);
        assertTrue(tank.getBottom() == -4.5);
        assertTrue(tank.getTop() == 3.5);
        assertTrue(tank.baseArea() == 1.0);
    }

    /**
     * Test method for {@link icd3.Tank#equals(Object)}.
     */
    @Test
    public void testTankEquality()
    {
        Tank defaultTank = new Tank();
        double[] unitBottom = { 0, 0, 0 };
        double[] unitTop = { 1, 1, 1 };
        Tank unitTank = new Tank(unitBottom, unitTop);
        double[] otherUnitBottom = new double[3];
        double[] otherUnitTop = new double[3];
        for (int i = 0; i < 3; ++i)
        {
            otherUnitBottom[i] = 0;
            otherUnitTop[i] = 1;
        }
        Tank otherUnitTank = new Tank(otherUnitBottom, otherUnitTop);

        assertTrue(defaultTank.equals(defaultTank));
        assertTrue(unitTank.equals(unitTank));
        assertTrue(unitTank.equals(otherUnitTank));
        assertTrue(otherUnitTank.equals(unitTank));
        assertFalse(defaultTank.equals(null));
        assertFalse(unitTank.equals(null));
    }
}
