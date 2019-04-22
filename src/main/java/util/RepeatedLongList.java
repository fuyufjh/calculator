package util;

import it.unimi.dsi.fastutil.longs.AbstractLongList;

public class RepeatedLongList extends AbstractLongList {

    private final long element;
    private final int size;

    public RepeatedLongList(int size, long element) {
        this.size = size;
        this.element = element;
    }

    @Override
    public long getLong(int index) {
        return element;
    }

    @Override
    public int size() {
        return size;
    }
}
