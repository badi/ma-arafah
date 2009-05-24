/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tajmi.data.clusterable;

import java.util.concurrent.Callable;

/**
 *
 * @author badi
 */
public interface DistanceAlgorithm<T> extends Callable<Double> {

    /**
     * Compute the params between this object and the other.
     * @para me the first object
     * @param you the other object
     * @return a double representing the params between the two objects
     */
    public DistanceAlgorithm params(T first, T second);



    public Double call ();
}
