/**
 * Ian Dimayuga
 * EECS293 HW02
 */
package icd3;

import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * A data structure to represent a system of Tanks related by their heights and vertical positions.
 *
 * @author ian
 *
 */
public class WaterSystem
{

    /**
     * The raw set of tanks in this system.
     */
    private Set<Tank> m_tankSet;

    /**
     * A map that associates to each break point the set of tanks whose bottoms are exactly at the break point.
     */
    private NavigableMap<Double, Set<Tank>> m_tanksByBottom;

    /**
     * A map that associates to each break point the set of tanks whose tops are exactly at the break point.
     */
    private NavigableMap<Double, Set<Tank>> m_tanksByTop;

    /**
     * A map that associates to each break point the set of tanks active at the break point.
     */
    private NavigableMap<Double, Set<Tank>> m_activeTanks;

    /**
     * A map that associates to each break point the total active base area at the break point.
     */
    private NavigableMap<Double, Double> m_activeBaseArea;

    /**
     * Initializes the WaterSystem to the specified set of Tanks.
     *
     * @param initialSystem
     *            The set of Tanks to be added to the WaterSystem.
     */
    public WaterSystem(Set<Tank> initialSystem)
    {
        // Store the raw set
        m_tankSet = new HashSet<>(initialSystem);

        // Pipe the data through the generator helpers to populate the maps
        m_tanksByBottom = generateTanksByEdge(m_tankSet, false);
        m_tanksByTop = generateTanksByEdge(m_tankSet, true);
        m_activeTanks = generateActiveTanks(m_tanksByBottom, m_tanksByTop);
        m_activeBaseArea = generateActiveBaseArea(m_activeTanks);
    }

    private static NavigableMap<Double, Set<Tank>> generateTanksByEdge(Set<Tank> tankSet, boolean top)
    {

        // Initialize the map to be returned
        NavigableMap<Double, Set<Tank>> tanksByEdge = new TreeMap<>();

        // Add each tank to the map with its top or bottom height as the key
        for (Tank tank : tankSet)
        {
            double edge = top ? tank.getTop() : tank.getBottom();

            // Create a new Set if the breakpoint is not yet recognized
            if (!tanksByEdge.containsKey(edge))
            {
                tanksByEdge.put(edge, new HashSet<Tank>());
            }

            // Add the Tank to the Set
            tanksByEdge.get(edge).add(tank);
        }

        return tanksByEdge;
    }

    private static NavigableMap<Double, Set<Tank>> generateActiveTanks(NavigableMap<Double, Set<Tank>> tanksByBottom,
                                                                       NavigableMap<Double, Set<Tank>> tanksByTop)
    {
        return null;
    }

    private static NavigableMap<Double, Double> generateActiveBaseArea(NavigableMap<Double, Set<Tank>> activeTanks)
    {
        return null;
    }
}
