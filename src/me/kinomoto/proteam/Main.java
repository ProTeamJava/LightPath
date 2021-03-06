package me.kinomoto.proteam;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import me.kinomoto.proteam.history.History;
import me.kinomoto.proteam.settings.SettingsPanel;
import me.kinomoto.proteam.settings.SurroundingsSettingsPanel;

/**
 * Main class including the window and all the panels creation
 *
 */
public class Main extends JFrame {
	private static final long serialVersionUID = 9128707449024404584L;
	private static final int MIN_WIDTH = 600;
	private static final int MIN_HEIGHT = 400;
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 600;

	private static final Point ROTATE_CURSOR_HOTSPOT = new Point(8, 8);

	private static final int SCROLL_MULTIPLIER = 5;

	private JMenuItem openA;
	private JMenuItem exportA;
	private JMenuItem saveAsA;
	private JMenuItem saveA;
	private JMenuItem exitA;
	private JMenuItem undoA;
	private JMenuItem redoA;
	private JMenuItem deleteA;
	private JMenuItem zoomInA;
	private JMenuItem zoomOutA;
	private JMenuItem aboutA;

	private static Cursor rotateCursor = null;

	private ImageIcon openI;
	private ImageIcon saveI;
	private ImageIcon saveAsI;
	private ImageIcon exportI;
	private ImageIcon exitI;
	private ImageIcon undoI;
	private ImageIcon redoI;
	private static ImageIcon deleteI;
	private ImageIcon zoomInI;
	private ImageIcon zoomOutI;
	private final ImageIcon appIcon = getIcon("LightPathIcon.png");

	private SurroundingsView surroundingsView;

	private JPanel toolBar;
	private SettingsPanel settingsPanel;

	private JScrollPane scroll;

	private double vSliderPos;
	private double hSliderPos;
	private JMenuBar menubar;

	/**
	 * @throws HeadlessException
	 */
	public Main() throws HeadlessException {
		super("LightPath");

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			// catch
		}

		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new BorderLayout());

		this.setIconImage(getAppIcon().getImage());

		initIcons();
		initMenu();
		initUI();
		initListeners();

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(getClass().getResource("/rotate.png"));
		rotateCursor = toolkit.createCustomCursor(image, ROTATE_CURSOR_HOTSPOT, "Rotate");

		setLocationRelativeTo(null);
		this.setVisible(true);

		initScrollListeners();

	}

	/**
	 * Method enabling to scroll the plane
	 */
	private void initScrollListeners() {
		Rectangle bounds = scroll.getViewport().getViewRect();
		Dimension size = scroll.getViewport().getViewSize();
		int x = (size.width - bounds.width) / 2;
		int y = (size.height - bounds.height) / 2;
		scroll.getViewport().setViewPosition(new java.awt.Point(x, y));

		scroll.removeMouseWheelListener(scroll.getMouseWheelListeners()[0]);
		scroll.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getModifiers() == InputEvent.CTRL_MASK) {
					saveSliderPositions();
					if (e.getUnitsToScroll() < 0)
						surroundingsView.scaleUp();
					else
						surroundingsView.scaleDown();
					restoreSliderPositions();
				} else if (e.getModifiers() == InputEvent.ALT_MASK) {
					scroll(e.getUnitsToScroll() * SCROLL_MULTIPLIER, 0);
				} else {
					scroll(0, e.getUnitsToScroll() * SCROLL_MULTIPLIER);
				}

			}
		});
	}

	/**
	 * Method implementing user interface panels
	 */
	private void initUI() {
		toolBar = new ToolBar(this);
		settingsPanel = new SettingsPanel();
		initSurroundings();

		this.add(toolBar, BorderLayout.WEST);
		this.add(settingsPanel, BorderLayout.EAST);
	}

	/**
	 * Method implementing SurroundingsView panel and its scrolling
	 */
	private void initSurroundings() {
		surroundingsView = new SurroundingsView(settingsPanel, this);
		settingsPanel.setPanel(new SurroundingsSettingsPanel(surroundingsView.surroundings));
		scroll = new JScrollPane(surroundingsView);
		this.add(scroll, BorderLayout.CENTER);
	}

	/**
	 * The creation of the Main from the saved file
	 * @param path input file path
	 */
	public void loadFromPath(String path) {
		History.clean();
		this.remove(scroll);

		try {
			surroundingsView = new SurroundingsView(settingsPanel, this, path);
		} catch (LoadException e) {
			initSurroundings();
			revalidate();
			repaint();
			initScrollListeners();
			JOptionPane.showMessageDialog(Main.this, e.getMessage());
			return;
		}

		settingsPanel.setPanel(new SurroundingsSettingsPanel(surroundingsView.surroundings));
		scroll = new JScrollPane(surroundingsView);
		this.add(scroll, BorderLayout.CENTER);
		revalidate();
		repaint();
		initScrollListeners();
	}

	/**
	 * Method initializing icons
	 */
	private void initIcons() {
		openI = getIcon("document-open.png");
		saveI = getIcon("document-save.png");
		saveAsI = getIcon("document-save-as.png");
		exportI = getIcon("document-export.png");
		exitI = getIcon("application-exit.png");
		undoI = getIcon("edit-undo.png");
		redoI = getIcon("edit-redo.png");
		deleteI = getIcon("edit-delete.png");
		zoomInI = getIcon("zoom-in.png");
		zoomOutI = getIcon("zoom-out.png");
	}

	/**
	 * Method implementing icons recognition
	 * @param name
	 * @return
	 */
	public ImageIcon getIcon(String name) {
		return new ImageIcon(getClass().getClassLoader().getResource(name));
	}

	/**
	 * The method initializing the FileMenu 
	 */
	private void initFileMenu() {
		JMenu fileM = new JMenu(Messages.get("file"));
		openA = new JMenuItem(Messages.get("open"), openI);
		openA.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(Messages.getChar("openM")));
		openA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		openA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				if (fc.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
					Main.this.loadFromPath(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});

		saveA = new JMenuItem(Messages.get("save"), saveI);
		saveA.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(Messages.getChar("exportM")));
		saveA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		saveA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		exportA = new JMenuItem(Messages.get("export"), exportI);
		exportA.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(Messages.getChar("exportM")));
		exportA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsPng();
			}
		});

		saveAsA = new JMenuItem(Messages.get("saveAs"), saveAsI);
		saveAsA.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(Messages.getChar("saveAsM")));
		saveAsA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.ALT_DOWN_MASK));
		saveAsA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAs();
			}
		});

		exitA = new JMenuItem(Messages.get("exit"), exitI);
		exitA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));

		fileM.add(openA);
		fileM.add(saveA);
		fileM.add(saveAsA);
		fileM.addSeparator();
		fileM.add(exportA);
		fileM.add(exitA);
		menubar.add(fileM);
	}

	/**
	 * The method initializing the EditMenu 
	 */
	private void initEditMenu() {
		JMenu editM = new JMenu(Messages.get("edit"));

		undoA = new JMenuItem(Messages.get("undo"), undoI);
		undoA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		undoA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				History.back();
			}
		});

		redoA = new JMenuItem(Messages.get("redo"), redoI);
		redoA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
		redoA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				History.foward();
			}
		});

		deleteA = new JMenuItem(Messages.get("delete"), deleteI);
		deleteA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		deleteA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				surroundingsView.surroundings.deleteSelected();
			}
		});

		editM.add(deleteA);
		editM.addSeparator();
		editM.add(undoA);
		editM.add(redoA);
		menubar.add(editM);

	}

	/**
	 * The method initializing the ViewMenu 
	 */
	private void initViewMenu() {
		JMenu viewM = new JMenu(Messages.get("view"));

		zoomInA = new JMenuItem(Messages.get("zoomIn"), zoomInI);
		zoomInA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));
		zoomInA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveSliderPositions();
				surroundingsView.scaleUp();
				restoreSliderPositions();
			}
		});

		zoomOutA = new JMenuItem(Messages.get("zoomOut"), zoomOutI);
		zoomOutA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
		zoomOutA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveSliderPositions();
				surroundingsView.scaleDown();
				restoreSliderPositions();
			}
		});

		viewM.add(zoomInA);
		viewM.add(zoomOutA);
		menubar.add(viewM);
	}

	/**
	 * The method initializing the HelpMenu
	 */
	private void initHelpMenu() {
		JMenu helpM = new JMenu(Messages.get("help"));
		aboutA = new JMenuItem(Messages.get("about"));

		helpM.add(aboutA);
		menubar.add(helpM);
	}

	/**
	 * The method initializing the {@link JMenuBar}
	 */
	private void initMenu() {
		menubar = new JMenuBar();

		initFileMenu();
		initEditMenu();
		initViewMenu();
		initHelpMenu();

		setJMenuBar(menubar);
	}

	/**
	 * The method initializing listeners that open new windows - exit and about
	 */
	private void initListeners() {
		exitA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (surroundingsView.surroundings.isModyfied() && JOptionPane.showConfirmDialog(Main.this, "Do you want to save file?", "Not saved modyfications!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					save();
				}
				Main.this.dispose();
			}
		});

		aboutA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutWindow about = new AboutWindow(Messages.get("aboutTitle"), Main.this);
				about.setVisible(true);
			}
		});
	}

	/**
	 * The main method creating the window
	 * @param args
	 */
	public static void main(String[] args) {
		Messages.setLocale(Locale.getDefault());
		@SuppressWarnings("unused")
		Main window = new Main();
	}

	/**
	 * The method saving sliders positions after scrolling
	 */
	private void saveSliderPositions() {
		Rectangle rect = scroll.getViewport().getViewRect();
		Dimension size = scroll.getViewport().getViewSize();
		vSliderPos = (rect.width / 2 + rect.x) / size.getWidth();
		hSliderPos = (rect.height / 2 + rect.y) / size.getHeight();
	}

	/**
	 * 	The method saving sliders positions after scrolling
	 */
	private void restoreSliderPositions() {
		Rectangle rect = scroll.getViewport().getViewRect();
		Dimension size = scroll.getViewport().getViewSize();
		int nx = (int) (vSliderPos * size.getWidth() - rect.width / 2);
		int ny = (int) (hSliderPos * size.getHeight() - rect.height / 2);
		scroll.getViewport().setViewPosition(new java.awt.Point(nx, ny));
	}

	/**
	 * The method placing the scrolls positions
	 * @param dx
	 * @param dy
	 */
	public void scroll(int dx, int dy) {
		java.awt.Point pos = scroll.getViewport().getViewPosition();
		pos.x += dx;
		pos.y += dy;
		if (pos.x < 0)
			pos.x = 0;
		if (pos.y < 0)
			pos.y = 0;
		scroll.getViewport().setViewPosition(pos);
	}

	/**
	 * The method enabling save a created PNG picture of the plane to the file.
	 */
	private void saveAsPng() {
		try {
			surroundingsView.saveAsPng();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(Main.this, e.getMessage());
		}
	}


	public static ImageIcon getDeleteI() {
		return deleteI;
	}

	public static Cursor getRotateCursor() {
		return rotateCursor;
	}

	public SurroundingsView getSurroundingsView() {
		return surroundingsView;
	}

	public ImageIcon getAppIcon() {
		return appIcon;
	}

	/**
	 * Saving to the chosen directory method
	 */
	private void saveAs() {
		JFileChooser fc = new JFileChooser();
		if (fc.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
			try {
				surroundingsView.saveAs(fc.getSelectedFile().getAbsolutePath());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(Main.this, e1.getMessage());
			}
		}
	}

	/**
	 * Saving method
	 */
	private void save() {
		if (surroundingsView.hasPath()) {
			try {
				surroundingsView.save();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(Main.this, e1.getMessage());
			}
		} else {
			saveAs();
		}

	}
}
