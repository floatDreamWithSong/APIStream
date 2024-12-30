package com.daydreamer.apistream.common.systemcall;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.enums.SelectableSQLType;
import com.daydreamer.apistream.common.modules.SystemSQLResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemSQLRunner {
    private String selectableSql(SelectableSQLType type, String sql) {
        SystemSQLResult result = new SystemSQLResult();
        result.sql = sql;
        try {
            switch (type) {
                case SELECT_ONE -> result.result = SqlRunner.db().selectOne(sql);
                case SELECT -> result.result = SqlRunner.db().selectList(sql);
                case SELECT_COUNT -> result.result = SqlRunner.db().selectCount(sql);
                case SELECT_OBJ -> result.result = SqlRunner.db().selectObj(sql);
                case SELECT_OBJS -> result.result = SqlRunner.db().selectObjs(sql);
            }
        } catch (Exception e) {
            String err = e.getMessage();
            log.error(err);
            result.error = err;
        }
        return JsonProcessor.gson.toJson(result);
    }

    public String selectOne(String sql) {
        return selectableSql(SelectableSQLType.SELECT_ONE, sql);
    }
    public String selectList(String sql) {
        return selectableSql(SelectableSQLType.SELECT, sql);
    }
    public String selectCount(String sql) {
        return selectableSql(SelectableSQLType.SELECT_COUNT, sql);
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
