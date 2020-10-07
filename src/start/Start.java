package start;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class Start extends JFrame {

	private static final long serialVersionUID = 811156888087713146L;

	public static final File location = new File(System.getProperty("user.dir"), "Mama Abrechnung");

	public static final File receiptsFile = new File(Start.location, "Kassenzettel");

	JList<Receipt> list;

	DefaultListModel<Receipt> listModel;

	ArrayList<Receipt> receipts;

	public Start() {
		super("Mamas Abrechnungsprogramm");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.receipts = new ArrayList<>();
		this.listModel = new DefaultListModel<>();
		this.list = new JList<>(this.listModel);
		Start.receiptsFile.mkdirs();
		this.read();
		this.setSize(400, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.list.setFixedCellHeight(20);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.list.setCellRenderer((thisList, value, index, isSelected, cellHasFocus) -> {
			JPanel panel = new JPanel(new GridLayout(1, 0, 10, 10));
			JLabel label = new JLabel("Datum: " + value.getLocaleDateTimeString());
			panel.add(label);
			JLabel label2 = new JLabel("Summe: " + new DecimalFormat("#.00").format(value.getSumme()) + "€");
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

		JButton newReceipt = new JButton("Neu");
//		newReceipt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		newReceipt.addActionListener(e -> {
			Receipt receipt = Receipt.newReceipt(this);
			if (receipt != null) {
				this.receipts.add(receipt);
				this.listModel.addElement(receipt);
				this.revalidate();
			}
		});

		JButton edit = new JButton("Bearbeiten");
//		edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		edit.addActionListener(e -> {
			if (this.list.getSelectedValue() == null) {
				return;
			}
			Receipt.editReceipt(this.list.getSelectedValue());
		});

		JButton delete = new JButton("Löschen");
//		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		delete.addActionListener(e -> {
			Receipt r = this.list.getSelectedValue();
			if (r != null) {
				if (JOptionPane.showConfirmDialog(Start.this, "Datensatz wirklich löschen?", "Löschen?",
						JOptionPane.YES_NO_OPTION) == 0) {
					this.listModel.removeElement(r);
					this.receipts.remove(r);
					new File(Start.receiptsFile, r.getID() + ".bon").delete();
				}
			}

		});

		menuBar.add(newReceipt);
		menuBar.add(edit);
		menuBar.add(delete);
		this.setJMenuBar(menuBar);
	}

	private void read() {
		Category.read();
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
