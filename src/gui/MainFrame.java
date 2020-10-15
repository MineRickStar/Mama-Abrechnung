package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import start.Category;
import start.Start;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -8026416994513756565L;

	public final ExpensePanel expensePanel;

	public final NewExpensePanel newExpensePanel;

	public MainFrame(Start start) {
		super("Mamas Abrechnungsprogramm");
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.expensePanel = new ExpensePanel(start);
		this.newExpensePanel = new NewExpensePanel(start);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridheight = 2;
		this.add(this.expensePanel, gbc);

		gbc.gridx = 1;
		gbc.gridheight = 1;
		this.add(this.newExpensePanel, gbc);

		gbc.gridy = 1;
		this.add(new JPanel(), gbc);

		this.setJMenuBar(this.addMenu(start));

		this.pack();
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private JMenuBar addMenu(Start start) {

		JMenuItem newCategory = new JMenuItem("Neue Kategorie");
		newCategory.setMnemonic(KeyEvent.VK_N);
		newCategory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK));
		newCategory.addActionListener(e -> this.newExpensePanel.addCategory(Category.newCategory(MainFrame.this)));

		JMenuItem editCategories = new JMenuItem("Kategorien bearbeiten");
		editCategories.addActionListener(e -> {
			if (Category.allCategories().isEmpty()) {
				JOptionPane.showMessageDialog(MainFrame.this, "Es gibt keine Kategorien zum bearbeiten");
			} else {
				Category.editCategories(MainFrame.this);
			}
		});

		JMenu category = new JMenu("Kategorien");

		category.add(newCategory);
		category.add(editCategories);

		JMenuBar bar = new JMenuBar();

		bar.add(category);
		return bar;
	}

}
