package com.daydreamer.faastest.systemcall;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.common.enums.SQLType;
import com.daydreamer.faastest.common.modules.SystemSQLResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemSQLRunner {
    public static String execSql(SQLType type, String sql) {
        SystemSQLResult result = new SystemSQLResult();
        try {
            switch (type) {
                case SELECTONE -> result.result = SqlRunner.db().selectOne(sql);
                case UPDATE -> result.result = SqlRunner.db().update(sql);
                case DELETE -> result.result = SqlRunner.db().delete(sql);
                case SELECT -> result.result = SqlRunner.db().selectList(sql);
                case INSERT -> result.result = SqlRunner.db().insert(sql);
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

    public static String createDatabase(String dbName) {
        SystemSQLResult result = new SystemSQLResult();
        String sql = "CREATE DATABASE " + dbName;
        try {
            SqlRunner.db().update(sql);
            result.result = "Database " + dbName + " created successfully.";
        } catch (Exception e) {
            String err = e.getMessage();
            log.error(err);
            result.error = err;
        }
        return JsonProcessor.gson.toJson(result);
    }

    public static String useDatabase(String dbName) {
        SystemSQLResult result = new SystemSQLResult();
        try {
            SqlRunner.db().update("USE " + dbName);
            result.result = "use successfully.";
        } catch (Exception e) {
            String err = e.getMessage();
            log.error(err);
            result.error = err;
        }
        return JsonProcessor.gson.toJson(result);
    }

    public static String dropDatabase(String dbName) {
        SystemSQLResult result = new SystemSQLResult();
        try {
            SqlRunner.db().update("drop database " + dbName);
            result.result = "drop successfully.";
        } catch (Exception e) {
            String err = e.getMessage();
            log.error(err);
            result.error = err;
        }
        return JsonProcessor.gson.toJson(result);
    }

    public static String createTable(String createTableSql) {
        SystemSQLResult result = new SystemSQLResult();
        try {
            SqlRunner.db().update("create table " + createTableSql);
            result.result = "Table created successfully.";
        } catch (Exception e) {
            String err = e.getMessage();
            log.error(err);
            result.error = err;
        }
        return JsonProcessor.gson.toJson(result);
    }
}
