package org.apache.camel;

/**
 * Created by ocruz on 4/14/16.
 */
public class TestMessage {

	private String test;

	/**
	 * Gets test.
	 *
	 * @return Value of test.
	 */
	public String getTest() {
		return test;
	}


	/**
	 * Sets new test.
	 *
	 * @param test New value of test.
	 */
	public void setTest(String test) {
		this.test = test;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TestMessage)) return false;

		TestMessage that = (TestMessage) o;

		return test != null ? test.equals(that.test) : that.test == null;
	}
}
