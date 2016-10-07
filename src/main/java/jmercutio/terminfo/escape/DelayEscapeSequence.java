package jmercutio.terminfo.escape;

public class DelayEscapeSequence {

	private final int delayMillis;

	public DelayEscapeSequence(int delayMillis) {
		super();
		this.delayMillis = delayMillis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + delayMillis;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DelayEscapeSequence other = (DelayEscapeSequence) obj;
		if (delayMillis != other.delayMillis)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "$<" + delayMillis + ">";
	}
}
