import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InterpreterVisitor extends CalculatorBaseVisitor<Object> {

    private final List<Object> variables;

    public InterpreterVisitor(List<Object> variables) {
        this.variables = variables;
    }

    @Override
    public Object visitCalculate(CalculatorParser.CalculateContext ctx) {
        return visit(ctx.plusOrMinus());
    }

    @Override
    public Object visitPlus(CalculatorParser.PlusContext ctx) {
        Object left = visit(ctx.plusOrMinus());
        Object right = visit(ctx.multOrDiv());
        if (left instanceof Long && right instanceof Long) {
            return (Long) left + (Long) right;
        } else if (left instanceof Long && right instanceof Double) {
            return (Long) left + (Double) right;
        } else if (left instanceof Double && right instanceof Long) {
            return (Double) left + (Long) right;
        } else if (left instanceof Double && right instanceof Double) {
            return (Double) left + (Double) right;
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public Object visitMinus(CalculatorParser.MinusContext ctx) {
        Object left = visit(ctx.plusOrMinus());
        Object right = visit(ctx.multOrDiv());
        if (left instanceof Long && right instanceof Long) {
            return (Long) left - (Long) right;
        } else if (left instanceof Long && right instanceof Double) {
            return (Long) left - (Double) right;
        } else if (left instanceof Double && right instanceof Long) {
            return (Double) left - (Long) right;
        } else if (left instanceof Double && right instanceof Double) {
            return (Double) left - (Double) right;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Object visitMultiplication(CalculatorParser.MultiplicationContext ctx) {
        Object left = visit(ctx.multOrDiv());
        Object right = visit(ctx.unaryMinus());
        if (left instanceof Long && right instanceof Long) {
            return (Long) left * (Long) right;
        } else if (left instanceof Long && right instanceof Double) {
            return (Long) left * (Double) right;
        } else if (left instanceof Double && right instanceof Long) {
            return (Double) left * (Long) right;
        } else if (left instanceof Double && right instanceof Double) {
            return (Double) left * (Double) right;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Object visitDivision(CalculatorParser.DivisionContext ctx) {
        Object left = visit(ctx.multOrDiv());
        Object right = visit(ctx.unaryMinus());
        if (left instanceof Long && right instanceof Long) {
            return (Long) left / (Long) right;
        } else if (left instanceof Long && right instanceof Double) {
            return (Long) left / (Double) right;
        } else if (left instanceof Double && right instanceof Long) {
            return (Double) left / (Long) right;
        } else if (left instanceof Double && right instanceof Double) {
            return (Double) left / (Double) right;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Object visitChangeSign(CalculatorParser.ChangeSignContext ctx) {
        Object value = visit(ctx.unaryMinus());
        if (value instanceof Long) {
            return -1 * (Long) value;
        } else if (value instanceof Double) {
            return -1.0 * (Double) value;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Object visitBraces(CalculatorParser.BracesContext ctx) {
        return visit(ctx.plusOrMinus());
    }

    @Override
    public Long visitInt(CalculatorParser.IntContext ctx) {
        return Long.parseLong(ctx.INT().getText());
    }

    @Override
    public Double visitDouble(CalculatorParser.DoubleContext ctx) {
        return Double.parseDouble(ctx.DOUBLE().getText());
    }

    @Override
    public Object visitVariable(CalculatorParser.VariableContext ctx) {
        final int varId = Integer.valueOf(ctx.ID().getText().substring(3)); // 'var123'
        Object value = variables.get(varId);
        assert value != null;
        return value;
    }

    @Override
    public Object visitFunction(CalculatorParser.FunctionContext ctx) {
        List<CalculatorParser.PlusOrMinusContext> inputs = ctx.plusOrMinus();
        List<Object> inputValues = inputs.stream()
                .map(in -> (Double) visit(in))
                .collect(Collectors.toList());
        String funcName = ctx.func().getText();
        switch (funcName) {
            case "sqrt":
                assert inputs.size() == 1;
                return Math.sqrt((Double) inputValues.get(0));
            case "log":
                assert inputs.size() == 1;
                return Math.log((Double) inputValues.get(0));
            default:
                throw new IllegalArgumentException();
        }
    }
}
