package util;

/**
 * 
 * A three dimensional Tuple Container that holds up to three values.
 * @param <A> the first {@link Number}
 * @param <B> the second {@link Number}
 */
public class Tuple<A extends Number, B extends Number, C extends Number> {

	Number firstVal;
	Number secondVal;
	Number thirdVal;
	
	public Tuple() {

	}
	
	public Tuple(A valOne, B valTwo, C valThree) {
		firstVal = valOne;
		secondVal = valTwo;
		thirdVal = valThree;
	}

	public Number getFirstValue() {
		return firstVal;
	}

	public Number getSecondValue() {
		return secondVal;
	}
	
	public Number getThirdValue() {
		return thirdVal;
	}

	public void addFirstValue(A val) {
		this.firstVal = val;
	}
	
	public void addSecondValue(B val) {
		this.secondVal = val;
	}
	
	public void addThirdValue(B val) {
		this.thirdVal = val;
	}

}
