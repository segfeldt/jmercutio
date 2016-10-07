# jmercutio
a text-based UI for Java over ssh -- no need for native libraries

jmercutio aims to provide a complete text-based user interface library for Java. A key feature is that it serves the interface over ssh, to terminal clients like xterm and putty.

This approach has a number of advantages:

- terminal emulators are widely available
- completely circumvents Java's limited console support
- no need for native wrappers around ncurses, so your application remains portable
- minimal requirements, can be used on headless servers with low bandwidth
- security built in to the protocol
- can serve multiple clients

These properties are particularly useful for administering web applications. You can provide a secure text-based admin interface inside your firewall. This is much more secure than providing admin pages on the same HTTP/HTTPS ports as your users access your application.

In many cases, administrators will have ssh access to a server anyway. In such cases, your applications text-based interface can be restricted to localhost. Your administrators ssh onto the server as usual, then ssh to the application's local interface. No need to open up new ports!

## Status

The application is in early development. However, I have several use cases for the project, so there is a lot of determination behind it.

First milestone is terminfo support.

## FAQ

- Why the name jmercutio? This is a tenuous pun. Mercutio curses the Capulets and Montegues in Romeo and Juliet. The library has a similar purpose to curses and ncurses. However, the API is deliberately quite different, to take advantage of Java language and JDK feature.

## License

This project uses the Apache 2.0 license.

This is the same license as used for Apache SSHD, so you only have one license to worry about for both jmercutio and its main dependency.
