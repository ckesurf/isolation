

main:
	javac Board.java Pair.java TimerBoard.java

play: 
	java Board

clean:
	rm -f *.class
