import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.sun.prism.paint.Stop;

public class StopwordsTools {
	
	private static StopwordsTools tools;

	static String testString = "OK #fooFighters just may be my heros today! http://t.co/7LwIobYkPK Foo \\Fighters \"\"Rick Roll\"\" Westboro Baptist \"\"Church\"\" (quotes are mine)\"";

	String[] stopwords = { "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is",
			"it", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to",
			"was", "will", "with"};
	String[] prefixs = { "http", "#" ,"@","$"};
	String[] punctuations ={",","\"","<",">",".","/","?",":",";","[","]","{","}","-","_","+","=","|","\\",")","(","*","&","!","%","^","&","0","1","2","3","4","5","6","7","8","9"};

	public static void main(String[] args) {
		new StopwordsTools().removeStopwords(testString);
	}
	
	public static StopwordsTools init(){
		if (tools==null) {
			tools = new StopwordsTools();
		}
		return tools;
	}

	List<String> removeStopwords(String string) {
		String[] tokens = string.split(" ");
		List<String> list = new ArrayList<String>(Arrays.asList(tokens));
		Iterator<String> iterator = list.iterator();
		List <String> listReturn = new ArrayList<>();

		while (iterator.hasNext()) {
			String s = iterator.next();
			s=s.toLowerCase().trim();
			boolean tag = false;
			if (!tag) {
				for(String punctuation : punctuations){
					s=s.replace(punctuation, "");
				}
				if (s.length()==0) {
					iterator.remove();
					tag = true;
				}
			}
			
			for (String prefix : prefixs) {
				if (s.startsWith(prefix)) {
					iterator.remove();
					tag = true;
					break;
				}
			}
			
			if(!tag){
				for(String stopword : stopwords){
					if(s.equals(stopword)){
						iterator.remove();
						tag = true;
						break;
					}
				}
			}
			if (!tag) {
				listReturn.add(s);
			}
		}

//		for (String s : listReturn) {
//			System.out.println(s);
//		}

		return listReturn;
	}
}
