package cloud.troila.profanity.dictionary;

public class RegxEntry {
	private String regx;
	
	private int flag;
	
	private String replaceWord;

	public RegxEntry(String regx, String replaceWord,int flag) {
		super();
		this.regx = regx;
		this.replaceWord = replaceWord;
		this.flag = flag;
	}

	public RegxEntry(String regx, String replaceWord) {
		super();
		this.regx = regx;
		this.replaceWord = replaceWord;
	}

	public RegxEntry() {
		super();
	}

	public String getRegx() {
		return regx;
	}

	public void setRegx(String regx) {
		this.regx = regx;
	}

	public String getReplaceWord() {
		return replaceWord;
	}

	public void setReplaceWord(String replaceWord) {
		this.replaceWord = replaceWord;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
