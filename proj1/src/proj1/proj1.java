package proj1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class proj1 {

	private LinkedList<String> list;

	public proj1() {
		list = new LinkedList<String>();
	}

	public static void main(String[] args) {
//		Scanner input = new Scanner(System.in);
//		//input.useDelimiter("' '|,|\n|\"|.|\b|\t|/|%|&|!|@|#|$|?|-|_|>|<");
//		input.useDelimiter("' '|\n");

		proj1 compressor = new proj1();

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		//compressor.compress(input);

		try {
			int firstChar = input.read();
			if (firstChar == 0) {
				// Consume space
				input.read();
				// TODO Decompress
				compressor.decompress(input);
			} else {
				// TODO Compress
				compressor.compress(input, firstChar);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parsing character-by-character now with Buffered Reader
	 * 
	 * @param input
	 */
	public void compress(BufferedReader input, int firstChar) {
		// Tallies of chars read and printed, for stats line at end
		int inCount = 0;
		int outCount = 0;
		
		// Initialize compressed output with leading 0
		System.out.print("0 ");
		outCount += 2;

		try {
			// Number of next char
			int charNumber = firstChar;
			// Cast number to char symbol
			char symbol = (char) charNumber;
			inCount++;

			// Master Loop - stop when EOF
			while (charNumber != -1) {
				// Word currently parsing
				String word = "";
				// Loop to end of word
				while (Character.isLetter(symbol)) {
					word += symbol;
					charNumber = input.read();
					if (charNumber == -1) {
						// End of file, exit loop
						break;
					} else {
						inCount++;
						symbol = (char) charNumber;
					}
				}
				// Hit end of word, search list
				int position = list.search(word);
				if (position == -1) {
					// New word, print it
					System.out.print(word);
					outCount += word.length();
				} else {
					// Previous word, replace with number
					System.out.print(position);
					outCount += Integer.toString(position).length();
				}
				// Loop through and print punctuation until next letter
				while (!Character.isLetter(symbol)) {
					System.out.print(symbol);
					outCount++;
					charNumber = input.read();
					if (charNumber == -1) {
						// End of file, exit loop
						break;
					} else {
						inCount++;
						symbol = (char) charNumber;
					}
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
		// Print out compression stats
		System.out.print("\n0 Uncompressed: " + inCount + " bytes;  Compressed: " + outCount + " bytes");
	}

	public void decompress(BufferedReader input) {
		// TODO Check for 0 to know when to chop off stats line
	}

	/**
	 * Private inner class defines state and behavior for a singly linked list to
	 * maintain list of read words. Functions included for add to front, removal at
	 * specified index, and linear search.
	 * 
	 * @author Nick Garner
	 *
	 * @param <E>
	 */
	private class LinkedList<E> {
		/** First Node in the list */
		private Node head;
		/** Running tally of number of list elements */
		private int size;

		/**
		 * Null constructor. Creates an empty LinkedList with a null head.
		 */
		public LinkedList() {
			head = new Node(null);
			size = 0;
		}

//		/**
//		 * Creates new LinkedList with head Node containing given data.
//		 * 
//		 * @param data Data to hold in the head Node of the new LinkedList.
//		 */
//		public LinkedList(E data) {
//			head = new Node(data);
//			if (head.data == null) {
//				size = 0;
//			} else {
//				size = 1;
//			}
//		}

		/**
		 * Creates a new head Node at the front of the list with the given data and
		 * points it to the current list head.
		 * 
		 * @param data Data to hold in the new head Node.
		 * @return True if successfully added to front, false if Exception occurs.
		 */
		public boolean addToFront(E data) {
			try {
				head = new Node(data, head);
				size++;
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		/**
		 * Loops through the list checking each element for a match to the given
		 * parameter. If found, returns index of match, otherwise adds param to front of
		 * list and returns 1.
		 * 
		 * @param data The object to search the list for.
		 * @return Position of param in list.
		 */
		public int search(E data) {
			// Pointer to iterate through list
			Node current = head;
			// Tally of current position in list
			int position = 1;

			// Loop to check for matching element in list
			while (current.data != null) {
				// If match found, remove at current index and add to front of list
				try {
					if (current.data.equals(data)) {
						if (position == 1) {
							// Already at front
							return position;
						} else {
							remove(position);
							addToFront(data);
							return position;
						}
					}
				} catch (NullPointerException e) {
					System.out.print("_NPE here_");
				}
				// No match, advance pointer and increment position
				current = current.next;
				position++;
			}

			// Couldn't find match, add to front and return -1
			addToFront(data);
			return -1;
		}

		/**
		 * Iterates through linked list for element at given position and then removes and
		 * returns the data at that position.
		 * 
		 * @param position Position of data to be removed and returned
		 * @return Data at given position
		 */
		public E remove(int position) {
			if (position > size()) {
				throw new IllegalArgumentException();
			}

			// Pointers to iterate over list and update links
			Node previous = null;
			Node current = head;
			// Counter to keep track of position in list
			int count = 1;

			// Loop to locate desired list element
			while (count < position) {
				previous = current;
				current = current.next;
				count++;
			}

			previous.next = current.next;
			size--;
			return current.data;
		}

		/**
		 * Returns current size of list
		 * 
		 * @return Size of list
		 */
		public int size() {
			return size;
		}

		/**
		 * Private inner class that maintains state and behavior for Nodes in the
		 * LinkedList. Nodes consist of data and a pointer to the next node in the list.
		 * 
		 * @author Nick Garner
		 *
		 */
		private class Node {
			/** Generic type data contained in the Node */
			private E data;
			/** Pointer to the next Node in the list */
			private Node next;

			/**
			 * Constructor with null next pointer. Used for starting new list with head
			 * only.
			 * 
			 * @param data Data to hold in the Node.
			 */
			public Node(E data) {
				this(data, null);
			}

			/**
			 * Constructs a Node with the given data and pointer to the next Node in the
			 * list.
			 * 
			 * @param data Data to hold in the Node.
			 * @param next Pointer to the next Node in the list.
			 */
			public Node(E data, Node next) {
				this.data = data;
				this.next = next;
			}
		}
	}
}