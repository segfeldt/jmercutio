package jmercutio.util;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory<A> {

	private final Map<A, A> flyweights = new HashMap<>();

	private int reuses = 0;

	public A get(A instance) {
		A flyweight;
		
		if (flyweights.containsKey(instance)) {
			flyweight = flyweights.get(instance);
			++reuses;
		} else {
			flyweights.put(instance, instance);
			flyweight = instance;
		}

		return flyweight;
	}

	public int size() {
		return flyweights.size();
	}

	/**
	 * 
	 * @return number of times a flyweight has been reused, purely for statistics to see how beneficial flyweight is (without
	 * taking into consideration the size of reused objects, of course).
	 */
	public int reuses() {
		return reuses;
	}

	@Override
	public String toString() {
		return "FlyweightFactory[size " + size() + ", reuses=" + reuses() + "]";
	}
}
