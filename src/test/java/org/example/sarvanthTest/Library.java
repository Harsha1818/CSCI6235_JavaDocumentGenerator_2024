package org.example.sarvanthTest;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private String name;
    private List<Book> books; // Aggregation: Library has many Books
    private List<Member> members; // Aggregation: Library has many Members

    public Library(String name) {
        this.name = name;
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void registerMember(Member member) {
        members.add(member);
    }

    public void loanBook(Book book, Member member) {
        if (books.contains(book)) {
            Loan loan = new Loan(book, member); // Composition: Loan is created for Book and Member
            System.out.println(member.getName() + " has borrowed " + book.getTitle());
        } else {
            System.out.println("Book not available.");
        }
    }
}
