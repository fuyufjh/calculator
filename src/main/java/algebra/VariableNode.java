package algebra;

public class VariableNode extends LeafNode implements AlgebraNode {

    private final DataType type;
    private final String name;

    public VariableNode(DataType type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public String generateCode() {
        return name;
    }
}
