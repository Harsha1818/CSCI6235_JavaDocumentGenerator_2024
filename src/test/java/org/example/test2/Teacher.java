package org.example.test2;

public class Teacher extends Person { // Class (C) extending Abstract Class (A)
    public Teacher(String name) {
        super(name);
    }

    @Override
    public void performDuty() {
        System.out.println(name + " is teaching the class.");
    }

    public void gradeExams() {
        System.out.println(name + " is grading exams.");
    }
}
