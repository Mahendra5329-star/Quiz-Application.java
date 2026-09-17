import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * =====================================================================
 *  TASK 4 — QUIZ APPLICATION
 *  A console-based multiple-choice quiz with categories, difficulty
 *  levels, randomized questions, scoring, and grade reporting.
 *
 *  Demonstrates:
 *    - Object-Oriented Programming (encapsulation, enums, immutability)
 *    - Java Collections (List, Map, EnumMap-style usage)
 *    - Conditional logic and loops
 *    - Exception handling
 *    - Input validation
 * =====================================================================
 */
public class QuizApplication {

    // =================================================================
    //  ENUMS
    // =================================================================

    /** Available quiz categories. */
    enum Category {
        JAVA("Java"),
        GENERAL_KNOWLEDGE("General Knowledge"),
        SCIENCE("Science"),
        HISTORY("History");

        private final String display;

        Category(String display) { this.display = display; }
        public String getDisplay() { return display; }
    }

    /** Difficulty levels. */
    enum Difficulty {
        EASY("Easy"),
        MEDIUM("Medium"),
        HARD("Hard");

        private final String display;

        Difficulty(String display) { this.display = display; }
        public String getDisplay() { return display; }
    }

    /** Letter grades assigned at the end of a quiz. */
    enum Grade {
        A(90, "A - Excellent!"),
        B(80, "B - Great job!"),
        C(70, "C - Good effort."),
        D(60, "D - Keep practicing."),
        F(0,  "F - Try again!");

        private final int minPercent;
        private final String message;

        Grade(int minPercent, String message) {
            this.minPercent = minPercent;
            this.message = message;
        }

        static Grade fromPercentage(double pct) {
            for (Grade g : values()) {
                if (pct >= g.minPercent) return g;
            }
            return F;
        }

        String getMessage() { return message; }
    }

    // =================================================================
    //  CUSTOM EXCEPTION
    // =================================================================

    /** Thrown when user input cannot produce a valid quiz. */
    static class InvalidInputException extends Exception {
        InvalidInputException(String message) { super(message); }
    }

    // =================================================================
    //  MODEL: Question  (immutable)
    // =================================================================

    static class Question {
        private final String text;
        private final List<String> options;
        private final int correctIndex;   // 0-based
        private final Category category;
        private final Difficulty difficulty;

        Question(String text, List<String> options, int correctIndex,
                 Category category, Difficulty difficulty) {
            if (text == null || text.isBlank())
                throw new IllegalArgumentException("Question text must not be blank.");
            if (options == null || options.size() < 2)
                throw new IllegalArgumentException("At least 2 options are required.");
            if (correctIndex < 0 || correctIndex >= options.size())
                throw new IllegalArgumentException("Correct index out of bounds.");
            if (category == null || difficulty == null)
                throw new IllegalArgumentException("Category and difficulty are required.");

            this.text = text;
            this.options = List.copyOf(options);
            this.correctIndex = correctIndex;
            this.category = category;
            this.difficulty = difficulty;
        }

        String getText()            { return text; }
        List<String> getOptions()   { return options; }
        int getCorrectIndex()       { return correctIndex; }
        Category getCategory()      { return category; }
        Difficulty getDifficulty()  { return difficulty; }

        boolean isCorrect(int answerIndex) { return answerIndex == correctIndex; }
        String getCorrectAnswer()          { return options.get(correctIndex); }
    }

    // =================================================================
    //  SERVICE: QuestionBank
    // =================================================================

    static class QuestionBank {
        private final List<Question> questions = new ArrayList<>();

        QuestionBank() { loadQuestions(); }

        private void loadQuestions() {
            // ---------- JAVA ----------
            add("Which keyword is used to inherit a class in Java?",
                List.of("implements", "extends", "inherits", "super"),
                1, Category.JAVA, Difficulty.EASY);

            add("What is the size of an int in Java?",
                List.of("2 bytes", "4 bytes", "8 bytes", "Depends on JVM"),
                1, Category.JAVA, Difficulty.EASY);

            add("Which collection does NOT allow duplicate elements?",
                List.of("ArrayList", "LinkedList", "Set", "Vector"),
                2, Category.JAVA, Difficulty.EASY);

            add("What is the default value of an object reference variable?",
                List.of("0", "null", "undefined", "garbage"),
                1, Category.JAVA, Difficulty.MEDIUM);

            add("Which method is called when an object is garbage collected?",
                List.of("delete()", "finalize()", "destroy()", "close()"),
                1, Category.JAVA, Difficulty.MEDIUM);

            add("What is the output of 10 / 3 in Java (integer division)?",
                List.of("3.33", "3", "4", "0"),
                1, Category.JAVA, Difficulty.MEDIUM);

            add("Which of these is NOT a functional interface in Java?",
                List.of("Runnable", "Comparator", "Cloneable", "Callable"),
                2, Category.JAVA, Difficulty.HARD);

            add("What is the average-case time complexity of HashMap.get()?",
                List.of("O(1)", "O(log n)", "O(n)", "O(n log n)"),
                0, Category.JAVA, Difficulty.HARD);

            // ---------- GENERAL KNOWLEDGE ----------
            add("What is the capital of France?",
                List.of("Berlin", "Madrid", "Paris", "Rome"),
                2, Category.GENERAL_KNOWLEDGE, Difficulty.EASY);

            add("How many continents are there on Earth?",
                List.of("5", "6", "7", "8"),
                2, Category.GENERAL_KNOWLEDGE, Difficulty.EASY);

            add("Who wrote 'Romeo and Juliet'?",
                List.of("Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"),
                1, Category.GENERAL_KNOWLEDGE, Difficulty.MEDIUM);

            add("Which country has the largest population?",
                List.of("USA", "China", "India", "Russia"),
                2, Category.GENERAL_KNOWLEDGE, Difficulty.MEDIUM);

            // ---------- SCIENCE ----------
            add("What is the chemical symbol for water?",
                List.of("WO", "H2O", "O2", "HO2"),
                1, Category.SCIENCE, Difficulty.EASY);

            add("What planet is known as the Red Planet?",
                List.of("Venus", "Jupiter", "Mars", "Saturn"),
                2, Category.SCIENCE, Difficulty.EASY);

            add("What is the speed of light in a vacuum (approx)?",
                List.of("300,000 km/s", "150,000 km/s", "3,000 km/s", "30,000 km/s"),
                0, Category.SCIENCE, Difficulty.MEDIUM);

            add("Which particle has a negative charge?",
                List.of("Proton", "Neutron", "Electron", "Photon"),
                2, Category.SCIENCE, Difficulty.EASY);

            add("What is the powerhouse of the cell?",
                List.of("Nucleus", "Ribosome", "Mitochondria", "Golgi body"),
                2, Category.SCIENCE, Difficulty.EASY);

            // ---------- HISTORY ----------
            add("In which year did World War II end?",
                List.of("1943", "1944", "1945", "1946"),
                2, Category.HISTORY, Difficulty.EASY);

            add("Who was the first President of the United States?",
                List.of("Thomas Jefferson", "George Washington", "Abraham Lincoln", "John Adams"),
                1, Category.HISTORY, Difficulty.EASY);

            add("The Great Wall of China was primarily built to defend against?",
                List.of("Mongol invasions", "Japanese pirates", "Indian armies", "Korean forces"),
                0, Category.HISTORY, Difficulty.MEDIUM);
        }

        private void add(String text, List<String> options, int correctIndex,
                         Category category, Difficulty difficulty) {
            questions.add(new Question(text, options, correctIndex, category, difficulty));
        }

        List<Question> filter(Category category, Difficulty difficulty) {
            List<Question> result = new ArrayList<>();
            for (Question q : questions) {
                if (category != null && q.getCategory() != category) continue;
                if (difficulty != null && q.getDifficulty() != difficulty) continue;
                result.add(q);
            }
            return result;
        }

        int countFor(Category category, Difficulty difficulty) {
            return filter(category, difficulty).size();
        }
    }

    // =================================================================
    //  SERVICE: QuizService
    // =================================================================

    static class QuizService {
        private final QuestionBank bank;
        private final Random random = new Random();

        QuizService(QuestionBank bank) {
            if (bank == null) throw new IllegalArgumentException("bank required");
            this.bank = bank;
        }

        int countAvailable(Category c, Difficulty d) {
            return bank.countFor(c, d);
        }

        /** Builds a randomized quiz from the given filters. */
        List<Question> buildQuiz(Category category, Difficulty difficulty, int numQuestions)
                throws InvalidInputException {

            if (numQuestions <= 0)
                throw new InvalidInputException("Number of questions must be positive.");

            List<Question> pool = bank.filter(category, difficulty);
            if (pool.isEmpty())
                throw new InvalidInputException("No questions available for the selected filters.");

            Collections.shuffle(pool, random);
            int count = Math.min(numQuestions, pool.size());
            return new ArrayList<>(pool.subList(0, count));
        }

        /**
         * Shuffles a question's options and returns a new Question whose
         * correct index tracks the shuffle. Uses an index permutation so
         * duplicate option text can never mark the wrong answer correct.
         */
        Question shuffleOptions(Question q) {
            List<String> original = q.getOptions();
            int n = original.size();

            List<Integer> order = new ArrayList<>(n);
            for (int i = 0; i < n; i++) order.add(i);
            Collections.shuffle(order, random);

            List<String> shuffled = new ArrayList<>(n);
            int newCorrect = -1;
            for (int i = 0; i < n; i++) {
                int src = order.get(i);
                shuffled.add(original.get(src));
                if (src == q.getCorrectIndex()) newCorrect = i;
            }
            return new Question(q.getText(), shuffled, newCorrect,
                                q.getCategory(), q.getDifficulty());
        }
    }

    // =================================================================
    //  UI: QuizUI
    // =================================================================

    static class QuizUI {
        private static final int MAX_QUESTIONS = 20;

        private final Scanner scanner;
        private final QuizService service;

        QuizUI(QuizService service) {
            this.service = service;
            this.scanner = new Scanner(System.in);
        }

        void start() {
            printBanner();
            boolean running = true;

            while (running) {
                try {
                    Category category = promptCategory();
                    Difficulty difficulty = promptDifficulty();
                    int numQuestions = promptNumberOfQuestions(category, difficulty);

                    List<Question> quiz = service.buildQuiz(category, difficulty, numQuestions);
                    int score = runQuiz(quiz);
                    showResults(score, quiz.size());

                    running = promptPlayAgain();

                } catch (InvalidInputException e) {
                    System.out.println("[!] " + e.getMessage());
                } catch (NoSuchElementException e) {
                    System.out.println("\nInput stream closed. Exiting.");
                    running = false;
                }
            }
            System.out.println("\nThanks for playing! Goodbye.");
        }

        // ---------- Prompts ----------

        private Category promptCategory() {
            Category[] cats = Category.values();
            System.out.println("\n--- Select a Category ---");
            for (int i = 0; i < cats.length; i++)
                System.out.printf("  %d) %s%n", i + 1, cats[i].getDisplay());
            System.out.printf("  %d) All Categories%n", cats.length + 1);

            int choice = readIntInRange("Your choice: ", 1, cats.length + 1);
            return (choice == cats.length + 1) ? null : cats[choice - 1];
        }

        private Difficulty promptDifficulty() {
            Difficulty[] diffs = Difficulty.values();
            System.out.println("\n--- Select a Difficulty ---");
            for (int i = 0; i < diffs.length; i++)
                System.out.printf("  %d) %s%n", i + 1, diffs[i].getDisplay());
            System.out.printf("  %d) Any Difficulty%n", diffs.length + 1);

            int choice = readIntInRange("Your choice: ", 1, diffs.length + 1);
            return (choice == diffs.length + 1) ? null : diffs[choice - 1];
        }

        private int promptNumberOfQuestions(Category c, Difficulty d)
                throws InvalidInputException {

            int available = service.countAvailable(c, d);
            if (available == 0)
                throw new InvalidInputException("No questions available for the selected filters.");

            int max = Math.min(MAX_QUESTIONS, available);
            if (available < MAX_QUESTIONS)
                System.out.printf("%n(Only %d question(s) match your filters.)%n", available);

            return readIntInRange("How many questions? (1-" + max + "): ", 1, max);
        }

        private boolean promptPlayAgain() {
            while (true) {
                System.out.print("\nPlay again? (y/n): ");
                String line = scanner.nextLine().trim().toLowerCase();
                if (line.equals("y") || line.equals("yes")) return true;
                if (line.equals("n") || line.equals("no"))  return false;
                System.out.println("Please enter 'y' or 'n'.");
            }
        }

        // ---------- Quiz loop ----------

        private int runQuiz(List<Question> quiz) {
            int score = 0;
            int index = 1;

            for (Question original : quiz) {
                Question q = service.shuffleOptions(original);

                System.out.println("\n----------------------------------------");
                System.out.printf("Question %d/%d  [%s | %s]%n",
                        index++, quiz.size(),
                        q.getCategory().getDisplay(),
                        q.getDifficulty().getDisplay());
                System.out.println(q.getText());

                List<String> options = q.getOptions();
                for (int i = 0; i < options.size(); i++)
                    System.out.printf("  %d) %s%n", i + 1, options.get(i));

                int answer = readIntInRange("Your answer: ", 1, options.size());

                if (q.isCorrect(answer - 1)) {
                    System.out.println("[Correct!]");
                    score++;
                } else {
                    System.out.println("[Wrong] Correct answer: " + q.getCorrectAnswer());
                }
            }
            return score;
        }

        private void showResults(int score, int total) {
            double pct = (total == 0) ? 0.0 : (score * 100.0) / total;
            Grade grade = Grade.fromPercentage(pct);

            System.out.println("\n========================================");
            System.out.println("             QUIZ RESULTS");
            System.out.println("========================================");
            System.out.printf("Score: %d / %d (%.1f%%)%n", score, total, pct);
            System.out.println("Grade: " + grade.getMessage());
            System.out.println("========================================");
        }

        // ---------- Helpers ----------

        /** Reads an integer within [min, max], re-prompting on bad input. */
        private int readIntInRange(String prompt, int min, int max) {
            while (true) {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                try {
                    int value = Integer.parseInt(line);
                    if (value < min || value > max) {
                        System.out.printf("Please enter a number between %d and %d.%n", min, max);
                        continue;
                    }
                    return value;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a whole number.");
                }
            }
        }

        private void printBanner() {
            System.out.println("========================================");
            System.out.println("        JAVA QUIZ APPLICATION");
            System.out.println("========================================");
        }
    }

    // =================================================================
    //  MAIN
    // =================================================================

    public static void main(String[] args) {
        QuestionBank bank = new QuestionBank();
        QuizService service = new QuizService(bank);
        new QuizUI(service).start();
    }
}