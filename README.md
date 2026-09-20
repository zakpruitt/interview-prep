# interview-prep

A local scratch space for LeetCode-style practice in Java 21, built as a standard
Maven project so IntelliJ's indexing, intellisense and run gutters work on every
new class without any manual module fiddling.

## Layout

```
pom.xml                          Maven build (Java 21 + JUnit 5)
src/main/java/leetcode/          one class per problem
src/test/java/leetcode/          one <Problem>Test per problem
tools/leetcode.py                pulls a problem down from leetcode.com
prep.ps1                         thin wrapper over the commands below
notes/                           freeform session notes
```

`ListNode` and `TreeNode` in the `leetcode` package match LeetCode's own
definitions, so linked-list and tree problems compile as pasted. Both have
helpers for tests: `ListNode.of(1, 2, 3)`, `ListNode.toArray(head)` and
`TreeNode.of(3, 9, 20, null, null, 15, 7)`.

## Starting a new problem

```powershell
.\prep.ps1 new two-sum
.\prep.ps1 new https://leetcode.com/problems/valid-sudoku/
```

That hits LeetCode's public GraphQL API (no login, free problems only) and writes:

* `src/main/java/leetcode/<Problem>.java` — the official Java signature, with the
  full problem statement as a javadoc above it, plus difficulty, topic tags and a
  link back. The body throws `UnsupportedOperationException` so it compiles from
  the first second.
* `src/test/java/leetcode/<Problem>Test.java` — a JUnit 5 stub with LeetCode's own
  example inputs listed in the javadoc, ready to turn into assertions.

Useful flags: `--name MyClassName`, `--force` to overwrite, `--no-test` to skip
the test stub. `TwoSumTest` is a filled-in example worth copying the shape of.

## Running

```powershell
.\prep.ps1 test           # whole suite
.\prep.ps1 test TwoSum    # just TwoSumTest
.\prep.ps1 build          # compile only
.\prep.ps1 list           # every problem in the repo, and whether it has a test
```

Plain Maven works the same: `mvn test`, `mvn test -Dtest=TwoSumTest`.

In IntelliJ, open the folder and let it import `pom.xml`. Green run arrows appear
next to each test class and method.
