# Software Engineering Project - Mesos

University project for the Software Engineering course. Mesos is a Java implementation of the Mesos card game by Cranio Creations. It is built with a Client/Server architecture (TCP Socket or RMI, selectable by the user), a TUI and a GUI for the user to interact with and a DB, server side, to keep trace of the info of the users who finished a game and their scores.

The project follows the full requirements set for grade 30L, including **three advanced features(FA)**: match leaderboard stored on a DB, support for multiple concurrent matches, and persistence.


## Prerequisites
- **Required:** Java 25

If you have another version of Java as your default follow the steps at the end of the Usage section.


## Usage
Open the JAR files from the `deliverables` folder.

### Server:
The server JAR is located in `/deliverables/MesosServer_jar`.

**Run from your terminal (Linux/macOS/Windows):**
```bash
java -jar Mesos.jar 
```

You'll be prompted to choose a port ID once the server starts.

### Client:
The client JAR is located in `/deliverables/MesosClient_jar`.

**Run from your terminal (Linux/macOS/Windows):**
```bash
java -jar Mesos.jar 
```

You'll be prompted to enter the port ID of the server you want to connect to.

>**Note:** make sure the Server is running and you have its port ID before starting the Client.

### If your default Java version is lower than 25:

1. Download and install Java 25 if you don't already have it.

2. Find the folder where Java 25 was installed (e.g. on Windows this is typically C:\Program Files\Java\jdk-25)

3. Inside that folder, locate the bin subfolder - this contains the java.exe (Windows) or java (Linux/macOS) executable.

4. Use the full path to that executable instead of java when running the JAR. 

For example, on Windows:
```bash
"C:\Program Files\Java\jdk-25\bin\java.exe" -jar Mesos.jar
```


