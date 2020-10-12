package start;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import gui.MainFrame;

public class Start {

	public static final File location = new File(System.getProperty("user.dir"), "Mama Abrechnung");

	public static final File allCategoriesFile = new File(Start.location, "Ausgaben");

	public ArrayList<Expense> expenses;

	public MainFrame mainFrame;

	public Start() {
		Start.allCategoriesFile.mkdirs();
		this.expenses = new ArrayList<>();
		this.read();
		this.mainFrame = new MainFrame(this);
	}

	private void read() {
		Category.read();
		File[] files = Start.allCategoriesFile.listFiles();
		Arrays.asList(files).forEach(file -> {
			try {
				byte[] bytes = Files.readAllBytes(file.toPath());
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
				this.expenses.add((Expense) ois.readObject());
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
