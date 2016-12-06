package erenik.microservice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FileHelper class for writing and reading contents to a file on given path. Used only for local tests.
 * @author Emil
 */
public class FileHelper {
	static String jsonBase = "{\n}";
	/**
	 * Writes file contents to file at given path.
	 * @param path
	 * @param contents Contents to write to file.
	 */
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
	/**
	 * Reads contents of file at given path.
	 * @param path
	 * @return Returns the contents of the file at given path or null if it fails.
	 */
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
			System.out.println("Couldn't find file at path");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
