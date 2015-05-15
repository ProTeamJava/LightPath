package me.kinomoto.proteam.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import me.kinomoto.proteam.Main;

public class SaveAsPngAction extends AbstractAction {
	private static final long serialVersionUID = -2516735275557305010L;
	Main main;

	public SaveAsPngAction(Main ref) {
		super();
		main = ref;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		main.saveAsPng();
	}

}
