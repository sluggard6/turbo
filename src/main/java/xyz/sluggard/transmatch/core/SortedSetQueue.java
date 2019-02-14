package xyz.sluggard.transmatch.core;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.ConcurrentSkipListSet;

public class SortedSetQueue<E> extends ConcurrentSkipListSet<E> implements Queue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2388470313905706775L;

	@Override
	public boolean offer(E e) {
		return add(e);
	}

	@Override
	public E remove() {
		E ret = first();
		remove(ret);
		return ret;
	}

	@Override
	public E poll() {
		return pollFirst();
	}

	@Override
	public E element() {
		return first();
	}

	@Override
	public E peek() {
		if(isEmpty()) return null;
		return element();
	}

	public SortedSetQueue() {
		// TODO Auto-generated constructor stub
	}

	public SortedSetQueue(Comparator<? super E> comparator) {
		super(comparator);
	}

}
