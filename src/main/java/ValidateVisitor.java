import algebra.*;

import java.util.List;
import java.util.Map;

public class ValidateVisitor extends CalculatorBaseVisitor<AlgebraNode> {

    private final Map<String, DataType> variableTypes;

    public ValidateVisitor(Map<String, DataType> variableTypes) {
        this.variableTypes = variableTypes;
    }

    @Override
    public AlgebraNode visitCalculate(CalculatorParser.CalculateContext ctx) {
        return visit(ctx.plusOrMinus());
    }

    @Override
    public AlgebraNode visitPlus(CalculatorParser.PlusContext ctx) {
        return new PlusNode(visit(ctx.plusOrMinus()), visit(ctx.multOrDiv()));
    }

    @Override
    public AlgebraNode visitMinus(CalculatorParser.MinusContext ctx) {
        return new MinusNode(visit(ctx.plusOrMinus()), visit(ctx.multOrDiv()));
    }

    @Override
    public AlgebraNode visitMultiplication(CalculatorParser.MultiplicationContext ctx) {
        return new MultiplicationNode(visit(ctx.multOrDiv()), visit(ctx.unaryMinus()));
    }

    @Override
    public AlgebraNode visitDivision(CalculatorParser.DivisionContext ctx) {
        return new DivisionNode(visit(ctx.multOrDiv()), visit(ctx.unaryMinus()));
    }

    @Override
    public AlgebraNode visitChangeSign(CalculatorParser.ChangeSignContext ctx) {
        return new ChangeSignNode(visit(ctx.unaryMinus()));
    }

    @Override
    public AlgebraNode visitDouble(CalculatorParser.DoubleContext ctx) {
        return new LiteralNode(Double.parseDouble(ctx.DOUBLE().getText()));
    }

    @Override
    public AlgebraNode visitInt(CalculatorParser.IntContext ctx) {
        return new LiteralNode(Long.parseLong(ctx.INT().getText()));
    }

    @Override
    public AlgebraNode visitVariable(CalculatorParser.VariableContext ctx) {
        final String variable = ctx.ID().getText();
        return new VariableNode(variableTypes.get(variable), variable);
    }

    @Override
    public AlgebraNode visitFunction(CalculatorParser.FunctionContext ctx) {
        List<CalculatorParser.PlusOrMinusContext> inputs = ctx.plusOrMinus();
        String funcName = ctx.func().getText();
        switch (funcName) {
            case "sqrt":
                assert inputs.size() == 1;
                return new SqrtFunctionNode(visit(inputs.get(0)));
            case "log":
                assert inputs.size() == 1;
                return new LogFunctionNode(visit(inputs.get(0)));
            default:
                throw new IllegalArgumentException();
        }
    }
}
