import algebra.AlgebraNode;
import algebra.DataType;
import com.google.common.collect.ImmutableMap;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.codehaus.janino.ExpressionEvaluator;

import java.util.Map;

public class Launcher {

    public static void main(String[] args) throws Exception {

        final String expr = "a + 2*3 - 2/x + log(x+1)";
        final Map<String, DataType> variableTypes = ImmutableMap.of(
                "a", DataType.LONG,
                "x", DataType.DOUBLE);
        final Map<String, Object> variables = ImmutableMap.of(
                "a", 10L,
                "x", 3.1415);

        CharStream input = CharStreams.fromString(expr);
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree root = parser.input();

        {
            Object result = solveByInterpreter(root, variables);
            System.out.println("Result: " + result + " (" + result.getClass() + ")");
        }
        {
            Object result = solveByCodeGen(root, variableTypes, variables);
            System.out.println("Result: " + result + " (" + result.getClass() + ")");
        }
    }

    private static Object solveByInterpreter(ParseTree root, Map<String, Object> variables) {
        InterpreterVisitor calcVisitor = new InterpreterVisitor(variables);
        return calcVisitor.visit(root);
    }

    private static Object solveByCodeGen(ParseTree root, Map<String, DataType> variableTypes, Map<String, Object> variables) throws Exception {
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
        int index = 0;
        for (Map.Entry<String, DataType> entry : variableTypes.entrySet()) {
            parameterNames[index] = entry.getKey();
            parameterTypes[index] = entry.getValue() == DataType.DOUBLE ? double.class : long.class;
            index++;
        }
        evaluator.setParameters(parameterNames, parameterTypes);
        evaluator.setExpressionType(rootNode.getType() == DataType.DOUBLE ? double.class : long.class);
        evaluator.cook(code);

        /*
         * Step 3. Evaluate with given parameters
         */
        Object[] parameterValues = new Object[numParameters];
        for (int i = 0; i < parameterNames.length; i++) {
            parameterValues[i] = variables.get(parameterNames[i]);
        }
        return evaluator.evaluate(parameterValues);
    }
}
