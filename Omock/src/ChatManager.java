import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


public class ChatManager {

	private ArrayList<Server> user;
	private ArrayList<BufferedReader> reader;
	private ArrayList<PrintWriter> writer;
	private Room room;
	
	
	ChatManager(Room room)
	{
		this.room = room;
		user = this.room.get_connect_user();
		reader = new ArrayList<BufferedReader>();
		writer = new ArrayList<PrintWriter>();
		
		int i =0;
		for(Server tmp_user: user)
		{
			try 
			{
				reader.add(new BufferedReader(new InputStreamReader(tmp_user.Getsocket().getInputStream())));
				writer.add(new PrintWriter(new OutputStreamWriter(tmp_user.Getsocket().getOutputStream())));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void SendAll(String input)
	{
		for(PrintWriter tmp_writer : writer)
		{
			tmp_writer.println(input);
			tmp_writer.flush();
		}
		
	}
		
}
