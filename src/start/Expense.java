package start;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Expense implements Serializable {

	private static final long serialVersionUID = 9056243021790851396L;

	public static Expense newExpense(JFrame parent) {
		// Wann
		LocalDateTime dateTime = DateSelector.dateTimeInput(parent);
		if (dateTime == null) {
			return null;
		}

		JDialog dialog = new JDialog(parent, "Neue Ausgabe", true);
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
		JTextField valueField = new JTextField(10);
		gbc.gridx++;
		panel.add(valueField, gbc);
		JPanel jButtonPanel = new JPanel();
		JButton ok = new JButton("OK");
		ok.addActionListener(e1 -> dialog.dispose());
		jButtonPanel.add(ok);
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = 4;
		panel.add(jButtonPanel, gbc);
		dialog.add(panel);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		// Blocks until finished

		Category category = (Category) categoriesComboBox.getSelectedItem();
		double value = 0;
		try {
			value = NumberFormat.getNumberInstance(Locale.GERMANY).parse(valueField.getText()).doubleValue();
		} catch (ParseException e) {
			return null;
		}
		return new Expense(dateTime, category, value);
	}

	LocalDateTime dateTime;

	Category category;

	double value;

	UUID id;

	public Expense(LocalDateTime dateTime, Category category, double value) {
		this.dateTime = dateTime;
		this.category = category;
		this.value = value;
		this.id = UUID.randomUUID();
		this.write();
	}

	private void write() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(new File(Start.allCategoriesFile, this.getID() + ".exp")))) {
			oos.writeObject(this);
		} catch (IOException IOE) {
			IOE.printStackTrace();
		}
	}

	public String getDateTimeString() {
		return this.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.YY"));
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public UUID getID() {
		return this.id;
	}

	public void writeObject(ObjectOutputStream stream) throws IOException {
		PutField fields = stream.putFields();

		fields.put("value", this.value);
		fields.put("category", this.category);
		fields.put("dateTime", this.dateTime);
		fields.put("ID", this.id);
	}

	public void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		GetField fields = stream.readFields();

		this.value = fields.get("value", 0);
		this.category = (Category) fields.get("category", null);
		this.dateTime = (LocalDateTime) fields.get("dateTime", LocalDateTime.MIN);
		this.id = (UUID) fields.get("ID", UUID.randomUUID());
	}
}
