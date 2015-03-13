package me.kinomoto.proteam;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AboutWindow extends JFrame {
	private static final long serialVersionUID = 8894819342901772083L;

	public AboutWindow(String title) throws HeadlessException {
		super(title);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setMinimumSize(new Dimension(400, 200));
		this.add(new JLabel("LightPath"));
		this.add(new JLabel("Authors: Monika Goszcz, Oskar Świtalski"));
	}

}
