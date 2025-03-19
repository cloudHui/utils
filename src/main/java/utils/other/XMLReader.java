package utils.other;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLReader {

	/**
	 * 获取标签
	 */
	public static void traverse(Node node, Set<String> properties) {
		if (node.getNodeName() != null) {
			properties.add(node.getNodeName());
		}
		// 遍历子节点
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			traverse(child, properties);
		}
	}

	public static void main(String[] args) {
		Map<String, Set<String>> directoryProperty = new HashMap<>();
		Set<String> directoryMap = new HashSet<>();
		Set<String> properties;
		//获取目录下五个子目录
		getFiveDirectory(directoryMap);

		for (String directory : directoryMap) {
			System.out.println("目录:" + directory);
			properties = directoryProperty.computeIfAbsent(directory, k -> new HashSet<>());
			//获取目录子文件
			getResourceXmlProperty(directory, properties);
			System.out.println("数量:" + properties.size());
			//System.out.println("参数:" + properties);
		}

		Map<String, Set<String>> directoryDirectory = new HashMap<>();
		for (Map.Entry<String, Set<String>> entry : directoryProperty.entrySet()) {
			for (Map.Entry<String, Set<String>> mapEntry : directoryProperty.entrySet()) {
				String key = entry.getKey() + " 对比" + mapEntry.getKey();
				for (String property : entry.getValue()) {
					if (!mapEntry.getValue().contains(property)) {
						Set<String> less = directoryDirectory.computeIfAbsent(key, k -> new HashSet<>());
						less.add(property);
					}
				}
			}
		}

		for (Map.Entry<String, Set<String>> entry : directoryDirectory.entrySet()) {
			System.out.println("目录: " + entry.getKey());
			System.out.println("差异数量: " + entry.getValue().size());
			//System.out.println("差异:" + entry.getValue());
		}
	}

	/**
	 * 获取目录下五个子目录
	 */
	private static void getFiveDirectory(Set<String> directoryMap) {
		String directoryPath = ConfigPathUtils.getResourceFilePath();
		File directory = new File(directoryPath);
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						directoryMap.add(file.getAbsolutePath());
					}
				}
			}
		}
	}

	/**
	 * 获取目录子文件
	 */
	private static void getResourceXmlProperty(String path, Set<String> properties) {
		try {
			Map<String, Integer> pathMap = new HashMap<>();
			DirectoryScanner.listFilesInDirectory(path, pathMap);
			for (Map.Entry<String, Integer> entry : pathMap.entrySet()) {
				String key = entry.getKey();
				if (key.contains(".xml")) {
					//System.out.println("目录: " + path + " 文件名: " + key);
					readFile(properties, key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读文件标签
	 */
	public static void readFile(Set<String> properties, String fileUrl) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File(fileUrl));

		// Optional, but recommended
		doc.getDocumentElement().normalize();
		// 开始遍历
		traverse(doc.getDocumentElement(), properties);
	}
}
