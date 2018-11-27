import algebra.*;

import java.util.Map;

public class ValidateVisitor extends CalculatorBaseVisitor<AlgebraNode> {

    private final Map<String, DataType> variableTypes;

    public ValidateVisitor(Map<String, DataType> variableTypes) {
        this.variableTypes = variableTypes;
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
}
