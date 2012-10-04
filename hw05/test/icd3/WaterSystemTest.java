/**
 * Ian Dimayuga
 * EECS293 HW05
 */
package icd3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ian
 *
 */
public class WaterSystemTest
{
    private static Tank s_tankA;
    private static Tank s_tankB;
    private static Tank s_tankC;
    private static Tank s_tankD;
    private static WaterSystem s_testSystem;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        // Create the test case example from the assignment
        double[] bottomA = { 0, 0, 5 };
        double[] topA = { 5, 1, 13 };
        double[] bottomB = { 0, 0, 11 };
        double[] topB = { 5, 1, 18 };
        double[] bottomC = { 0, 0, 15 };
        double[] topC = { 4, 1, 21 };
        double[] bottomD = { 0, 0, 19 };
        double[] topD = { 8, 1, 23 };

        s_tankA = new Tank(bottomA, topA);
        s_tankB = new Tank(bottomB, topB);
        s_tankC = new Tank(bottomC, topC);
        s_tankD = new Tank(bottomD, topD);

        Set<Tank> tankSet = new HashSet<Tank>();
        tankSet.add(s_tankA);
        tankSet.add(s_tankB);
        tankSet.add(s_tankC);
        tankSet.add(s_tankD);

        s_testSystem = new WaterSystem(tankSet);
    }

    /**
     * Test method for {@link icd3.WaterSystem#tanksByBottom()}.
     */
    @Test
    public void testTanksByBottom()
    {
        NavigableMap<Double, Set<Tank>> tanks = s_testSystem.tanksByBottom();
        assertTrue(tanks.get(5.0).size() == 1);
        assertTrue(tanks.get(5.0).contains(s_tankA));
        assertFalse(tanks.containsKey(13.0));
        assertTrue(tanks.get(11.0).size() == 1);
        assertTrue(tanks.get(11.0).contains(s_tankB));
        assertFalse(tanks.containsKey(18.0));
        assertTrue(tanks.get(15.0).size() == 1);
        assertTrue(tanks.get(15.0).contains(s_tankC));
        assertFalse(tanks.containsKey(21.0));
        assertTrue(tanks.get(19.0).size() == 1);
        assertTrue(tanks.get(19.0).contains(s_tankD));
        assertFalse(tanks.containsKey(23.0));
    }

    /**
     * Test method for {@link icd3.WaterSystem#tanksByTop()}.
     */
    @Test
    public void testTanksByTop()
    {
        NavigableMap<Double, Set<Tank>> tanks = s_testSystem.tanksByTop();
        assertTrue(tanks.get(13.0).size() == 1);
        assertTrue(tanks.get(13.0).contains(s_tankA));
        assertFalse(tanks.containsKey(5.0));
        assertTrue(tanks.get(18.0).size() == 1);
        assertTrue(tanks.get(18.0).contains(s_tankB));
        assertFalse(tanks.containsKey(11.0));
        assertTrue(tanks.get(21.0).size() == 1);
        assertTrue(tanks.get(21.0).contains(s_tankC));
        assertFalse(tanks.containsKey(15.0));
        assertTrue(tanks.get(23.0).size() == 1);
        assertTrue(tanks.get(23.0).contains(s_tankD));
        assertFalse(tanks.containsKey(19.0));
    }

    /**
     * Test method for {@link icd3.WaterSystem#activeTanks()}.
     */
    @Test
    public void testActiveTanks()
    {
        NavigableMap<Double, Set<Tank>> tanks = s_testSystem.activeTanks();
        assertTrue(tanks.get(5.0).size() == 1);
        assertTrue(tanks.get(5.0).contains(s_tankA));
        assertTrue(tanks.get(11.0).size() == 2);
        assertTrue(tanks.get(11.0).contains(s_tankA) && tanks.get(11.0).contains(s_tankB));
        assertTrue(tanks.get(13.0).size() == 1);
        assertTrue(tanks.get(13.0).contains(s_tankB));
        assertTrue(tanks.get(15.0).size() == 2);
        assertTrue(tanks.get(15.0).contains(s_tankB) && tanks.get(15.0).contains(s_tankC));
        assertTrue(tanks.get(18.0).size() == 1);
        assertTrue(tanks.get(18.0).contains(s_tankC));
        assertTrue(tanks.get(19.0).size() == 2);
        assertTrue(tanks.get(19.0).contains(s_tankC) && tanks.get(19.0).contains(s_tankD));
        assertTrue(tanks.get(21.0).size() == 1);
        assertTrue(tanks.get(21.0).contains(s_tankD));
        assertTrue(tanks.get(23.0).size() == 0);
    }

    /**
     * Test method for {@link icd3.WaterSystem#activeBaseArea()}.
     */
    @Test
    public void testActiveBaseArea()
    {
        NavigableMap<Double, Double> tanks = s_testSystem.activeBaseArea();
        assertTrue(tanks.get(5.0) == 5.0);
        assertTrue(tanks.get(11.0) == 10.0);
        assertTrue(tanks.get(13.0) == 5.0);
        assertTrue(tanks.get(15.0) == 9.0);
        assertTrue(tanks.get(18.0) == 4.0);
        assertTrue(tanks.get(19.0) == 12.0);
        assertTrue(tanks.get(21.0) == 8.0);
        assertTrue(tanks.get(23.0) == 0.0);
    }

    /**
     * Test method for {@link icd3.WaterSystem#heightToTankLevel(Double)}.
     */
    @Test
    public void testHeightToTankLevel()
    {
        Map<Tank, Double> tanks = s_testSystem.heightToTankLevel(16.0);
        assertTrue(tanks.get(s_tankA) == 8.0);
        assertTrue(tanks.get(s_tankB) == 5.0);
        assertTrue(tanks.get(s_tankC) == 1.0);
        assertTrue(tanks.get(s_tankD) == 0.0);
    }

    /**
     * Test method for below-minimum water height behavior of {@link icd3.WaterSystem#heightToTankLevel(Double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHeightToTankLevelLowHeight()
    {
        s_testSystem.heightToTankLevel(-1.0);
    }

    /**
     * Test method for above-maximum water height behavior of {@link icd3.WaterSystem#heightToTankLevel(Double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHeightToTankLevelHighHeight()
    {
        s_testSystem.heightToTankLevel(24.0);
    }

    /**
     * Test method for {@link icd3.WaterSystem#heightToVolume(Double)}.
     */
    @Test
    public void testHeightToVolume()
    {
        assertTrue(s_testSystem.heightToVolume(16.0) == 69.0);
        assertTrue(s_testSystem.heightToVolume(5.0) == 0.0);
        assertTrue(s_testSystem.heightToVolume(23.0) == 131.0);
    }

    /**
     * Test method for {@link icd3.WaterSystem#volumeToTankLevel(Double)}.
     */
    @Test
    public void testVolumeToTankLevel()
    {
        Map<Tank, Double> tanks = s_testSystem.volumeToTankLevel(69.0);
        assertTrue(tanks.get(s_tankA) == 8.0);
        assertTrue(tanks.get(s_tankB) == 5.0);
        assertTrue(tanks.get(s_tankC) == 1.0);
        assertTrue(tanks.get(s_tankD) == 0.0);
    }
}
