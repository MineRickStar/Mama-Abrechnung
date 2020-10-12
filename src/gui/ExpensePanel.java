package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import start.Expense;
import start.Start;

public class ExpensePanel extends JPanel {

	private static final long serialVersionUID = 7401431401070613206L;

	JList<Expense> list;

	public DefaultListModel<Expense> listModel;

	public ExpensePanel(Start start) {
		super(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		this.add(new JLabel("Ausgaben:"), gbc);
		this.listModel = new DefaultListModel<>();
		this.list = new JList<>(this.listModel);

		this.list.setFixedCellHeight(20);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setFixedCellWidth(400);

		this.list.setCellRenderer((thisList, value, index, isSelected, cellHasFocus) -> {
			JPanel panel = new JPanel(new GridLayout(1, 0, 10, 10));
			JLabel label = new JLabel("Datum: " + value.getDateTimeString());
			panel.add(label);
			JLabel label2 = new JLabel("Betrag: " + new DecimalFormat("#.00").format(value.getValue()) + " €");
			panel.add(label2);
			JLabel label3 = new JLabel("Kategorie: " + value.getCategory());
			panel.add(label3);

			if (isSelected) {
				panel.setBackground(Color.LIGHT_GRAY);
			} else {
				panel.setBackground(Color.WHITE);
			}
			return panel;
		});
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridy++;
		gbc.ipady = 200;
		this.add(new JScrollPane(this.list), gbc);
		start.expenses.stream().filter(Expense::isVisible).forEach(this.listModel::addElement);

		JButton delete = new JButton("Löschen");
		delete.setEnabled(false);
		this.list.addListSelectionListener(e -> delete.setEnabled(!this.list.getSelectionModel().isSelectionEmpty()));
		delete.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0), "delete");
		delete.getActionMap().put("delete", new AbstractAction() {

			private static final long serialVersionUID = -5157642119761363006L;

			@Override
			public void actionPerformed(ActionEvent e) {
				delete.doClick();
			}
		});
		delete.addActionListener(e -> {
			if (ExpensePanel.this.list.getSelectedValue() != null) {
				if (JOptionPane.showConfirmDialog(ExpensePanel.this, "Die Ausgabe wirklich löschen?",
						"Wirklich löschen?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					Expense ex = ExpensePanel.this.list.getSelectedValue();
					start.expenses.remove(ex);
					ExpensePanel.this.listModel.remove(ExpensePanel.this.list.getSelectedIndex());
					ex.setVisible(false);
				}
			}
		});
		gbc.gridy++;
		gbc.ipady = 0;
		this.add(delete, gbc);

		this.setMinimumSize(new Dimension(300, 400));
	}

}
