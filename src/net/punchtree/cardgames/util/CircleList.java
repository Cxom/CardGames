package net.punchtree.cardgames.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CircleList<E> implements Iterable<E> {

	List<E> list;
	int iterationPosition = 0;

	public CircleList(Collection<E> list) {
		this.list = new ArrayList<>(list);
	}
	
	public CircleList(E[] es) {
		list = Arrays.asList(es);
	}
	
	public CircleList() {
		list = new ArrayList<>();
	}
	
	public int size() {
		return list.size();
	}
	
	public void add(E element) {
		add(list.size(), element);
	}
	
	public void add(int index, E element) {
		if (index <= this.iterationPosition) {
			++this.iterationPosition;
		}
		list.add(index, element);
	}
	
	public void remove(E element) {
		int index = list.indexOf(element);
		if (index != -1) {
			remove(index);
		}
	}
	
	public void remove(int index) {
		if (index <= iterationPosition) {
			--iterationPosition;
		}
		list.remove(index);
	}
	
	public E next() {
		if (isEmpty()) {
			return null;
		}
		++iterationPosition;
		if (iterationPosition >= list.size()) {
			iterationPosition = 0;
		}
		return list.get(iterationPosition);
	}
	
	public E current() {
		if (isEmpty()) {
			return null;
		}
		return list.get(iterationPosition);
	}
	
	public E previous() {
		if (isEmpty()) {
			return null;
		}
		--iterationPosition;
		if (iterationPosition == -1) {
			iterationPosition = list.size() - 1;
		}
		return list.get(iterationPosition);
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return new CircleIterator();
	}
	
	class CircleIterator implements Iterator<E> { 
	      
		private final int initialIterationPosition;
		private int finalIterationPosition;
		private int currentIterationPosition;
		
	    CircleIterator() { 
	        initialIterationPosition = CircleList.this.iterationPosition;
	        
	        currentIterationPosition = initialIterationPosition;
	        finalIterationPosition = initialIterationPosition + list.size();
	    } 
	      
	    // Checks if the next element exists 
	    public boolean hasNext() { 
	    	return (currentIterationPosition < finalIterationPosition);
	    } 
	      
	    // moves the cursor/iterator to next element 
	    public E next() {
	    	if (!hasNext()) {
	    		throw new IndexOutOfBoundsException();
	    	}
	    	
	    	E retval = list.get(currentIterationPosition % list.size());
	    	++currentIterationPosition;
	    	return retval;
	    } 
	      
	    public void remove() { 
	        if (isEmpty()) {
	        	return;
	        }
        	list.remove(currentIterationPosition % list.size());
        	--finalIterationPosition;
	    } 
	}
	
}
