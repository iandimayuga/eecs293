Ian Dimayuga (icd3)
EECS 293 HW06

RunningMedian.txt contains the pseudocode for the RunningMedian class.
SortedArrayList.txt contains the pseudocode for the SortedArrayList class, which is a data structure used by RunningMedian.

This design is intended to allow for arbitrary sizes of running median.
The only restriction is that elements be added two at a time, as specified.
This keeps the evenness of the list the same as the evenness of the median, eliminating the need to take averages.

Further, the SortedArrayList design is a general ArrayList-style design that allows for both random access and arbitrary bulk insertion, using a one-pass insertion sort to preserve order.
Because the input array must first be sorted before it is interpolated, an insertion executes in O(N + K log(K)) time, where N is the current size of the SortedArrayList and K is the size of the input.
This is acceptable because K is not expected to scale as quickly as N in most uses, and in this particular use K is known to be 2.
