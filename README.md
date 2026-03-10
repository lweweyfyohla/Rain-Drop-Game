# RainDrop Game

A simple 2D game built with JavaFX and FXGL game engine where you catch falling raindrops with a bucket!

## About

RainDrop is a classic arcade game where the player controls a bucket to catch falling raindrops. The game progressively gets more challenging as the speed increases over time. Miss too many drops and it's game over!

> **AI Disclosure:** This README, the code debugging process, and the Maven configuration (`pom.xml`) were assisted by **Claude Sonnet** (Anthropic). AI was used to help structure the project setup, troubleshoot runtime issues, and write this documentation.

## Features

- **Simple Controls**: Use arrow keys to move the bucket left and right
- **Progressive Difficulty**: The game speed increases over time to keep you challenged
- **Score System**: Earn points for each raindrop caught, lose points when you miss
- **Game Over Condition**: Miss 3 raindrops when your score is at 0 and the game ends
- **Visual & Audio Feedback**: Enjoy sound effects when catching raindrops
- **Clean UI**: Simple, intuitive interface showing your current score

## Game Rules

1. Control the bucket using the **LEFT** and **RIGHT** arrow keys
2. Catch falling raindrops to increase your score (+1 per catch)
3. Missing a raindrop decreases your score (-1) if your score is above 0
4. If you miss 3 raindrops while your score is 0, the game is over
5. The game gets progressively faster, increasing the challenge!

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 11 or higher**
  - [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **Apache Maven 3.6 or higher**
  - [Download Maven](https://maven.apache.org/download.cgi)

To verify your installations:
```bash
java -version
mvn -version
```

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/lweweyfyohla/Rain-Drop-Game.git
   cd Rain-Drop-Game
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

## How to Run

To start the game, use Maven's exec plugin:

```bash
mvn exec:java
```

Alternatively, you can compile and run manually:

```bash
# Compile the project
mvn compile

# Run the game
mvn exec:java -Dexec.mainClass="raindrop.game.RainDrop"
```

## Building the Project

To build a JAR file of the project:

```bash
mvn package
```

The compiled JAR will be located in the `target/` directory.

## Technology Stack

- **Java 11**: Programming language
- **JavaFX 17.0.9**: UI framework
- **FXGL 17.2**: Game engine for JavaFX

## Project Structure

```
Rain-Drop-Game/
├── src/
│   └── main/
│       ├── java/
│       │   └── raindrop.game/
│       │       ├── RainDrop.java          # Main game class
│       │       ├── DropMovement.java      # Drop movement component
│       │       ├── DropMissedEvent.java   # Custom event for missed drops
│       │       └── GameObjectType.java    # Game object type definitions
│       └── resources/
│              └── assets/  
│                      └── sounds/
│                              └── waterdrip.wav
│                      └── textures/
│                              └── bucket.png
│                              └── raindrop.png
│                              └── goldenDrop.png
│
├── system/                                                      # FXGL system data
├── pom.xml                                                      # Maven configuration
└── README.md                                                    # This file
```

## Assets
The game uses the following assets:
- `bucket.png` - Player bucket sprite
- `raindrop.png` - Raindrop sprite
- `goldenDrop.png` - Golden raindrop sprite
- `waterdrip.wav` - Sound effect for catching raindrops

## Screenshot
### Start the Game

<img width="596" height="464" alt="image" src="https://github.com/user-attachments/assets/0229176b-330b-4089-92b4-f0dcda4d269e" />

### Game Play

<img width="602" height="472" alt="image" src="https://github.com/user-attachments/assets/d2f0c1a8-f514-412f-81d3-a508849ce9bc" />

### End Game

<img width="593" height="469" alt="image" src="https://github.com/user-attachments/assets/a953439d-7712-4a04-bd3f-b3d599707db9" />

## Resources
### Asset Credits
- `bucket.png` - from Nano Banana
- `raindrop.png` - from Nano Banana
- `goldenDrop.png` - from Nano Banana
- `waterdrip.wav` - from Pixabay

### Acknowledgments
This project was developed with assistance from **Claude Sonnet** (Anthropic AI):
- **README.md** - written with AI assistance
- **Code Debugging** - runtime issues diagnosed and fixed with AI assistance
- **Maven Configuration (`pom.xml`)** - project setup and dependency configuration assisted by AI

## License
This project is open source and available for educational purposes.

## Contributing
Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

## Development
To set up the development environment:
1. Import the project into your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Ensure the Maven project is properly imported
3. Run `mvn clean install` to download all dependencies
4. Start coding!
