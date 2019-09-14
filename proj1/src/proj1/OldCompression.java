package proj1;

import java.util.Scanner;

public class OldCompression {

	/**
	 * Trying to parse with Scanner via whitespace & \n delimiter
	 * 
	 * @param input
	 */
	public void compressV2(Scanner input) {
		// TODO Implement count of bytes processed and compressed (count input and
		// output)
		// TODO Implement print of counts
		// Running tally of bytes read
		int inCount = 0;
		// Tally of bytes printed
		int outCount = 0;

		while (input.hasNext()) {
			// Store word with punctuation
			String word = input.next();
			inCount += word.length();
			word = word.trim();
			// Store first and last chars in word to check for punctuation
			Character front = word.charAt(0);
			Character back = word.charAt(word.length() - 1);

			// Check for punctuation then update word to remove
			if (!Character.isLetter(front)) {
				if (word.length() == 1) {
					System.out.print(word + " ");
					outCount += 2;
					continue;
				}
				System.out.print(front);
				outCount++;
				word = word.substring(1);
			}
			if (!Character.isLetter(back)) {
				if (word.length() != 1) {
					word = word.substring(0, word.length() - 1);
				}
			}

			// Search for word in list
			int index = list.search(word);
			if (index == 1) {
				// Print and then loop around
				if (!Character.isLetter(back)) {
					System.out.print(word + back + " ");
					outCount += word.length() + 2;
				} else {
					System.out.print(word + " ");
					outCount += word.length() + 1;
				}
			} else {
				// Replace word with position in list
				System.out.print(index + " ");
				outCount += Integer.toString(index).length() + 1;
			}
		}
		// Loop one more time for last word
		String temp = input.next().trim();
		int index = list.search(temp);
		if (index == 1) {
			// Print and then loop around
			System.out.print(temp);
		} else {
			System.out.print(index);
		}
	}
	
}
