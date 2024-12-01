package org.example.association;

public class Book {
    private String title;

    public Book(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void readBy(Author author) { // Association relationship
        System.out.println(title + " is being read by " + author.getName());
    }
}
