package me.kinomoto.proteam.history;

import java.util.LinkedList;
import java.util.List;

/**
 * History class store the nodes of actions that may be repeated or deleted by using Back and Forward options from the menu.
 */
public class History {
	private static List<HistoryNodeAbstract> past = new LinkedList<>();
	private static List<HistoryNodeAbstract> future = new LinkedList<>();
	private static boolean stopHistory = false;

	private History() {
	}

	public static boolean isBackable() {
		return !past.isEmpty();
	}

	public static boolean isForwardable() {
		return !future.isEmpty();
	}

	public static void back() {
		if(stopHistory)
			return;
		for (;;) {
			if (!isBackable())
				return;
			HistoryNodeAbstract node = ((LinkedList<HistoryNodeAbstract>) past).getLast();
			if (node.isEmpty()) {
				past.remove(node);
			} else {
				node.undo();
				past.remove(node);
				future.add(node);
				return;
			}
		}
	}

	public static void foward() {
		if(stopHistory)
			return;
		if (!isForwardable())
			return;
		HistoryNodeAbstract node = ((LinkedList<HistoryNodeAbstract>) future).getLast();
		node.redo();
		future.remove(node);
		past.add(node);
	}

	public static void addNode(HistoryNodeAbstract node) {
		past.add(node);
		future.clear();
	}

	public static HistoryNodeAbstract getLastNode() {
		return ((LinkedList<HistoryNodeAbstract>) past).getLast();
	}
	
	public static void clean() {
		past.clear();
		future.clear();
	}
	
	public static void setStop(boolean stop) {
		stopHistory = stop;
	}
}
