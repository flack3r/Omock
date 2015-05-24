import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


public class RoomManager {

	private ArrayList <Server> user; //총 유저의 쓰레드
	private ArrayList <Room> room;
	private ArrayList <String> room_name; //방 이름(최대 20개)
	private int personidx; //접속한 총 유저 수
	
	RoomManager()
	{
		user = new ArrayList<Server>();
		room = new ArrayList<Room>();
		room_name = new ArrayList<String>();
		personidx = 0;
	}
	
	public String Get_all_room()
	{
		return room_name.toString();
	}
	
	public int get_all_user()
	{
		return personidx;
	}
	
	public int get_roomidx()
	{
		return room.size();
	}
	
	public synchronized void addUser(Server user) //대기방에 들어온 유저 추가
	{
		this.user.add(user);
		personidx++;
	}
	
	/* 같은방 존재시 -1 , 방만들기 성공시 방번호 리턴 */
	public synchronized int CreateRoom(String name, Server user) 
	{
		//같은방 있는지 찾음
		for(String tmp: room_name)
		{
			if(name.equals(tmp))
			{
				return -1;
			}
		}
		
		//방 생성
		Room tmp = new Room();
		tmp.SetRoom(name, get_roomidx()+1, user);
		
		room_name.add(name);
		room.add(tmp);
		
		return get_roomidx();
	}
	
	/* 들어갈 방이 없다면 -1, 방에 들어 갔다면 들어간 room idx번호 리턴*/
	public synchronized int EnterRoom(String name, Server user)
	{
		//들어갈 방 있는지 체크
		for(Room tmp: room)
		{
			if(name.equals(tmp.get_name()))
			{
				tmp.SetEnterRoom(user);
				return tmp.get_Room_num();
			}
		}
		return -1;
	}
	
	/* 방 나가기 성공시 0, 실패시 -1 리턴 */
	public synchronized int OutRoom(String name, Server user)
	{
		for(Room tmp : room)
		{
			if(tmp.get_name().equals(name)) // 나갈 방 찾음
			{
				if(tmp.Is_connect(user)) // 방에 들어와 있는 유저인지 확인
				{
					tmp.OutRoom(user);
					
					if(tmp.get_psersonidx() == 0) // 방에 아무도 없을 때 방 삭제
					{
						room.remove(tmp);
						room_name.remove(name);
					}
					return 0;
				}
			}
		}
		return -1;
	}
	
	public void ChatRoom(String name,String msg)
	{
		for(Room tmp: room)
		{
			if(tmp.get_name().equals(name)) //방 찾기
			{
				ChatManager chat = new ChatManager(tmp);
				chat.SendAll(msg);
			}
		}
	}

}

class Room
{
	private String Rname; //방 제목
	private int Ridx; //방 번호
	private int personidx;  //방에 접속한 사람 수
	private ArrayList <Server> connect_user;
	
	Room()
	{
		personidx = 0;
		connect_user = new ArrayList <Server>();
	}
	
	public boolean Is_connect(Server user)
	{
		return connect_user.contains(user);
	}
	
	public int get_Room_num()
	{
		return Ridx;
	}
	
	public String get_name()
	{
		return Rname;
	}
	
	public int get_psersonidx()
	{
		return personidx;
	}
	
	public ArrayList get_connect_user()
	{
		return connect_user;
	}
	
	public void SetRoom(String name,int idx,Server user) //방 생성시
	{
		Rname = name;
		Ridx = idx;
		connect_user.add(user);
		personidx++;
	}

	public void SetEnterRoom(Server user) //방 접속시
	{
		personidx++;
		connect_user.add(user);
	}
	
	public void OutRoom(Server user)
	{
		personidx--;
		connect_user.remove(user);
	}

}