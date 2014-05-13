package view;

/**
 * One static function for turning a credibility rating into an RGB color string.
 * 
 */
public class ColorScale {

	/*
	 * links credibility of sth. to a color from green to yellow to red
	 * 
	 * @param credibility 
	 * 			credibility from 0-100 (0%-100%)
	 * @return color
	 * 			as a rgb hex-value
	 */
	public static String makeColor(int credibility) {

		int value;

		if (credibility != 0)
			value = (int) Math.round(510 * credibility / 100);
		else
			value = 0;

		// value is between 0 and 510;
		// 0 = red, 255 = yellow, 510 = green.
		// use exponential interpolation
		int redValue;
		int greenValue;
		if (value < 255) {
			redValue = 255;
			greenValue = (int) Math.round(Math.sqrt(value) * 16);
		} else {
			greenValue = 255;
			value = value - 255;
			redValue = (int) Math.round(255 - (value * value / 255));
		}

		// append 0 if values are smaller than 16
		String redValueAsHex = Integer.toString(redValue, 16);
		if (redValueAsHex.length() == 1)
			redValueAsHex = "0" + redValueAsHex;

		String greenValueAsHex = Integer.toString(greenValue, 16);
		if (greenValueAsHex.length() == 1)
			greenValueAsHex = "0" + greenValueAsHex;

		String hexColor = "#" + redValueAsHex + greenValueAsHex + "00";

		return hexColor;
	}
	
	/**
	 * 
	 * @param score an integer value between 0 and 100
	 * @return a greyscale value in rgb
	 */
	public static String makeGreyscale(int score) {
		int max = Integer.parseInt("80", 16);
		int greyscaleValue = (int) (max * (1-score/100.0f));
		String greyscaleValueHex = Integer.toString(greyscaleValue,16);
		String hexColor = "#" + greyscaleValueHex + "80" + greyscaleValueHex;
		return hexColor;
	}
}
