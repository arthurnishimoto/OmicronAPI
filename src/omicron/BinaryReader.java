package omicron;

import java.util.Arrays;

public class BinaryReader
{
	public int position = 0;
	byte[] binaryData;
	
	public BinaryReader(byte[] data)
	{
		binaryData = data;
	}
	
	public float ReadFloat()
	{
		return Float.intBitsToFloat(bytesToIntBits(binaryData, position));
	}
	
	public int ReadInt()
	{
		return bytesToIntBits(binaryData, position);
	}
	
	public byte[] ReadBytes(int size)
	{
		return Arrays.copyOfRange(binaryData, position, size);
	}
	
	private int bytesToIntBits(byte[] bytes, int offset)
    {
    	int intBits =  (bytes[offset] & 0xFF) 
                | ((bytes[offset+1] & 0xFF) << 8) 
                | ((bytes[offset+2] & 0xFF) << 16) 
                | ((bytes[offset+3] & 0xFF) << 24);
    	position += 4;
    	return intBits;
    }
}