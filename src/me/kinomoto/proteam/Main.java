package me.kinomoto.proteam;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import me.kinomoto.proteam.action.OpenAction;
import me.kinomoto.proteam.action.SaveAsAction;
import me.kinomoto.proteam.action.SaveAsPngAction;
import me.kinomoto.proteam.history.History;
import me.kinomoto.proteam.settings.SettingsPanel;
import me.kinomoto.proteam.settings.SurroundingsSettingsPanel;

public class Main extends JFrame {
	private static final long serialVersionUID = 9128707449024404584L;

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

	OpenAction open;
	SaveAsPngAction savePng;
	SaveAsAction saveas;

	private SurroundingsView surroundingsView;

	private JPanel toolBar;
	private SettingsPanel settingsPanel;

	private JScrollPane scroll;

	private double vSliderPos;
	private double hSliderPos;

	public Main() throws HeadlessException {
		super("LightPath");

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			// catch
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
		settingsPanel = new SettingsPanel();
		surroundingsView = new SurroundingsView(settingsPanel, this);
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

		settingsPanel.setPanel(new SurroundingsSettingsPanel(surroundingsView.surroundings));
	}

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

	public ImageIcon getIcon(String name) {
		return new ImageIcon(getClass().getClassLoader().getResource(name));
	}

	private void initMenu() {
		JMenuBar menubar = new JMenuBar();

		JMenu fileM = new JMenu(Messages.get("file"));
		openA = new JMenuItem(Messages.get("open"), openI);
		openA.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(Messages.getChar("openM")));
		openA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

		saveA = new JMenuItem(Messages.get("save"), saveI);
		saveA.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(Messages.getChar("exportM")));
		saveA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

		exportA = new JMenuItem(Messages.get("export"), exportI);
		exportA.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(Messages.getChar("exportM")));

		saveAsA = new JMenuItem(Messages.get("saveAs"), saveAsI);
		saveAsA.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(Messages.getChar("saveAsM")));
		saveAsA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.ALT_DOWN_MASK));

		exitA = new JMenuItem(Messages.get("exit"), exitI);
		exitA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));

		open = new OpenAction(this);
		savePng = new SaveAsPngAction(this);
		saveas = new SaveAsAction(this);

		openA.addActionListener(open);
		exportA.addActionListener(savePng);
		saveAsA.addActionListener(saveas);

		fileM.add(openA);
		fileM.add(saveA);
		fileM.add(saveAsA);
		fileM.addSeparator();
		fileM.add(exportA);
		fileM.add(exitA);
		menubar.add(fileM);

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
				if (surroundingsView.surroundings.isModyfied() && 
					JOptionPane.showConfirmDialog(Main.this, "Do you want to save file?", "Not saved modyfications!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						// TODO :)
					
				}
				Main.this.dispose();
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
		@SuppressWarnings("unused")
		Main window = new Main();
	}

	private void saveSliderPositions() {
		Rectangle rect = scroll.getViewport().getViewRect();
		Dimension size = scroll.getViewport().getViewSize();
		vSliderPos = (rect.width / 2 + rect.x) / size.getWidth();
		hSliderPos = (rect.height / 2 + rect.y) / size.getHeight();
	}

	private void restoreSliderPositions() {
		Rectangle rect = scroll.getViewport().getViewRect();
		Dimension size = scroll.getViewport().getViewSize();
		int nx = (int) (vSliderPos * size.getWidth() - rect.width / 2);
		int ny = (int) (hSliderPos * size.getHeight() - rect.height / 2);
		scroll.getViewport().setViewPosition(new java.awt.Point(nx, ny));
	}

	public void saveAsPng() {
		surroundingsView.saveAsPng();
	}


	public static ImageIcon getDeleteI() {
		return deleteI;
	}
}
