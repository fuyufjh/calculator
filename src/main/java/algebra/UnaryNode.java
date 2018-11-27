package algebra;

import java.util.Collections;
import java.util.List;

public abstract class UnaryNode extends AbstractNode implements AlgebraNode {

    private final AlgebraNode input;

    public UnaryNode(AlgebraNode input) {
        this.input = input;
    }

    public AlgebraNode getInput() {
        return input;
    }

    @Override
    public List<AlgebraNode> getInputs() {
        return Collections.singletonList(input);
    }
}
