package me.kinomoto.proteam.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import me.kinomoto.proteam.Main;

public class SaveAsPngAction extends AbstractAction {
	private static final long serialVersionUID = -2516735275557305010L;
	Main main;

	public SaveAsPngAction(Main ref, JMenuItem name) {
		super();
		main = ref;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		main.surroundingsView.saveAsPng();
	}

}
