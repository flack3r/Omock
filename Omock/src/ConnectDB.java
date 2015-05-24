import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ConnectDB {

	private static final String rs = null;
	private Connection con;
	
	ConnectDB()
	{
		try
		{
			con = DriverManager.getConnection("jdbc:mysql://localhost","root","1234");
		}
		catch(SQLException sql)
		{
			System.out.println("SQLException: "+sql.getMessage());
			System.out.println("SQLState: "+sql.getSQLState());
		}
	}
	
	public int Qlogin(String id,int pw) // 로긴 성공시 return 1 ,실패시 0;
	{
		try 
		{
			ResultSet rs = null;
			
			String sql = "select id from omock.login where id=? and pw=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setInt(2, pw);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				return 1;
			}
			
			return 0;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public int Id_exist(String id) //return 1 when data is exist 
	{
		try
		{
			int flag =0;
			ResultSet rs = null;
			
			String sql = "select * from omock.login where id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				flag = 1;
				/*
				String str = rs.getString(1);
				System.out.println(str);
				*/
			}
			
			return flag;
		}
		catch(SQLException sql)
		{
			System.out.println("SQLException: "+sql.getMessage());
			System.out.println("SQLState: "+sql.getSQLState());
		}
		return 0;
	}
	
	public int Register(String id,int pw)
	{
		try
		{
			if(Id_exist(id) == 0)
			{
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO omock.login values(?,?)");
			
				pstmt.setString(1, id);
				pstmt.setInt(2, pw);
			
				pstmt.executeUpdate();
				System.out.println("register succes");
				return 0;
			}
			else
			{
				System.out.println("id is exist");
				return 1;
			}
			
		}
		catch(SQLException sql)
		{
			System.out.println("SQLException: "+sql.getMessage());
			System.out.println("SQLState: "+sql.getSQLState());
		}
		
		return 1;
	}
	
	public void Qrecord(String id)
	{
		try
		{
			ResultSet rs = null;
			
			String sql = "select win,lose from omock.record where id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				String level = rs.getString(1);
				String ex = rs.getString(2);
				System.out.println("win is "+level);
				System.out.println("lose is "+ex);
			}
	
		}
		catch(SQLException sql)
		{
			System.out.println("SQLException: "+sql.getMessage());
			System.out.println("SQLState: "+sql.getSQLState());
		}
	}
	
	public void Win(String id)
	{
		try
		{
			if(Id_exist(id) == 0)
			{
				System.out.println("id is not exist");
			}
			else
			{
				PreparedStatement pstmt = con.prepareStatement("UPDATE record SET win = win+1 where id=?");
				
				pstmt.setString(1, id);
				
				pstmt.executeUpdate();
				System.out.println("win up succes");
			}
			
			
		}
		catch(SQLException sql)
		{
			System.out.println("SQLException: "+sql.getMessage());
			System.out.println("SQLState: "+sql.getSQLState());
		}
	}
	
	public void Lose(String id)
	{
		try
		{
			if(Id_exist(id) == 0)
			{
				System.out.println("id is not exist");
			}
			else
			{
				PreparedStatement pstmt = con.prepareStatement("UPDATE record SET lose = lose+1 where id=?");
				
				pstmt.setString(1, id);
				
				pstmt.executeUpdate();
				System.out.println("lose up succes");
			}
			
			
		}
		catch(SQLException sql)
		{
			System.out.println("SQLException: "+sql.getMessage());
			System.out.println("SQLState: "+sql.getSQLState());
		}
	}
}
