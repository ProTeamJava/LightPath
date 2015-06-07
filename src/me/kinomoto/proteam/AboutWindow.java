package me.kinomoto.proteam;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AboutWindow extends JFrame {
	private static final long serialVersionUID = 8894819342901772083L;
	private static final String FONT_NAME = "Dialog";
	private static final int FONT_SIZE_TITLE = 22;
	private static final int FONT_SIZE_BIG = 16;
	private static final int FONT_SIZE_NORMAL = 12;

	private static final int MINIMUM_WIDTH = 400;
	private static final int MINIMUM_HEIGHT = 350;

	public AboutWindow(String title, Main m) throws HeadlessException {
		super(title);
		this.setIconImage(m.appIcon.getImage());
		this.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("min:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblLightpath = new JLabel(Messages.get("appName"));
		lblLightpath.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_TITLE));
		getContentPane().add(lblLightpath, "1, 4, center, default");
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("LightPathIcon.png")));
		getContentPane().add(iconLabel, "1, 2, center, default");
		
		JLabel lblAuthors = new JLabel(Messages.get("authors"));
		lblAuthors.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_BIG));
		getContentPane().add(lblAuthors, "1, 8, center, default");
		
		JLabel authorsNamesLabel = new JLabel(Messages.get("authorsNames"));
		authorsNamesLabel.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_NORMAL));
		getContentPane().add(authorsNamesLabel, "1, 10, center, default");
		
		JLabel lblx = new JLabel("GNU GPLv2");
		lblx.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_NORMAL));
		getContentPane().add(lblx, "1, 12, center, default");
		
		JButton btnZ = new JButton(Messages.get("close"), new ImageIcon(getClass().getClassLoader().getResource("close.png")));
		btnZ.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutWindow.this.setVisible(false);
			}
		});
		getContentPane().add(btnZ, "1, 16");
		setLocationRelativeTo(null);
	}

}
