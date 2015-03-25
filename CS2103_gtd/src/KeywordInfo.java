/**
 * KeywordInfo.java
 * @author Hanbin
 * Created: 03/03/2015
 * Last Updated: 03/03/2015
 */

public class KeywordInfo implements Comparable<KeywordInfo> {
	
	// MACROS
	private static final int KEYWORD_DOES_NOT_EXIST = -1;
	private static final String PARAMETER_DOES_NOT_EXIST = null;
	private static final int THIS_BEFORE_OTHER = -1;
	private static final int THIS_EQUAL_OTHER = 0;
	private static final int THIS_AFTER_OTHER = 1;
	
	// Fields
	private String keyword;
	private int position;
	private String parameter;

	// Constructors
	public KeywordInfo() {
		keyword = null;
		position = KEYWORD_DOES_NOT_EXIST;
		parameter = PARAMETER_DOES_NOT_EXIST;
	}

	public KeywordInfo(String _keyword) {
		keyword = _keyword;
		position = KEYWORD_DOES_NOT_EXIST;
		parameter = PARAMETER_DOES_NOT_EXIST;
	}

	// Setters and getters
	public void setKeyword(String _keyword) {
		if (keyword == null) {
			keyword = _keyword;
		} else {
			keyword = _keyword;
			position = KEYWORD_DOES_NOT_EXIST;
			parameter = PARAMETER_DOES_NOT_EXIST;
		}
	}

	public String getKeyword() {
		return keyword;
	}

	public void setPosition(int index) {
		position = index;
		parameter = PARAMETER_DOES_NOT_EXIST;
	}

	public int getPosition() {
		return position;
	}

	public void setParameter(String _parameter) {
		parameter = _parameter;
	}

	public String getParameter() {
		return parameter;
	}

	@Override
	public int compareTo(KeywordInfo otherKeywordInfo) {
		if (this.getPosition() == KEYWORD_DOES_NOT_EXIST) {
			return THIS_AFTER_OTHER;
		}
		if (this.getPosition() < otherKeywordInfo.getPosition() ||
				otherKeywordInfo.getPosition() == KEYWORD_DOES_NOT_EXIST) {
			return THIS_BEFORE_OTHER;
		} else if (this.getPosition() == otherKeywordInfo.getPosition()) {
			return THIS_EQUAL_OTHER;
		} else {
			return THIS_AFTER_OTHER;
		}
	}
}