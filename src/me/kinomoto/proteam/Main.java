package me.kinomoto.proteam;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("unused")
public class Main extends JFrame {
	private static final long serialVersionUID = 9128707449024404584L;

	private JMenuItem openA;
	private JMenuItem saveA;
	private JMenuItem saveAsA;
	private JMenuItem exitA;
	private JMenuItem undoA;
	private JMenuItem redoA;
	private JMenuItem zoomInA;
	private JMenuItem zoomOutA;
	private JMenuItem aboutA;

	private ImageIcon openI;
	private ImageIcon saveI;
	private ImageIcon saveAsI;
	private ImageIcon exitI;
	private ImageIcon undoI;
	private ImageIcon redoI;
	private ImageIcon zoomInI;
	private ImageIcon zoomOutI;
	private ImageIcon aboutI;

	private SurroundingsView surroundingsView;

	private JPanel toolBar;
	private JPanel settingsPanel;

	private JScrollPane scroll;

	public Main() throws HeadlessException {
		super("LightPath");

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(600, 400));
		this.setSize(1200, 600);
		this.setLayout(new BorderLayout());

		ImageIcon appIcon = new ImageIcon(getClass().getClassLoader().getResource("LightPathIcon.png"));
		this.setIconImage(appIcon.getImage());

		initIcons();
		initMenu();
		initUI();
		initListeners();

		setLocationRelativeTo(null);
		this.setVisible(true);
		
		Rectangle bounds = scroll.getViewport().getViewRect();
		Dimension size = scroll.getViewport().getViewSize();
		int x = (size.width - bounds.width) / 2;
		int y = (size.height - bounds.height) / 2;
		scroll.getViewport().setViewPosition(new java.awt.Point(x, y));
	}

	private void initUI() {

		toolBar = new ToolBar(this);
		surroundingsView = new SurroundingsView();
		settingsPanel = new SettingsPanel();
		scroll = new JScrollPane(surroundingsView);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		
		this.add(scroll, BorderLayout.CENTER);
		this.add(toolBar, BorderLayout.WEST);
		this.add(settingsPanel, BorderLayout.EAST);
		
		scroll.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO scrollPane, resizing and sliders
				
			}
		});
	}

	private void initIcons() {
		openI = getIcon("document-open.png");
		saveI = getIcon("document-save.png");
		saveAsI = getIcon("document-save-as.png");
		exitI = getIcon("application-exit.png");
		undoI = getIcon("edit-undo.png");
		redoI = getIcon("edit-redo.png");
		zoomInI = getIcon("zoom-in.png");
		zoomOutI = getIcon("zoom-out.png");
	}

	public ImageIcon getIcon(String name) {
		return new ImageIcon(getClass().getClassLoader().getResource(name));
	}

	private void initMenu() {
		JMenuBar menubar = new JMenuBar();

		JMenu fileM = new JMenu(Messages.get("file"));
		openA = new JMenuItem(Messages.get("open"), openI);
		saveA = new JMenuItem(Messages.get("save"), saveI);
		saveAsA = new JMenuItem(Messages.get("saveAs"), saveAsI);
		exitA = new JMenuItem(Messages.get("exit"), exitI);

		fileM.add(openA);
		fileM.add(saveA);
		fileM.add(saveAsA);
		fileM.add(exitA);
		menubar.add(fileM);

		JMenu editM = new JMenu(Messages.get("edit"));
		undoA = new JMenuItem(Messages.get("undo"), undoI);
		redoA = new JMenuItem(Messages.get("redo"), redoI);

		editM.add(undoA);
		editM.add(redoA);
		menubar.add(editM);

		JMenu viewM = new JMenu(Messages.get("view"));
		zoomInA = new JMenuItem(Messages.get("zoomIn"), zoomInI);
		zoomOutA = new JMenuItem(Messages.get("zoomOut"), zoomOutI);

		viewM.add(zoomInA);
		viewM.add(zoomOutA);
		menubar.add(viewM);

		JMenu helpM = new JMenu(Messages.get("help"));
		aboutA = new JMenuItem(Messages.get("about"));

		helpM.add(aboutA);
		menubar.add(helpM);

		setJMenuBar(menubar);
	}

	private void initListeners() {
		exitA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		aboutA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutWindow about = new AboutWindow(Messages.get("aboutTitle"));
				about.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		Messages.setLocale(Locale.getDefault());
		Main window = new Main();
	}

}
