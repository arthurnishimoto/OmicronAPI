import omicron.OmicronAPI;

public class EventViewer {
	
	static OmicronAPI omicron;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		omicron = new OmicronAPI();
		omicron.ConnectToServer("127.0.0.1", 28000, 7124);
		
		while(true)
		{
			omicron.ListenForData();
		}
	}

}
