package util;

/**
 * 
 * A three dimensional Tuple Container that holds up to three values.
 * 
 * @param <A> the first {@link Number}
 * @param <B> the second {@link Number}
 * 
 * @author Michael Bowes
 */
public class Tuple {

	int firstVal;
	int secondVal;

	public Tuple() {
		firstVal = 0;
		secondVal = 0;
	}

	public Tuple(int valOne, int valTwo) {
		firstVal = valOne;
		secondVal = valTwo;
	}

	public int getFirstValue() {
		return firstVal;
	}

	public int getSecondValue() {
		return secondVal;
	}

	public void addFirstValue(int val) {
		this.firstVal = val;
	}

	public void addSecondValue(int val) {
		this.secondVal = val;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (((Tuple) obj).getFirstValue() == this.getFirstValue()
				&& ((Tuple) obj).getSecondValue() == this.getSecondValue()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return firstVal + secondVal;
	}

}
