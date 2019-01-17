For correct build and run the program Task Manager from Intellig Idea:

1. Open 'Maven Projects' on the right side of Idea.
2. Lifecycle --> clean
3. Lifecycle --> compile
4. Plugins --> assembly --> assembly:single.
5. Go to target directory.
6. Create run.bat and write down into the file next lines:
java -classpath TaskManager-1.0-SNAPSHOT-jar-with-dependencies.jar ua.edu.sumdu.j2se.Yermolenko.tasks.Main
pause
7. Save and quit the file.


For start the program from console by using maven:

1. Download the project from a repository: https://github.com/EEM86/Lab01
2. Unzip archive
3. Start a console in the root directory where *.pom file is situated
4. Write down:
mvn exec:java -Dexec.mainClass="ua.edu.sumdu.j2se.Yermolenko.tasks.Main"
5. Program must start.