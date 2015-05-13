import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Server {

	public static void main(String[] args)
	{
		try
		{
			Connection con = null;
			
			con = DriverManager.getConnection("jdbc:mysql://localhost", "root", "q1w2e3r4t5y6");
			java.sql.Statement st = null;
			ResultSet rs = null;
			st = con.createStatement();
			//rs = st.executeQuery("Show databases");
			
			if(st.execute("show databases"))
			{
				rs = st.getResultSet();
			}
			
			while(rs.next())
			{
				String str = rs.getNString(1);
				System.out.println(str);
			}
			
		}
		catch (SQLException sqex)
		{
			System.out.println("SQLException");
		}
	}
}
