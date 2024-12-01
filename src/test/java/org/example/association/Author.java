package org.example.association;

public class Author {
    private String name;

    public Author(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void writeBook(Book book) { // Association relationship
        System.out.println(name + " is writing the book: " + book.getTitle());
    }
}
