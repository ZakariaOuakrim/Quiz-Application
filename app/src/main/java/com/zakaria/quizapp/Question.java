package com.zakaria.quizapp;

public class Question {
    private String questionText;
    private String[] options;
    private String correctAnswer;
    private int imageResource; // can be 0 if no image
    private int questionNumber;

    public Question(int questionNumber, String questionText, String[] options, String correctAnswer, int imageResource) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.imageResource = imageResource;
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public int getImageResource() { return imageResource; }
    public int getQuestionNumber() { return questionNumber; }
}
