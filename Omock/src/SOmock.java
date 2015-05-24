import java.net.ServerSocket;
import java.net.Socket;


public class SOmock {

	public static void main(String[] args)
	{
		try
		{
			ServerSocket serversock = new ServerSocket(9197);
			System.out.println(serversock+"socket create");
			
			RoomManager Rmanager = new RoomManager(); //쓰레드 관리자 생성
			
			while(true)
			{
				Socket client = serversock.accept();
				Server tserver = new Server(client,Rmanager);
				tserver.start();
				Rmanager.addUser(tserver); //대기방 인원 추가
			}
	
		}
		catch(Exception ex)
		{
			System.out.println("server create error");
		}
	}
	
	
}
