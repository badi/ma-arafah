
package tajmi.abstracts.som;

import java.util.concurrent.Callable;
import tajmi.abstracts.som.NeighborhoodFunc;
import tajmi.som.Field;
import tajmi.som.Position;

/**
 * Updates the field after determining the best matching unit (bmu) to a datum projected.
 * @author badi
 */
public abstract class UpdateFunc<F,D> implements Callable<Field<F>> {


    Field<F> field;
    D datum;
    Position bmu_pos;
    double learning_restraint;
    NeighborhoodFunc neighborhoodf;

    public Position getBestMatchingUnitPosition() {
        return bmu_pos;
    }

    public D getDatum() {
        return datum;
    }

    public Field<F> getField() {
        return field;
    }

    public double getLearningRestraint() {
        return learning_restraint;
    }

    public NeighborhoodFunc getNeighborhoodFunc() {
        return neighborhoodf;
    }




    /**
     * @param field
     * @param datum the input datum being projected onto the field
     * @param bmu the best matching unit
     * @param restraint determined by the neightborhood function
     * @return
     */
    public UpdateFunc params(Field<F> field, D datum, Position bmu_pos, double learning_restraint, NeighborhoodFunc neighborhoodf) {
        this.field = field;
        this.datum = datum;
        this.bmu_pos = bmu_pos;
        this.learning_restraint = learning_restraint;
        this.neighborhoodf = neighborhoodf;

        return this;
    }


    public abstract Field<F> call ();

}
