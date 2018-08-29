package daily.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sentences {
	
	
	public static void main(String[] args) {
		
				
		try {
			Sentences.getSentences(MotivationalMemes.generateNatureNews());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<String> getSentences(String bodyText) {
		
		ArrayList<String> sentences = new ArrayList<String>();
		Pattern pat = Pattern.compile("<.*?>");
		Matcher matcher = pat.matcher(bodyText);
		
		bodyText = matcher.replaceAll("");
		
		Pattern pat1 = Pattern.compile(".*?\\.\\s");
		Matcher m = pat1.matcher(bodyText);
		
		
		while(m.find()) {
			sentences.add(m.group(0));
		}
		
		System.out.println(sentences.get(0));
		System.out.println(sentences.size());
		
		
		return sentences;
	}

}
