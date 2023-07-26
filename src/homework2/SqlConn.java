package homework2;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class SqlConn {
    private final String  url;
    private Connection dbConn;
    public SqlConn(String u){
        this.url = u;
        try
        {
            dbConn = DriverManager.getConnection(url);
            //System.out.println("DB Connection [성공]");
        }
        catch (SQLException e)
        {
            System.out.println("DB Connection [실패]");
            e.printStackTrace();
        }
    }
    public void closeDbConn(){
        try
        {
            if(dbConn != null)
            {
                dbConn.close();
                dbConn = null;
                //System.out.println("DB Close [성공]");
            }
        }
        catch (SQLException e)
        {
            System.out.println("DB Close [실패]");
            e.printStackTrace();
        }
    }
    public List<Map<String, String>> selectAddress(String addr){
        PreparedStatement st;
        List<Map<String, String>> resultList = new ArrayList<>();
        try{
            st = dbConn.prepareStatement("select distinct 도로명주소.name AS 도로명, 도.name AS 도, " +
                    "시.name AS 시, 군.name AS 군, 구.name AS 구 from 도로명주소 " +
                    "LEFT JOIN 도 ON 도로명주소.do=도.'index' " +
                    "LEFT JOIN 군 ON 도로명주소.gun=군.'index' " +
                    "LEFT JOIN 구 ON 도로명주소.gu=구.'index' " +
                    "LEFT JOIN 시 ON 도로명주소.si=시.'index' where 도로명주소.name = ?");
            st.setString(1, addr);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Map<String, String> tempMap = new HashMap<>();
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++)
                    tempMap.put(rs.getMetaData().getColumnName(i + 1), rs.getString(rs.getMetaData().getColumnName(i + 1)));
                resultList.add(tempMap);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }
}
