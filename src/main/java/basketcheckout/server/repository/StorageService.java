package cabify.basketcheckout.server.repository;

public interface StorageService<T> {

    public T read(String id);

    public void write(T t);

    public void remove(String id);

}
