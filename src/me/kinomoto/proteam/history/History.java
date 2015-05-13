package me.kinomoto.proteam.history;

import java.util.LinkedList;

public class History {
	static private LinkedList<HistoryNodeAbstract> past = new LinkedList<>();
	static private LinkedList<HistoryNodeAbstract> future = new LinkedList<>();
	
	static public boolean isBackable() {
		return past.size() > 0;
	}
	
	static public boolean isForwardable() {
		return future.size() > 0;
	}
	
	static public void back() {
		if(!isBackable()) return;
		HistoryNodeAbstract node = past.getLast();
		node.undo();
		past.remove(node);
		future.add(node);
	}
	
	static public void foward() {
		if(!isForwardable()) return;
		HistoryNodeAbstract node = future.getLast();
		node.redo();
		future.remove(node);
		past.add(node);
	}
	
	static public void addNode(HistoryNodeAbstract node) {
		past.add(node);
		future.clear();
	}
}
