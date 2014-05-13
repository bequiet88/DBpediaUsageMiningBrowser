package filters;

import ldb.RDFTriple;
import result.Result;
import result.Table;
import filter.FilterTable;

/**
 * Filter for testing parameters.
 * 
 */
public class ParameterTestFilter extends FilterTable {
	protected byte testByte;
	protected short testShort;
	protected int testInt;
	protected long testLong;
	protected float testFloat;
	protected double testDouble;
	protected char testChar;
	protected String testString;
	protected boolean testBoolean;

	public void setTestByte(byte testByte) {
		this.testByte = testByte;
	}

	public void setTestShort(short testShort) {
		this.testShort = testShort;
	}

	public void setTestInt(int testInt) {
		this.testInt = testInt;
	}

	public void setTestLong(long testLong) {
		this.testLong = testLong;
	}

	public void setTestFloat(float testFloat) {
		this.testFloat = testFloat;
	}

	public void setTestDouble(double testDouble) {
		this.testDouble = testDouble;
	}

	public void setTestChar(char testChar) {
		this.testChar = testChar;
	}

	public void setTestString(String testString) {
		this.testString = testString;
	}

	public void setTestBoolean(boolean testBoolean) {
		this.testBoolean = testBoolean;
	}

	@Override
	public Result execute(Table table) {
		RDFTriple byteTriple = new RDFTriple("testByte", "is", testByte);
		RDFTriple shortTriple = new RDFTriple("testShort", "is", testShort);
		RDFTriple intTriple = new RDFTriple("testInt", "is", testInt);
		RDFTriple longTriple = new RDFTriple("testLong", "is", testLong);
		RDFTriple floatTriple = new RDFTriple("testFloat", "is", testFloat);
		RDFTriple doubleTriple = new RDFTriple("testDouble", "is", testDouble);
		RDFTriple booleanTriple = new RDFTriple("testBoolean", "is", testBoolean);
		RDFTriple charTriple = new RDFTriple("testChar", "is", testChar);
		RDFTriple stringTriple = new RDFTriple("testString", "is", testString);

		return new Table(new RDFTriple[] { byteTriple, shortTriple, intTriple, longTriple, floatTriple, doubleTriple,
				booleanTriple, charTriple, stringTriple });

	}

}
