package uk.gov.homeoffice.toolkit.concurrent;

public interface Handler<T> {

    void result(T result);
}
