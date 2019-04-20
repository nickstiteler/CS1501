import java.io.*;


// TO COMPRESS: java LZWmod - < input > output 
// TO EXPAND: java LZWmod + < input > output
// NOTE: Does not support choosing to reset or run without resetting
// this implementation will automatically increase codeword width as needed

public class LZWmod
{
	private static final int R = 256;        // number of input chars
    private static final int L = 65536;       // number of codewords = 2^W
    private static final int W = 12;         // codeword width

    private static int W2 = 9;
    private static int L2 = 512;

    public static void compress() 
    { 
    	StringBuilder input;
    	StringBuilder app = new StringBuilder();
    	TrieST<Integer> st = new TrieST<Integer>();
        
        for (int i = 0; i < R; i++)
        {
            st.put(new StringBuilder().append((char) i),i);
        }
        int code = R+1;  // R is codeword for EOF

        input = tryByte();

        while (input != null)
        {
        	StringBuilder strb = st.longestPrefixOf(input);

        	while(strb.length() == input.length())
        	{
        		app = tryByte();
        		input.append(app);

        		strb = st.longestPrefixOf(input);
        	}

        	BinaryStdOut.write(st.get(strb),W2);

        	if(app != null && code < L2)
        	{
        		st.put(input,code++);
        		if(code == L2)
        		{
        			if(W2 < 16)
        			{
        				L2 = (int) Math.pow(2,++W2);
        			}
        		}
        	}
        	input = app;

        }

        BinaryStdOut.write(R, W2);
        BinaryStdOut.close();
    } 


    public static void expand() {
        String[] st = new String[L];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int code = BinaryStdIn.readInt(W2);
        String value = st[code];

        while (true) 
        {
            BinaryStdOut.write(value);
            code = BinaryStdIn.readInt(W2);
           	
           	if(code == R)
           	{
           		break;
           	}

           	String str = st[code];

           	if(i == code)
           	{
           		str = value + value.charAt(0);
           	}
           	if(i+1<L)
           	{
           		st[i++] = value + str.charAt(0);
           	}
           	if(i+1 == L2 && W2 < 16)
           	{
           		L2 = (int) Math.pow(2,++W2);
           	}

           	if(i==L)
           	{
           		BinaryStdOut.write(value);
           		code = BinaryStdIn.readInt(W2);

           		while(code != R)
           		{
           			BinaryStdOut.write(st[code]);
           			code = BinaryStdIn.readInt(W2);
           		}
           		break;

           	}
           	value = str;
        }
        BinaryStdOut.close();
    }

    //wraps BinaryStdIn and returns the next char
    private static StringBuilder tryByte()
    {
    	char c;
    	StringBuilder strb = new StringBuilder();

    	try
    	{
    		c = BinaryStdIn.readChar();

    		strb.append((char)c);
    	}
    	catch(Exception e)
    	{
    		strb = null;
    	}
    	return strb;
    }



    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }
}