package me.kinomoto.proteam.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import me.kinomoto.proteam.Main;

public class SaveAsAction extends AbstractAction {
	private static final long serialVersionUID = 8927972004033724123L;
	Main main;

	public SaveAsAction(Main ref, JMenuItem name) {
		super();
		main = ref;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO saveAs
	}

}