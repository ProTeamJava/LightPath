package me.kinomoto.proteam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

public class SurroundingsView extends JPanel {

	private static final long serialVersionUID = 5447523639086911950L;

	Surroundings surroundings = new Surroundings();

	public SurroundingsView() {

		this.setLayout(new FlowLayout());

		this.setPreferredSize(new Dimension(3000, 4000));

		surroundings.add(new Mirror(new Point(100, 100), "a"));
		surroundings.add(new BeamSource(new Segment(new Point(50, 100), new Point(100, 100)), 550));
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		// g.setColor(Color.getHSBColor(1000, 1000, 1000));
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		surroundings.paint((Graphics2D) g);

	}

}
