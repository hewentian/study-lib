package iterator;

public class ListImpl<E> implements List<E> {
    private Object[] elementData;
    private int size;

    public ListImpl() {
        elementData = new Object[10];
    }

    @Override
    public boolean add(E e) {
        elementData[size++] = e;
        return true;
    }

    @Override
    public E get(int index) {
        return (E) elementData[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        private int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            E e = get(index++);
            return e;
        }
    }
}
