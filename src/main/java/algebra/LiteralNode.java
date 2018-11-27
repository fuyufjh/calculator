package algebra;

public class LiteralNode extends LeafNode implements AlgebraNode {

    private final Object value;

    public LiteralNode(Object value) {
        assert value != null;
        assert value instanceof Long || value instanceof Double;
        this.value = value;
    }

    @Override
    public DataType getType() {
        return value instanceof Double ? DataType.DOUBLE : DataType.LONG;
    }

    @Override
    public String generateCode() {
        return value.toString();
    }
}
