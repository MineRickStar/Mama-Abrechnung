package start;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Category implements Serializable {

	private static final long serialVersionUID = -2796738695119367881L;

	private static ArrayList<Category> allCategories = new ArrayList<>();

	public static ArrayList<Category> allCategories() {
		return Category.allCategories;
	}

	public static Category newCategory(JFrame parent) {
		return Category.display(parent, null);
	}

	public static void editCategories(JFrame parent) {
		JDialog dialog = new JDialog(parent, "Kategorien bearbeiten", true);
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		DefaultListModel<Category> listModel = new DefaultListModel<>();
		JList<Category> categoryList = new JList<>(listModel);
		categoryList.setFixedCellWidth(200);
		categoryList.setFixedCellHeight(20);
		Category.allCategories.forEach(listModel::addElement);

		gbc.gridx++;
		gbc.gridy++;
		dialog.add(new JScrollPane(categoryList), gbc);
		JPanel buttonPanel = new JPanel();
		JButton edit = new JButton("bearbeiten");
		edit.addActionListener(e -> {
			if (categoryList.getSelectedValue() != null) {
				Category.display(parent, categoryList.getSelectedValue());
			}
		});
		buttonPanel.add(edit);
		JButton delete = new JButton("Löschen");
		delete.addActionListener(e -> {
			if (categoryList.getSelectedValue() != null) {
				if (JOptionPane.showConfirmDialog(dialog, "Wirklich löschen?", "Löschen",
						JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
					return;
				}
				Category c = categoryList.getSelectedValue();
				Category.allCategories.remove(c);
				listModel.removeElement(c);
				for (Iterator<Category> iterator = Category.allCategories.iterator(); iterator.hasNext();) {
					Category category = iterator.next();
					if (c.equals(category.getParentCategory())) {
						listModel.removeElement(category);
						iterator.remove();
					}
				}
			}
		});
		buttonPanel.add(delete);
		gbc.gridy++;
		dialog.add(buttonPanel, gbc);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}

	private static Category display(JFrame parent, Category category) {
		JDialog dialog = new JDialog(parent, "Neue Kategorie", true);
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		JLabel parentLabel = new JLabel("Überkategorie");
		gbc.gridx++;
		gbc.gridy++;
		dialog.add(parentLabel, gbc);
		JComboBox<Category> parentCategory = new JComboBox<>(Category.allCategories.toArray(new Category[0]));
		parentCategory.insertItemAt(null, 0);
		parentCategory.setSelectedIndex(0);
		if (category != null) {
			parentCategory.setSelectedItem(category.getParentCategory());
		}
		gbc.gridx++;
		dialog.add(parentCategory, gbc);
		JLabel categoryLabel = new JLabel("Kategorie");
		gbc.gridy++;
		gbc.gridx = 0;
		dialog.add(categoryLabel, gbc);
		JTextField textField = new JTextField(10);
		if (category != null) {
			textField.setText(category.getName());
		}
		gbc.gridx++;
		dialog.add(textField, gbc);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(e -> dialog.dispose());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		dialog.add(okButton, gbc);

		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);

		if (textField.getText().isEmpty()) {
			return null;
		}

		return new Category((Category) parentCategory.getSelectedItem(), textField.getText());
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(Category::write));
	}

	public static void read() {
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(new File(Start.location, "Alle Kategorien.category")))) {
			Category.allCategories = (ArrayList<Category>) ois.readObject();
		} catch (FileNotFoundException e) {
			// thats ok
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void write() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(new File(Start.location, "Alle Kategorien.category")))) {
			oos.writeObject(Category.allCategories);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Category parentCategory;

	String name;

	public Category(Category parentCategory, String name) {
		this.parentCategory = parentCategory;
		this.name = name;
		Category.allCategories.add(this);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getParentCategory() {
		return this.parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof Category) {
			boolean nameEqual = this.name.equals(((Category) o).name);
			if (this.parentCategory != null) {
				return nameEqual && this.parentCategory.equals(((Category) o).parentCategory);
			} else {
				return nameEqual;
			}
		}
		return false;
	}

}
