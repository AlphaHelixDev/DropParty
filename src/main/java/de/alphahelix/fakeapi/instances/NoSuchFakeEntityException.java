package de.alphahelix.fakeapi.instances;

public class NoSuchFakeEntityException extends Exception {
    public NoSuchFakeEntityException() {
        super("There is no such FakeEntity");
    }
}
