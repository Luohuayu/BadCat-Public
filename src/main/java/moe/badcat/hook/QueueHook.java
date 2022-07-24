package moe.badcat.hook;

import moe.badcat.BadCat;
import moe.badcat.utils.ReflectionUtils;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;

@SuppressWarnings("ALL")
public class QueueHook {
    private static Queue<FutureTask<?>> originQueue;

    public static void hook() {
        originQueue = (Queue<FutureTask<?>>) ReflectionUtils.getPrivateField(Minecraft.class, Minecraft.getMinecraft(), "scheduledTasks", "field_152351_aB");
        ReflectionUtils.setPrivateField(Minecraft.class, Minecraft.getMinecraft(), "scheduledTasks", "field_152351_aB", originQueue instanceof ArrayDeque ? new ArrayDequeHook((ArrayDeque<FutureTask<?>>)originQueue) : new BaseQueueHook(originQueue));
    }

    public static void unhook() {
        ReflectionUtils.setPrivateField(Minecraft.class, Minecraft.getMinecraft(), "scheduledTasks", "field_152351_aB", originQueue);
    }

    static class ArrayDequeHook extends ArrayDeque<FutureTask<?>> {
        private final ArrayDeque<FutureTask<?>> queue;

        public ArrayDequeHook(ArrayDeque<FutureTask<?>> queue) {
            this.queue = queue;
        }

        @Override
        public int size() {
            return queue.size();
        }

        @Override
        public boolean isEmpty() {
            boolean empty = queue.isEmpty();
            if (empty) {
                BadCat.getInstance().onTick();
            }
            return empty;
        }

        @Override
        public boolean contains(Object o) {
            return queue.contains(o);
        }

        @Override
        public Iterator<FutureTask<?>> iterator() {
            return queue.iterator();
        }

        @Override
        public Object[] toArray() {
            return queue.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return queue.toArray(a);
        }

        @Override
        public boolean add(FutureTask<?> futureTask) {
            return queue.add(futureTask);
        }

        @Override
        public boolean remove(Object o) {
            return queue.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return queue.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends FutureTask<?>> c) {
            return queue.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return queue.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return queue.retainAll(c);
        }

        @Override
        public void clear() {
            queue.clear();
        }

        @Override
        public boolean offer(FutureTask<?> futureTask) {
            return queue.offer(futureTask);
        }

        @Override
        public FutureTask<?> remove() {
            return queue.remove();
        }

        @Override
        public FutureTask<?> poll() {
            return queue.poll();
        }

        @Override
        public FutureTask<?> element() {
            return queue.element();
        }

        @Override
        public FutureTask<?> peek() {
            return queue.peek();
        }

        @Override
        public boolean removeIf(Predicate<? super FutureTask<?>> filter) {
            return super.removeIf(filter);
        }

        @Override
        public ArrayDeque<FutureTask<?>> clone() {
            return queue.clone();
        }

        @Override
        public boolean offerFirst(FutureTask<?> futureTask) {
            return queue.offerFirst(futureTask);
        }

        @Override
        public boolean offerLast(FutureTask<?> futureTask) {
            return queue.offerLast(futureTask);
        }

        @Override
        public boolean removeFirstOccurrence(Object o) {
            return queue.removeFirstOccurrence(o);
        }

        @Override
        public boolean removeLastOccurrence(Object o) {
            return queue.removeLastOccurrence(o);
        }

        @Override
        public FutureTask<?> getFirst() {
            return queue.getFirst();
        }

        @Override
        public FutureTask<?> getLast() {
            return queue.getLast();
        }

        @Override
        public FutureTask<?> peekFirst() {
            return queue.peekFirst();
        }

        @Override
        public FutureTask<?> peekLast() {
            return queue.peekLast();
        }

        @Override
        public FutureTask<?> pollFirst() {
            return queue.pollFirst();
        }

        @Override
        public FutureTask<?> pollLast() {
            return queue.pollLast();
        }

        @Override
        public FutureTask<?> pop() {
            return queue.pop();
        }

        @Override
        public FutureTask<?> removeFirst() {
            return queue.removeFirst();
        }

        @Override
        public FutureTask<?> removeLast() {
            return queue.removeLast();
        }

        @Override
        public Iterator<FutureTask<?>> descendingIterator() {
            return queue.descendingIterator();
        }

        @Override
        public Spliterator<FutureTask<?>> spliterator() {
            return queue.spliterator();
        }

        @Override
        public Stream<FutureTask<?>> parallelStream() {
            return queue.parallelStream();
        }

        @Override
        public Stream<FutureTask<?>> stream() {
            return queue.stream();
        }

        @Override
        public void addFirst(FutureTask<?> futureTask) {
            queue.addFirst(futureTask);
        }

        @Override
        public void addLast(FutureTask<?> futureTask) {
            queue.addLast(futureTask);
        }

        @Override
        public void push(FutureTask<?> futureTask) {
            queue.push(futureTask);
        }

        @Override
        public void forEach(Consumer<? super FutureTask<?>> action) {
            queue.forEach(action);
        }

        @Override
        public String toString() {
            return queue.toString();
        }

        @Override
        public boolean equals(Object o) {
            return queue.equals(o);
        }

        @Override
        public int hashCode() {
            return queue.hashCode();
        }
    }

    static class BaseQueueHook implements Queue<FutureTask<?>>, Serializable {
        private final Queue<FutureTask<?>> queue;

        BaseQueueHook(Queue<FutureTask<?>> queue) {
            this.queue = queue;
        }

        @Override
        public int size() {
            return queue.size();
        }

        @Override
        public boolean isEmpty() {
            boolean empty = queue.isEmpty();
            if (empty) {
                BadCat.getInstance().onTick();
            }
            return empty;
        }

        @Override
        public boolean contains(Object o) {
            return queue.contains(o);
        }

        @Override
        public Iterator<FutureTask<?>> iterator() {
            return queue.iterator();
        }

        @Override
        public Object[] toArray() {
            return queue.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return queue.toArray(a);
        }

        @Override
        public boolean add(FutureTask<?> futureTask) {
            return queue.add(futureTask);
        }

        @Override
        public boolean remove(Object o) {
            return queue.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return queue.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends FutureTask<?>> c) {
            return queue.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return queue.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return queue.retainAll(c);
        }

        @Override
        public void clear() {
            queue.clear();
        }

        @Override
        public boolean offer(FutureTask<?> futureTask) {
            return queue.offer(futureTask);
        }

        @Override
        public FutureTask<?> remove() {
            return queue.remove();
        }

        @Override
        public FutureTask<?> poll() {
            return queue.poll();
        }

        @Override
        public FutureTask<?> element() {
            return queue.element();
        }

        @Override
        public FutureTask<?> peek() {
            return queue.peek();
        }

        @Override
        public String toString() {
            return queue.toString();
        }

        @Override
        public boolean equals(Object o) {
            return queue.equals(o);
        }

        @Override
        public int hashCode() {
            return queue.hashCode();
        }
    }
}
