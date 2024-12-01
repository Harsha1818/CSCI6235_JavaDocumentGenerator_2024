package org.example.test2;

public class Student implements ExamTaker { // Class (C) implementing interface (I)
    private String name;
    private int grade;

    public Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public void takeExam() { // Realization: Implements a method from ExamTaker
        System.out.println(name + " is taking the exam.");
    }
}
