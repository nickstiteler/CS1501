import java.util.*;

public class Substitute implements SymCipher
{
	private byte[] key;
	private byte[] unKey;

	//empty constructor
	public Substitute()
	{
		ArrayList<Byte> lByte = new ArrayList<Byte>();

		key = new byte[256];
		unKey = new byte[256];

		for(int i=0;i<256;i++)
		{
			lByte.add((byte)i);
		}

		Collections.shuffle(lByte);

		for(int i=0;i<256;i++)
		{
			key[i] = lByte.get(i);
			unKey[key[i] & 0xFF] = (byte) i; 
		}
	}

	// constructor for byte array 
	public Substitute(byte[] b)
	{
		if(b.length != 256)
		{
			throw new IllegalArgumentException("Illegal key");
		}

		this.key = b.clone();
		unKey = new byte[256];

		for(int i=0;i<256;i++)
		{
			unKey[b[i] & 0xFF] = (byte) i;
		}
	}

	// returns a cop of the key 
	public byte[] getKey()
	{
		return key.clone();
	}

	// encoder 
	public byte[] encode(String S)
	{
		byte[] bStr = S.getBytes();
		byte[] cStr = new byte[S.length()];

		for(int i=0;i<bStr.length;i++)
		{
			cStr[i] = key[bStr[i] & 0xFF];
		}

		return cStr.clone();
	}

	//decoder
	public String decode(byte[] bytes)
	{
		byte[] unByte = new byte[bytes.length];

		for(int i=0;i<bytes.length;i++)
		{
			unByte[i] = (unKey[bytes[i] & 0xFF]);
		}
		return new String(unByte);
	}
}