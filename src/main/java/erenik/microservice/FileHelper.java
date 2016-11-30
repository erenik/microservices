package erenik.microservice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {
	static String jsonBase = "{\n}";
	static void WriteFileContents(String path, String contents)
	{
		try {
			FileOutputStream out = new FileOutputStream(path);
			byte[] bytes = contents.getBytes();
			out.write(bytes, 0, bytes.length);
			out.close();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static String GetFileContents(String path)
	{
		try {
			FileInputStream in = new FileInputStream(path);
			int maxLength = 10000;
			byte[] bytes = new byte[maxLength];
			int numBytesRead = in.read(bytes, 0, maxLength);
			in.close();
			char[] c = new char[numBytesRead];
			for (int i = 0; i < numBytesRead; ++i)
				c[i] = (char) bytes[i];
			String text = new String(c);
			return text;
		} catch (IOException e)
		{
			// Couldn't find it? Create it.
			System.out.println("Couldn't find contents, creating JSON");
			WriteFileContents(path, jsonBase);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "BAD";
	}

}
