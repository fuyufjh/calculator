import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleLists;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import util.RepeatedDoubleList;
import util.RepeatedLongList;

import java.util.List;
import java.util.stream.Collectors;

public class VectorInterpreterVisitor extends CalculatorBaseVisitor<List> {

    private final int vectorLength;
    private final List<List> variableVectors;

    public VectorInterpreterVisitor(List<List> variableVectors) {
        this.variableVectors = variableVectors;
        this.vectorLength = variableVectors.get(0).size();
    }

    @Override
    public List visitCalculate(CalculatorParser.CalculateContext ctx) {
        return visit(ctx.plusOrMinus());
    }

    @Override
    public List visitPlus(CalculatorParser.PlusContext ctx) {
        Object left = visit(ctx.plusOrMinus());
        Object right = visit(ctx.multOrDiv());
        if (left instanceof LongList && right instanceof LongList) {
            LongList result = new LongArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((LongList) left).getLong(i) + ((LongList) right).getLong(i));
            }
            return result;
        } else if (left instanceof LongList && right instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((LongList) left).getLong(i) + ((DoubleList) right).getDouble(i));
            }
            return result;
        } else if (left instanceof DoubleList && right instanceof LongList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((DoubleList) left).getDouble(i) + ((LongList) right).getLong(i));
            }
            return result;
        } else if (left instanceof DoubleList && right instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((DoubleList) left).getDouble(i) + ((DoubleList) right).getDouble(i));
            }
            return result;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List visitMinus(CalculatorParser.MinusContext ctx) {
        Object left = visit(ctx.plusOrMinus());
        Object right = visit(ctx.multOrDiv());
        if (left instanceof LongList && right instanceof LongList) {
            LongList result = new LongArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((LongList) left).getLong(i) - ((LongList) right).getLong(i));
            }
            return result;
        } else if (left instanceof LongList && right instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((LongList) left).getLong(i) - ((DoubleList) right).getDouble(i));
            }
            return result;
        } else if (left instanceof DoubleList && right instanceof LongList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((DoubleList) left).getDouble(i) - ((LongList) right).getLong(i));
            }
            return result;
        } else if (left instanceof DoubleList && right instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((DoubleList) left).getDouble(i) - ((DoubleList) right).getDouble(i));
            }
            return result;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List visitMultiplication(CalculatorParser.MultiplicationContext ctx) {
        Object left = visit(ctx.multOrDiv());
        Object right = visit(ctx.unaryMinus());
        if (left instanceof LongList && right instanceof LongList) {
            LongList result = new LongArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((LongList) left).getLong(i) * ((LongList) right).getLong(i));
            }
            return result;
        } else if (left instanceof LongList && right instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((LongList) left).getLong(i) * ((DoubleList) right).getDouble(i));
            }
            return result;
        } else if (left instanceof DoubleList && right instanceof LongList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((DoubleList) left).getDouble(i) * ((LongList) right).getLong(i));
            }
            return result;
        } else if (left instanceof DoubleList && right instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((DoubleList) left).getDouble(i) * ((DoubleList) right).getDouble(i));
            }
            return result;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List visitDivision(CalculatorParser.DivisionContext ctx) {
        Object left = visit(ctx.multOrDiv());
        Object right = visit(ctx.unaryMinus());
        if (left instanceof LongList && right instanceof LongList) {
            LongList result = new LongArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((LongList) left).getLong(i) / ((LongList) right).getLong(i));
            }
            return result;
        } else if (left instanceof LongList && right instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((LongList) left).getLong(i) / ((DoubleList) right).getDouble(i));
            }
            return result;
        } else if (left instanceof DoubleList && right instanceof LongList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((DoubleList) left).getDouble(i) / ((LongList) right).getLong(i));
            }
            return result;
        } else if (left instanceof DoubleList && right instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < vectorLength; i++) {
                result.add(((DoubleList) left).getDouble(i) / ((DoubleList) right).getDouble(i));
            }
            return result;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List visitChangeSign(CalculatorParser.ChangeSignContext ctx) {
        List input = visit(ctx.unaryMinus());
        if (input instanceof LongList) {
            LongList result = new LongArrayList(vectorLength);
            for (int i = 0; i < input.size(); i++) {
                result.add(-((LongList) input).getLong(i));
            }
            return result;
        } else if (input instanceof DoubleList) {
            DoubleList result = new DoubleArrayList(vectorLength);
            for (int i = 0; i < input.size(); i++) {
                result.add(-((DoubleList) input).getDouble(i));
            }
            return result;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List visitBraces(CalculatorParser.BracesContext ctx) {
        return visit(ctx.plusOrMinus());
    }

    @Override
    public LongList visitInt(CalculatorParser.IntContext ctx) {
        long value = Long.parseLong(ctx.INT().getText());
        return new RepeatedLongList(vectorLength, value);
    }

    @Override
    public DoubleList visitDouble(CalculatorParser.DoubleContext ctx) {
        double value = Double.parseDouble(ctx.DOUBLE().getText());
        return new RepeatedDoubleList(vectorLength, value);
    }

    @Override
    public List visitVariable(CalculatorParser.VariableContext ctx) {
        final int varId = Integer.valueOf(ctx.ID().getText().substring(3)); // 'var123'
        List value = variableVectors.get(varId);
        assert value != null;
        return value;
    }

    @Override
    public List visitFunction(CalculatorParser.FunctionContext ctx) {
        List<CalculatorParser.PlusOrMinusContext> inputs = ctx.plusOrMinus();
        List<List> inputValues = inputs.stream()
                .map(in -> visit(in))
                .collect(Collectors.toList());

        String funcName = ctx.func().getText();
        switch (funcName) {
            case "sqrt": {
                DoubleList result = new DoubleArrayList(vectorLength);
                assert inputs.size() == 1;
                List arg = inputValues.get(0);
                if (arg instanceof DoubleList) {
                    for (int i = 0; i < vectorLength; i++) {
                        result.add(Math.sqrt(((DoubleList) arg).getDouble(i)));
                    }
                } else if (arg instanceof LongList) {
                    for (int i = 0; i < vectorLength; i++) {
                        result.add(Math.sqrt((double) ((LongList) arg).getLong(i)));
                    }
                }
                return result;
            }
            case "log": {
                DoubleList result = new DoubleArrayList(vectorLength);
                assert inputs.size() == 1;
                List arg = inputValues.get(0);
                if (arg instanceof DoubleList) {
                    for (int i = 0; i < vectorLength; i++) {
                        result.add(Math.log(((DoubleList) arg).getDouble(i)));
                    }
                } else if (arg instanceof LongList) {
                    for (int i = 0; i < vectorLength; i++) {
                        result.add(Math.log((double) ((LongList) arg).getLong(i)));
                    }
                }
                return result;
            }
            default:
                throw new IllegalArgumentException();
        }
    }
}
