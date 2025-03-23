# Sport Ticker Backend

A backend service for managing sports matches, players, teams, and live match events. Built with Quarkus, GraphQL, Kafka, and Panache ORM.

The data is exposed via GraphQL APIs for easy data fetching. This allows the data consumer to choose what data to get from a certain entity and its relations to prevent overfetching. 

At first this project was meant to be using Kafka aswell for managing real time Event updates. But since GraphQL provides this Functionality out of the box using subscriptions i decided to omit a dedicated service. When creating Match Events the new event is still broadcasted on a Kakfa Channel. This could be used for a future Dashboard for example that shows real time updates.

## Features

- Manage teams and players
- Create matches and track scores and status
- Add match events (goals, cards, substitutions)
- Subscribe to live match events via GraphQL subscriptions
- Broadcast match events through Kafka
- Persist all data using Hibernate and Panache

## Technology Stack

- Quarkus (Java framework)
- SmallRye GraphQL (API layer)
- Panache ORM (Hibernate-based data access)
- Apache Kafka (event messaging)
- PostgreSQL or H2 (relational database)
- Maven (build tool)

## Example GraphQL Queries

These Examples can easily be tested using the GraphQL features of the Postman client.

### Adding some Teams, Players and a Match
```graphql
mutation {
    yb: createTeam(name: "BSC Young Boys"){id}
    fcb: createTeam(name: "FC Basel"){id}

    player1: createPlayer(name: "Player 1", teamId: 1){id}
    player2: createPlayer(name: "Player 2", teamId: 2){id}

    match: createMatch(teamAId: 1, teamBId: 2){id}
}
```

### Read Matches with Team and Player Data
```graphql
query {
    matches {
        id,
        scoreA,
        scoreB
        teamA{
            name,
            players{
                id,
                name
            }
        },
        teamB{
            name,
            players{
                id,
                name
            }
        }
    }
}
```

### Add a Match Event
```graphql
mutation {
    addMatchEvent(matchId: 1, type: GOAL, playerId: 1){
        id
    }
}
```

### Subscribe to Match event for certain Mathc (Requires WebSocket Access)