package edu.Lewis.week2;

	import java.util.*;

	/**
	 * Wordle-style guessing game
	 * Demonstrates good object-oriented design in a single file
	 */
	public class WordleGame {

	    private static final int MAX_TRIES = 6;
	    private WordBank wordBank;
	    private List<Guess> guesses;

	    public WordleGame() {
	        this.wordBank = new WordBank();
	        this.guesses = new ArrayList<>();
	    }

	    public void play() {
	        Scanner scanner = new Scanner(System.in);

	        while (guesses.size() < MAX_TRIES) {
	            System.out.print("Enter a 5-letter word: ");
	            String input = scanner.nextLine().toUpperCase();

	            if (!wordBank.isValidWord(input)) {
	                System.out.println("Invalid word. Try again.");
	                continue;
	            }

	            Guess guess = new Guess(input);
	            guess.evaluate(wordBank.getAnswer());
	            guesses.add(guess);

	            displayGuess(guess);

	            if (guess.isCorrect()) {
	                System.out.println("🎉 You guessed the word!");
	                return;
	            }
	        }

	        System.out.println("Game over! The word was: " + wordBank.getAnswer());
	    }

	    private void displayGuess(Guess guess) {
	        for (Tile tile : guess.getTiles()) {
	            System.out.print(tile.getLetter() + "(" + tile.getFeedback() + ") ");
	        }
	        System.out.println();
	    }

	    public static void main(String[] args) {
	        WordleGame game = new WordleGame();
	        game.play();
	    }
	}

	/* ================= Supporting Classes ================= */

	enum FeedbackType {
	    GREEN, YELLOW, GRAY
	}

	class Tile {
	    private char letter;
	    private FeedbackType feedback;

	    public Tile(char letter, FeedbackType feedback) {
	        this.letter = letter;
	        this.feedback = feedback;
	    }

	    public char getLetter() {
	        return letter;
	    }

	    public FeedbackType getFeedback() {
	        return feedback;
	    }
	}

	class Guess {
	    private String word;
	    private List<Tile> tiles;

	    public Guess(String word) {
	        this.word = word;
	        this.tiles = new ArrayList<>();
	    }

	    public void evaluate(String answer) {
	        boolean[] used = new boolean[5];
	        tiles.clear();

	        // First pass: GREEN
	        for (int i = 0; i < 5; i++) {
	            if (word.charAt(i) == answer.charAt(i)) {
	                tiles.add(new Tile(word.charAt(i), FeedbackType.GREEN));
	                used[i] = true;
	            } else {
	                tiles.add(null);
	            }
	        }

	        // Second pass: YELLOW / GRAY
	        for (int i = 0; i < 5; i++) {
	            if (tiles.get(i) != null) continue;

	            char c = word.charAt(i);
	            boolean found = false;

	            for (int j = 0; j < 5; j++) {
	                if (!used[j] && c == answer.charAt(j)) {
	                    found = true;
	                    used[j] = true;
	                    break;
	                }
	            }

	            tiles.set(i, new Tile(c, found ? FeedbackType.YELLOW : FeedbackType.GRAY));
	        }
	    }

	    public boolean isCorrect() {
	        for (Tile tile : tiles) {
	            if (tile.getFeedback() != FeedbackType.GREEN) {
	                return false;
	            }
	        }
	        return true;
	    }

	    public List<Tile> getTiles() {
	        return tiles;
	    }
	}

	class WordBank {
	    private static final String[] WORDS = {
	        "APPLE", "MOUSE", "CHAIR", "PLANT", "BRAIN",
	        "STONE", "HOUSE", "TRAIN", "LIGHT", "WATER"
	    };

	    private String answer;

	    public WordBank() {
	        Random rand = new Random();
	        answer = WORDS[rand.nextInt(WORDS.length)];
	    }

	    public boolean isValidWord(String word) {
	        if (word.length() != 5) return false;
	        for (String w : WORDS) {
	            if (w.equals(word)) return true;
	        }
	        return false;
	    }

	    public String getAnswer() {
	        return answer;
	    }
	}
