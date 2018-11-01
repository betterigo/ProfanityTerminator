package cloud.troila.profanity.dictionary.node;

import java.util.LinkedList;
import java.util.List;

/**
 * DFA算法，字段节点实体类
 * @author haodonglei
 *
 */
public class WordNode {

	private int value;
	
	private List<WordNode> subNodes;
	
	private boolean last;

	
	public WordNode() {
		super();
	}
	
	public WordNode(int value, boolean last) {
		super();
		this.value = value;
		this.last = last;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}
	
	private WordNode addSubNode(WordNode node) {
		if(subNodes == null) {
			subNodes = new LinkedList<>();
		}
		subNodes.add(node);
		return node;
	}
	
	public WordNode addSubNodeIfNoExist(int value,boolean isLast) {
		if(subNodes == null) {	
			return addSubNode(new WordNode(value,isLast));
		}else {
			for(WordNode sub : subNodes) {
				if(sub.value == value) {//已经存在该节点
					if(!sub.isLast() && isLast) {
						sub.setLast(isLast);
						return sub;
					}
				}
			}
			return addSubNode(new WordNode(value, isLast));
		}
	}
	
	public WordNode searchSubNode(int value) {//使用stream
		if(subNodes!=null) {			
			return subNodes.stream().filter(n->n.value == value).findFirst().orElse(null);
		}
		return null;
	}

	@Override
	public int hashCode() {
		return value;
	}
	
	
}
