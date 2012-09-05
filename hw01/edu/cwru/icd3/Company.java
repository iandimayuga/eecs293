/**
 * Ian Dimayuga
 * EECS293 HW1
 */
package edu.cwru.icd3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author ian A data structure to represent a strictly hierarchical organization of employees.
 */
public class Company {
    /**
     * Map of employee name to manager name. Each employee has one manager, with the exception of the head.
     */
    private Map<String, String> m_managerMap;

    /**
     * Map of employee name to the set of direct subordinates.
     */
    private Map<String, Set<String>> m_employeeMap;

    /**
     * Creates an empty Company with no employees.
     */
    public Company() {
        m_managerMap = new HashMap<String, String>();
        m_employeeMap = new HashMap<String, Set<String>>();
    }

    /**
     * Adds an employee to the company, specifying their manager.
     *
     * @param employee
     *            Name of the employee to be added.
     * @param manager
     *            Name of an existing employee to be the manager of the new employee. A manager of null is used if and
     *            only if the Company is currently empty, and the new employee will be the head.
     * @throws NullPointerException
     *             If employee is null.
     * @throws IllegalArgumentException
     *             If the specified manager is null but the Company is not empty.
     * @throws NoSuchElementException
     *             If the specified manager is nonexistent.
     * @throws IllegalArgumentException
     *             If there is already an employee with that name in the Company.
     */
    public void add(String employee, String manager) {
        // Null-check employee
        if (employee == null) {
            throw new NullPointerException("employee cannot be null");
        }
        if (manager == null) {
            // If manager is null, but the Company isn't empty, throw exception
            if (m_employeeMap.size() > 0) {
                throw new IllegalArgumentException("manager is null, but Company is not empty.");
            }
        } else if (!m_employeeMap.containsKey(manager)) {
            // If manager doesn't exist in the Company, throw exception
            // This includes the case where the Company is empty but a manager was specified
            throw new NoSuchElementException(String.format("Company has no employee with name %s.", manager));
        }
        // If employee already exists in the Company, throw exception
        if (m_managerMap.containsKey(employee)) {
            throw new IllegalArgumentException(
                    String.format("Company already contains an employee named %s.", employee));
        }
        // Add employee to Company, and to manager's set of direct reports if manager exists
        if (manager != null) {
            m_employeeMap.get(manager).add(employee);
        }
        m_employeeMap.put(employee, new HashSet<String>());
        m_managerMap.put(employee, manager);
    }

    /**
     * Removes an employee from the Company, including from any list of direct reports. The employee may not be a
     * manager.
     *
     * @param employee
     *            Name of the employee to delete.
     * @throws NullPointerException
     *             If employee is null.
     * @throws NoSuchElementException
     *             If employee is nonexistent.
     * @throws IllegalArgumentException
     *             If the employee has any direct reports.
     */
    public void delete(String employee) {
        // Null-check employee
        if (employee == null) {
            throw new NullPointerException("employee cannot be null");
        }

        // Check for existence
        if (!m_managerMap.containsKey(employee)) {
            throw new NoSuchElementException(String.format("Company has no employee with name %s.", employee));
        }
        // Check for direct reports
        if (m_employeeMap.get(employee).size() > 0) {
            throw new IllegalArgumentException(String.format("Employee '%s' has direct reports and cannot be deleted.",
                    employee));
        }

        String manager = m_managerMap.get(employee);
        if (manager != null) {
            // Remove employee from manager's department (unless manager is null)
            m_employeeMap.get(manager).remove(employee);
        }
        // Delete records of employee
        m_managerMap.remove(employee);
        m_employeeMap.remove(employee);
    }

    /**
     * Gets the name of an employee's manager.
     *
     * @param employee
     *            The name of the employee to query.
     * @return The manager of the employee, or null if the employee is the head.
     * @throws NullPointerException
     *             If employee is null.
     * @throws NoSuchElementException
     *             If employee is nonexistent.
     */
    public String managerOf(String employee) {
        // Null-check employee
        if (employee == null) {
            throw new NullPointerException("employee cannot be null");
        }

        // Check for existence
        if (!m_managerMap.containsKey(employee)) {
            throw new NoSuchElementException(String.format("Company has no employee with name %s.", employee));
        }

        // Look up manager
        return m_managerMap.get(employee);
    }

    /**
     * Gets the names of all Company managers.
     *
     * @return A set of all employees who have at least one direct report.
     */
    public Set<String> managerSet() {
        return new HashSet<String>(m_managerMap.values());
    }
}
