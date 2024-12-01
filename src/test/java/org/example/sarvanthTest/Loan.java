package org.example.sarvanthTest;

public class Loan { // Composition: Loan cannot exist without a Book and a Member
    private Book book;
    private Member member;

    public Loan(Book book, Member member) {
        this.book = book;
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }
}
