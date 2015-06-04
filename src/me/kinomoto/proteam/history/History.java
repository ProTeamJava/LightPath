package me.kinomoto.proteam.history;

import java.util.LinkedList;
import java.util.List;

public class History {
	private static List<HistoryNodeAbstract> past = new LinkedList<>();
	private static List<HistoryNodeAbstract> future = new LinkedList<>();

	private History() {
	}

	public static boolean isBackable() {
		return !past.isEmpty();
	}

	public static boolean isForwardable() {
		return !future.isEmpty();
	}

	public static void back() {
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
}
