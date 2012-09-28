/**
 * Ian Dimayuga
 * EECS293 HW04
 */
package icd3;

import java.util.AbstractMap.SimpleEntry;
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
     *
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
        return deepCloneMap(this.fetchTanksByBottom());
    }

    /**
     * Helper method to fetch or generate map.
     *
     * @return The desired Map.
     */
    private NavigableMap<Double, Set<Tank>> fetchTanksByBottom()
    {
        // If the cache is invalid, then regenerate the requested map
        if (null == m_tanksByBottom)
        {
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
        return deepCloneMap(this.fetchTanksByTop());
    }

    /**
     * Helper method to fetch or generate map.
     *
     * @return The desired Map.
     */
    private NavigableMap<Double, Set<Tank>> fetchTanksByTop()
    {
        // If the cache is invalid, then regenerate the requested map
        if (null == m_tanksByTop)
        {
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
        return deepCloneMap(this.fetchActiveTanks());
    }

    /**
     * Helper method to fetch or generate map.
     *
     * @return The desired Map.
     */
    private NavigableMap<Double, Set<Tank>> fetchActiveTanks()
    {
        // If the cache is invalid, then regenerate the requested map
        if (null == m_activeTanks)
        {
            // Call dependencies which can either return cached structures or regenerate them in turn
            m_activeTanks = generateActiveTanks(this.fetchTanksByBottom(), this.fetchTanksByTop());
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
        // Deep Cloning not necessary because Doubles are immutable
        return new TreeMap<>(this.fetchActiveBaseArea());
    }

    /**
     * Helper method to fetch or generate map.
     *
     * @return The desired Map.
     */
    private NavigableMap<Double, Double> fetchActiveBaseArea()
    {
        // If the cache is invalid, then regenerate the requested map
        if (null == m_activeBaseArea)
        {
            // Call dependency which can either return cached structure or regenerate it in turn
            m_activeBaseArea = generateActiveBaseArea(this.fetchActiveTanks());
        }
        return m_activeBaseArea;
    }

    /**
     * Deep clones a map from Double to Set of Tanks
     *
     * @param originalMap The map to be cloned.
     * @return A new map with the same mappings and cloned Sets as values
     */
    private static NavigableMap<Double, Set<Tank>> deepCloneMap(NavigableMap<Double, Set<Tank>> originalMap)
    {
        NavigableMap<Double, Set<Tank>> clonedMap = new TreeMap<>();

        // Iterate through the entrySet of the originalMap
        Set<Entry<Double, Set<Tank>>> originalSet = originalMap.entrySet();

        // Copy the tank Set into a new HashSet and put in new map
        for (Entry<Double, Set<Tank>> entry : originalSet)
        {
            clonedMap.put(entry.getKey(), new HashSet<Tank>(entry.getValue()));
        }

        return clonedMap;
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

        // Create a running Set of active tanks
        Set<Tank> runningActiveTanks = new HashSet<>();

        BreakPointIterator slider = new BreakPointIterator(tanksByBottom.entrySet(), tanksByTop.entrySet());

        while (slider.iterate())
        {
            runningActiveTanks.addAll(slider.toActivate());
            runningActiveTanks.removeAll(slider.toDeactivate());

            activeTanks.put(slider.currentBreakPoint(), new HashSet<Tank>(runningActiveTanks));
        }

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

    /**
     * Encapsulates the parallel iteration for two sets of breakpoint->tankset maps
     *
     */
    private static class BreakPointIterator
    {
        /**
         * Iterator for the tanksByBottom entrySet
         */
        private Iterator<Entry<Double, Set<Tank>>> m_bottoms;

        /**
         * Iterator for the tanksByTop entrySet
         */
        private Iterator<Entry<Double, Set<Tank>>> m_tops;

        /**
         * Current entry in the bottom set
         */
        private Entry<Double, Set<Tank>> m_currentBottom;

        /**
         * Current entry in the top set
         */
        private Entry<Double, Set<Tank>> m_currentTop;

        /**
         * Object to represent the empty set for purposes of activation and deactivation
         */
        private Set<Tank> m_emptySet;

        /**
         * Signal that the bottom is finished and we now need only advance the top
         */
        private boolean m_bottomFinished;

        /**
         * Instantiates the slider, and prepares it for its first iteration. Call iterate() to begin.
         *
         * @param bottomSet The set of entries from break points to tank bottoms
         * @param topSet The set of entries from break points to tank tops
         */
        public BreakPointIterator(Set<Entry<Double, Set<Tank>>> bottomSet, Set<Entry<Double, Set<Tank>>> topSet)
        {
            m_bottoms = bottomSet.iterator();
            m_tops = topSet.iterator();

            m_bottomFinished = false;

            m_emptySet = new HashSet<>();

            // Initialize current pointers to minimum possible value
            m_currentBottom = m_currentTop = new SimpleEntry<>(Double.NEGATIVE_INFINITY, m_emptySet);
        }

        /**
         * Get the set of tanks to activate at the current breakpoint.
         *
         * @return The set of tanks whose bottoms are at this breakpoint. May be empty.
         */
        public Set<Tank> toActivate()
        {
            // We activate the tanks whose bottoms are at the current break point
            if (m_currentBottom.getKey() == this.currentBreakPoint())
            {
                return m_currentBottom.getValue();
            }
            else
            {
                // If there are no tanks whose bottoms are at the current break point, no activation occurs
                return m_emptySet;
            }
        }

        /**
         * Get the set of tanks to deactivate at the current breakpoint.
         *
         * @return The set of tanks whose tops are at this breakpoint. May be empty.
         */
        public Set<Tank> toDeactivate()
        {
            // We deactivate the tanks whose tops are at the current break point
            if (m_currentTop.getKey() == this.currentBreakPoint())
            {
                return m_currentTop.getValue();
            }
            else
            {
                // If there are no tanks whose tops are at the current break point, no deactivation occurs
                return m_emptySet;
            }
        }

        /**
         * Get the current breakpoint.
         *
         * @return The breakpoint being currently considered. Will be NEGATIVE_INFINITY if iteration has not begun.
         */
        public double currentBreakPoint()
        {
            // The top is now the current break point if we are out of bottoms
            if (m_bottomFinished)
            {
                return m_currentTop.getKey();
            }
            else
            {
                // Otherwise, whichever is lower is the one we are currently considering
                return Math.min(m_currentBottom.getKey(), m_currentTop.getKey());
            }
        }

        /**
         * Advances to the next break point.
         *
         * @return false if there is no next break point.
         */
        public boolean iterate()
        {
            // There is no next break point if both iterators are exhausted
            if (!m_bottoms.hasNext() && !m_tops.hasNext())
            {
                return false;
            }

            // Get the current break point
            double breakPoint = this.currentBreakPoint();

            // Whichever current entry is at the current break point should be advanced
            if (m_currentTop.getKey() == breakPoint)
            {
                m_currentTop = m_tops.next();
            }
            if (m_currentBottom.getKey() == breakPoint)
            {
                // Only advance the bottom if it is not done
                if (m_bottoms.hasNext())
                {
                    m_currentBottom = m_bottoms.next();
                }
                else
                {
                    // Signal that the bottom is exhausted, and we need to only advance the tops from now on
                    m_bottomFinished = true;
                }
            }

            return true;
        }
    }
}
