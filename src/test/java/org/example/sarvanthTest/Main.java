package org.example.sarvanthTest;

public class Main {
    public static <Staff> void main(String[] args) {
        Library library = new Library("City Library");

        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee");

        library.addBook(book1);
        library.addBook(book2);

        Member member1 = new Member("Alice");
        Staff staff1 = new Staff("Bob", "Librarian");

        library.registerMember(member1);
        library.loanBook(book1, member1); // Loaning a book to a member
        staff1.assistMember(member1); // Staff assisting member
    }
}
