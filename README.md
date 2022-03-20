[comment]: <> (# Introduction to Artificial Intelligence)

# IAI Assignment 1: Book Finding

## Algorithms

Both types of search algorithms follow this steps:

- First we try to explore the hole available map. We want to find positions of the Cloak, the Book, the Cat, and Filch. 
```findAWayThroughPoints(Game.INF)``` initiates the algorithms specified in ```- Search.strategy``` field, to look for
a point that in fact aren't reachable. Therefore, algorithm, when trying to get to the point, will visit every possible
node and provide information for detecting the desired positions of the Cloak, the Book, the Cat, and Filch.
- If we found a Cloak it is logical to use it as a first destination, and then explore the map again, because some
places could've been unreachable without it: ```findAWayThroughPoints(history.CLOAK, Game.INF)```
- By this point I assume we have as much as possible information about the map.
- Now knowing that the shortest path can or can not contain the cloak we try these 
sequences of destination points: ```START->BOOK->EXIT```, ```START->CLOAK->BOOK->EXIT``` 
and ```START->BOOK->CLOAK->EXIT```. If the next destination point of a sequence is unknown or unreachable we skip it.
- Now we just need to return fastest player that won, or if none of them won - resign, because
there could not be found any way to definitely win. 

Both algorithms use the history field, they add every new player created to it to update the known information about
the environment. Then algorithms use method ```search.history.getMoves(player)``` to get all available moves that
definitely won't make current player lose.

### Backtracking

The algorithm uses standard dfs with two modifications:

- when dfs finds the destination position it stops all recursive instances and returns a player that
came to the position
- for every player dfs uses more optimal moves first (from moves that not make current agent lose),
optimality is chosen by heuristics (distance to destination point)

### A*

The algorithm uses standard A* with PriorityQueue that sort possible moves by f(move) = g(move) + h(move), where

- g(move) - number of steps done to come to this position
- h(move) - estimated number of steps to destination(manhattan distance)

## Statistics

### perception 1
|Backtracking:status | avg len | number af games|
|---|---|---|
|STARTED|0,000000|6|
|WON|10,159960|994|

|A*:status | avg len | number af games|
|---|---|---|
|STARTED|0,000000|6|
|WON|10,018109|994|


### perception 2
|Backtracking:status | avg len | number af games|
|---|---|---|
|STARTED|0,000000|16|
|WON|10,162602|984|

|A*:status | avg len | number af games|
|---|---|---|
|STARTED|0,000000|16|
|WON|10,020325|984|


the LOST row is the games where cat seen harry in the initial state the STARTED row is the games where harry could not
find any solution


5288 : -3
seed 4031 : 16
seed 4699 : 13

## Interesting case

```
[HARRY][    ][    ][    ][    ][    ][    ][    ][    ]
[    ][    ][BOOK][    ][    ][    ][    ][    ][    ]
[    ][    ][    ][    ][    ][    ][SEEN][SEEN][SEEN]
[    ][    ][    ][SEEN][SEEN][SEEN][SEEN][SEEN, CAT][SEEN]
[    ][    ][    ][SEEN][SEEN][SEEN][SEEN][SEEN][SEEN]
[    ][    ][    ][SEEN][SEEN][CAT, SEEN][SEEN][SEEN][    ]
[    ][    ][    ][SEEN][SEEN][SEEN][SEEN][SEEN][    ]
[    ][    ][    ][SEEN][SEEN][SEEN][SEEN][SEEN][    ]
[    ][    ][    ][    ][    ][    ][    ][CLOAK][EXIT]
```

The backtracking goes in approximately correct direction, but when makes mistake it goes until sees a dead end and
returns to the other side

```
1	.	.	.	14	12	10	9	.
.	2	3	15	13	11	7	8	.
.	.	16	4	5	6	.	.	.
.	.	17	.	.	.	.	.	.
.	.	18	.	.	.	.	.	.
.	.	19	.	.	.	.	.	.
.	.	20	.	.	.	.	.	.
.	.	21	.	.	.	.	.	.
.	.	.	22	23	24	25	26	27	
```

A* algorithm was developing both solutions: go right or down. And when found a dead end continued to develop the best
solution: went down.

```
1	.	.	.	.	.	.	.	.	
.	2	3	.	.	.	.	.	.	
.	.	.	4	.	.	.	.	.	
.	.	5	.	.	.	.	.	.	
.	.	6	.	.	.	.	.	.	
.	.	7	.	.	.	.	.	.	
.	.	8	.	.	.	.	.	.	
.	.	9	.	.	.	.	.	.	
.	.	.	10	11	12	13	14	15	
```

To optimize a backtracking algorithm we could shuffle move order
(a little) (only when priorities of the moves are the same) in dfs and search for path several times. After that some
paths will go firstly to right then back and down, and some of them directly down. 



537 : -1



