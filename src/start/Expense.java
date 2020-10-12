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
import java.util.UUID;

public class Expense implements Serializable {

	private static final long serialVersionUID = 9056243021790851396L;

	LocalDateTime dateTime;

	Category category;

	double value;

	UUID ID;

	boolean visible;

	public Expense(LocalDateTime dateTime, Category category, double value) {
		this.dateTime = dateTime;
		this.category = category;
		this.value = value;
		this.ID = UUID.randomUUID();
		this.visible = true;
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

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		this.write();
	}

	public UUID getID() {
		return this.ID;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		PutField fields = stream.putFields();

		fields.put("value", this.value);
		fields.put("category", this.category);
		fields.put("dateTime", this.dateTime);
		fields.put("visible", this.visible);
		fields.put("ID", this.ID);

		stream.writeFields();
	}

	private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		GetField fields = stream.readFields();

		this.value = fields.get("value", 0.0);
		this.category = (Category) fields.get("category", null);
		this.dateTime = (LocalDateTime) fields.get("dateTime", LocalDateTime.MIN);
		this.visible = fields.get("visible", true);
		this.ID = (UUID) fields.get("ID", UUID.randomUUID());
	}
}
