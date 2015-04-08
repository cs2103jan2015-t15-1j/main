/**
 * KeywordInfoList.java
 * @author Hanbin
 * Created: 03/03/2015
 * Last Updated: 03/03/2015
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@author A0135295B
public class KeywordInfoList {

	private static final String EMPTY_STR = "";
	private static final String PARAMETER_DOES_NOT_EXIST = null;
	private static final int KEYWORD_DOES_NOT_EXIST = -1;
	private static final String SINGLE_SPACE_STR = " ";
	private static final String SINGLE_SPACE_REGEX = "\\s";
	private static final int SINGLE_SPACE_LENGTH = 1;
	private static final int FIRST_ARRAY_INDEX = 0;
	
	private ArrayList<KeywordInfo> kList;
	private String usercommand;
	
	public KeywordInfoList(String _usercommand, String[] keywords) {
		usercommand = _usercommand.trim();
		kList = new ArrayList<KeywordInfo>();
		updateKeywords(keywords);
		updateKeywordPositions(usercommand);
		updateKeywordParameters(usercommand);
	}
	
	public String getDescription() {
		int firstSpacePos = usercommand.indexOf(SINGLE_SPACE_STR);
		int firstKeywordPos = kList.get(FIRST_ARRAY_INDEX).getPosition();
		String descriptionChunk = EMPTY_STR;
		if (firstKeywordPos == KEYWORD_DOES_NOT_EXIST) {
			descriptionChunk = usercommand.substring(firstSpacePos);
		} else {
			descriptionChunk = usercommand.substring(firstSpacePos, firstKeywordPos);
		}
		return descriptionChunk.trim();
	}
	
	public String getParameter(String keywordRegex) {
		for (int i = 0; i < kList.size(); i++) {
			if (kList.get(i).getKeyword().equals(keywordRegex)) {
				return kList.get(i).getParameter();
			}
		}
		return PARAMETER_DOES_NOT_EXIST;
	}
	
	private void updateKeywords(String[] keywords) {
		for (int i = 0; i < keywords.length; i++) {
			KeywordInfo keywordInfo = new KeywordInfo(keywords[i]);
			kList.add(keywordInfo);
		}
	}
	
	private void updateKeywordPositions(String usercommand) {
		for (int i = 0; i < kList.size(); i++) {
			String keyword = kList.get(i).getKeyword();
			String keywordPadded = SINGLE_SPACE_REGEX + keyword + SINGLE_SPACE_REGEX;
			
			Pattern keywordPattern = Pattern.compile(keywordPadded);
			Matcher keywordMatcher = keywordPattern.matcher(usercommand);
			
			if (keywordMatcher.find()) {
				kList.get(i).setPosition(keywordMatcher.start() + SINGLE_SPACE_LENGTH);
			} else {
				kList.get(i).setPosition(KEYWORD_DOES_NOT_EXIST);
			}
		}
	}
	
	private void updateKeywordParameters(String usercommand) {
		assert !kList.isEmpty();
		Collections.sort(kList);
		
		if (kList.get(FIRST_ARRAY_INDEX).getPosition() != KEYWORD_DOES_NOT_EXIST) {
			
			for (int i = 0; i < kList.size(); i++) {
				
				int thisKeywordPosition = kList.get(i).getPosition();
				String parameterChunk = null;
				boolean isLastParameter = false;
				
				if (i < kList.size() - 1) {
					int nextKeywordPosition = kList.get(i + 1).getPosition();
					
					if (nextKeywordPosition != KEYWORD_DOES_NOT_EXIST) {
						parameterChunk = usercommand.substring(thisKeywordPosition, nextKeywordPosition);
						
					} else {
						isLastParameter = true;
						parameterChunk = usercommand.substring(thisKeywordPosition);
					}
					
				} else {
					assert i == kList.size() - 1;
					parameterChunk = usercommand.substring(thisKeywordPosition);
				}
				
				// Remove the keyword from parameter chunk.
				String parameter = removeFirstWord(parameterChunk);
				kList.get(i).setParameter(parameter);
				
				if (isLastParameter) {
					break;
				}
			}
		}
	}
	
	private String removeFirstWord(String str) {
		str = str.trim();
		int firstSpacePos = str.indexOf(SINGLE_SPACE_STR);
		if (0 < firstSpacePos) {
			str = str.substring(firstSpacePos);
			return str.trim();
		} else {
			return EMPTY_STR;
		}
	 }
}
