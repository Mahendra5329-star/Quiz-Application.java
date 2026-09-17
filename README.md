Quiz Application
A console-based multiple-choice quiz application built in Java, featuring categories, difficulty levels, randomized questions, scoring, and grade reporting.

📋 Overview
The Quiz Application is an interactive, menu-driven Java program that lets users test their knowledge across multiple categories and difficulty levels. Questions and answer options are shuffled on every run, and a final score with a letter grade is displayed at the end.

✨ Features
Multiple Categories – Java, General Knowledge, Science, and History.
Three Difficulty Levels – Easy, Medium, and Hard.
Filter Options – Choose a specific category/difficulty or select "All/Any".
Randomized Questions – Question order is shuffled each session.
Shuffled Answer Options – Options are permuted per question so the correct answer isn't always in the same position.
Configurable Length – Choose how many questions to answer (up to 20 per quiz).
Scoring & Grading – Instant feedback per question, plus a final percentage and letter grade (A–F).
Input Validation – All prompts re-prompt on invalid input.
Exception Handling – Custom InvalidInputException for domain errors; graceful exit on closed input stream.
Play Again Loop – Re-run quizzes without restarting the program.

🏗️ Architecture
The application is organized into several nested classes and enums inside QuizApplication:

Component	Responsibility
Category (enum)	Available quiz categories with display names.
Difficulty (enum)	Easy / Medium / Hard with display names.
Grade (enum)	Letter grades (A–F) with minimum percentages and messages.
InvalidInputException	Custom checked exception for invalid quiz configurations.
Question	Immutable model of a single question (text, options, correct index, category, difficulty).
QuestionBank	Loads and stores all questions; provides filtering and counting.
QuizService	Business logic: builds randomized quizzes and shuffles answer options.
QuizUI	Console interface: prompts, quiz loop, results, input helpers.
QuizApplication	Entry point (main).
Design Principles Demonstrated
Encapsulation – Private fields with controlled accessors.
Immutability – Question uses List.copyOf and has no setters.
Enums with Behavior – Each enum carries display text or grading logic.
Separation of Concerns – Model (Question), data (QuestionBank), logic (QuizService), and UI (QuizUI) are distinct.
Defensive Programming – Constructor validation and safe shuffling via index permutation.

🧠 How Option Shuffling Works
Instead of shuffling the option strings directly (which could mis-map the correct answer when duplicates exist), the app shuffles an index permutation and rebuilds the option list while tracking the new position of the correct answer. This guarantees the correct answer is always correctly identified.

✅ Validation & Error Handling
Scenario	Behavior
Non-numeric menu input	Re-prompts with a clear message.
Out-of-range number	Re-prompts with the valid range.
No questions match filters	Throws InvalidInputException with a friendly message.
Non-positive question count	Rejected by validation.
Blank question text / <2 options / bad correct index	Rejected at construction time.
Closed input stream	Cleanly exits with a goodbye message.
🚀 Getting Started
Prerequisites
Java Development Kit (JDK) 11 or higher (uses List.of, List.copyOf, isBlank).

A terminal or command prompt.
Compilation
Save the source as QuizApplication.java, then compile:

bash
javac QuizApplication.java
Running the Application
bash
java QuizApplication

🖥️ Usage
Main Flow
Select a Category – Pick 1–4 or "All Categories".
Select a Difficulty – Pick 1–3 or "Any Difficulty".
Choose Number of Questions – Enter a value from 1 up to 20 (or fewer if limited by filters).
Answer Questions – Enter the number of your chosen option.
View Results – See score, percentage, and grade.
Play Again – Type y or n.

Example Session
text
========================================
        JAVA QUIZ APPLICATION
========================================

--- Select a Category ---
  1) Java
  2) General Knowledge
  3) Science
  4) History
  5) All Categories
Your choice: 1

--- Select a Difficulty ---
  1) Easy
  2) Medium
  3) Hard
  4) Any Difficulty
Your choice: 1

How many questions? (1-4): 3
----------------------------------------
Question 1/3  [Java | Easy]
Which keyword is used to inherit a class in Java?
  1) implements
  2) super
  3) extends
  4) inherits
Your answer: 3
[Correct!]
...

========================================
             QUIZ RESULTS
========================================
Score: 2 / 3 (66.7%)
Grade: D - Keep practicing.
========================================
Play again? (y/n): n
Thanks for playing! Goodbye.

📊 Grading Scale
Grade	Minimum Percentage	Message
A	90%	A - Excellent!
B	80%	B - Great job!
C	70%	C - Good effort.
D	60%	D - Keep practicing.
F	0%	F - Try again!

📁 Project Structure
text
QuizApplication.java
├── enum Category
├── enum Difficulty
├── enum Grade
├── class InvalidInputException
├── class Question
├── class QuestionBank
├── class QuizService
├── class QuizUI
└── public class QuizApplication (main)

🔧 Possible Enhancements
Persist high scores or user history to a file/database.

Add a timer per question or per quiz.
Support true/false and multi-select question types.
Add explanations shown after wrong answers.
Load questions from an external JSON/CSV file.
Implement a GUI (JavaFX or Swing).
Add unit tests with JUnit.

📄 License
This project is provided as-is for educational purposes. Feel free to use, modify, and distribute it as needed.

👤 Author
Created as a demonstration of Java OOP, collections, enums, and console application development.

