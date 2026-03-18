# Assignment 2 — Trees: N-ary File System Tree

## Theoretical Contribution

Models a file system as an **N-ary tree** where every folder is an internal node
and every file is a leaf. Storing a **parent pointer** in each node reduces path
resolution from `O(n·L)` (flat string scan) to `O(h)` (walk parent chain), where
`h` is the tree height. In a balanced tree, `h = O(log n)`.

## Project Structure

```
src/main/java/com/assignment/
├── Demo.java               — interactive file explorer (mvin, mvout, mvroot, mkdir, rm, ls, tree, pwd)
├── Benchmark.java          — 4 performance experiments with JVM warmup
└── tree/
    ├── TreeNode.java       — N-ary node (name, type, parent pointer, children list)
    ├── FileSystemTree.java — insert, delete, search (DFS + BFS), path resolution, metrics
    └── TreeTraversal.java  — pre-order, post-order, level-order, pretty-print
```

## How to Compile and Run

```bash
# Compile
find src -name "*.java" | xargs javac -d out

# Interactive explorer
java -cp out com.assignment.Demo

# Performance experiments
java -cp out com.assignment.Benchmark
```

## Demo Commands

| Command       | Description                        |
|---------------|------------------------------------|
| `ls`          | list contents of current directory |
| `mkdir <n>`   | create a folder                    |
| `rm <n>`      | delete a file or folder            |
| `mvin <n>`    | move into a child folder           |
| `mvout`       | go up one level                    |
| `mvroot`      | jump back to root                  |
| `tree`        | print full directory tree          |
| `pwd`         | show current path                  |
| `exit`        | quit                               |

## Complexity Summary

| Operation         | Time           | Space |
|-------------------|----------------|-------|
| insertFile/Folder | O(1) amortised | O(1)  |
| deleteByRef       | O(subtree + k) | O(1)  |
| searchDFS         | O(n)           | O(h)  |
| searchBFS         | O(n)           | O(w)  |
| getPath           | O(h)           | O(h)  |
| height()          | O(n)           | O(h)  |
| findById          | O(n)           | O(h)  |
| countNodes        | O(n)           | O(h)  |

`n` = total nodes · `h` = tree height · `w` = max width · `k` = sibling count

## Work Split

| File                | Author          |
|---------------------|-----------------|
| TreeNode.java       | Shahu Kor       |
| FileSystemTree.java | Ashutosh Yadav  |
| TreeTraversal.java  | Ashutosh Yadav  |
| Demo.java           | Ashutosh Yadav  |
| Benchmark.java      | Shahu Kor       |