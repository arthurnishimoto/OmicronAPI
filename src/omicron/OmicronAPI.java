package omicron;

import java.io.*;
import java.net.*;

public class OmicronAPI {
  
	Socket msgSocket;
	DatagramSocket dataSocket;
	PrintWriter outputWriter;
	
	byte[] receiveData = new byte[512];
	
	public OmicronAPI()
	{
		
	}
	
	public boolean ConnectToServer(String ipAddress, int msgPort, int dataPort)
	{
		try {
			// Create the TCP socket for message passing and reliable data
			msgSocket = new Socket(ipAddress, msgPort);
			
			// Handshake message to tell server what type of data
			// we want and on what UDP port to send the data on
			String handshapeMessage = "omicron_data_on," + dataPort;
			
			// Send the handshake message
			outputWriter = new PrintWriter(msgSocket.getOutputStream(), true);
			outputWriter.println(handshapeMessage);
			
			// Create the UDP socket for unreliable/fast data
			dataSocket = new DatagramSocket(dataPort);
			
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void ListenForData()
	{
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			dataSocket.receive(receivePacket);
			//String modifiedSentence = new String(receivePacket.getData());
			//System.out.println("FROM SERVER:" + modifiedSentence);
			
			byte[] eventBuffer = receivePacket.getData();
			EventData event = ByteArrayToEventData(eventBuffer);
			
			System.out.println(event.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static EventData ByteArrayToEventData(byte[] receiveBytes)
	{
		//MemoryStream ms = new MemoryStream();
		//ms.Write(receiveBytes, 0, receiveBytes.Length);

		BinaryReader reader = new BinaryReader(receiveBytes);
		EventData ed = new EventData();
		reader.position = 0;
		
		ed.timestamp = reader.ReadInt();
		ed.sourceId = reader.ReadInt();
		ed.serviceId = reader.ReadInt();
		ed.serviceType = reader.ReadInt();
		ed.type = reader.ReadInt();
		ed.flags = reader.ReadInt();
		
		ed.posx = reader.ReadFloat();
		ed.posy = reader.ReadFloat();
		ed.posz = reader.ReadFloat();
		ed.orw = reader.ReadFloat();
		ed.orx = reader.ReadFloat();
		ed.ory = reader.ReadFloat();
		ed.orz = reader.ReadFloat();
		
		ed.extraDataType = EventBase.ExtraDataType.values()[reader.ReadInt()];
		ed.extraDataItems = reader.ReadInt();
		ed.extraDataMask = reader.ReadInt();
		
		ed.extraData = reader.ReadBytes(EventData.ExtraDataSize);

		return ed;
	}
}
