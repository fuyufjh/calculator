package algebra;

import java.util.Collections;
import java.util.List;

public abstract class LeafNode extends AbstractNode implements AlgebraNode {

    @Override
    public List<AlgebraNode> getInputs() {
        return Collections.emptyList();
    }
}
