package com.example.quizapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {
    String questionText;
    String[] options;
    int correctIndex;

    public Question(String q, String[] opts, int correct) {
        questionText = q;
        options = opts;
        correctIndex = correct;
    }

    public static ArrayList<Question> getQuestions() {
        ArrayList<Question> list = new ArrayList<>();
        list.add(new Question("What is the capital of Japan?", new String[]{"Seoul", "Beijing", "Tokyo", "Bangkok"}, 2));
        list.add(new Question("Which planet is known as Red Planet?", new String[]{"Venus", "Mars", "Jupiter", "Saturn"}, 1));
        list.add(new Question("Who wrote Hamlet?", new String[]{"Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"}, 1));
        list.add(new Question("What is the largest ocean?", new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, 3));
        list.add(new Question("2 + 2 * 2 = ?", new String[]{"6", "8", "4", "10"}, 0));
        list.add(new Question("Which element has symbol 'O'?", new String[]{"Gold", "Oxygen", "Osmium", "Oganesson"}, 1));
        list.add(new Question("Year World War II ended?", new String[]{"1945", "1939", "1942", "1950"}, 0));
        list.add(new Question("Fastest land animal?", new String[]{"Lion", "Cheetah", "Tiger", "Leopard"}, 1));
        list.add(new Question("How many continents are there?", new String[]{"5", "6", "7", "8"}, 2));
        list.add(new Question("Which is a programming language?", new String[]{"Python", "Snake", "Lizard", "Cobra"}, 0));
        list.add(new Question("What does CPU stand for?", new String[]{"Central Process Unit", "Central Processing Unit", "Computer Personal Unit", "Central Processor Unit"}, 1));
        list.add(new Question("Largest planet in solar system?", new String[]{"Earth", "Saturn", "Jupiter", "Neptune"}, 2));

        Collections.shuffle(list); // shuffled each time
        return list;
    }
}
