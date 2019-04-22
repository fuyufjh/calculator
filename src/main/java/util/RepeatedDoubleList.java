package util;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;

public class RepeatedDoubleList extends AbstractDoubleList {

    private final double element;
    private final int size;

    public RepeatedDoubleList(int size, double element) {
        this.size = size;
        this.element = element;
    }

    @Override
    public double getDouble(int index) {
        return element;
    }

    @Override
    public int size() {
        return size;
    }
}
