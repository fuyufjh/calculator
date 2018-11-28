package algebra;

import java.util.Collections;
import java.util.List;

public class SqrtFunctionNode extends FunctionNode implements AlgebraNode {

    private final AlgebraNode arg;

    public SqrtFunctionNode(AlgebraNode arg) {
        assert arg.getType() == DataType.DOUBLE;
        this.arg = arg;
    }

    @Override
    public List<AlgebraNode> getInputs() {
        return Collections.singletonList(arg);
    }

    @Override
    public DataType getType() {
        return DataType.DOUBLE;
    }

    @Override
    public String generateCode() {
        return "java.lang.Math.sqrt(" + arg.generateCode() + ")";
    }
}
