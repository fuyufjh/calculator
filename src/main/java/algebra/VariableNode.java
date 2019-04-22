package algebra;

public class VariableNode extends LeafNode implements AlgebraNode {

    private final DataType type;
    private final int varId;

    public VariableNode(DataType type, int varId) {
        this.type = type;
        this.varId = varId;
    }

    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public String generateCode() {
        return "var" + varId;
    }
}
