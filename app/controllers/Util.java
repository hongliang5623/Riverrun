package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 * @category 工具类
 * 
 */
public class Util {
	/**
	 * URL参数List
	 */
	public static List<String> URLparameters = new ArrayList<String>();

	/**
	 * 添加新的功能列
	 * 
	 * @param list
	 * @param map
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, Object>> addRow(List<Map<String, Object>> list, Map<String, String> map) throws IOException {
		if (map != null && list != null) {
			Set<String> mapKey = map.keySet();
			for (int j = 0; j < list.size(); j++) {
				// Map<String, String> peopleOrThings = com.ict.util.FileStream.getTo();
				for (String str : mapKey) {
					String rowValue = map.get(str);
//					String people = FileStream.getPropertiesVal((String) list.get(j).get("type"));
//					// System.out.println("----------------------"+people);
//					String goTo = FileStream.getPropertiesVal(people);
//					if (goTo != null && !"".equals(goTo)) {
//						rowValue = rowValue.replace("{action}", goTo);
//					}
					analysisURL(rowValue);
					if (URLparameters != null) {
						for (int i = 0; i < URLparameters.size(); i++) {
							String up = URLparameters.get(i).trim();
							Object value = list.get(j).get(up);
							if (value != null) {
								if(value.toString().contains("'")){
									value=URLEncoder.encode(value.toString(), "UTF-8");
								}
								if (str.equals("checkbox")) {
									rowValue = rowValue.replace("{name}", list.get(j).get("name").toString());
								} else {
									rowValue = rowValue.replace("{" + up + "}", value.toString());
								}

							}
						}
						list.get(j).put(str, rowValue);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 添加新的功能列
	 * 
	 * @param list
	 * @param map
	 * @return
	 */
	public static List<Map<String, Object>> addRowList(List<Map<String, Object>> list, Map<String, String> map) {
		if (map != null && list != null) {
			Set<String> mapKey = map.keySet();
			for (int j = 0; j < list.size(); j++) {
				for (String str : mapKey) {
					String rowValue = map.get(str);
					analysisURL(rowValue);
					if (URLparameters != null) {
						for (int i = 0; i < URLparameters.size(); i++) {
							String up = URLparameters.get(i).trim();
							Object value = list.get(j).get(up);
							if (value != null) {
								rowValue = rowValue.replace("{" + up + "}", value.toString());
							}
						}
						list.get(j).put(str, rowValue);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 解析URL得到参数
	 * 
	 * @param str
	 * @return
	 */
	public static String analysisURL(String str) {
		int pos1 = 0;
		int pos2 = 0;
		pos2 = str.indexOf('{', pos2);
		if (pos2 == -1)
			return str;
		pos1 = str.indexOf('}', pos2);
		if (pos1 == -1)
			return str;
		if (pos2 < pos1) {
			String replace = str.substring(pos2, pos1 + 1);
			String url = replace.replaceAll("\\{|\\}", "");
			URLparameters.add(url);
			str = str.replace(replace, "");
			return analysisURL(str);
		} else
			return str;
	}

	/**
	 * 字符串生成List
	 * 
	 * @param str
	 * @return
	 */
	public static List<String> createList(String str) {
		if (str != null && !"".equals(str)) {
			List<String> list = new ArrayList();
			String[] strs = str.split(",");
			for (int i = 0; i < strs.length; i++) {
				list.add(strs[i]);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 数据库JDBC-LIST形式
	 * 
	 * @param addRowMap
	 * @param labelStr
	 * @param valueStr
	 * @param resultList
	 * @return
	 */
	public static Map<String, Object> listShowTbale(Map<String, String> addRowMap, String labelStr, String valueStr, List<Map<String, Object>> resultList) {
		if (resultList != null && resultList.size() > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			resultList = addRowList(resultList, addRowMap);
			List<String> value = createList(valueStr);
			List<String> label = createList(labelStr);
			map.put("label", label);
			map.put("value", value);
			map.put("addRowMap", addRowMap);
			map.put("nodeList", resultList);
			return map;
		} else {
			return null;
		}

	}

	/**
	 * JSON-MAP形式-搜索人物
	 * 
	 * @param addRowMap
	 * @param labelStr
	 * @param valueStr
	 * @param resultMap
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> mapShowTbale(Map<String, String> addRowMap, String labelStr, String valueStr, Map<String, Object> resultMap) throws IOException {
		if (resultMap != null) {
			List<String> value = createList(valueStr);
			List<String> label = createList(labelStr);
			List<Map<String, Object>> rsList = (List<Map<String, Object>>) resultMap.get("results");
			List<Map<String, Object>> rsListss = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < rsList.size(); i++) {
				List<Map<String, Object>> relsut = addRow((List<Map<String, Object>>) rsList.get(i).get("nodeList"), addRowMap);
				for (int j = 0; j < relsut.size(); j++) {
					String name = relsut.get(j).get("sname").toString();
					if (name.length() > 40) {
						relsut.get(j).put("sname", "<a title=\"" + name + "\">" + name.substring(0, 40) + "...</a>");
					} else {
						relsut.get(j).put("sname", "<a title=\"" + name + "\">" + name + "</a>");
					}
					if (relsut.get(j).get("additional") != null) {
						String additional = relsut.get(j).get("additional").toString();
						if (additional.length() > 40) {
							relsut.get(j).put("additional", "<a title=\"" + additional + "\">" + additional.substring(0, 40) + "...</a>");
						} else {
							relsut.get(j).put("additional", "<a title=\"" + additional + "\">" + additional + "</a>");
						}
					}
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("label", label);
				map.put("value", value);
				map.put("type", rsList.get(i).get("type"));
				map.put("type_cn", rsList.get(i).get("type_cn"));
				map.put("count", rsList.get(i).get("count"));
				map.put("addRowMap", addRowMap);
				map.put("nodeList", relsut);
				rsListss.add(map);
			}
			resultMap.put("results", rsListss);
			return resultMap;
		} else {
			return null;
		}

	}

	/**
	 * JSON-MAP形式-搜索人物
	 * 
	 * @param addRowMap
	 * @param labelStr
	 * @param valueStr
	 * @param resultMap
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> mapShowTbaleForEvent(Map<String, String> addRowMap, String labelStr, String valueStr, Map<String, Object> resultMap) throws IOException {
		if (resultMap != null) {
			List<String> value = createList(valueStr);
			List<String> label = createList(labelStr);
			List<Map<String, Object>> rsList = (List<Map<String, Object>>) resultMap.get("results");
			List<Map<String, Object>> rsListss = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < rsList.size(); i++) {
				List<Map<String, Object>> result = addRow((List<Map<String, Object>>) rsList.get(i).get("nodeList"), addRowMap);
				for (int j = 0; j < result.size(); j++) {
					String _name = result.get(j).get("name").toString();
					String _method = result.get(j).get("method").toString();
					String _channel = result.get(j).get("channel").toString();
					String insert_time = result.get(j).get("insert_time").toString();
					Date date = new Date(Long.valueOf(insert_time) * 1000);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dt = format.format(date);
					//将name中的引号用竖线(|)代替，防止前端因为引号问题造成方法调用失效
					String nameForMethod = _name.replaceAll("\"", "|");
					nameForMethod = nameForMethod.replaceAll("'", "|");
					if (_name.length() > 20) {
						result.get(j).put("name", "<div title=\"" + _name + "\" tagtitle='" + nameForMethod + "'>" + _name.substring(0, 20) + "...</div>");
					} else {
						result.get(j).put("name", "<div title=\"" + _name + "\" tagtitle='" + nameForMethod + "'>" + _name + "</div>");
					}
					result.get(j).put("method", "<div title=\"" + _method + "\">" + _method + "</div>");
					result.get(j).put("channel", "<div title=\"" + _channel + "\">" + _channel + "</div>");
					result.get(j).put("createTime", "<div title=\"" + dt + "\">" + dt + "</div>");
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("label", label);
				map.put("value", value);
				map.put("type", rsList.get(i).get("type"));
				map.put("count", rsList.get(i).get("count"));
				map.put("addRowMap", addRowMap);
				map.put("nodeList", result);
				rsListss.add(map);
			}
			resultMap.put("results", rsListss);
			return resultMap;
		} else {
			return null;
		}

	}
	
	/**
	 * JSON-MAP形式-搜索事件
	 * 
	 * @param addRowMap
	 * @param labelStr
	 * @param valueStr
	 * @param resultMap
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> eventMapShowTbale(Map<String, String> addRowMap, String labelStr, String valueStr, Map<String, Object> resultMap) throws IOException {
		if (resultMap != null) {
			List<String> value = createList(valueStr);
			List<String> label = createList(labelStr);
			List<Map<String, Object>> resultList = addRow((List<Map<String, Object>>) resultMap.get("nodeList"), addRowMap);
			for (int j = 0; j < resultList.size(); j++) {
				String ab = resultList.get(j).get("ab").toString();
				String title = resultList.get(j).get("title").toString();
				String tags = resultList.get(j).get("tags").toString();
				tags = tags.replaceAll("\"", "");
				tags = tags.replaceAll("\\[", "");
				tags = tags.replaceAll("\\]", "");

				if (ab.length() > 20) {
					resultList.get(j).put("ab", "<a title=\"" + ab + "\">" + ab.substring(0, 20) + "...</a>");
				} else {
					resultList.get(j).put("ab", "<a title=\"" + ab + "\">" + ab + "</a>");
				}
				if (title.length() > 20) {
					resultList.get(j).put("title", "<a title=\"" + title + "\">" + title.substring(0, 20) + "...</a>");
				} else {
					resultList.get(j).put("title", "<a title=\"" + title + "\">" + title + "</a>");
				}
				if (tags.length() > 20) {
					resultList.get(j).put("tags", "<a title=\"" + tags + "\">" + tags.substring(0, 20) + "...</a>");
				} else {
					resultList.get(j).put("tags", "<a title=\"" + tags + "\">" + tags + "</a>");
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("label", label);
			map.put("value", value);
			map.put("type", resultMap.get("type"));
			map.put("count", resultMap.get("count"));
			map.put("addRowMap", addRowMap);
			map.put("nodeList", resultList);
			return map;
		} else {
			return null;
		}
	}

	public static String readTxtFile(String filePath) {
		StringBuffer text = new StringBuffer();
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// System.out.println(lineTxt);
					text.append(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return text.toString();
	}

	/**
	 * 字符串截取
	 * 
	 * @param str
	 *            需要接取的字符串
	 * @param len
	 *            截取长度
	 * @return
	 */
	public static String substring(String str, int len) {
		if (str != null) {
			if (str.length() > len) { // 判断字符串常度
				str = str.substring(0, len) + "...";// 截取字符串 长度为len
			}
		}
		return str;
	}

}
