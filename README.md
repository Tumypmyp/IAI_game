[comment]: <> (# Introduction to Artificial Intelligence)

# IAI Assignment 1: Book Finding

## Algorithms

Both algorithms do this steps

- First we try to explore the all available map
  ```dfsToCard(Card.CLOAK, game.EXIT, game.initialPlayer)```, if we can find ```Card.CLOAK```, then we should explore
  again for ```Card.BOOK```, continuing from player that found ```Card.CLOAK```.

- If we didn't find ```Card.CLOAK```, then the only possible solution is to first got to ```Card.BOOK```, then
  to ```Card.EXIT```(also it is possible that ```Card.BOOK``` and/or ```Card.EXIT``` aren't reacheabale without
  the```CLOAK```, then game is ```LOST```).

- Else when ```CLOAK``` is found we search for ```BOOK```. Then return the shortest path from
  ```START->CLOAK->BOOK->EXIT```, ```START->BOOK->CLOAK->EXIT``` or ```START->BOOK->EXIT```.

### Backtracking

The algorithm uses standard dfs with two modifications:

- when dfs finds destination ```Card``` it stops all recursive instances(destination is a parameter Point)
- when dfs finds ```Card.BOOK``` or ```Card.CLOAK``` their place is stored in Algorithm fields
- for vertex v dfs goes to more optimal neigbours first, optimality is chosen by heuristics
  (distance to destination point)

### A*

The algorithm uses standard A* with PriorityQueue that sort possible moves by f(move) = g(move) + h(move), where

- g(move) - number of steps done to come to this position
- h(move) - estimated number of steps to destination(manhattan distance)

## Statistics
Backtracking:

|status | avg len | number af games|
|---|---|---|
|WON|10.91025641025641 |78|
|LOST|0.0| 21|
|STARTED|0.0| 1|

A*:

|status | avg len | number af games|
|---|---|---|
|WON|10.85897435897436| 78|
|LOST|0.0| 21|
|STARTED|0.0| 1|

the LOST row is the games where cat seen harry in the initial state
the STARTED row is the games where  harry could not find any solution



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

```1	.	.	.	.	.	.	.	.	
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