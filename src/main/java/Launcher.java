import algebra.AlgebraNode;
import algebra.DataType;
import com.google.common.collect.ImmutableList;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.codehaus.janino.ExpressionEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Launcher {

    public static void main(String[] args) throws Exception {

        final String expr = "var0 + 2*3 - 2/var1 + log(var1+1)";
        final List<DataType> variableTypes = ImmutableList.of(DataType.LONG, DataType.DOUBLE);

        CharStream input = CharStreams.fromString(expr);
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree root = parser.input();

        Random random = new Random();

        final int repeat = 10;
        final int testSize = 1000000;

        for (int t = 0; t < repeat; t++) {
            final List<List<Object>> variablesList = new ArrayList<>(testSize);
            for (int i = 0; i < testSize; i++) {
                final List<Object> variables = ImmutableList.of(random.nextLong(), random.nextDouble());
                variablesList.add(variables);
            }
            doTest(root, variableTypes, variablesList);
        }
    }

    private static void doTest(ParseTree root, List<DataType> variableTypes, List<List<Object>> variablesList) throws Exception {

        System.out.println("------------------------------------------------");

        {
            long beginTime = System.nanoTime();
            for (List<Object> variables : variablesList) {
                solveByInterpreter(root, variables);
            }
            long endTime = System.nanoTime();
            System.out.println("[ITERATE] Run for " + variablesList.size() + " times takes " + (endTime - beginTime) + " ns");
        }

        {
            ExpressionEvaluator evaluator = solveByCodeGen_Compile(root, variableTypes);

            long beginTime = System.nanoTime();
            for (List<Object> variables : variablesList) {
                solveByCodeGen_Execute(evaluator, variables);
            }
            long endTime = System.nanoTime();
            System.out.println("[COMPILE] Run for " + variablesList.size() + " times takes " + (endTime - beginTime) + " ns");
        }
    }

    private static Object solveByInterpreter(ParseTree root, List<Object> variables) {
        InterpreterVisitor calcVisitor = new InterpreterVisitor(variables);
        return calcVisitor.visit(root);
    }

    private static ExpressionEvaluator solveByCodeGen_Compile(ParseTree root, List<DataType> variableTypes) throws Exception {
        /*
         * Step 1. Convert AST to Algebra node (validating)
         */
        ValidateVisitor validateVisitor = new ValidateVisitor(variableTypes);
        AlgebraNode rootNode = validateVisitor.visit(root);

        /*
         * Step 2. Generate code from algebra node and compile with janino
         */
        String code = rootNode.generateCode();
        System.out.println("Code Generated: " + code);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        final int numParameters = variableTypes.size();
        String[] parameterNames = new String[numParameters];
        Class[] parameterTypes = new Class[numParameters];
        for (int i = 0; i < variableTypes.size(); i++) {
            parameterNames[i] = "var" + i;
            parameterTypes[i] = variableTypes.get(i) == DataType.DOUBLE ? double.class : long.class;
        }
        evaluator.setParameters(parameterNames, parameterTypes);
        evaluator.setExpressionType(rootNode.getType() == DataType.DOUBLE ? double.class : long.class);
        evaluator.cook(code);
        return evaluator;
    }

    private static Object solveByCodeGen_Execute(ExpressionEvaluator evaluator, List<Object> variables) throws Exception {
        final int numParameters = variables.size();

        /*
         * Step 3. Evaluate with given parameters
         */
        Object[] parameterValues = new Object[numParameters];
        for (int i = 0; i < numParameters; i++) {
            parameterValues[i] = variables.get(i);
        }
        return evaluator.evaluate(parameterValues);
    }
}
