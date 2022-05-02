package practice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

public class JsonParsingUtil {
	
	public static JSONObject jsonFileToJsonObject(String absoluteFilePath) throws IOException {
		
		File file = new File(absoluteFilePath);
		
		if(!file.exists()) {
			throw new FileNotFoundException("해당 파일을 찾을 수 없습니다.");
		}
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		StringBuffer bf = new StringBuffer();
		
		String line = "";
		
		while( (line = br.readLine()) != null) {
			bf.append(line);
		}
		
		String content = bf.toString();
		
		return new JSONObject(content);
	}

}
