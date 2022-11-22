package com.mengxinya.ys.logiclang;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LangTests {
    @Test
    public void testFloor() throws LogicLangException {
        LogicExpr expr = composeExpr(
                makeMaybeExpr("小贝", List.of(1, 2, 3, 4, 5)),
                makeMaybeExpr("小库", List.of(1, 2, 3, 4, 5)),
                makeMaybeExpr("小赖", List.of(1, 2, 3, 4, 5)),
                makeMaybeExpr("小米", List.of(1, 2, 3, 4, 5)),
                makeMaybeExpr("小麦", List.of(1, 2, 3, 4, 5)),
                makeDistinctConditionExpr(List.of("小贝", "小库", "小赖", "小米", "小麦")),
                makeNotConditionExpr("小贝", 5),
                makeNotConditionExpr("小库", 1),
                makeNotConditionExpr("小赖", 1),
                makeNotConditionExpr("小赖", 5),
                makeGtConditionExpr("小米", "小库"),
                makeNotNearConditionExpr("小麦", "小赖"),
                makeNotNearConditionExpr("小赖", "小库"),
                makePrintExpr("小贝", "小库", "小赖", "小米", "小麦")
        );

        eval(expr);
    }

    @Test
    public void testLiar() throws LogicLangException {
        LogicExpr expr = composeExpr(
                makeMaybeExpr("小贝", List.of(1, 2, 3, 4, 5)),
                makeMaybeExpr("小艾", List.of(1, 2, 3, 4, 5)),
                makeMaybeExpr("小丽", List.of(1, 2, 3, 4, 5)),
                makeMaybeExpr("小凯", List.of(1, 2, 3, 4, 5)),
                makeMaybeExpr("小马", List.of(1, 2, 3, 4, 5)),
                makeDistinctConditionExpr(List.of("小贝", "小艾", "小丽", "小凯", "小马")),
                composeOrExpr(
                        makeEqConditionExpr("小凯", 2),
                        makeEqConditionExpr("小贝", 3)
                ),
                composeOrExpr(
                        makeEqConditionExpr("小艾", 1),
                        makeEqConditionExpr("小丽", 2)
                ),
                composeOrExpr(
                        makeEqConditionExpr("小丽", 3),
                        makeEqConditionExpr("小艾", 5)
                ),
                composeOrExpr(
                        makeEqConditionExpr("小凯", 2),
                        makeEqConditionExpr("小马", 4)
                ),
                composeOrExpr(
                        makeEqConditionExpr("小马", 4),
                        makeEqConditionExpr("小贝", 1)
                ),
                makePrintExpr("小贝", "小艾", "小丽", "小凯", "小马")
        );

        eval(expr);
    }

    public static LogicExpr composeExpr(LogicExpr... exprArr) {
        if (exprArr.length == 1) {
            return exprArr[0];
        }

        LogicExpr restExpr = composeExpr(Arrays.copyOfRange(exprArr, 1, exprArr.length));
        return context -> {
            Context newContext = Context.copy(context);
            eval(exprArr[0], newContext);

            // 如果是Maybe表达式，当执行抛异常的时候，要自动选择重试方案。
            if ((exprArr[0] instanceof MaybeExpr head)) {
                try {
                    eval(restExpr, newContext);
                } catch (LogicLangException exception) {
                    eval(composeExpr(head.nextChance(), restExpr), Context.copy(context));
                }
            } else {
                eval(restExpr, newContext);
            }
        };
    }

    public static LogicExpr composeOrExpr(LogicExpr... exprArr) {
        if (exprArr.length == 1) {
            return exprArr[0];
        }
        LogicExpr restExpr = composeOrExpr(Arrays.copyOfRange(exprArr, 1, exprArr.length));
        return context -> {
            try {
                eval(exprArr[0], Context.copy(context));
            } catch (LogicLangException exception) {
                eval(restExpr, Context.copy(context));
            }
        };
    }

    public static MaybeExpr makeMaybeExpr(String varName, List<Integer> valList) {
        return new MaybeExpr() {
            @Override
            public MaybeExpr nextChance() throws LogicLangException {
                if (valList.size() == 1) {
                    throw new LogicLangException("no change " + varName);
                }
                return makeMaybeExpr(varName, valList.subList(1, valList.size()));
            }

            @Override
            public void execute(Context context) {
                context.setValue(varName, valList.get(0));
            }
        };
    }

    public static LogicExpr makePrintExpr(String... varNames) {
        return context -> {
            for (String varName : varNames) {
                System.out.println(varName + ": " + context.getValue(varName));
            }
        };
    }

    public static LogicExpr makeEqConditionExpr(String varName, Object value) {
        return context -> {
            if (!context.getValue(varName).equals(value)) {
                throw new LogicLangException("Eq: " + varName + ": " + value);
            }
        };
    }

    public static LogicExpr makeNotNearConditionExpr(String varName1, String varName2) {
        return context -> {
            if (Math.abs(
                    (Integer)(context.getValue(varName1)) - (Integer)(context.getValue(varName2))
            ) <= 1) {
                throw new LogicLangException("NotNear");
            }
        };
    }

    public static LogicExpr makeGtConditionExpr(String varName1, String varName2) {
        return context -> {
            if (!((Integer)(context.getValue(varName1)) > (Integer)(context.getValue(varName2)))) {
                throw new LogicLangException("GT");
            }
        };
    }

    public static LogicExpr makeNotConditionExpr(String varName, Object value) {
        return context -> {
            if (context.getValue(varName).equals(value)) {
                throw new LogicLangException("Not: " + value);
            }
        };
    }

    public static LogicExpr makeDistinctConditionExpr(List<String> args) {
        return context -> {
            if (!(args.stream().map(context::getValue).collect(Collectors.toSet()).size() == args.size())) {
                throw new LogicLangException("Distinct: " + args);
            }
        };
    }

    public static void eval(LogicExpr expr, Context context) throws LogicLangException {
        expr.execute(context);
    }

    public static void eval(LogicExpr expr) throws LogicLangException {
        eval(expr, Context.emptyContext());
    }

}
