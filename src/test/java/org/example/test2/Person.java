package org.example.test2;
import java.lang.String;
import java.math.BigDecimal;
public abstract class Person { // Abstract Class (A)
    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void performDuty(); // Abstract method
}
