package me.kinomoto.proteam;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Locale;
import java.util.TimerTask;
import java.util.Timer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("unused")
public class Main extends JFrame {
	private static final long serialVersionUID = 9128707449024404584L;
	
	private JMenuItem exitA;
	private JMenuItem undoA;
	private JMenuItem redoA;
	
	private ImageIcon exitI;

	public Main() throws HeadlessException {
		super("LightPath");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(600, 400));

		ImageIcon appIcon = new ImageIcon(getClass().getClassLoader().getResource("LightPathIcon.png"));
		this.setIconImage(appIcon.getImage());
		
		exitI = new ImageIcon(getClass().getClassLoader().getResource("application-exit.png"));
		
		initMenu();
		initListeners();
	}

	private void initMenu() {
		JMenuBar menubar = new JMenuBar();

		JMenu fileM = new JMenu(Messages.getString("file"));
		exitA = new JMenuItem(Messages.getString("exit"), exitI);
		
		fileM.add(exitA);
		menubar.add(fileM);

		JMenu editM = new JMenu(Messages.getString("edit"));
		undoA = new JMenuItem(Messages.getString("undo"));
		
		editM.add(undoA);
		menubar.add(editM);

		setJMenuBar(menubar);
	}
	
	private void initListeners()
	{
		exitA.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}
		});
	}

	public static void main(String[] args)
	{
		Messages.setLocale(Locale.getDefault());
		Main window = new Main();
		window.setVisible(true);
	}

}
