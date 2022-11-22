package com.mengxinya.ys.logiclang;

import antlr4.parser.expr.LogiclangLexer;
import antlr4.parser.expr.LogiclangParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

public class LogicExprVisitorImplTest {
    LogicExprVisitorImpl visitor = new LogicExprVisitorImpl();

    @Test
    public void test() throws LogicLangException {
        String expr = """
                var xiaobei = maybe(1, 2, 3, 4, 5);
                var xiaoku = maybe(1, 2, 3, 4, 5);
                var xiaolai = maybe(1, 2, 3, 4, 5);
                var xiaomi = maybe(1, 2, 3, 4, 5);
                var xiaomai = maybe(1, 2, 3, 4, 5);

                distinct(xiaobei, xiaoku, xiaolai, xiaomi, xiaomai);
                notEq(xiaobei, 5);
                notEq(xiaoku, 1);
                notEq(xiaolai, 1);
                notEq(xiaolai, 5);
                gt(xiaomi, xiaoku);
                notNear(xiaomai, xiaolai);
                notNear(xiaolai, xiaoku);

                print(xiaobei, xiaoku, xiaolai, xiaomi, xiaomai);
                """;
        LogicExpr logicExpr = eval(expr);
        LangTests.eval(logicExpr);
    }

    private LogicExpr eval(String expr) {
        CharStream input = CharStreams.fromString(expr);
        LogiclangLexer lexer = new LogiclangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LogiclangParser parser = new LogiclangParser(tokens);
        LogiclangParser.StmtContext tree = parser.stmt();
        return visitor.visit(tree);
    }
}
