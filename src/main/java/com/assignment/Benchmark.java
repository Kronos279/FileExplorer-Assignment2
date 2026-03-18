package main.java.com.assignment;

import main.java.com.assignment.tree.FileSystemTree;
import main.java.com.assignment.tree.TreeNode;
import main.java.com.assignment.tree.TreeTraversal;

// Runs four performance experiments on the N-ary File System Tree.
// Each experiment warms up the JVM first, then measures and prints a results table.
public class Benchmark {

    public static void main(String[] args) {

        section("EXPERIMENT 1 - INSERT SCALABILITY");
        header();
        for (int n : new int[]{100, 500, 1000, 5000, 10000}) {
            for (int w = 0; w < 3; w++) buildFlat(n);
            FileSystemTree fs = new FileSystemTree("root");
            long t0 = System.nanoTime();
            for (int i = 0; i < n; i++) fs.insertFile(fs.getRoot(), "f" + i + ".txt");
            row("insertFile (flat)", n, (System.nanoTime() - t0) / n, "O(1) amort.");
        }

        section("EXPERIMENT 2 — DFS vs BFS SEARCH");
        for (int n : new int[]{500, 2000, 5000}) {
            FileSystemTree fs = buildChain(n);
            fs.insertFile(getDeepest(fs.getRoot()), "needle.txt");
            for (int w = 0; w < 3; w++) { fs.searchDFS("needle"); fs.searchBFS("needle"); }

            long t0 = System.nanoTime(); fs.searchDFS("needle"); long dfsDep    = System.nanoTime() - t0;
            t0 = System.nanoTime(); fs.searchBFS("needle"); long bfsDep    = System.nanoTime() - t0;
            t0 = System.nanoTime(); fs.searchDFS("dir_0");  long dfsShallow = System.nanoTime() - t0;
            t0 = System.nanoTime(); fs.searchBFS("dir_0");  long bfsShallow = System.nanoTime() - t0;

            System.out.println("  n = " + n);
            header();
            row("DFS — deep target",    n, dfsDep,     "O(n)");
            row("BFS — deep target",    n, bfsDep,     "O(n)");
            row("DFS — shallow target", n, dfsShallow, "O(n)");
            row("BFS — shallow target", n, bfsShallow, "O(n)");
            System.out.println();
        }

        section("EXPERIMENT 3 — PATH RESOLUTION  O(h) vs O(n)");
        header();
        for (int h : new int[]{10, 50, 100, 500, 1000}) {
            FileSystemTree fs = buildChain(h);
            TreeNode deepest  = getDeepest(fs.getRoot());
            for (int w = 0; w < 3; w++) { fs.getPath(deepest); fs.findById(deepest.getId()); }

            long t0 = System.nanoTime(); fs.getPath(deepest);         long ptr = System.nanoTime() - t0;
            t0 = System.nanoTime(); fs.findById(deepest.getId()); long dfs = System.nanoTime() - t0;
            row("getPath  (ptr walk) h=" + h, h, ptr, "O(h)");
            row("findById (DFS)      h=" + h, h, dfs, "O(n)");
        }


        section("EXPERIMENT 4 — DELETE COST  O(subtree)");
        header();
        for (int sub : new int[]{10, 50, 200, 1000, 5000}) {
            for (int w = 0; w < 3; w++) {
                FileSystemTree tmp = new FileSystemTree("r");
                tmp.deleteByRef(buildSubtree(tmp, tmp.getRoot(), sub));
            }
            FileSystemTree fs = new FileSystemTree("root");
            TreeNode       st = buildSubtree(fs, fs.getRoot(), sub);
            long t0           = System.nanoTime();
            fs.deleteByRef(st);
            row("deleteByRef subtree=" + sub, sub, System.nanoTime() - t0, "O(subtree)");
        }

        section("COMPLEXITY SUMMARY");
        System.out.println("  Operation          | Time             | Space");
        System.out.println("  -------------------|------------------|----------");
        System.out.println("  insertFile/Folder  | O(1) amortised   | O(1)");
        System.out.println("  deleteByRef        | O(subtree + k)   | O(1)");
        System.out.println("  searchDFS          | O(n)             | O(h)");
        System.out.println("  searchBFS          | O(n)             | O(w)");
        System.out.println("  getPath            | O(h)             | O(h)");
        System.out.println("  height()           | O(n)             | O(h)");
        System.out.println("  findById           | O(n)             | O(h)");
        System.out.println("  countNodes         | O(n)             | O(h)");
        System.out.println("\n  n=total nodes  h=tree height  w=max width  k=sibling count");
    }


    // build a flat tree with n file children under root
    private static FileSystemTree buildFlat(int n) {
        FileSystemTree fs = new FileSystemTree("root");
        for (int i = 0; i < n; i++) fs.insertFile(fs.getRoot(), "f" + i + ".txt");
        return fs;
    }

    // build a linear chain of n nested folders
    private static FileSystemTree buildChain(int n) {
        FileSystemTree fs  = new FileSystemTree("root");
        TreeNode       cur = fs.getRoot();
        for (int i = 0; i < n; i++) cur = fs.insertFolder(cur, "dir_" + i);
        return fs;
    }

    // build a subtree of size n under parent and returns the subtree root
    private static TreeNode buildSubtree(FileSystemTree fs, TreeNode parent, int n) {
        TreeNode sub = fs.insertFolder(parent, "subtree");
        for (int i = 0; i < n - 1; i++) fs.insertFile(sub, "f" + i + ".txt");
        return sub;
    }

    // return the deepest node in the tree
    private static TreeNode getDeepest(TreeNode root) {
        TreeNode deepest = root;
        int      maxD    = 0;
        for (TreeNode n : TreeTraversal.preOrderList(root)) {
            int d = depth(n);
            if (d > maxD) { maxD = d; deepest = n; }
        }
        return deepest;
    }

    private static int depth(TreeNode n) {
        int d = 0;
        while (n.getParent() != null) { d++; n = n.getParent(); }
        return d;
    }


    private static void section(String title) {
        System.out.println("\n" + "=".repeat(56));
        System.out.println("  " + title);
        System.out.println("=".repeat(56));
    }

    private static void header() {
        System.out.printf("  %-30s | %-8s | %-12s | %s%n", "Operation", "n", "Time (ns)", "Complexity");
        System.out.println("  " + "-".repeat(65));
    }

    private static void row(String op, int n, long ns, String complexity) {
        System.out.printf("  %-30s | %-8d | %-12d | %s%n", op, n, ns, complexity);
    }
}