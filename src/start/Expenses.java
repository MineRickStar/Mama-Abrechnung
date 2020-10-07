package start;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

public class Expenses extends JDialog {

	private static final long serialVersionUID = 6437401497062928854L;

	public static ArrayList<Expense> getExpenses(JFrame parent) {
		return new Expenses(parent, new ArrayList<Expense>()).expenses;
	}

	public static ArrayList<Expense> getExpenses(JFrame parent, ArrayList<Expense> startExpenses) {
		return new Expenses(parent, startExpenses).expenses;
	}

	private ArrayList<Expense> expenses;

	JTable table = new JTable();

	private Expenses(JFrame parent, ArrayList<Expense> expenses) {
		super(parent, "Ausgaben für Kassenzettel", true);
		this.setMinimumSize(new Dimension(200, 500));
		this.setResizable(false);
		this.setLayout(new GridBagLayout());
		this.expenses = expenses;
		this.table = new JTable();

		DefaultTableColumnModel tcm = new DefaultTableColumnModel() {

			private static final long serialVersionUID = -326482524999650583L;

			@Override
			public void moveColumn(int columnIndex, int newIndex) {
				return;
			}
		};

		DefaultTableModel dtm = new DefaultTableModel(new String[] { "Ausgabe", "Wert" }, expenses.size()) {

			private static final long serialVersionUID = 2870381787414394153L;

			@Override
			public Class<?> getColumnClass(int column) {
				return column == 1 ? Double.class : Category.class;
			}

			@Override
			public Object getValueAt(int row, int column) {
				Expense e = expenses.get(row);
				if (column == 1) {
					return e.getValue();
				}
				return e.getCategory();
			}
		};

		this.table.setColumnModel(tcm);
		this.table.setModel(dtm);

		JPanel buttonPanel = new JPanel();
		JButton newExpense = new JButton("Neue Ausgabe");
		newExpense.addActionListener(e -> {
			JDialog dialog = new JDialog(Expenses.this, "Neue Ausgabe", true);
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.gridy = 0;
			gbc.gridx++;
			panel.add(new JLabel("Kategorie:"), gbc);
			JComboBox<Category> categoriesComboBox = new JComboBox<>(Category.allCategories().toArray(new Category[0]));
			gbc.gridx++;
			panel.add(categoriesComboBox, gbc);
			gbc.gridx++;
			panel.add(new JLabel("Wert:"), gbc);
			JTextField value = new JTextField(10);
			value.addActionListener(e1 -> {
				try {
					if (this.table.getSelectedRow() == -1) {
						return;
					}
					// TODO get nicht
					expenses.get(this.table.getSelectedRow()).setValue(
							NumberFormat.getNumberInstance(Locale.GERMANY).parse(value.getText()).doubleValue());
				} catch (ParseException e2) {
				}
			});
			gbc.gridx++;
			panel.add(value, gbc);
			JPanel jButtonPanel = new JPanel();
			JButton newCategory = new JButton("Neue Kategorie");
			newCategory.addActionListener(e1 -> {
				new Thread(() -> {
					String newCategory1 = JOptionPane.showInputDialog("Neue Kategorie");
					if (newCategory1 == null) {
						return;
					}
					Category categorie = new Category(newCategory1);
					categoriesComboBox.addItem(categorie);
					dialog.pack();
				}).start();
			});
			jButtonPanel.add(newCategory);
			JButton ok = new JButton("OK");
			ok.addActionListener(e1 -> {
				if (categoriesComboBox.getSelectedIndex() == -1) {
					return;
				}
				double textFieldValue = 0;
				try {
					textFieldValue = NumberFormat.getNumberInstance(Locale.GERMANY).parse(value.getText())
							.doubleValue();

				} catch (ParseException ex) {
					// TODO maybe inform for Error
					return;
				}
				expenses.add(new Expense((Category) categoriesComboBox.getSelectedItem(), textFieldValue));
				Expenses.this.revalidate();
				dtm.addRow(new Object[] { dtm.getValueAt(this.table.getRowCount(), 0),
						dtm.getValueAt(this.table.getRowCount(), 1) });
				dialog.dispose();
			});
			jButtonPanel.add(ok);
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.gridwidth = 4;
			panel.add(jButtonPanel, gbc);
			dialog.add(panel);
			dialog.setResizable(false);
			dialog.pack();
			dialog.setLocationRelativeTo(Expenses.this);
			dialog.setVisible(true);
		});

		buttonPanel.add(newExpense);

		JButton remove = new JButton("Ausgabe entfernen");
		remove.addActionListener(e -> {
			if (this.table.getSelectedRow() == -1) {
				return;
			}
			expenses.remove(this.table.getSelectedRow());
			dtm.removeRow(this.table.getSelectedRow());
		});
		buttonPanel.add(remove);

		JButton ready = new JButton("Fertig");
		ready.addActionListener(e -> this.dispose());

		buttonPanel.add(ready);

		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		this.getRootPane().getActionMap().put("Enter", new AbstractAction() {
			static final long serialVersionUID = -1421073818940434794L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Expenses.this.dispose();
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy++;
		this.add(new JScrollPane(this.table), gbc);
		gbc.gridy++;
		this.add(buttonPanel, gbc);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
