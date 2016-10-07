package jmercutio.terminfo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

public class TerminalNames implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;

	private final Set<String> aliases;
	
	private final String description;

	public TerminalNames(String name) {
		this(name, null, name);
	}
	
	public TerminalNames(String name, Set<String> aliases, String description) {
		super();
		this.name = name;
		if (aliases == null || aliases.isEmpty()) {
			this.aliases = Collections.emptySet();
		} else {
			this.aliases = aliases;
		}
		if (description == null || description.isEmpty() || description.equals(name)) {
			this.description = name;
		} else {
			this.description = description;
		}
	}

	public String getName() {
		return name;
	}

	public Set<String> getAliases() {
		return aliases;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(name);
		for (String alias: aliases) {
			str.append('|');
			str.append(alias);
		}
		// don't need to use equals here, see constructor:
		if (description != name) {
			str.append('|');
			str.append(description);
		}
		return str.toString();
	}
}
