import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.sun.prism.paint.Stop;

public class WordFrequency {

	HashMap<String, String> tweetsMap = new HashMap<>();
	HashMap<String, String> tweetsLabel = new HashMap<>();

	WordsFrequencyMap positive = new WordsFrequencyMap();
	WordsFrequencyMap negative = new WordsFrequencyMap();
	WordsFrequencyMap neutral = new WordsFrequencyMap();
	
	int threshold = 230;

	public static void main(String[] args) {
		new WordFrequency().wrok();
	}

	void wrok() {
		loadFiles(Strings.train, tweetsMap);
		loadFiles(Strings.train_labels, tweetsLabel);
		for (String key : tweetsMap.keySet()) {
			handleThis(tweetsMap.get(key), tweetsLabel.get(key));
		}

		output();
	}

	private void output() {
		positive.setFeatures(threshold);
		negative.setFeatures(threshold);
		neutral.setFeatures(threshold);
		
		System.out.println("positive:");
		// myPrint(positive);
		featurePrint(positive, neutral, negative);
		System.out.println();
		System.out.println("negative:");
		featurePrint(negative, positive, neutral);
		// myPrint(negative);
		System.out.println();
		System.out.println("neutral:");
		featurePrint(neutral, positive, negative);
		// myPrint(neutral);

	}

	void featurePrint(WordsFrequencyMap map0, WordsFrequencyMap map1, WordsFrequencyMap map2) {
		map0.finalFeatures.removeAll(map1.standbyFeatures);
		map0.finalFeatures.removeAll(map2.standbyFeatures);
		
		File file = new File(Strings.featuresFile+threshold+".txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (String key : map0.finalFeatures) {
			System.out.print(key + " ");
			try {
				FileUtils.write(file, key+"\n", true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void myPrint(WordsFrequencyMap frequencyMap) {
		frequencyMap.convert();
		Set<Integer> set = frequencyMap.countsMap.keySet();

		Integer[] keys = set.toArray(new Integer[set.size()]);
		Arrays.sort(keys, Collections.reverseOrder());
		for (Integer key : keys) {

			if (key > 5) {
				System.out.print(key + " " + frequencyMap.countsMap.get(key) + "  *  ");
			}
		}

	}

	public void handleThis(String string, String string2) {

		List<String> listReturn = StopwordsTools.init().removeStopwords(string);
		switch (string2) {
		case "positive":
			setMapCount(listReturn, positive);
			break;
		case "negative":
			setMapCount(listReturn, negative);

			break;
		case "neutral":
			setMapCount(listReturn, neutral);

			break;
		}

	}

	private void setMapCount(List<String> listReturn, WordsFrequencyMap frequencyMap) {
		for (String string : listReturn) {
			int count = 0;

			if (frequencyMap.wordsMap.containsKey(string)) {
				count = frequencyMap.get(string);
				count++;
				frequencyMap.put(string, count);
			} else {
				count++;
				frequencyMap.put(string, count);
			}

		}
	}

	private void loadFiles(String file, HashMap<String, String> map) {
		try {
			List<String> datas = FileUtils.readLines(new File(file));
			for (String data : datas) {
				String key = data.substring(0, 18);
				String value = data.substring(19);
				map.put(key, value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
