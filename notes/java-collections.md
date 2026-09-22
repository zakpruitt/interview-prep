# Java Collections — What to Reach For (modern)

## The short answer

| Need | Use | Not |
|---|---|---|
| List | `ArrayList` | `Vector`, `LinkedList` |
| Stack (LIFO) | `ArrayDeque` | `Stack` |
| Queue (FIFO) | `ArrayDeque` | `LinkedList` |
| Set | `HashSet` | — |
| Set, insertion order | `LinkedHashSet` | — |
| Set, sorted | `TreeSet` | — |
| Map | `HashMap` | `Hashtable` |
| Map, insertion order | `LinkedHashMap` | — |
| Map, sorted by key | `TreeMap` | — |
| Heap / priority | `PriorityQueue` | — |
| Thread-safe map | `ConcurrentHashMap` | `Hashtable`, `synchronizedMap` |

Legacy and effectively never correct in new code: `Vector`, `Stack`, `Hashtable`, `Enumeration`.
All three are synchronized on every method, which you almost never want.

## Why the "not" column

**`Stack` → `ArrayDeque`.** `Stack` extends `Vector`, so every op is synchronized, it leaks
`Vector`'s index-based API (`get(i)`) onto a LIFO type, and it **iterates bottom-to-top** —
the opposite of pop order, a quiet bug source. `Stack`'s own javadoc says to use `Deque`
instead. Caveat: `ArrayDeque` rejects `null` elements (NPE).

**`LinkedList` → `ArrayDeque`.** `LinkedList` allocates a node per element; bad cache
locality. `ArrayDeque` is a circular buffer and beats it for both stack and queue use.
`LinkedList` only wins if you're removing from the middle via an `Iterator`, which is rare.

**`Hashtable` → `HashMap`/`ConcurrentHashMap`.** Same synchronized-on-everything problem.
For real concurrency `ConcurrentHashMap` scales far better (lock striping, not one big lock).

## Declare the interface, instantiate the impl

```java
List<String> names   = new ArrayList<>();
Map<String, Integer> counts = new HashMap<>();
Deque<Character> stack = new ArrayDeque<>();
Set<Integer> seen = new HashSet<>();
Queue<Node> queue = new ArrayDeque<>();   // PriorityQueue also fits Queue
```

`Deque` is the interface for both stacks and queues — `push`/`pop`/`peek` work on the head
and read exactly like a stack; `offer`/`poll` read like a queue.

## Complexity cheat sheet

| Op | ArrayList | ArrayDeque | HashMap/HashSet | TreeMap/TreeSet | PriorityQueue |
|---|---|---|---|---|---|
| add | O(1)* | O(1)* | O(1) | O(log n) | O(log n) |
| remove | O(n) | O(1) head/tail | O(1) | O(log n) | O(log n) peek-min O(1) |
| get / contains | O(1) index | — | O(1) | O(log n) | O(n) search |
| ordered iteration | insertion | insertion | **none** | **sorted** | **no** |

\* amortized — occasional resize copy.

Two traps:
- `HashMap` iteration order is **unspecified**, not insertion order. Need order? `LinkedHashMap`.
- `PriorityQueue` iteration order is **not sorted** — only `poll()` comes out in order.

## Methods worth knowing

```java
map.getOrDefault(k, 0)                  // counting without null checks
map.computeIfAbsent(k, x -> new ArrayList<>()).add(v);  // multimap in one line
map.merge(k, 1, Integer::sum)           // frequency counter
map.putIfAbsent(k, v)

List.of(1, 2, 3)                        // immutable, Java 9+
Map.of("a", 1, "b", 2)                  // immutable, small maps
Arrays.asList(arr)                      // fixed-size view, NOT immutable

new PriorityQueue<>(Comparator.reverseOrder())        // max-heap
new PriorityQueue<>(Comparator.comparingInt(n -> n.cost))
```

`List.of(...)` etc. are genuinely immutable and reject `null`. `Arrays.asList` is a
fixed-size *view* — `set()` works, `add()` throws.

## Picking one under pressure

1. Do I need key → value? → `Map`. Otherwise `List` or `Set`.
2. Do I need uniqueness? → `Set`.
3. Do I care about order? → none: `Hash*`. Insertion: `LinkedHash*`. Sorted: `Tree*`.
4. Only ever touching the ends? → `ArrayDeque`.
5. Repeatedly pulling the min/max? → `PriorityQueue`.

Default to `HashMap`/`HashSet`/`ArrayList`/`ArrayDeque`. Reach past them only when you can
name the property you need.
