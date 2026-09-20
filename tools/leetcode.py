"""Pull a LeetCode problem down into this project as a solution + test stub.

Usage:
    python tools/leetcode.py two-sum
    python tools/leetcode.py https://leetcode.com/problems/valid-sudoku/
    python tools/leetcode.py two-sum --name MyTwoSum --force

Only uses the standard library. Hits LeetCode's public GraphQL endpoint, so it
works for free (non-premium) problems without logging in.
"""

import argparse
import html
import json
import os
import re
import sys
import urllib.error
import urllib.request

GRAPHQL = "https://leetcode.com/graphql"
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
PACKAGE = "leetcode"
MAIN_DIR = os.path.join(ROOT, "src", "main", "java", PACKAGE)
TEST_DIR = os.path.join(ROOT, "src", "test", "java", PACKAGE)

QUERY = """
query questionData($titleSlug: String!) {
  question(titleSlug: $titleSlug) {
    questionFrontendId
    title
    titleSlug
    difficulty
    isPaidOnly
    content
    exampleTestcases
    topicTags { name }
    codeSnippets { langSlug code }
  }
}
"""


def slugify(target):
    """Accept a slug, a full problem URL, or a messy 'Two Sum' style title."""
    target = target.strip()
    match = re.search(r"leetcode\.com/problems/([a-zA-Z0-9\-]+)", target)
    if match:
        return match.group(1)
    if "/" in target or " " in target:
        target = re.sub(r"^.*/problems/", "", target).strip("/ ")
    target = re.sub(r"[^a-zA-Z0-9]+", "-", target)
    return target.strip("-").lower()


def fetch(slug):
    payload = json.dumps({"query": QUERY, "variables": {"titleSlug": slug}}).encode()
    request = urllib.request.Request(
        GRAPHQL,
        data=payload,
        headers={
            "Content-Type": "application/json",
            "User-Agent": "Mozilla/5.0",
            "Referer": "https://leetcode.com/problems/%s/" % slug,
        },
    )
    try:
        with urllib.request.urlopen(request, timeout=30) as response:
            body = json.load(response)
    except urllib.error.URLError as error:
        sys.exit("Could not reach leetcode.com: %s" % error)
    question = (body.get("data") or {}).get("question")
    if not question:
        sys.exit("No problem found for slug '%s'. Check the URL or spelling." % slug)
    return question


def class_name(slug):
    """two-sum -> TwoSum, 3sum -> ThreeSum (Java classes can't start with a digit)."""
    digits = {"0": "Zero", "1": "One", "2": "Two", "3": "Three", "4": "Four",
              "5": "Five", "6": "Six", "7": "Seven", "8": "Eight", "9": "Nine"}
    parts = [p for p in re.split(r"[^a-zA-Z0-9]+", slug) if p]
    name = "".join(p[:1].upper() + p[1:] for p in parts)
    if name and name[0].isdigit():
        rest = name[1:]
        name = digits[name[0]] + rest[:1].upper() + rest[1:]
    return name or "Problem"


def to_text(content):
    """LeetCode ships the description as HTML; flatten it to javadoc-safe text."""
    if not content:
        return ""
    text = re.sub(r"(?i)<sup>([^<]*)</sup>", r"^\1", content)
    text = re.sub(r"(?i)<br\s*/?>", "\n", text)
    text = re.sub(r"(?i)</(p|div|li|pre|ul|ol)>", "\n", text)
    text = re.sub(r"(?i)<li>", "  - ", text)
    text = re.sub(r"<[^>]+>", "", text)
    text = html.unescape(text).replace(chr(160), " ").replace("*/", "* /")
    lines = [line.replace("\t", "").rstrip() for line in text.splitlines()]
    out = []
    for line in lines:
        if line or (out and out[-1]):
            out.append(line)
    return "\n".join(out).strip()


def javadoc(question, url):
    body = ["/**", " * %s. %s (%s)" % (question["questionFrontendId"],
                                       question["title"], question["difficulty"]),
            " *", " * %s" % url]
    tags = [tag["name"] for tag in question["topicTags"]]
    if tags:
        body.append(" * Topics: %s" % ", ".join(tags))
    body.append(" *")
    for line in to_text(question["content"]).splitlines():
        body.append((" * " + line).rstrip())
    body.append(" */")
    return "\n".join(body)


def solution_source(question, name, url):
    snippet = next((s["code"] for s in question["codeSnippets"]
                    if s["langSlug"] == "java"), None)
    if snippet:
        body = snippet.strip()
        # Drop the leading "Definition for ListNode/TreeNode" comment: this
        # project ships real ListNode and TreeNode classes in the package.
        body = re.sub(r"\A/\*.*?\*/\s*", "", body, flags=re.S)
        # LeetCode always names the class Solution; use our problem name instead.
        body = re.sub(r"^class Solution\b", "class " + name, body, flags=re.M)
        # The snippet leaves method bodies empty, which doesn't compile.
        body = re.sub(r"(\)\s*\{)[ \t]*\n[ \t]*\n?([ \t]*\})",
                      r'\1\n        throw new UnsupportedOperationException("TODO");\n\2',
                      body)
    else:
        body = "class %s {\n\n}" % name
    body = body.replace("class " + name, "public class " + name, 1)
    # Snippets assume java.util is in scope (List, Map, TreeMap, ...).
    imports = "import java.util.*;\n\n" if re.search(
        r"\b(List|Map|Set|Queue|Deque|Stack|Arrays|Collections|Iterator|Random)\b",
        body) else ""
    return "package %s;\n\n%s%s\n%s\n" % (PACKAGE, imports, javadoc(question, url), body)


def method_signatures(source):
    """Pull out public method names so the test stub can reference them."""
    return re.findall(r"public\s+[\w<>\[\],.\s]+?\s+(\w+)\s*\(", source)


def test_source(question, name, url, solution):
    examples = (question.get("exampleTestcases") or "").splitlines()
    example_block = "\n".join(" * %s" % line for line in examples) or " * (none)"
    methods = [m for m in method_signatures(solution) if m != name]
    target = methods[0] if methods else "solve"
    return """package %s;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * %s -- %s
 *
 * LeetCode example input (one argument per line, grouped per case):
%s
 */
class %sTest {

    private final %s solution = new %s();

    @Test
    void example() {
        // TODO: assert against solution.%s(...)
        fail("write the first test case");
    }
}
""" % (PACKAGE, question["title"], url, example_block, name, name, name, target)


def write(path, content, force):
    if os.path.exists(path) and not force:
        print("skipped (already exists): %s" % os.path.relpath(path, ROOT))
        return False
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8", newline="\n") as handle:
        handle.write(content)
    print("wrote %s" % os.path.relpath(path, ROOT))
    return True


def main():
    parser = argparse.ArgumentParser(description="Scaffold a LeetCode problem locally.")
    parser.add_argument("problem", help="slug, title, or full leetcode.com URL")
    parser.add_argument("--name", help="override the generated Java class name")
    parser.add_argument("--force", action="store_true", help="overwrite existing files")
    parser.add_argument("--no-test", action="store_true", help="skip the test stub")
    args = parser.parse_args()

    slug = slugify(args.problem)
    question = fetch(slug)
    if question.get("isPaidOnly"):
        sys.exit("'%s' is a premium problem; its description isn't public." % slug)

    url = "https://leetcode.com/problems/%s/" % question["titleSlug"]
    name = args.name or class_name(question["titleSlug"])
    solution = solution_source(question, name, url)

    write(os.path.join(MAIN_DIR, name + ".java"), solution, args.force)
    if not args.no_test:
        write(os.path.join(TEST_DIR, name + "Test.java"),
              test_source(question, name, url, solution), args.force)

    print("\n%s. %s (%s)" % (question["questionFrontendId"], question["title"],
                             question["difficulty"]))
    print(url)
    print("\nRun just this test:  mvn -q test -Dtest=%sTest" % name)


if __name__ == "__main__":
    main()
