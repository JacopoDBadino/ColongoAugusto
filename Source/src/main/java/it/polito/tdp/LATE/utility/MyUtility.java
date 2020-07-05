package it.polito.tdp.LATE.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyUtility {
	public static String readTextFile(String filename) throws IOException{
		String content = new String(Files.readAllBytes(Paths.get(filename)));
		return content;
	}
}
