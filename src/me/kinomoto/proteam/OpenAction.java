package me.kinomoto.proteam;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

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
		JFileChooser chooser = new JFileChooser();

		int ret = chooser.showOpenDialog(main);

		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			main.path = Paths.get(file.getAbsolutePath());
			// byte[] encoded = Files.readAllBytes(main.path);
			// main.text.setText(new String(encoded, "UTF-8"));
			img = null;
			try {
				img = ImageIO.read(file);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ImageIO.
			// main.surroundingsView.add
			main.modyfied = false;
		}
	}

}
