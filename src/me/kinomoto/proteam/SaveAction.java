package me.kinomoto.proteam;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

public class SaveAction extends AbstractAction {
	private static final long serialVersionUID = -2516735275557305010L;
	Main main;

	public SaveAction(Main ref, JMenuItem name) {
		super();
		main = ref;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (main.path == null) {
			JFileChooser chooser = new JFileChooser();
			int ret = chooser.showSaveDialog(main);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				main.path = Paths.get(file.getAbsolutePath());
			} else {
				return;
			}
		}

		main.surroundingsView.save();
		main.modyfied = false;
	}

}
