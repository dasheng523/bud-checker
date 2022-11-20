package com.mengxinya.ys.checker;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class DuplicationChecker implements Checker<JSONObject> {

    private final Evaluator<JSONObject, SqlCondition> evaluator;

    private final SqlExecutor sqlExecutor;

    public DuplicationChecker(Evaluator<JSONObject, SqlCondition> evaluator, DuplicationCheckParams params) {
        this.evaluator = evaluator;
        this.sqlExecutor = params.getSqlExecutor();
    }

    @Override
    public Boolean eval(JSONObject input) {
        SqlCondition sqlCondition = evaluator.eval(input);

        // TODO 还应该可以自定义一些额外的参数
        List<Map<String, Object>> data = this.sqlExecutor.query(sqlCondition.getCondition(), sqlCondition.getParams());
        return data == null || data.isEmpty();
    }
}
