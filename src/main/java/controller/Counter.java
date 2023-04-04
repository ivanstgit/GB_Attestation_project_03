package controller;

public class Counter implements AutoCloseable {
    int counter;

    public Counter() {
        counter = 0;
    }

    public int add() {
        counter++;
        return this.counter;
    }

    public int get() throws CounterException {
        if (counter != 0) {
            throw new CounterException(
                    "работа с объектом типа счетчик была не в ресурсном try и/или ресурс остался открыт");
        }
        return this.counter;
    }

    @Override
    public void close() throws CounterException {
        if (counter != 1) {
            throw new CounterException(
                    "работа с объектом типа счетчик была не в ресурсном try и/или ресурс остался открыт");
        }
        counter--;
    }

}
