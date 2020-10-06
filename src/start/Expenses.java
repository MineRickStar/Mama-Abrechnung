package start;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

public class Expenses extends JDialog {

	private static final long serialVersionUID = 6437401497062928854L;

	public static HashMap<Category, Double> getExpenses(JFrame parent) {
		return new Expenses(parent, new HashMap<Category, Double>()).expenses;
	}

	public static HashMap<Category, Double> getExpenses(JFrame parent, HashMap<Category, Double> startExpenses) {
		return new Expenses(parent, startExpenses).expenses;
	}

	private HashMap<Category, Double> expenses;

	JTable table = new JTable();

	private Expenses(JFrame parent, HashMap<Category, Double> expenses) {
		super(parent, "Ausgaben für Kassenzettel", true);
		this.setMinimumSize(new Dimension(200, 500));
		this.setResizable(false);
		this.expenses = expenses;
		this.table = new JTable();

		DefaultTableColumnModel tcm = new DefaultTableColumnModel() {

			private static final long serialVersionUID = -326482524999650583L;

			@Override
			public void moveColumn(int columnIndex, int newIndex) {
				// Columns must not be moved
				return;
			}
		};

		DefaultTableModel dtm = new DefaultTableModel(new String[] { "Ausgabe", "Wert" }, expenses.size()) {

			private static final long serialVersionUID = 2870381787414394153L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}

			@Override
			public Class<?> getColumnClass(int column) {
				return column == 1 ? Double.class : Category.class;
			}
		};

		this.table.setColumnModel(tcm);
		this.table.setModel(dtm);
		this.add(new JScrollPane(this.table));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
