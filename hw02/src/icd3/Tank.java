/**
 * Ian Dimayuga
 * EECS293 HW02
 */
package icd3;

import java.util.Arrays;

/**
 * An immutable data structure representing a rectangular prism in three-dimensional space.
 *
 * @author ian
 *
 */
public class Tank
{

    /**
     * A simple map from 0,1,2 to x,y,z for output purposes.
     */
    private static final char[] s_coordinateName = { 'x', 'y', 'z' };

    /**
     * Point in space representing the minimum x,y,z point of the tank.
     */
    private double[] m_bottomLeft;

    /**
     * Point in space representing the maximum x,y,z point of the tank.
     */
    private double[] m_topRight;

    /**
     * Hash Code of this object. Determined by concatenating bottomLeft with topRight and using Arrays.hashCode. The
     * Hash Code is 0 if the Tank has not yet been set.
     */
    private int m_hashCode;

    /**
     * Mutation flag to guarantee immutability of the object.
     */
    private boolean m_isSet;

    /**
     * Initialize a Tank with zero size at 0,0,0. The Tank can then be set to specified coordinates. This Tank is not
     * equivalent to a Tank explicitly set to zero size at 0,0,0.
     */
    public Tank()
    {
        // Set default value
        m_bottomLeft = new double[3];
        m_topRight = new double[3];

        // The hash code is 0 for an unset Tank.
        m_hashCode = 0;

        // Allow to be set once
        m_isSet = false;
    }

    /**
     * Initialize an immutable Tank with specified coordinates.
     */
    public Tank(double[] bottomLeft, double[] topRight)
    {
        // Call overloaded initializing constructor
        this();

        // Set the coordinates. This will lock them in.
        this.setCoordinates(bottomLeft, topRight);
    }

    /**
     * Set the Tank's minimum and maximum points to the specified coordinates. This can only be done once. Calling the
     * parameterized constructor counts as setting the coordinates.
     *
     * @param bottomLeft A point in space that will represent the minimum x,y,z of the tank.
     * @param topRight A point in space that will represent the maximum x,y,z of the tank.
     * @throws IllegalStateException If setCoordinates or the parameterized constructor have been called before.
     * @throws IllegalArgumentException If there are not enough coordinates in either parameter.
     * @throws IllegalArgumentException If any of bottomLeft's components are greater than topRight's corresponding
     *             component.
     */
    public void setCoordinates(double[] bottomLeft, double[] topRight)
    {
        // Enforce immutability
        if (m_isSet)
        {
            throw new IllegalStateException("Tank coordinates cannot be set more than once.");
        }

        // Check for too few elements
        if (bottomLeft.length < 3 || topRight.length < 3)
        {
            // Format meaningful exception output (extra conditionals here are okay because we are about to throw)
            String wrongParam = (bottomLeft.length < 3 ? "bottomLeft" : "topRight");
            int wrongValue = (bottomLeft.length < 3 ? bottomLeft.length : topRight.length);
            throw new IllegalArgumentException(String.format("Parameter %s has only %d elements. Expected: %d",
                    wrongParam, wrongValue, 3));
        }

        // Validate that bottomLeft is less than or equal to topRight in all components
        for (int i = 0; i < 3; ++i)
        {
            if (bottomLeft[i] > topRight[i])
            {
                throw new IllegalArgumentException(String.format("Edge %d (%c-coordinate) is negative.", i,
                        s_coordinateName[i]));
            }
        }

        // Copy each vector (ignoring any coordinates after 3)
        m_bottomLeft = Arrays.copyOf(bottomLeft, 3);
        m_topRight = Arrays.copyOf(topRight, 3);

        // Generate hashcode. This only has to be done once because the Tank is now immutable.
        double[] hashArray = new double[6];
        System.arraycopy(m_bottomLeft, 0, hashArray, 0, 3);
        System.arraycopy(m_topRight, 0, hashArray, 3, 3);

        m_hashCode = Arrays.hashCode(hashArray);

        // Set mutation flag
        m_isSet = true;
    }

    /**
     * Get the bottom height of the Tank.
     *
     * @return The minimum z-value.
     */
    public double getBottom()
    {
        return m_bottomLeft[2];
    }

    /**
     * Get the top height of the Tank.
     *
     * @return The maximum z-value.
     */
    public double getTop()
    {
        return m_topRight[2];
    }

    /**
     * Get the area of the base of the tank.
     *
     * @return The area of the cross-section in the xy plane.
     */
    public double baseArea()
    {
        return (m_topRight[0] - m_bottomLeft[0]) * (m_topRight[1] - m_bottomLeft[1]);
    }

    /**
     * Returns a hash code for the Tank. The hash code is computed by concatenating the bottomLeft and topRight arrays.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return m_hashCode;
    }

    /**
     * Compares this Tank to the specified object. The result is true if and only if the argument is not null and is a
     * Tank object whose hashCode evaluates to the same number as this object's.
     *
     * @param o The object to compare this Tank against
     *
     * @return true if the given object represents a Tank equivalent to this, and false otherwise.
     */
    @Override
    public boolean equals(Object o)
    {
        return null == o || o.getClass() != Tank.class ? false : o.hashCode() == this.hashCode();
    }

    /**
     * Returns a string representation of the Tank.
     *
     * @return "Tank: Default" for the default Tank, otherwise the coordinates of the Tank.
     */
    @Override
    public String toString()
    {
        return m_isSet ? String.format("Tank: %s to %s", Arrays.toString(m_bottomLeft), Arrays.toString(m_topRight))
                : "Tank: Default";
    }
}
