runFDTests: FrontendDeveloperTests.java
	javac -cp ../junit5.jar:. FrontendDeveloperTests.java
	java -jar ../junit5.jar --class-path=. --select-class=FrontendDeveloperTests

clean:
	rm *.class

runBDTests:
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests

runApp: App.java
	javac -cp ../junit5.jar:. App.java
	java -jar ../junit5.jar --class-path=. --select-class=App
