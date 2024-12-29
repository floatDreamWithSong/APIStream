package com.daydreamer.faastest.common.systemcall;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.common.enums.SelectableSQLType;
import com.daydreamer.faastest.common.modules.SystemSQLResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemSQLRunner {
    private String selectableSql(SelectableSQLType type, String sql) {
        SystemSQLResult result = new SystemSQLResult();
        result.sql = sql;
        try {
            switch (type) {
                case SELECTONE -> result.result = SqlRunner.db().selectOne(sql);
                case SELECT -> result.result = SqlRunner.db().selectList(sql);
                case SELECTCOUNT -> result.result = SqlRunner.db().selectCount(sql);
                case SELECTOBJ -> result.result = SqlRunner.db().selectObj(sql);
                case SELECTOBJS -> result.result = SqlRunner.db().selectObjs(sql);
            }
        } catch (Exception e) {
            String err = e.getMessage();
            log.error(err);
            result.error = err;
        }
        return JsonProcessor.gson.toJson(result);
    }

    public String selectOne(String sql) {
        return selectableSql(SelectableSQLType.SELECTONE, sql);
    }
    public String select(String sql) {
        return selectableSql(SelectableSQLType.SELECT, sql);
    }
    public String selectCount(String sql) {
        return selectableSql(SelectableSQLType.SELECTCOUNT, sql);
    }
    public String selectObj(String sql) {
        return selectableSql(SelectableSQLType.SELECTOBJ, sql);
    }
    public String selectObjs(String sql) {
        return selectableSql(SelectableSQLType.SELECTOBJS, sql);
    }


    public String execSql(String sql) {
        SystemSQLResult result = new SystemSQLResult();
        result.sql = sql;
        try {
            SqlRunner.db().update(sql);
            result.result = "success";
        } catch (Exception e) {
            String err = e.getMessage();
            log.error(err);
            result.error = err;
        }
        return JsonProcessor.gson.toJson(result);
    }

}
