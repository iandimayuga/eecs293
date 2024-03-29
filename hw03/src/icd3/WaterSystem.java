/**
 * Ian Dimayuga
 * EECS293 HW03
 */
package icd3;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
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
     * @param initialSystem The set of Tanks to represent the WaterSystem.
     */
    public WaterSystem(Set<Tank> initialSystem)
    {
        // Store the raw set
        this.setTankSystem(initialSystem);
    }

    /**
     * Sets the system to the specified set of tanks.
     * @param tankSystem The set of Tanks representing the WaterSystem.
     */
    public void setTankSystem(Set<Tank> tankSystem)
    {
        m_tankSet = new HashSet<>(tankSystem);

        // Invalidate the cache
        m_tanksByBottom = m_tanksByTop = m_activeTanks = null;
        m_activeBaseArea = null;
    }

    /**
     * Get a NavigableMap associating bottom edges with sets of tanks.
     *
     * @return The desired Map.
     */
    public NavigableMap<Double, Set<Tank>> tanksByBottom()
    {
        // If the cache is invalid, then regenerate the requested map
        if (null == m_tanksByBottom) {
            m_tanksByBottom = generateTanksByEdge(m_tankSet, false);
        }
        return m_tanksByBottom;
    }

    /**
     * Get a NavigableMap associating top edges with sets of tanks.
     *
     * @return The desired Map.
     */
    public NavigableMap<Double, Set<Tank>> tanksByTop()
    {
        // If the cache is invalid, then regenerate the requested map
        if (null == m_tanksByTop) {
            m_tanksByTop = generateTanksByEdge(m_tankSet, true);
        }
        return m_tanksByTop;
    }

    /**
     * Get a NavigableMap associating breakpoints to sets of active tanks.
     *
     * @return The desired Map.
     */
    public NavigableMap<Double, Set<Tank>> activeTanks()
    {
        // If the cache is invalid, then regenerate the requested map
        if (null == m_activeTanks) {
            // Call dependencies which can either return cached structures or regenerate them in turn
            m_activeTanks = generateActiveTanks(this.tanksByBottom(), this.tanksByTop());
        }
        return m_activeTanks;
    }

    /**
     * Get a NavigableMap associating breakpoints to total active cross-section area.
     *
     * @return The desired Map.
     */
    public NavigableMap<Double, Double> activeBaseArea()
    {
        // If the cache is invalid, then regenerate the requested map
        if (null == m_activeBaseArea) {
            // Call dependency which can either return cached structure or regenerate it in turn
            m_activeBaseArea = generateActiveBaseArea(this.activeTanks());
        }
        return m_activeBaseArea;
    }

    /**
     * Generate a NavigableMap of breakpoints to sets of tanks. Breakpoints can either be the tops or bottoms.
     *
     * @param tankSet The set of Tanks in the WaterSystem.
     * @param top true if the Map should associate top edges with tanks, false if it should associate bottom edges.
     * @return The desired Map.
     */
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

    /**
     * Generate a NavigableMap of breakpoints to sets of active tanks.
     *
     * @param tanksByBottom A NavigableMap of bottom edges to sets of tanks.
     * @param tanksByTop A NavigableMap of top edges to sets of tanks.
     * @return The desired Map.
     */
    private static NavigableMap<Double, Set<Tank>> generateActiveTanks(NavigableMap<Double, Set<Tank>> tanksByBottom,
                                                                       NavigableMap<Double, Set<Tank>> tanksByTop)
    {
        // Initialize the map to be returned
        NavigableMap<Double, Set<Tank>> activeTanks = new TreeMap<>();

        if (tanksByBottom.size() == 0) {
            return activeTanks;
        }

        // Create a running Set of active tanks
        Set<Tank> runningActiveTanks = new HashSet<>();

        // Get the EntrySet Iterators from both tanksByBottom and tanksByTop
        Iterator<Entry<Double, Set<Tank>>> bottomsFromBelow = tanksByBottom.entrySet().iterator();
        Iterator<Entry<Double, Set<Tank>>> topsFromBelow = tanksByTop.entrySet().iterator();

        // Current top and bottom points
        Entry<Double, Set<Tank>> currentBottom = bottomsFromBelow.next();
        Entry<Double, Set<Tank>> currentTop = topsFromBelow.next();

        // Don't advance the bottom break point if there is nothing left
        boolean bottomFinished = false, topFinished = false;

        // Iterate through both entry sets in parallel, activating tanks as they are encountered in bottoms,
        // and deactivating them as they are encountered in tops
        // The last bottom can never be at or above the last top, so this condition is sufficient
        while (!topFinished)
        {
            // This will be set to either the bottom or the top, or both (if equal)
            double currentBreakPoint = 0;

            // Hold onto the top break point so both ifs are checking the same value
            double top = currentTop.getKey();

            // The lower current key is the current break point to consider
            // Both conditionals evaluate to true if bottom and top are equal, so they both would advance
            if (bottomFinished || top <= currentBottom.getKey())
            {
                currentBreakPoint = top;

                // This is a top break point, so deactivate
                runningActiveTanks.removeAll(currentTop.getValue());

                // Then advance the current top key, unless top is finished
                if (topsFromBelow.hasNext())
                {
                    currentTop = topsFromBelow.next();
                }
                else
                {
                    topFinished = true;
                }
            }
            if (!bottomFinished && currentBottom.getKey() <= top)
            {
                currentBreakPoint = currentBottom.getKey();

                // This is a bottom break point, so activate
                runningActiveTanks.addAll(currentBottom.getValue());

                // Then advance the current bottom key, unless bottom is finished
                if (bottomsFromBelow.hasNext())
                {
                    currentBottom = bottomsFromBelow.next();
                }
                else
                {
                    bottomFinished = true;
                }
            }

            // Add the entry
            activeTanks.put(currentBreakPoint, new HashSet<Tank>(runningActiveTanks));
        }
        assert(bottomFinished);

        return activeTanks;
    }

    /**
     * Generate a NavigableMap of breakpoints to the total base area of the active tanks.
     *
     * @param activeTanks A NavigableMap associating breakpoints to sets of active tanks.
     * @return The desired Map.
     */
    private static NavigableMap<Double, Double> generateActiveBaseArea(NavigableMap<Double, Set<Tank>> activeTanks)
    {
        // Initialize the map to be returned
        NavigableMap<Double, Double> activeBaseArea = new TreeMap<>();

        // Iterate through the break points and sum the areas at each height
        for (double height : activeTanks.navigableKeySet())
        {

            // Get active tanks at height
            Set<Tank> tanks = activeTanks.get(height);

            // Get the total base area
            double totalArea = 0;
            for (Tank tank : tanks)
            {
                totalArea += tank.baseArea();
            }

            // Create the entry for this break point
            activeBaseArea.put(height, totalArea);
        }

        return activeBaseArea;
    }
}
