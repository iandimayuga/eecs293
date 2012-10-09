Ian Dimayuga (icd3)
EECS 293 HW06

RunningMedian.txt contains the pseudocode for the RunningMedian class.

SortedArrayList.txt contains the pseudocode for the SortedArrayList class, which is a data structure used by RunningMedian.

This design is intended to allow for arbitrary sizes of running median, determined by the size of the initial input.
The only restriction is that elements be added two at a time, as specified.
This keeps the evenness of the list the same as the evenness of the median, eliminating the need to take averages.

Further, the SortedArrayList design is a general ArrayList-style design that allows for both random access and arbitrary bulk insertion, using a one-pass insertion sort to preserve order.
Because the input array must first be sorted before it is interpolated, an insertion executes in O(N + K log(K)) time, where N is the current size of the SortedArrayList and K is the size of the input.
This is acceptable and desirable because K is not expected to scale as quickly as N in most uses, and in this particular use K is known to be 2.

Finally, preserving random access allows us to take the median in O(M) time, where M is the width of the median.
This is derived from an O(1) calculation to determine the range of the median, and an O(M) array copy operation to retrieve the sub-list.

*One-Pass Bulk Insertion*

The Insertion sort algorithm begins at the end of the array and moves toward the beginning.
Each iteration shifts the current value to the right by the number of new elements yet to be inserted.
This happens N times.
When an element is inserted in its correct place (shifted to the right), the shift amount is decremented.
This happens K times.
This way, there is always exactly enough room being made for the elements that have yet to find their place.
We then make sure to repeat this process with the new element before moving on.

Note that the K elements must be first sorted before this insertion can work.
This operation will have O(K log(K)) complexity.

Thus, the entire operation runs in O(N + K + K log(K)), which simplifies to O(N + K log(K)).

*Wide Median Calculation*

The wide median of given width M is a range of elements centered around the true median of the list.
The starting point of this median is found easily if we have random access to the list.
This is obtained simply by finding the center, and shifting half the width to the left.

Specifically, the starting index I is given by N / 2 - M / 2, where N is the length of the list.
The ending point (exclusive) is then given by I + M.
This is an O(1) calculation.

The copy operation runs in O(M), so the entire operation is O(M) complexity.
