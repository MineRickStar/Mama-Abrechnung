package start;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class Start extends JFrame {

	private static final long serialVersionUID = 811156888087713146L;

	public static final File location = new File(System.getProperty("user.dir"), "Mama Abrechnung");

	public static final File allCategoriesFile = new File(Start.location, "Kategorien");

	JList<Expense> list;

	DefaultListModel<Expense> listModel;

	public ArrayList<Expense> expenses;

	public Start() {
		super("Mamas Abrechnungsprogramm");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.expenses = new ArrayList<>();
		this.listModel = new DefaultListModel<>();
		this.list = new JList<>(this.listModel);
		Start.allCategoriesFile.mkdirs();
		this.read();
		this.setSize(400, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.list.setFixedCellHeight(20);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.list.setCellRenderer((thisList, value, index, isSelected, cellHasFocus) -> {
			JPanel panel = new JPanel(new GridLayout(1, 0, 10, 10));
			JLabel label = new JLabel("Datum: " + value.getDateTimeString());
			panel.add(label);
			JLabel label2 = new JLabel("Summe: " + new DecimalFormat("#.00").format(value.getValue()) + "€");
			panel.add(label2);
			JLabel label3 = new JLabel("Kategorie: " + value.getCategory());
			panel.add(label3);

			if (isSelected) {
				panel.setBackground(Color.GRAY);
			} else {
				panel.setBackground(Color.LIGHT_GRAY);
			}
			return panel;
		});

		this.addMenu();
		this.add(new JScrollPane(this.list));
		this.setVisible(true);
	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu expenseMenu = new JMenu("Ausgabe");

		JMenuItem newExpense = new JMenuItem("Neue Ausgabe");
		newExpense.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		newExpense.addActionListener(e -> {
			Expense expense = Expense.newExpense(this);
			if (expense != null) {
				this.expenses.add(expense);
				this.listModel.addElement(expense);
				this.revalidate();
			}
		});

		JMenuItem delete = new JMenuItem("Ausgabe Löschen");
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		delete.addActionListener(e -> {
			Expense r = this.list.getSelectedValue();
			if (r != null) {
				if (JOptionPane.showConfirmDialog(Start.this, "Datensatz wirklich löschen?", "Löschen?",
						JOptionPane.YES_NO_OPTION) == 0) {
					this.listModel.removeElement(r);
					this.expenses.remove(r);
					new File(Start.allCategoriesFile, r.getID() + ".exp").delete();
				}
			}
		});

		JMenu category = new JMenu("Kategorien");

		JMenuItem newCategory = new JMenuItem("Neue Kategorie");
		newCategory.addActionListener(e -> Category.newCategory(Start.this));

		JMenuItem editCategories = new JMenuItem("Kategorien bearbeiten");
		editCategories.addActionListener(e -> Category.editCategories(Start.this));

		expenseMenu.add(newExpense);
		expenseMenu.add(delete);

		category.add(newCategory);
		category.add(editCategories);

		menuBar.add(expenseMenu);
		menuBar.add(category);

		this.setJMenuBar(menuBar);
	}

	private void read() {
		Category.read();
		File[] files = Start.allCategoriesFile.listFiles();
		Arrays.asList(files).forEach(file -> {
			try {
				byte[] bytes = Files.readAllBytes(file.toPath());
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
				Expense e = (Expense) ois.readObject();
				this.expenses.add(e);
				this.listModel.addElement(e);
			} catch (FileNotFoundException e) {
				// thats ok
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {
		new Location("Nicht wichtig");
		new Start();
	}

}
