import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class Server extends Thread {
	
	private ConnectDB Cdb;
	private Socket s;
	private int Roomidx; //방 번호(대기방-> 0)
	private String name; // id 이름
	private RoomManager Rmanager;
	
	
	Server(Socket s,RoomManager manager)
	{
		this.s = s;
		this.Roomidx = 0;
		this.name = "";
		Rmanager = manager;
		Cdb = new ConnectDB();
	}
	
	public Socket Getsocket()
	{
		return s;
	}
	
	public int Get_Current_Room() //방번호 얻음
	{
		return Roomidx;
	}
	
	public String Get_name() //이름 얻음
	{
		return name;
	}
	
	
	public void run()
	{
		try
		{
			System.out.println(this.s+"is connected");
			InputStream input = s.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			OutputStream output = s.getOutputStream();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
			
			String msg;
			
			while((msg = reader.readLine()) != null) // get client msg
			{
				int result = 1;
				System.out.println("Client: "+msg);
				String[] parse = msg.split("_");
				
				/* login_id_pw */
				if(parse[0].equals("login"))
				{
					result = Cdb.Qlogin(parse[1], Integer.parseInt(parse[2]));
					if(result == 0) // if fail
					{
						writer.println(result);
						writer.flush();
					}
					else // success
					{
						name = parse[1]; //이름 설정
						writer.println(result);
						writer.flush();
					}
				}
				
				/* register_id_pw */
				else if(parse[0].equals("register"))
				{
					result = Cdb.Register(parse[1],Integer.parseInt(parse[2]));
					writer.println(result);
					writer.flush();
				}
				
				/* create_name */
				else if(parse[0].equals(("create")))
				{
					Roomidx = Rmanager.CreateRoom(parse[1], this);
					if(Roomidx == -1) // 같은이름 방 존재 시 -1
					{
						writer.println(Roomidx);
						writer.flush();
						Roomidx = 0;
					}
					writer.println(Roomidx);  //생성 완료시 방번호
					writer.flush();
				}
				
				/* join_name */
				else if(parse[0].equals("join"))
				{
					Roomidx = Rmanager.EnterRoom(parse[1], this);
					if(Roomidx == -1) //방이 존재하지 않을 시
					{
						writer.println(Roomidx);
						writer.flush();
						Roomidx = 0;
					}
					else
					{
						writer.println(Roomidx); //입장 완료시 방번호
						writer.flush();
					}
				}
				
				/* out_name */
				else if(parse[0].equals("out"))
				{
					int tmp = Rmanager.OutRoom(parse[1], this);
					if(tmp == -1) //방 나가기 실패
					{
						writer.println("exit_error");
						writer.flush();
					}
					else
					{
						Roomidx=0;
						writer.println(Roomidx); //성공시 0 리턴
						writer.flush();
					}
				}
				
				/* RoomList */
				else if(parse[0].equals("RoomList"))
				{
					writer.println(Rmanager.Get_all_room());
					writer.flush();
				}
				
				/* chat_name_msg */
				else if(parse[0].equals("chat"))
				{
					Rmanager.ChatRoom(parse[1],"["+name+"]"+parse[2]);
					
				}
				/*debug 용도*/
				else if(parse[0].equals("debug"))
				{
					writer.println("[Room number]"+Roomidx +" [name]"+ name);
					writer.flush();
				}
				
				//writer.println(msg);
				//writer.flush();
			}
			
		}
		catch(IOException io)
		{
			System.out.println(io.getStackTrace());
		}
		
	}

}
