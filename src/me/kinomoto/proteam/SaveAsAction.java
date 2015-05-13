package me.kinomoto.proteam;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;


public class SaveAsAction extends AbstractAction {
	private static final long serialVersionUID = 8927972004033724123L;
	Main main;

	public SaveAsAction(Main ref, JMenuItem name) {
		super();
		main = ref;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

			main.surroundingsView.save();
			main.modyfied = false;
		

	}

}
