# Mini Project 1: Reciprocal-Array-Sum using the Java Fork/Join Framework

## Parallel Programming in Java - Coursera

### Project Instructions

-  Modify the `ReciprocalArraySum.parArraySum()` method to implement the reciprocal-array-sum computation in parallel using the Java Fork Join framework by partitioning the input array in half and computing the reciprocal array sum on the first and second half in parallel, before combining the results. There are TODOs in the source file to guide you, and you are free to refer to the lecture and demonstration videos.  

-  Modify the `ReciprocalArraySum.parManyTaskArraySum` method to implement the reciprocal-array-sum computation in parallel using Java's Fork Join framework again, but using a given number of tasks (not just two).  Note that the getChunkStartInclusive and getChunkEndExclusive utility methods are provided for your convenience to help with calculating the region of the input array a certain task should process.
