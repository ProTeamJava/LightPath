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

	public AboutWindow(String title) throws HeadlessException {
		super(title);
		this.setMinimumSize(new Dimension(400, 350));
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
		
		JLabel lblLightpath = new JLabel("LightPath");
		lblLightpath.setFont(new Font("Dialog", Font.BOLD, 22));
		getContentPane().add(lblLightpath, "1, 4, center, default");
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("LightPathIcon.png")));
		getContentPane().add(lblNewLabel, "1, 2, center, default");
		
		JLabel lblAuthors = new JLabel(Messages.get("authors"));
		lblAuthors.setFont(new Font("Dialog", Font.BOLD, 16));
		getContentPane().add(lblAuthors, "1, 8, center, default");
		
		JLabel lblNewLabel_1 = new JLabel(Messages.get("authorsNames"));
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		getContentPane().add(lblNewLabel_1, "1, 10, center, default");
		
		JLabel lblx = new JLabel("GNU GPLv2");
		lblx.setFont(new Font("Dialog", Font.PLAIN, 12));
		getContentPane().add(lblx, "1, 12, center, default");
		
		JButton btnZ = new JButton(Messages.get("close"), new ImageIcon(getClass().getClassLoader().getResource("close.png")));
		btnZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutWindow.this.setVisible(false);
			}
		});
		getContentPane().add(btnZ, "1, 16");
		setLocationRelativeTo(null);
	}

}
