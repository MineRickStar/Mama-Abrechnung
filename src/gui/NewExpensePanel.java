package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.IntStream;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import start.Category;
import start.Expense;
import start.Start;

public class NewExpensePanel extends JPanel {

	private static final long serialVersionUID = 1519088749293030855L;

	DefaultComboBoxModel<Category> model;

	public NewExpensePanel(Start start) {
		super(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridy = 0;
		gbc.gridx = 0;
		this.add(new JLabel("Datum:"), gbc);
		JPanel datePanel = new JPanel(new GridLayout(3, 1, 5, 5));
		LocalDate date = LocalDate.now();
		JComboBox<Integer> day = new JComboBox<>(IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new));
		day.setMaximumRowCount(31);
		day.setSelectedIndex(date.getDayOfMonth() - 1);

		this.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "new");
		this.getActionMap().put("new", new AbstractAction() {

			private static final long serialVersionUID = -5157642119761363006L;

			@Override
			public void actionPerformed(ActionEvent e) {
				day.requestFocusInWindow();
			}
		});

		JComboBox<String> month = new JComboBox<>(Arrays.asList(Month.values()).stream()
				.map(t -> t.getDisplayName(TextStyle.FULL, Locale.GERMANY)).toArray(String[]::new));
		month.setMaximumRowCount(12);
		month.setSelectedIndex(date.getMonthValue() - 1);
		JComboBox<Integer> year = new JComboBox<>(IntStream.rangeClosed(date.getYear() - 5, date.getYear()).boxed()
				.sorted(Collections.reverseOrder()).toArray(Integer[]::new));
		year.setSelectedItem(date.getYear());
//		year.setEditable(true);
		datePanel.add(day);
		datePanel.add(month);
		datePanel.add(year);
		gbc.gridx++;
		this.add(datePanel, gbc);
		gbc.gridy++;
		gbc.gridx = 0;
		this.add(new JLabel("Kategorie:"), gbc);
		this.model = new DefaultComboBoxModel<>(Category.allCategories().toArray(new Category[0]));
		JComboBox<Category> categoriesComboBox = new JComboBox<>(this.model);
		categoriesComboBox.setSelectedIndex(-1);
		gbc.gridx++;
		this.add(categoriesComboBox, gbc);
		gbc.gridy++;
		gbc.gridx = 0;
		this.add(new JLabel("Wert:"), gbc);
		JTextField valueField = new JTextField(10);
		gbc.gridx++;
		this.add(valueField, gbc);
		JPanel jButtonPanel = new JPanel();
		JButton add = new JButton("Hinzufügen");
		add.addActionListener(event -> {
			Category category = (Category) categoriesComboBox.getSelectedItem();
			double value = 0;
			try {
				value = NumberFormat.getNumberInstance(Locale.GERMANY).parse(valueField.getText()).doubleValue();
			} catch (ParseException exception) {
				return;
			}
			if (category != null) {
				int localDay = day.getSelectedIndex() + 1;
				int localMonth = month.getSelectedIndex() + 1;
				int localYear = (int) year.getSelectedItem();
				Expense expense = new Expense(
						LocalDateTime.of(LocalDate.of(localYear, localMonth, localDay), LocalTime.MIN), category,
						value);
				start.expenses.add(expense);
				start.mainFrame.expensePanel.listModel.addElement(expense);
				day.setSelectedIndex(date.getDayOfMonth() - 1);
				month.setSelectedIndex(date.getMonthValue() - 1);
				year.setSelectedItem(date.getYear());
				categoriesComboBox.setSelectedIndex(-1);
				valueField.setText("");
			}

		});
		add.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), "add");
		add.getActionMap().put("add", new AbstractAction() {

			private static final long serialVersionUID = 1623081804580331020L;

			@Override
			public void actionPerformed(ActionEvent e) {
				add.doClick();
			}
		});
		jButtonPanel.add(add);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		this.add(jButtonPanel, gbc);
		this.setMinimumSize(new Dimension(200, 200));
	}

	public void addCategory(Category c) {
		if (c != null) {
			this.model.addElement(c);
		}
	}

	public void removeCategory(Category c) {
		this.model.removeElement(c);
	}
}
