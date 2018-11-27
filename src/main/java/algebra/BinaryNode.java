package algebra;

import java.util.Arrays;
import java.util.List;

public abstract class BinaryNode extends AbstractNode implements AlgebraNode {

    private final AlgebraNode left;
    private final AlgebraNode right;

    public BinaryNode(AlgebraNode left, AlgebraNode right) {
        this.left = left;
        this.right = right;
    }

    public AlgebraNode getLeft() {
        return left;
    }

    public AlgebraNode getRight() {
        return right;
    }

    @Override
    public List<AlgebraNode> getInputs() {
        return Arrays.asList(left, right);
    }
}
