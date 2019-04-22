import algebra.AlgebraNode;
import algebra.DataType;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.codehaus.janino.ExpressionEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Launcher {

    private static final int REPEAT = 10;
    private static final int TEST_SIZE = 3<<20;

    private static final boolean OUTPUT_RESULT = false;

    public static void main(String[] args) throws Exception {

        final String expr = "var0 + 2*3 - 2/var1 + log(var1+1)";
        final List<DataType> variableTypes = ImmutableList.of(DataType.LONG, DataType.DOUBLE);

        CharStream input = CharStreams.fromString(expr);
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree root = parser.input();

        Random random = new Random();

        for (int t = 0; t < REPEAT; t++) {
            Runtime.getRuntime().gc();

            List<List<Object>> variablesList = new ArrayList<>(TEST_SIZE);
            LongList vector0 = new LongArrayList(TEST_SIZE);
            DoubleList vector1 = new DoubleArrayList(TEST_SIZE);
            for (int i = 0; i < TEST_SIZE; i++) {
                final long var0 = random.nextLong();
                final double var1 = random.nextDouble();
                final List<Object> variables = ImmutableList.of(var0, var1);
                vector0.add(var0);
                vector1.add(var1);
                variablesList.add(variables);
            }
            ImmutableList<List> vectors = ImmutableList.of(vector0, vector1);
            doTest(root, variableTypes, variablesList, vectors);
        }
    }

    private static void doTest(ParseTree root, List<DataType> variableTypes, List<List<Object>> variablesList, List<List> vectors) throws Exception {

        System.out.println("------------------------------------------------");

        {
            long beginTime = System.nanoTime();
            for (List<Object> variables : variablesList) {
                Object result = solveByInterpreter(root, variables);
                if (OUTPUT_RESULT) {
                    System.out.println(result);
                }
            }
            long endTime = System.nanoTime();
            System.out.println("[ITERATE] Run for " + variablesList.size() + " times takes " + (endTime - beginTime) + " ns");
        }

        {
            ExpressionEvaluator evaluator = solveByCodeGen_Compile(root, variableTypes);

            long beginTime = System.nanoTime();
            for (List<Object> variables : variablesList) {
                Object result = solveByCodeGen_Execute(evaluator, variables);
                if (OUTPUT_RESULT) {
                    System.out.println(result);
                }
            }
            long endTime = System.nanoTime();
            System.out.println("[COMPILE] Run for " + variablesList.size() + " times takes " + (endTime - beginTime) + " ns");
        }

        {
            long beginTime = System.nanoTime();
            List results = solveByVectorInterpreter(root, vectors);
            if (OUTPUT_RESULT) {
                for (Object result : results) {
                    System.out.println(result);
                }
            }
            long endTime = System.nanoTime();
            System.out.println("[VECTOR]  Run for " + variablesList.size() + " times takes " + (endTime - beginTime) + " ns");
        }
    }

    private static Object solveByInterpreter(ParseTree root, List<Object> variables) {
        InterpreterVisitor calcVisitor = new InterpreterVisitor(variables);
        return calcVisitor.visit(root);
    }

    private static List solveByVectorInterpreter(ParseTree root, List<List> variables) {
        VectorInterpreterVisitor calcVisitor = new VectorInterpreterVisitor(variables);
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
        System.err.println("Code Generated: " + code);

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
