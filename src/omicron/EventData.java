package omicron;

import java.nio.charset.StandardCharsets;

public class EventData extends EventBase
{
    public long timestamp;
    public long sourceId;
    public int serviceId;
    public int serviceType;
    public long type;
    public long flags;
    public float posx;
    public float posy;
    public float posz;
    public float orx;
    public float ory;
    public float orz;
    public float orw;

    public final static int ExtraDataSize = 1024;
    public EventBase.ExtraDataType extraDataType;
    public long extraDataItems;
    public long extraDataMask;
    public byte[] extraData = new byte[ExtraDataSize];
    
    private int bytesToIntBits(byte[] bytes, int offset)
    {
    	return (bytes[offset] & 0xFF) 
                | ((bytes[offset+1] & 0xFF) << 8) 
                | ((bytes[offset+2] & 0xFF) << 16) 
                | ((bytes[offset+3] & 0xFF) << 24);
    }
    public boolean getExtraDataVector3(int index, float[] data)
    {
        if (extraDataType != EventBase.ExtraDataType.ExtraDataVector3Array) return false;
        if (index >= extraDataItems) return false;

        int offset = index * 3 * 4;
        //data[0] = OFLOAT_PTR(extraData[offset]);
        //data[1] = OFLOAT_PTR(extraData[offset + 4]);
        //data[2] = OFLOAT_PTR(extraData[offset + 8]);

        //data[0] = BitConverter.ToSingle(extraData, offset);
        //data[1] = BitConverter.ToSingle(extraData, offset + 4);
        //data[2] = BitConverter.ToSingle(extraData, offset + 8);
        
        data[0] = Float.intBitsToFloat(bytesToIntBits(extraData, offset));
        data[1] = Float.intBitsToFloat(bytesToIntBits(extraData, offset + 4));
        data[2] = Float.intBitsToFloat(bytesToIntBits(extraData, offset + 8));
        return true;
    }
	
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public float getExtraDataFloat(int index)
    {
        if (extraDataType != EventBase.ExtraDataType.ExtraDataFloatArray) return 0;
        if (index >= extraDataItems) return 0;
        	
        return Float.intBitsToFloat(bytesToIntBits(extraData, index * 4));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public int getExtraDataInt(int index)
    {
        if (extraDataType != EventBase.ExtraDataType.ExtraDataIntArray) return 0;
        if (index >= extraDataItems) return 0;

        return bytesToIntBits(extraData, index * 4);
    }
	
	///////////////////////////////////////////////////////////////////////////////////////////////
    public String getExtraDataString()
    {
        //if (extraDataType != EventBase.ExtraDataType.ExtraDataString) return "";
		//extraData = Encoding.Convert(Encoding.GetEncoding("iso-8859-1"), Encoding.UTF8, extraData);
		//string dataString = Encoding.UTF8.GetString(extraData, 0, (int)extraDataItems);
		//return dataString;
    	String dataString = new String(extraData, StandardCharsets.UTF_8);
    	return dataString.substring(0, (int)extraDataItems);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public long getExtraDataSize()
    {
        if (extraDataType == ExtraDataType.ExtraDataNull)
            return 0;
        else if (extraDataType == ExtraDataType.ExtraDataFloatArray || extraDataType == ExtraDataType.ExtraDataIntArray)
            return extraDataItems * 4;
        else if (extraDataType == ExtraDataType.ExtraDataVector3Array)
            return extraDataItems * 4 * 3;
        else if (extraDataType == ExtraDataType.ExtraDataString)
            return extraDataItems;
        else
            return extraDataItems;
    }
    
	///////////////////////////////////////////////////////////////////////////////////////////////
	public String toString()
	{
		String info = "";
		
		info = "EVENT service ID ("+serviceId+") source ID("+sourceId+")\n";
		info += "\tpos("+posx+","+posy+","+posz+")\n";
		info += "\textraDataItems("+extraDataItems+")\n";
		info += "\textraDataMask("+extraDataMask+")\n";
		
		if( serviceType == EventBase.ServiceType.ServiceTypePointer )
		{
			info += "\tserviceType(POINTER)\n";
			switch((int)type)
			{
			case(EventBase.Type.Down): info += "\ttype(DOWN)\n"; break;
			case(EventBase.Type.Move): info += "\ttype(MOVE)\n"; break;
			case(EventBase.Type.Up): info += "\ttype(UP)\n"; break;
			}
			info += "\tsize("+getExtraDataFloat(0)+","+getExtraDataFloat(1)+")\n";
		}
		else if( serviceType == EventBase.ServiceType.ServiceTypeMocap )
		{
			info += "\tserviceType(MOCAP)\n";
			info += "\torientationWXYZ("+orw+","+orx+","+ory+","+orz+")\n";
		}
		else if( serviceType == EventBase.ServiceType.ServiceTypeSpeech )
		{
			info += "\tserviceType(SPEECH)\n";
			info += "\tspeech("+getExtraDataString()+")\n";
			info += "\tconfidence("+posx+")\n";
		}
		return info;
	}
};