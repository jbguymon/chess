# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```



Link to the sequence diagram created for P2:

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7gyTJTspKCqfofw7HOXl3kuwpihKboynKZbvBWjwwMZAKYCqwYagAchA8Xarq+pukaSVNEF6kwFaNo3mFDpJr6zpdjAPZ9tuHmWf6zLTJe0BIAAXig65WjGcaFFptXwMgqYwOmACMBE5qoebzNBRYlq6RGGNAyWlTsMDZLtaAgNA3goOADb0VVvLhZZ1kxVu7mCsO-L1IecjxM+8TTvpXIXQuAoRbUcAToY70ygAZptbw-fuIH1fUIN3fV7Y6aWjnihkqgAZgyMwxJYGEV9NFkWMFFUfWJGoQ2o3JhN2EwLh+GjPjCXEaR3wk4hZOs3RTaeN4fj+F4KDoDEcSJILwuOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcM7NIeh8KVMjLzE5epPIZCZmwhh10ujZQlSw59uns5aiuS1D23k9MCMmA8PyOepsc2goWXTVlTLlF65vHlerGoHevpeqMDZblOqx7rwu2fYjLruKUNXXVnbdr2-b3WNcuSp1erdX1A3RigsYKfrmE02AaZOLNjPzYtBZjCt0D1Cn0dp-qep7RnaC7QKWc5-tZ1No9i42w1fvAO7nmh-Sdv2FLU6ryHv33sK0goNw6IBpeYPeDMRjaHoBj5zVy-F81ZcfuZ-qS87-4IIBH-wm1MCmlqjW2piUNudM8JZm5gxXmzF-AonXP4bA4oNT8TRDAAA4kqDQMtAHy0wSrdW9glQ63jugZu78rZ4wnqZLG-9Zaw29jghyaJsFzBdiSdeVJPZbx9qvAO8Eg4H2hv9SOqd8pxyEQnVUWUcrD0kRPbes886Lz+s-RqJd174MrhRGu-Vyr10bvGKmlRRLt07tmfkPdlrFgHsneRWoR4wDHgKJRe0Z4GPFPPBiaicZWVtqvbhMheGjm9kydhKB0E5AxCI8K4dIrig4G4vsxpwBIBoMwnMj8l6FwCSvYswBLDRLAAAHhIXMHk5RtGG3-vUSJJT0aY2xuXEBeMKlqALA0cYHSACS0gCzTXCMEQIIJNjxF1CgQqkFFjfGSKANU0yDJjG+B0zKSpZkXBgJ0YBuMW7gJwlAyePScFdJOXMfpgzhmjOWOMyZSylorJBPMkAiz1p7FWUqdZcxNnbJgYxPmAQOAAHY3BOBQE4GIEZghwC4gANngEDLBFYiit0YbjeWrQOjENIVXSiQcsxrKVLsxMtTqHG1oTBZYRLlloRaXk6yHTVAYkBkeFAkS3KIw3r9HyvtL7aEEfipCcSw5ClFEkiR6dyGFETnIyV+olGeNzoUPxrSi6aNflynRHU9FQF6gYwaDdhqULAZNGac1rH5lsatBx8qXETLcdKqeyivHIX+aqjRQSy48OqlvF66JIkYhpd9VVCS4ZrmRZUhGnYkZ1MRWyyJTTf70Oof43SYw+kDPqEMkZJLQFmNbochm5yUCXOzdc-5cD+aWFPrZTYIskAJDADWvsEB60ACkIDikjYYfwLy1SovAeimomLmQyR6B0sh0j0BZmwAgIplA4AQFslANYma80GweOSmA4xKVEznQuqAS6V1fBBJmuh9KOz5IZCwzt4pE0oEJK7TlMbuXeXCXyl8AqJ4isXGG8RCipXTplbI21gGFVOqVftHJ6iGW2yaqXLVeT2p4v0XXIaTdTHjQOVNDuFrcxWsLHY0sQ8nGSNcVIoVwsPFCRUW6xsvjQn+Osl6rlPrN5hJ9oGzNv6-phpgJHNxWTDCg2vsJ-paVQMpw6THfUMn+kwHBj4BeTG1XXo1Yh19Oi9DrhRE+nI5ViMmoLTh81XdLVLSIza6TFZnGZsU74TgPiYPMdtpE4J7leVNHnTWqAGID2+ePdAUksr4DLs2iaM0NZwwjSQ1e6y3nD01Kvf6SLwZzQuJi4YjDJi2n7LNRmfDC1CN92I-UOA8QIAGEnkF2A48qwZdrPWf5b84MNQQ8EtR9QQwcCnBPVSPnF3hdXeJ6QvGj7hqjlqSAwnZMwBsCdJAoNUDrkOiu5LRsYB3rQA+jGyaWk6JGBu-LtN6bQIYwC+BXgikNqbdd+UiBgywGANgOdhA8gFBgIO8ww7JKK2VqrdWxhKFkq-C8S2X5h3WSO510Jz1uB4B5LElz-0ZCnyZIYE0CB4q2lU-xk+Z9MepKZSj-H6Pz5Y8o2bDQrWt1g53RDiyh3jtjXMZA4t-ygA
