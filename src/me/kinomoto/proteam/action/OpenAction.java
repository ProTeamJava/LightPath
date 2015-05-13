package me.kinomoto.proteam.action;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import me.kinomoto.proteam.Main;

public class OpenAction extends AbstractAction {
	private static final long serialVersionUID = -2316184093044173420L;
	Main main;
	public BufferedImage img;

	public OpenAction(Main ref, JMenuItem name) {
		super();
		main = ref;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//TODO
	}

}
