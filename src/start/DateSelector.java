package start;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.IntStream;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

public class DateSelector extends JDialog {

	private static final long serialVersionUID = -2762924014971294152L;

	public static LocalDateTime dateTimeInput(JFrame parent) {
		return new DateSelector(parent).dateTime;
	}

	private LocalDateTime dateTime;

	private DateSelector(JFrame parent) {
		super(parent, "Datum und Uhrzeit", true);
		this.setLayout(new GridBagLayout());
		this.setMinimumSize(new Dimension(250, 200));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(DateSelector.this, "Wirklich schlieﬂen?", "Schlieﬂen?",
						JOptionPane.YES_NO_OPTION) == 0) {
					DateSelector.this.dateTime = null;
					DateSelector.this.dispose();
				}
			}
		});

		this.dateTime = LocalDateTime.now();

		JLabel dayLabel = new JLabel("Tag:");
		JLabel monthLabel = new JLabel("Monat:");
		JLabel yearLabel = new JLabel("Jahr:");

		JComboBox<Integer> jcd = new JComboBox<>(IntStream.range(1, 31).boxed().toArray(Integer[]::new));
		jcd.setSelectedIndex(this.dateTime.getDayOfMonth() - 1);
		JComboBox<String> jcm = new JComboBox<>(Arrays.asList(Month.values()).stream()
				.map(month -> month.getDisplayName(TextStyle.FULL, Locale.GERMANY)).toArray(String[]::new));
		jcm.setSelectedIndex(this.dateTime.getMonthValue() - 1);
		JTextField yearField = new JTextField(String.valueOf(this.dateTime.getYear()), 8);

		JButton ok = new JButton("OK");

		AbstractAction action = new AbstractAction() {
			private static final long serialVersionUID = 6406481287857494362L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int day = jcd.getSelectedIndex() + 1;
				int month = jcm.getSelectedIndex() + 1;
				int year = DateSelector.this.dateTime.getYear();
				try {
					Integer.parseInt(yearField.getText());
				} catch (NumberFormatException num) {
				}
				DateSelector.this.dateTime = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.MIDNIGHT);
				DateSelector.this.dispose();
			}
		};

		ok.addActionListener(action);

		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		this.getRootPane().getActionMap().put("Enter", action);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy++;
		this.add(dayLabel, gbc);
		gbc.gridy++;
		this.add(monthLabel, gbc);
		gbc.gridy++;
		this.add(yearLabel, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx++;
		gbc.gridy = 0;
		this.add(jcd, gbc);
		gbc.gridy++;
		this.add(jcm, gbc);
		gbc.gridy++;
		this.add(yearField, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.gridy++;
		this.add(ok, gbc);

		this.pack();
		this.setLocationRelativeTo(parent);
		this.setVisible(true);
	}

}
