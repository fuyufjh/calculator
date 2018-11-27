package algebra;

import java.util.List;

public abstract class AbstractNode implements AlgebraNode {

    private transient DataType dataType;

    @Override
    public DataType getType() {
        if (dataType == null) {
            dataType = inferTypeFromInputs();
        }
        return dataType;
    }

    private DataType inferTypeFromInputs() {
        List<AlgebraNode> inputs = getInputs();
        assert !inputs.isEmpty();
        for (AlgebraNode input : inputs) {
            if (input.getType() == DataType.DOUBLE) {
                return DataType.DOUBLE;
            }
        }
        return DataType.LONG;
    }
}
