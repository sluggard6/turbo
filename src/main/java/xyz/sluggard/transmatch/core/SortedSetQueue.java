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
	}

	public SortedSetQueue(Comparator<? super E> comparator) {
		super(comparator);
	}

	public String toString() {
		if(isEmpty()) {
			return "SortedSetQueue=[]";
		}
		StringBuilder builder = new StringBuilder("SortedSetQueue=[");
		this.forEach(e -> {
			builder.append(e).append(",");
		});
		return builder.substring(0, builder.length() - 1)+"]";
		
	}
}
