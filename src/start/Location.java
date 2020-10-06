package start;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements Serializable {

	private static final long serialVersionUID = 8894809238506515952L;

	private static final ArrayList<Location> allLocations = new ArrayList<>();

	public static ArrayList<Location> allLocation() {
		return Location.allLocations;
	}

	private final String name;

	public Location(String name) {
		this.name = name;
		Location.allLocations.add(this);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof Location) {
			return this.name.equals(((Location) o).name);
		}
		return false;
	}

}
