package start;

import java.io.Serializable;

public class Expense implements Serializable {

	private static final long serialVersionUID = 9056243021790851396L;

	Category category;

	double value;

	public Expense(Category category, double value) {
		this.category = category;
		this.value = value;
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

}
