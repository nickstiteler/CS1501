import java.util.*;



public class Add128 implements SymCipher
{
	private byte[] key;

	//base constructor 
	public Add128()
	{
		Random rand = new Random();
		key = new byte[128];
		rand.nextBytes(key);
	}

	//constructor with byte 
	public Add128(byte[] b)
	{
		if(b.length != 128)
		{
			throw new IllegalArgumentException("Invalid Key");
		}

		key = new byte[128];
		this.key = b.clone();
	}

	// encodes a string 
	public byte[] encode(String S)
	{
		byte[] str = S.getBytes();
		for(int i = 0; i < str.length;i++)
		{
			str[i] = (byte) (str[i] + key[i%key.length]);
		}
		return str.clone();
	}

	// decodes and encrypted message in the form of bytes
	public String decode(byte[] bytes)
	{
		byte[] str = bytes.clone();

		for(int i = 0; i<str.length;i++)
		{
			str[i] = (byte) (str[i]- key[i%key.length]);
		}

		return new String(str);
	}

	//gets the key of an array of bytes
	public byte[] getKey()
	{
		return key.clone();
	}
}