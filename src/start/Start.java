package start;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class Start extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 811156888087713146L;

	public static final File receiptsFile = new File(new File(System.getProperty("user.dir"), "Mama Abrechnung"),
			"Kassenzettel");

	JList<Receipt> list;

	DefaultListModel<Receipt> listModel;

	ArrayList<Receipt> receipts;

	public Start() {
		super("Mamas Abrechnungsprogramm");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.receipts = new ArrayList<>();
		this.listModel = new DefaultListModel<>();
		this.list = new JList<>(this.listModel);
		if (!Start.receiptsFile.mkdirs()) {
			// Only read if the location does not exist
			this.read();
		}
		this.setSize(400, 800);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.list.setCellRenderer((thisList, value, index, isSelected, cellHasFocus) -> {
			JPanel panel = new JPanel(new GridLayout(1, 0, 10, 10));
			JLabel label = new JLabel("Datum: " + value.getLocaleDateTimeString());
			panel.add(label);
			JLabel label2 = new JLabel("Summe: " + value.getSumme() + " €");
			panel.add(label2);

			if (isSelected) {
				panel.setBackground(Color.GRAY);
			} else {
				panel.setBackground(Color.LIGHT_GRAY);
			}
			return panel;
		});

		this.addMenu();
		this.add(new JScrollPane(this.list));
		this.setVisible(true);
	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");

		JMenuItem newReceipt = new JMenuItem("Neu");
		newReceipt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		newReceipt.addActionListener(e -> {
			Receipt receipt = Receipt.newReceipt(this);
			if (receipt != null) {
				this.receipts.add(receipt);
				this.listModel.addElement(receipt);
				this.revalidate();
			}
		});

		JMenuItem edit = new JMenuItem("Bearbeiten");
		edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		edit.addActionListener(e -> {
			if (this.list.getSelectedValue() == null) {
				return;
			}
			Receipt.editReceipt(this.list.getSelectedValue());
		});

		JMenuItem delete = new JMenuItem("Löschen");
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		delete.addActionListener(e -> {
			Receipt r = this.list.getSelectedValue();
			if (r != null) {
				if (JOptionPane.showConfirmDialog(Start.this, "Datensatz wirklich löschen?", "Löschen?",
						JOptionPane.YES_NO_OPTION) == 0) {
					this.listModel.removeElement(r);
					this.receipts.remove(r);
					// TODO file löschen
				}
			}

		});

		file.add(newReceipt);
		file.add(edit);
		file.add(delete);
		menuBar.add(file);
		this.setJMenuBar(menuBar);
	}

	private void read() {
		File[] files = Start.receiptsFile.listFiles();
		Arrays.asList(files).forEach(file -> {
			try {
				byte[] bytes = Files.readAllBytes(file.toPath());
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
				Receipt r = (Receipt) ois.readObject();
				this.receipts.add(r);
				this.listModel.addElement(r);
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
