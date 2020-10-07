package start;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Receipt implements Serializable {

	private static final long serialVersionUID = -6694875015242084424L;

	public static Receipt newReceipt(JFrame parent) {
		// Wann
		LocalDateTime dateTime = DateSelector.dateTimeInput(parent);
		if (dateTime == null) {
			return null;
		}
		// Wo
		JComboBox<Location> comboBox = new JComboBox<>(Location.allLocation().toArray(Location[]::new));
		JOptionPane.showMessageDialog(parent, comboBox, "Wo?", JOptionPane.QUESTION_MESSAGE);
		Location location = (Location) comboBox.getSelectedItem();
		// Ausgaben
		ArrayList<Expense> expenses = Expenses.getExpenses(parent);
		if (expenses.isEmpty()) {
			return null;
		}

		return new Receipt(dateTime, location, expenses);
	}

	public static void editReceipt(Receipt receipt) {
		System.out.println("edit");
	}

	LocalDateTime dateTime;

	Location location;

	final ArrayList<Expense> expenses;

	double summe;

	UUID id;

	public Receipt(LocalDateTime dateTime, Location location, ArrayList<Expense> expenses) {
		this.dateTime = dateTime;
		this.location = location;
		this.expenses = expenses;
		this.summe = expenses.stream().mapToDouble(Expense::getValue).sum();
		this.id = UUID.randomUUID();
		this.write();
	}

	private void write() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(new File(Start.receiptsFile, this.getID() + ".bon")))) {
			oos.writeObject(this);
		} catch (IOException IOE) {
			IOE.printStackTrace();
		}
	}

	public String getID() {
		return this.id.toString();
	}

	public double getSumme() {
		return this.summe;
	}

	public LocalDateTime getLocalDateTime() {
		return this.dateTime;
	}

	public String getLocaleDateTimeString() {
		return this.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.YY"));
	}

	public void writeObject(ObjectOutputStream stream) throws IOException {
		PutField fields = stream.putFields();

		fields.put("summe", this.summe);
		fields.put("location", this.location);
		fields.put("dateTime", this.dateTime);
		fields.put("ID", this.id);
	}

	public void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		GetField fields = stream.readFields();

		this.summe = fields.get("summe", 0);
		this.location = (Location) fields.get("location", null);
		this.dateTime = (LocalDateTime) fields.get("dateTime", LocalDateTime.MIN);
		this.id = (UUID) fields.get("ID", UUID.randomUUID());
	}

}
