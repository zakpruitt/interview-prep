# Interview Prep Notes

## General patterns (the actual meta-skill)

**Restate the constraint in plain English before picking a data structure.**
Say the rule out loud, slowly, and ask what it structurally resembles:
- "The most recently opened thing must be resolved first" → **stack** (LIFO)
- "Have I seen this before, yes/no" → **HashSet**
- "How many times has this occurred" → **HashMap** (or `groupingBy` + `counting()`)
- "Give me the first-seen unresolved item" → **queue** (FIFO)
- "Sorted order / repeated min-max extraction" → **heap**
- "Convert a 2D position into one flat index" → `row * width + col` (or `rowBand * 3 + colBand` for a 3x3-of-3x3 grid)

**Sorting is not free — don't reach for it out of habit.**
If the problem asks you to *find/identify* something (not *produce something in order*), ask
whether you actually need the ordering sorting gives you. Sorting is O(n log n); a single pass
with the right structure is usually O(n). Reaching for sort was the recurring wrong first
instinct tonight (non-repeating chars, min/max) — check for this reflex specifically.

**State Big-O honestly before calling something "optimized."**
Don't label a solution "O(n)" without tracing it. "Compare adjacent elements and swap" is
bubble sort — O(n²) — not a fast pass, even though it sounds like a quick single loop.

**Two-pointer only applies when there's real structure to exploit** — sorted data, or
searching for a pair/window. It doesn't help just because a problem involves a string/array.

**Say the plan out loud, then check it made it into the code.**
Recurring gap tonight: correctly deriving the logic in words, then leaving it out of the
actual code (missing initialization, referencing a variable never computed, etc.). Do a
final read-through comparing code against your own stated plan before calling it done.

---

## Problem-specific notes

**Non-repeating characters** — counting problem → `HashMap`/array of counts. Sorting first
is a downgrade (O(n log n) vs already-optimal O(n)). Use a fixed-size array (`int[26]`) over
a HashMap when the character set is bounded — same complexity, better constant factor.

**Min/max with fewest comparisons** — process in pairs: compare the pair to each other (1),
then smaller vs running min (1), larger vs running max (1) = 3 comparisons per 2 elements
(~3n/2) vs. naive 2 comparisons per element (~2n). Both O(n) time — this is a constant-factor
trick, not an asymptotic one. Watch odd vs. even length array seeding.

**Valid Parentheses** — the counter/balance-check trick (+1 open, -1 close, check ==0) is
fundamentally broken here — it can't encode order or type, only total count. This needs a
**stack**: push the *expected closing bracket* when you see an opener; on a closer, check it
against `stack.pop()` directly. Guard: `isEmpty()` check before popping, and `isEmpty()` check
at the end (catches unclosed openers).

**Top-3 frequent numbers (streams)** — `Collectors.groupingBy(n -> n, Collectors.counting())`
is the idiomatic "count occurrences" one-liner — think of it as SQL `GROUP BY` + `COUNT`.
Don't use `.map()` or `.forEach()` to mutate an external HashMap as a side effect — it's a
documented Streams anti-pattern (breaks under `.parallelStream()`, real race condition risk).
`.boxed()` converts `IntStream` → `Stream<Integer>` — needed because `groupingBy` only works
on object streams, not primitive ones.

**Valid Sudoku** — validity ≠ solvability (explicitly noted in the problem — don't conflate).
Box index from `(row, col)`: `rowBand = row/3`, `colBand = col/3` (integer division truncates,
naturally buckets 0-8 into bands 0/1/2 — no need to reason about inclusive/exclusive ranges).
Flatten to one index with `rowBand * 3 + colBand`, or skip flattening and just use a `3x3`
array of sets indexed `[rowBand][colBand]` — same complexity, arguably safer under pressure.
Use `Set<Character>`, not counting — this problem only needs "have I seen this," not "how many."

---

## Java data structure quick reference

**HashSet** — no duplicates allowed, by definition. `.add(x)` returns `true` if `x` was new
and got added, `false` if it was already present (silent no-op, no exception). This lets you
combine "check" and "act" in one call: `if (!set.add(x)) { /* duplicate */ }`. Without relying
on the return value, the manual equivalent is `contains()` then `add()` as two steps — same
result, twice the operations, but a safe fallback if the return-value trick doesn't come to
mind under pressure.

**Array of objects (`new HashSet[9]`, `new HashSet[3][3]`, etc.)** — always defaults every
slot to `null` for object types (not a Java-version thing — true since 1.0, and always will
be; only *primitive* arrays like `new int[9]` auto-init to zero values). You must manually
create an instance for every slot before use, or you'll NPE the first time you call a method
on one. `Arrays.setAll(arr, i -> new HashSet<>())` is a one-line alternative to a for-loop for
1D arrays; for 2D, a plain nested for-loop is just as clear and less error-prone to write live.

**HashMap.merge(key, 1, Integer::sum)** — manual frequency-counting one-liner: puts 1 if the
key is absent, adds 1 to the existing value if present. What `groupingBy`/`counting()` does
for you automatically in Streams.

**Streams — tier 1 (know cold):** `.stream()`, `.filter()`, `.map()`, `.collect()`,
`.forEach()`, `.sorted()`, `.count()`.
**Streams — tier 2 (recognize, look up syntax):** `Collectors.groupingBy()` (+ downstream
`Collectors.counting()`, or other downstream collectors), `.boxed()`.

---

## Spring notes

**IoC / Dependency Injection** — Spring creates and wires objects for you; classes don't
`new` up their own dependencies. This is *not* the same thing as Maven/POM dependency
management — POM fetches external *libraries* (jars) onto your classpath; DI is about who
constructs and hands you object *instances* at runtime. Two unrelated concepts sharing a word.

**Bean scope** — singleton (one shared instance for the whole app) is the *default*, not an
inherent property of "being a bean." Prototype scope gives a fresh instance per injection.

**Singleton beans under concurrency** — Spring just hands out the same object to every caller;
thread-safety is the bean author's responsibility, not something Spring manages. Safe if the
bean is stateless or has only immutable fields (the common case — most services just wrap a
call to something else). Dangerous the moment a bean has *mutable instance fields* touched
by multiple threads — that's a real, silent race condition, not a hypothetical. Fixes in order
of preference: don't store the state as a field at all (pass via method params instead),
`ThreadLocal`, `@Scope("prototype")`, or proper synchronization as a last resort.

**Practical lifecycle hooks** — `@PostConstruct` (setup after dependencies are injected) and
`@PreDestroy` (cleanup on shutdown) are the two hooks you'll actually use day to day. Everything
else (Aware interfaces, BeanPostProcessor phases) is Spring's internal machinery for how it
gets there — not something you reach for directly in typical application code.

**Logging** — SLF4J is a *facade* (an API your code logs against), not a competing
implementation to Log4j2/Logback — SLF4J usually sits on top of one of them. Lombok's
`@Slf4j` is a code-gen annotation that auto-generates a static logger field for you — it is
not a Spring bean, not in the ApplicationContext, not managed by IoC.
