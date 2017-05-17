import java.util.HashMap;
import java.util.HashSet;

import com.sun.org.apache.regexp.internal.recompile;

public class WordsFrequencyMap {
	HashMap<String, Integer> wordsMap = new HashMap<>();
	HashMap<Integer, String> countsMap = new HashMap<>();
	HashSet<String> finalFeatures = new HashSet<>();
	HashSet<String> standbyFeatures = new HashSet<>();

	void put(String key, Integer value) {
		wordsMap.put(key, value);
	}

	Integer get(String key) {
		return wordsMap.get(key);
	}

	void convert() {
		for (String key : wordsMap.keySet())
			if (countsMap.containsKey(wordsMap.get(key))) {
				countsMap.put(wordsMap.get(key), countsMap.get(wordsMap.get(key))+" "+key);
			}else {
				countsMap.put(wordsMap.get(key), key);
			}
	}
	
	void setFeatures(int threshold){
		for(String word : wordsMap.keySet()){
			if (wordsMap.get(word)>=threshold) {
				finalFeatures.add(word);
				standbyFeatures.add(word);
			}
		}
	}
}
