package dsm.db.impl;

import dsm.db.SqlBuilder;
import dsm.utils.SimpleUtils;

/**
 * @ClassName : dsm.db.impl.SqlBuilderImpl
 * @Description :
 * @Date 2021-05-10 17:03:53
 * @Author 张怀栏
 */
public class SqlBuilderImpl implements SqlBuilder {

    private StringBuilder builder = new StringBuilder();

    private StringBuilder selectBuilder = new StringBuilder();

    private StringBuilder whereBuilder = new StringBuilder();

    private String tableName;

    private String operation;


    @Override
    public SqlBuilder setTable(String tablename) {
        this.tableName = tablename;
        return this;
    }

    @Override
    public SqlBuilder select(String selectedcolumn) {
        operation = "Select";
        selectBuilder.append(selectedcolumn).append(",");
        return this;
    }

    @Override
    public SqlBuilder insert(String column, Object value) {
        operation = "Insert";
        selectBuilder.append(column).append(",");
        if (value instanceof Integer) {
            whereBuilder.append(column).append("=").append(value);
        } else {
            whereBuilder.append(column).append("=").append('\'').append(value).append('\'');
        }
        whereBuilder.append(",");
        return this;
    }

    @Override
    public SqlBuilder update(String column, Object value) {
        operation = "Update";
        if (value instanceof Integer) {
            selectBuilder.append(column).append("=").append(value);
        } else {
            selectBuilder.append(column).append("=").append('\'').append(value).append('\'');
        }
        selectBuilder.append(',');
        return this;
    }

    @Override
    public SqlBuilder delete(String column, Object value) {
        operation = "Delete";
        if (value instanceof Integer) {
            selectBuilder.append(column).append("=").append(value);
        } else {
            selectBuilder.append(column).append("=").append('\'').append(value).append('\'');
        }
        selectBuilder.append(',');
        return this;
    }

    @Override
    public SqlBuilder where(String column, Object value) {
        if (value instanceof Integer) {
            whereBuilder.append(column).append("=").append(value);
        } else {
            whereBuilder.append(column).append("=").append('\'').append(value).append('\'');
        }
        whereBuilder.append(',');
        return this;
    }

    @Override
    public SqlBuilder reset() {
        builder = new StringBuilder();
        selectBuilder = new StringBuilder();
        whereBuilder = new StringBuilder();
        operation = null;
        tableName = null;
        return this;
    }

    @Override
    public SqlBuilder and() {
        CharSequence seq = whereBuilder.subSequence(0,whereBuilder.length()-1);
        whereBuilder.delete(0,whereBuilder.length());
        whereBuilder.append(seq);
        whereBuilder.append("and").append(" ");
        return this;
    }

    @Override
    public SqlBuilder or() {
        CharSequence seq = whereBuilder.subSequence(0,whereBuilder.length()-1);
        whereBuilder.delete(0,whereBuilder.length());
        whereBuilder.append(seq);
        whereBuilder.append("or").append(" ");
        return this;
    }

    @Override
    public String toString() {
        builder.append(operation).append(" ");
        if ("Delete".equals(operation)) {
            builder.append("From").append(" ");
            builder.append(tableName).append(" ");
            builder.append("Where").append(" ");
            builder.append(whereBuilder.subSequence(0, whereBuilder.length() - 1)).append(" ");
        } else if("Select".equals(operation)){
            builder.append(selectBuilder.subSequence(0, selectBuilder.length() - 1)).append(" ");
            builder.append("From").append(" ").append(tableName).append(" ");
            builder.append("Where").append(" ");
            builder.append(whereBuilder.subSequence(0, whereBuilder.length() - 1)).append(" ");
        }else if("Update".equals(operation)){
            builder.append(tableName).append(" ");
            builder.append("Set").append(" ");
            builder.append(selectBuilder.subSequence(0, selectBuilder.length() - 1)).append(" ");
            builder.append("Where").append(" ");
            builder.append(whereBuilder.subSequence(0, whereBuilder.length() - 1)).append(" ");
        }else if("Insert".equals(operation)){
            builder.append("into").append(" ");
            builder.append(tableName);
            builder.append("(").append(selectBuilder.subSequence(0,selectBuilder.length()-1)).append(")").append(" ");
            builder.append("Values(").append(whereBuilder.subSequence(0,whereBuilder.length()-1)).append(")");
        }
        return builder.toString();
    }
}
