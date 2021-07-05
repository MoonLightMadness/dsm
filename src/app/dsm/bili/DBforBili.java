package app.dsm.bili;

import app.utils.SimpleUtils;
import app.utils.TimeFormatter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBforBili  {
    private String dbName="Entertainment";
    private Connection conn;
    public DBforBili() throws SQLException, ClassNotFoundException {
        linkToDb();
    }
    public void linkToDb() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        // Get Connection From Database
        conn = DriverManager.getConnection("jdbc:sqlite:Entertainment.db");
        // Not Allow Auto Commit,For efficient
        conn.setAutoCommit(false);

    }
    public String getDateTime() throws SQLException {
        String s="Select last From updateDate";
        Statement statement=conn.createStatement();
        ResultSet rs=statement.executeQuery(s);
        String goal=rs.getString("last");
        rs.close();
        statement.close();
        return goal;
    }
    public void updateDateTime(String date) throws SQLException {
        String s="Update updateDate Set last='"+date+"'";
        Statement statement=conn.createStatement();
        statement.executeUpdate(s);
        statement.close();
        conn.commit();
    }
    public void write(String url,String author,String point,String title,String tags,String date) throws SQLException {
        String s1="'"+url+"','"+title+"','"+author+"','"+point+"','"+tags+"','"+date+"'";
        String s="Insert into Info(Url,Title,Author,Point,Tags,UpdateDate) Values("+s1+")";
        Statement statement=conn.createStatement();
        statement.executeUpdate(s);
        statement.close();
        conn.commit();
    }
    public void writeWithoutTags(String url,String author,String point,String title,String date) throws SQLException {
        String s1="'"+url+"','"+title+"','"+author+"','"+point+"','"+date+"'";
        String s="Insert into Info(Url,Title,Author,Point,UpdateDate) Values("+s1+")";
        Statement statement=conn.createStatement();
        statement.executeUpdate(s);
        statement.close();
        conn.commit();
    }
    public void writeTags(String title,String tags,String updatedate) throws SQLException {
        String s="Update Info Set Tags='"+tags+"' Where Title='"+title+"' And UpdateDate='"+updatedate+"'";
        Statement statement=conn.createStatement();
        statement.executeUpdate(s);
        statement.close();
        conn.commit();
    }

    /**
     * 写入今日数据的标签
     *
     * @param tags 标签
     * @param url  url
     * @throws SQLException sqlexception异常
     */
    public void writeTodayTags(String url,String tags) throws SQLException {
        tags = tags.replaceAll("\'"," ");
        tags = tags.replaceAll("\""," ");
        String s="Update Info Set Tags='"+tags+"' Where Url='"+url+"' And updateDate='"+SimpleUtils.getTimeStamp2(TimeFormatter.DAY_LEVEL)+"'";
        Statement statement=conn.createStatement();
        statement.executeUpdate(s);
        statement.close();
        conn.commit();
    }

    public void turnToUndone() throws SQLException {
        String s="Update updateDate Set status='UNDONE'";
        Statement statement=conn.createStatement();
        statement.executeUpdate(s);
        statement.close();
        conn.commit();
    }
    public String getStatus() throws SQLException {
        String s="Select status From updateDate";
        Statement statement=conn.createStatement();
        ResultSet rs=statement.executeQuery(s);
        String goal=rs.getString("status");
        rs.close();
        statement.close();
        return goal;
    }
    public void setToDone() throws SQLException {
        String s="Update updateDate Set status='DONE'";
        Statement statement=conn.createStatement();
        statement.executeUpdate(s);
        statement.close();
        conn.commit();
    }
    public List<Extracted> read(Date date) throws SQLException {
        List<Extracted> list=new ArrayList<Extracted>();
        String s="Select * From Info Where UpdateDate='"+date+"'";
        Statement statement=conn.createStatement();
        ResultSet rs=statement.executeQuery(s);
        while (rs.next()){
            Extracted e=new Extracted(rs.getString(1),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(2),
                    rs.getString(5)
            );
            list.add(e);
        }
        rs.close();
        statement.close();
        return list;
        }
        public List<Extracted> read(String date) throws SQLException {
            List<Extracted> list=new ArrayList<Extracted>();
            String s="Select * From Info Where UpdateDate='"+date+"'";
            Statement statement=conn.createStatement();
            ResultSet rs=statement.executeQuery(s);
            while (rs.next()){
                Extracted e=new Extracted(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
            );
            list.add(e);
        }
        rs.close();
        statement.close();
        return list;
    }

    /**
     * 获取今天的数据的URL
     *
     * @return {@link List<String>}
     * @throws SQLException sqlexception异常
     */
    public List<String> readToay() throws SQLException {
        List<String> list = new ArrayList<>();
        String s = "Select * From Info Where UpdateDate='"+ SimpleUtils.getTimeStamp2(TimeFormatter.DAY_LEVEL) +"'";
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(s);
        while (rs.next()){
            String res = rs.getString(1);
            list.add(res);
        }
        return list;
    }
    public void close() throws SQLException {
        conn.close();
    }
}
