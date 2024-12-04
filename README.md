# ECM2414-Card-Game

## Overview

This project is a card game implemented in Java. The game involves multiple players drawing and discarding cards from decks until a player wins by having all cards in their hand with the same value. The game is designed to be thread-safe and uses a singleton pattern for the `CardGame` class.

## Project Structure

```

ECM2414-Card-Game/
├── src/
│ ├── Card.java
│ ├── CardGame.java
│ ├── Deck.java
│ └── Player.java
├── test/
│ ├── CardGameTest.java
│ ├── DeckTest.java
│ ├── CardTest.java
│ └── PlayerTest.java
├── README.md
└── pack.txt

```

## Requirements

- Java Development Kit (JDK) 8 or higher
- JUnit 5 for running the test suite

## Setup

1. **Clone the repository:**

   ```sh
   git clone https://github.com/W3r5l3y/ECM2414-Card-Game.git
   cd ECM2414-Card-Game
   ```

````

2. **Compile the source code:**

   ```sh
   javac -d bin .\src*.java
   ```

3. **Run the game:**

   ```sh
   java -cp bin CardGame
   ```

## Running the Test Suite

1. **Compile the test classes:**

   ```sh
   javac -cp lib/junit-platform-console-standalone-1.11.3.jar:bin -d bin test/*.java
   ```

2. **Run the tests:**

   ```sh
   java -jar lib/junit-platform-console-standalone-1.11.3.jar -cp bin --scan-class-path
   ```

## Test Suite

The test suite includes the following test classes:

- `CardGameTest.java`: Tests for the `CardGame` class.
- `DeckTest.java`: Tests for the `Deck` class.
- `CardTest.java`: Tests for the `Card` class.
- `PlayerTest.java`: Tests for the `Player` class.
````
