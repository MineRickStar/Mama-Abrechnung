package start;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {

	private static final long serialVersionUID = -2796738695119367881L;

	private static ArrayList<Category> allCategories = new ArrayList<>();

	public static ArrayList<Category> allCategories() {
		return Category.allCategories;
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(Category::write));
	}

	public static void read() {
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(new File(Start.location, "Categorien")))) {
			Category.allCategories = (ArrayList<Category>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void write() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(new File(Start.location, "Categorien")))) {
			oos.writeObject(Category.allCategories);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String name;

	public Category(String name) {
		this.name = name;
		Category.allCategories.add(this);
	}

	@Override
	public String toString() {
		return this.name;
	}

}
