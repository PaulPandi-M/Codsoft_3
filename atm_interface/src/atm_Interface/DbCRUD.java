package atm_Interface;

import java.sql.*;

public class DbCRUD {
    Connection con;
    Statement st;
    DbCRUD()
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/atm";
            String username="root",password="";
            con=DriverManager.getConnection(url,username,password);
            st=con.createStatement();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    //CRUD
    public int insert(String name,long mobile,long accno,int pin,long amount)
    {
        try
        {
            String query="INSERT INTO userdata VALUES("+"'"+name+"',"+mobile+","+accno+","+pin+","+amount+")";
            return st.executeUpdate(query);
        }
        catch(Exception e)
        {
            return 0;
        }
    }
    public ResultSet select(long accno)
    {
        try
        {
            String query="SELECT * FROM userdata WHERE accno="+accno;
            return st.executeQuery(query);
        }
        catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    public int update(String query)
    {
        try
        {
            return st.executeUpdate(query);
        }
        catch(Exception e)
        {
            System.out.println(e);
            return 0;
        }
    }
    public int delete(String query)
    {
        try
        {
            return st.executeUpdate(query);
        }
        catch(Exception e)
        {
            System.out.println(e);
            return 0;
        }
    }
    public void dbClose()
    {
        try{
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}