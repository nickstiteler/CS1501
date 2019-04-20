import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.BigInteger;


public class SecureChatClient extends JFrame implements Runnable,ActionListener
{
	public static final int PORT = 8765;

	JTextArea outputArea;
	JLabel prompt;
	JTextField inputField;
	String userName, serverName;
	Socket connection;
	private byte[] encryptName;
	ObjectOutputStream writ;
	ObjectInputStream myRead;
	BigInteger E;
	BigInteger N;
	BigInteger key;
	BigInteger encryptedKey;
	SymCipher cipher;
	String cipherType;

	public SecureChatClient()
	{
		try
		{
            userName = JOptionPane.showInputDialog(this, "Enter your username: "); 
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: "); 
            InetAddress addr = InetAddress.getByName(serverName);
            connection = new Socket(addr, PORT);  

            writ = new ObjectOutputStream(connection.getOutputStream()); 
            writ.flush(); 

            myRead = new ObjectInputStream(connection.getInputStream()); 

            E = (BigInteger) myRead.readObject(); 
            N = (BigInteger) myRead.readObject();
            System.out.println("Key E: " + E + "\nKey N: " + N);

            cipherType = (String) myRead.readObject();  
            System.out.println("Encryption type: " + cipherType);

            if (cipherType.equalsIgnoreCase("sub")) 
            {
                cipher = new Substitute();
            } 
            else if (cipherType.equalsIgnoreCase("add")) 
            {
                cipher = new Add128();
            }

            key = new BigInteger(1, cipher.getKey());
            System.out.println("Symmetric Key: " + key);
            encryptedKey = key.modPow(E, N); 

            writ.writeObject(encryptedKey); 
            writ.flush(); 
            encryptName = cipher.encode(userName); 

            writ.writeObject(encryptName); 
            writ.flush(); 

            this.setTitle(userName);      

            Box b = Box.createHorizontalBox();  
            outputArea = new JTextArea(8, 30); 
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Group, " + userName + "\n");

            inputField = new JTextField("");  
            inputField.addActionListener(this);

            prompt = new JLabel("Type your messages below:");
            Container c = getContentPane();

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  
            outputThread.start();                    

            addWindowListener(
                    new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            try 
                            {
                                writ.writeObject(cipher.encode("CLIENT CLOSING"));
                                writ.flush();
                            } catch (IOException io) {
                                System.out.println("Problem closing client!");
                            }
                            System.exit(0);
                        }
                    }
            );

            setSize(500, 200);
            setVisible(true);
		}catch(Exception e)
		{
			System.out.println("Problem starting client!");
		}
	}
	public void run()
	{
		while(true)
		{
			try
			{
				byte[] crypt = (byte[]) myRead.readObject();
				String curr = cipher.decode(crypt);
				outputArea.append(curr + "\n");
				byte[] bytes = curr.getBytes();

				System.out.println("Recieved array of bytes: " + Arrays.toString(crypt));
                System.out.println("Decrypted array of bytes: " + Arrays.toString(bytes));
                System.out.println("Corresponding string: " + curr);
			} catch(Exception e)
			{
				System.out.println(e + ",closing client");
				break;
			}
		}
		System.exit(0);
	}
	public void actionPerformed(ActionEvent e)
	{
		String curr = e.getActionCommand();
		inputField.setText("");

		try
		{
			curr = userName + ":" + curr;
			byte[] byteMsg = cipher.encode(curr);
			writ.writeObject(byteMsg);
			writ.flush();

			byte[] bytes = curr.getBytes();
			System.out.println("Original String Message: " + curr);
			System.out.println("Array of bytes: " + Arrays.toString(bytes));
            System.out.println("Encrypted array of bytes: " + Arrays.toString(byteMsg));
		}catch(IOException io)
		{
			System.err.println("Error: Failed to send message to server");
		}
	}
	public static void main(String[] args)
	{
		SecureChatClient JR = new SecureChatClient();
		JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}