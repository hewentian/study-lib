package iterator;

public interface List<E> {
    Iterator<E> iterator();

    int size();

    E get(int index);

    boolean add(E e);
}
