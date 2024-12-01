package org.example.test2;

import java.util.List;

public class School { // Class (C)
    private String name;
    private Address address; // Composition: School "has-a" Address
    private List<Student> students; // Aggregation: School "contains" Students
    private Teacher teacher; // Association: School "works with" a Teacher

    public School(String name, Address address, List<Student> students) {
        this.name = name;
        this.address = address;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void assignTeacher(Teacher teacher) { // Association relationship with Teacher
        this.teacher = teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }
}
