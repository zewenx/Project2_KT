import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.pmml.jaxbbindings.InstanceField;

public class GenerateARFF {
	HashMap<String, String> tweetsMap = new HashMap<>();
	HashMap<String, String> tweetsLabel = new HashMap<>();

	WordsFrequencyMap positive = new WordsFrequencyMap();
	WordsFrequencyMap negative = new WordsFrequencyMap();
	WordsFrequencyMap neutral = new WordsFrequencyMap();
	
	String threshold = "manually";

	public static void main(String[] args) {
//		new GenerateARFF().wrok("train");
//		new GenerateARFF().wrok("dev");
		new GenerateARFF().wrok("test");
	}

	void wrok(String type) {
		String filePath ="";
		switch (type) {
		case "train":
			loadFiles(Strings.train, tweetsMap);
			loadFiles(Strings.train_labels, tweetsLabel);
			filePath = Strings.train_arff+"_"+threshold+Strings.arff;
			break;
		case "dev":
			loadFiles(Strings.dev, tweetsMap);
			loadFiles(Strings.dev_labels, tweetsLabel);
			filePath = Strings.dev_arff+"_"+threshold+Strings.arff;
			break;
		case "test":
			loadFiles(Strings.dev, tweetsMap);
			filePath = Strings.test_arff+"_"+threshold+Strings.arff;
		default:
			break;
		}
		
		
		ArrayList<Attribute> attributes = new ArrayList<>();
		
		loadFeatures(attributes);
		
		
		Instances instances = new Instances("features", attributes, 30000);
		init(instances,attributes);
		saveARFF(instances,filePath);
	}
	
	private void saveARFF(Instances instances,String path) {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(instances);
		
		File arffFile = new File(path);
		if (arffFile.exists()) {
			arffFile.delete();
		}
		try {
			arffFile.createNewFile();
			saver.setFile(arffFile);
			saver.writeBatch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init(Instances instances, ArrayList<Attribute> attributes) {
		for(String tweetID : tweetsMap.keySet()){
			Instance instance = new DenseInstance(attributes.size());
			instance.setValue(attributes.get(0), Long.parseLong(tweetID));
			for(int i = 1; i<attributes.size()-1;i++){
				instance.setValue(attributes.get(i), count(tweetsMap.get(tweetID), attributes.get(i).name()));
			}
			if (tweetsLabel.get(tweetID)!= null) {
				instance.setValue(attributes.get(attributes.size() - 1), tweetsLabel.get(tweetID));
			}
			instances.add(instance);
		}
		
	}
	
	Integer count(String source, String target){
		return StringUtils.countMatches(source.toLowerCase(), target.toLowerCase());
	}
	

	private void loadFeatures(ArrayList<Attribute> attributes) {
		Attribute attributeFist = new Attribute("id", false);
		attributes.add(attributeFist);
		try {
			List<String> features = FileUtils.readLines(new File(Strings.featuresFile+threshold+".txt"));
			for (String string : features) {
				Attribute attribute = new Attribute(string, false);
				attributes.add(attribute);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FastVector fvClassVal = new FastVector(3);
		 fvClassVal.addElement("positive");
		 fvClassVal.addElement("negative");
		 fvClassVal.addElement("neutral");
		 Attribute ClassAttribute = new Attribute("sentiment", fvClassVal);
		
		attributes.add(ClassAttribute);
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
