package edu.coursera.parallel;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * Class wrapping methods for implementing reciprocal array sum in parallel.
 */
public final class ReciprocalArraySum {

    /**
     * Default constructor.
     */
    private ReciprocalArraySum() {
    }

    /**
     * Sequentially compute the sum of the reciprocal values for a given array.
     *
     * @param input Input array
     * @return The sum of the reciprocals of the array input
     */
    protected static double seqArraySum(final double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += 1 / input[i];
        }
        return sum;
    }

    /**
     * Computes the size of each chunk, given the number of chunks to create
     * across a given number of elements.
     *
     * @param nChunks The number of chunks to create
     * @param nElements The number of elements to chunk across
     * @return The default chunk size
     */
    private static int getChunkSize(final int nChunks, final int nElements) {
        // Integer ceil
        return (nElements + nChunks - 1) / nChunks;
    }

    /**
     * Computes the inclusive element index that the provided chunk starts at,
     * given there are a certain number of chunks.
     *
     * @param chunk The chunk to compute the start of
     * @param nChunks The number of chunks created
     * @param nElements The number of elements to chunk across
     * @return The inclusive index that this chunk starts at in the set of
     *         nElements
     */
    private static int getChunkStartInclusive(final int chunk,
            final int nChunks, final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        return chunk * chunkSize;
    }

    /**
     * Computes the exclusive element index that the provided chunk ends at,
     * given there are a certain number of chunks.
     *
     * @param chunk The chunk to compute the end of
     * @param nChunks The number of chunks created
     * @param nElements The number of elements to chunk across
     * @return The exclusive end index for this chunk
     */
    private static int getChunkEndExclusive(final int chunk, final int nChunks,
            final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        final int end = (chunk + 1) * chunkSize;
        if (end > nElements) {
            return nElements;
        } else {
            return end;
        }
    }

    /**
     * This class stub can be filled in to implement the body of each task
     * created to perform reciprocal array sum in parallel.
     */
    private static class ReciprocalArraySumTask extends RecursiveAction {
        private final int startIndexInclusive;
        private final int endIndexExclusive;
        private final double[] input;
        private double value;

        /**
         * Constructor.
         * @param setStartIndexInclusive Set the starting index to begin
         *        parallel traversal at.
         * @param setEndIndexExclusive Set ending index for parallel traversal.
         * @param setInput Input values
         */
        ReciprocalArraySumTask(final int setStartIndexInclusive,
                final int setEndIndexExclusive, final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
        }

        /**
         * Getter for the value produced by this task.
         * @return Value produced by this task
         */
        public double getValue() {
            return value;
        }

        @Override
        protected void compute() {
            value = 0;
            for (int i = startIndexInclusive; i < endIndexExclusive; i++) {
                value += 1/input[i];
            }
        }
    }

    /**
     *
     * @param input Input array
     * @return The sum of the reciprocals of the array input
     */
    protected static double parArraySum(final double[] input) {
        assert input.length % 2 == 0;
        double sum = 0;
        int len = input.length/ 2;

        ReciprocalArraySumTask upperHalf = new ReciprocalArraySumTask(0, len, input);
        ReciprocalArraySumTask lowerHalf = new ReciprocalArraySumTask(len, input.length, input);
        upperHalf.fork();
        lowerHalf.compute();
        upperHalf.join();

        sum  = upperHalf.getValue() + lowerHalf.getValue();
        return sum;
    }

    /**
     * @param input Input array
     * @param numTasks The number of tasks to create
     * @return The sum of the reciprocals of the array input
     */
    protected static double parManyTaskArraySum(final double[] input,
            final int numTasks) {
        double sum = 0;
        ReciprocalArraySumTask output[] = new ReciprocalArraySumTask[numTasks];
        for (int i=0; i<numTasks; i++) {
            output[i] = new ReciprocalArraySumTask(
                    getChunkStartInclusive(i, numTasks, input.length),
                    getChunkEndExclusive(i, numTasks, input.length),
                    input
            );
        }
        for (int i=1; i<numTasks; i++) {
            output[i].fork();
        }
        output[0].compute();
        for (int i=1; i<numTasks; i++) {
            output[i].join();
        }
        for (int i=0; i<numTasks; i++) {
            sum += output[i].getValue();
        }
        return sum;
    }
}
