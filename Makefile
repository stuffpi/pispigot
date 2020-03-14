all: PiSpigot.class

PiSpigot.class: PiSpigot.java
	javac PiSpigot.java

run: PiSpigot.class
	java PiSpigot 100

clean:
	rm -f *.class
