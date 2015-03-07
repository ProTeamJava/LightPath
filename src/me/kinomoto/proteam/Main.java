package me.kinomoto.proteam;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class Main extends JFrame {
	private static final long serialVersionUID = 9128707449024404584L;

	public Main() throws HeadlessException {
		super("LightPath");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(600, 400));
	}
	
	public static void main(String[] args)
	{
		Main window = new Main();
		window.setVisible(true);
	}

}
