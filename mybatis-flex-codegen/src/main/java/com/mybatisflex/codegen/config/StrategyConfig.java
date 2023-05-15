package com.mybatisflex.codegen.config;

import com.mybatisflex.codegen.template.EnjoyTemplate;
import com.mybatisflex.codegen.template.ITemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 表策略配置。
 *
 * @author 王帅
 * @since 2023-05-14
 */
@Data
@Accessors(chain = true)
public class StrategyConfig {

    /**
     * 使用哪个模板引擎来生成代码。
     */
    protected ITemplate templateEngine;
    /**
     * 数据库表前缀，多个前缀用英文逗号（,） 隔开。
     */
    private String tablePrefix;
    /**
     * 逻辑删除的默认字段名称。
     */
    private String logicDeleteColumn;
    /**
     * 乐观锁的字段名称。
     */
    private String versionColumn;
    /**
     * 是否生成视图映射。
     */
    private boolean generateForView;
    /**
     * 是否覆盖之前生成的文件。
     */
    private boolean overwriteEnable;
    /**
     * 单独为某张表添加独立的配置。
     */
    private Map<String, TableConfig> tableConfigMap;
    /**
     * 设置某个列的全局配置。
     */
    private Map<String, ColumnConfig> columnConfigMap;
    /**
     * 生成那些表，白名单。
     */
    private Set<String> generateTables;
    /**
     * 不生成那些表，黑名单。
     */
    private Set<String> unGenerateTables;

    public void addTableConfig(TableConfig tableConfig) {
        if (tableConfigMap == null) {
            tableConfigMap = new HashMap<>();
        }
        tableConfigMap.put(tableConfig.getTableName(), tableConfig);
    }

    public TableConfig getTableConfig(String tableName) {
        return tableConfigMap == null ? null : tableConfigMap.get(tableName);
    }

    public void addColumnConfig(ColumnConfig columnConfig) {
        if (columnConfigMap == null) {
            columnConfigMap = new HashMap<>();
        }
        columnConfigMap.put(columnConfig.getColumnName(), columnConfig);
    }

    public void addColumnConfig(String tableName, ColumnConfig columnConfig) {
        TableConfig tableConfig = getTableConfig(tableName);
        if (tableConfig == null) {
            tableConfig = new TableConfig();
            tableConfig.setTableName(tableName);
            addTableConfig(tableConfig);
        }

        tableConfig.addColumnConfig(columnConfig);
    }

    public ColumnConfig getColumnConfig(String tableName, String columnName) {
        ColumnConfig columnConfig = null;

        TableConfig tableConfig = getTableConfig(tableName);
        if (tableConfig != null) {
            columnConfig = tableConfig.getColumnConfig(columnName);
        }

        if (columnConfig == null && columnConfigMap != null) {
            columnConfig = columnConfigMap.get(columnName);
        }

        if (columnConfig == null) {
            columnConfig = new ColumnConfig();
        }

        //全局配置的逻辑删除
        if (columnName.equals(logicDeleteColumn) && columnConfig.getLogicDelete() == null) {
            columnConfig.setLogicDelete(true);
        }

        //全部配置的乐观锁版本
        if (columnName.equals(versionColumn) && columnConfig.getVersion() == null) {
            columnConfig.setVersion(true);
        }


        return columnConfig;
    }

    public void addGenerateTable(String... tables) {
        if (generateTables == null) {
            generateTables = new HashSet<>();
        }

        for (String table : tables) {
            if (table != null && table.trim().length() > 0) {
                generateTables.add(table.trim());
            }
        }
    }

    public void addUnGenerateTable(String... tables) {
        if (unGenerateTables == null) {
            unGenerateTables = new HashSet<>();
        }

        for (String table : tables) {
            if (table != null && table.trim().length() > 0) {
                unGenerateTables.add(table.trim());
            }
        }
    }

    public boolean isSupportGenerate(String table) {
        if (unGenerateTables != null && unGenerateTables.contains(table)) {
            return false;
        }

        //不配置指定比表名的情况下，支持所有表
        if (generateTables == null || generateTables.isEmpty()) {
            return true;
        }

        for (String generateTable : generateTables) {
            if (generateTable.equals(table)) {
                return true;
            }
        }

        return false;
    }

    public ITemplate getTemplateEngine() {
        if (templateEngine == null) {
            templateEngine = new EnjoyTemplate();
        }
        return templateEngine;
    }

}