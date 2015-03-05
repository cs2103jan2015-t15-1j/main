/**
 * KeywordInfoList.java
 * @author Hanbin
 * Created: 03/03/2015
 * Last Updated: 03/03/2015
 */

import java.util.ArrayList;
import java.util.Collections;


public class KeywordInfoList {

	// MACROS
	private static final String EMPTY_STRING = "";
	private static final String PARAMETER_DOES_NOT_EXIST = null;
	private static final int KEYWORD_DOES_NOT_EXIST = -1;
	private static final String SINGLE_SPACE = " ";
	private static final int ARRAY_POSITION_FIRST = 0;
	
	// Fields
	private ArrayList<KeywordInfo> kList;
	private String usercommand;
	
	// Constructor
	public KeywordInfoList(String _usercommand, String[] keywords) {
		usercommand = _usercommand.trim();
		kList = new ArrayList<KeywordInfo>();
		updateKeywords(keywords);
		updateKeywordPositions(usercommand);
		updateKeywordParametersSorted(kList, usercommand);
	}
	
	// Public methods
	public String getDescription() {
		int firstSpacePos = usercommand.indexOf(SINGLE_SPACE);
		int firstKeywordPos = kList.get(ARRAY_POSITION_FIRST).getPosition();
		String descriptionRaw = EMPTY_STRING;
		if (firstKeywordPos == KEYWORD_DOES_NOT_EXIST) {
			descriptionRaw = usercommand.substring(firstSpacePos);
		} else {
			descriptionRaw = usercommand.substring(firstSpacePos, firstKeywordPos);
		}
		return descriptionRaw.trim();
	}
	
	public String getParameter(String keywordRegex) {
		for (int i = 0; i < kList.size(); i++) {
			if (kList.get(i).getKeyword().matches(keywordRegex)) {
				return kList.get(i).getParameter();
			}
		}
		return PARAMETER_DOES_NOT_EXIST;
	}
	
	// Private methods	
	private void updateKeywords(String[] keywords) {
		for (int i = 0; i < keywords.length; i++) {
			KeywordInfo keywordInfo = new KeywordInfo(keywords[i]);
			kList.add(keywordInfo);
		}
	}
	
	private void updateKeywordPositions(String usercommand) {
		for (int i = 0; i < kList.size(); i++) {
			String keyword = kList.get(i).getKeyword();
			String keywordPadded = SINGLE_SPACE + keyword + SINGLE_SPACE;
			if (usercommand.contains(keywordPadded)) {
				int fromIndex = usercommand.indexOf(keywordPadded);
				int keywordPosition = usercommand.indexOf(keyword, fromIndex);
				kList.get(i).setPosition(keywordPosition);
			} else {
				kList.get(i).setPosition(KEYWORD_DOES_NOT_EXIST);
			}
		}
	}
	
	private void updateKeywordParametersSorted(ArrayList<KeywordInfo> keywordInfoList, String usercommand) {
		Collections.sort(keywordInfoList);
		if (keywordInfoList.get(ARRAY_POSITION_FIRST).getPosition() != KEYWORD_DOES_NOT_EXIST) {
			for (int i = 0; i < keywordInfoList.size(); i++) {
				boolean lastParameter = false;
				String parameter = null;
				int thisKeywordPosition = keywordInfoList.get(i).getPosition();
				if (i < keywordInfoList.size() - 1) {
					int nextKeywordPosition = keywordInfoList.get(i + 1).getPosition();
					if (nextKeywordPosition != KEYWORD_DOES_NOT_EXIST) {
						parameter = usercommand.substring(thisKeywordPosition, nextKeywordPosition);
					} else {
						parameter = usercommand.substring(thisKeywordPosition);
						lastParameter = true;
					}
				} else {
					parameter = usercommand.substring(thisKeywordPosition);
				}
				parameter = removeFirstWord(parameter);
				keywordInfoList.get(i).setParameter(parameter);
				if (lastParameter) {
					break;
				}
			}
		}
	}
	
	private String removeFirstWord(String str) {
		str = str.trim();
		int firstSpacePos = str.indexOf(SINGLE_SPACE);
		if (0 < firstSpacePos) {
			str = str.substring(firstSpacePos);
			str = str.trim();
		} else {
			str = EMPTY_STRING;
		}
		return str;
	 }
	
	/*
	private void updateKeywordParametersUnsorted(ArrayList<KeywordInfo> keywordInfoList, String usercommand) {
		ArrayList<KeywordInfo> tempList = new ArrayList<KeywordInfo>(keywordInfoList);
		updateKeywordParametersSorted(tempList, usercommand);
		for (int i = 0; i < keywordInfoList.size(); i++) {
			String thisKeyword = keywordInfoList.get(i).getKeyword();
			for (int j = 0; j < tempList.size(); j++) {
				if (tempList.get(j).getKeyword().equals(thisKeyword)) {
					String parameter = tempList.get(j).getParameter();
					keywordInfoList.get(i).setParameter(parameter);
					break;
				}
			}
		}
	}
	*/
}
