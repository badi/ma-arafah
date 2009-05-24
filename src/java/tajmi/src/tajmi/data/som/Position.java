
package tajmi.data.som;

/**
 * Used to index the position within the Field.
 * Should only be used withing the tajmi.data.clusterable.som namespace
 * and any classes that the implement the interfaces therein.
 * 
 * @author badi
 */
public class Position {

    private int x, y;

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals (Object o) {
        if (o instanceof Position) {
            Position p = (Position) o;
            Position t = this;
            return (t.x() == p.x() && t.y() == p.y());
        } else return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.x;
        hash = 67 * hash + this.y;
        return hash;
    }
}
