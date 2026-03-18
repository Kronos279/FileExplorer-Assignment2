package main.java.com.assignment;
import main.java.com.assignment.tree.FileSystemTree;
import main.java.com.assignment.tree.TreeNode;
import main.java.com.assignment.tree.TreeTraversal;

import java.util.List;
import java.util.Scanner;


public class Demo {

    private final FileSystemTree fs;
    private TreeNode             cwd;
    private final Scanner        input;

    public Demo() {
        this.fs    = new FileSystemTree("root");
        this.cwd   = fs.getRoot();
        this.input = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new Demo().run();
    }

    public void run() {
        printBanner();
        handleTree();

        while (true) {
            System.out.print("\n[" + fs.getPath(cwd) + "] $ ");
            String line = input.nextLine().trim();

            if (line.isEmpty()) continue;

            String[] parts   = line.split("\\s+", 2);
            String   command = parts[0].toLowerCase();
            String   arg     = parts.length > 1 ? parts[1].trim() : "";

            switch (command) {
                case "ls"     -> handleLs();
                case "mkdir"  -> handleMkdir(arg);
                case "rm"     -> handleRm(arg);
                case "mvin"   -> handleMvin(arg);
                case "mvout"  -> handleMvout();
                case "mvroot" -> handleMvroot();
                case "tree"   -> handleTree();
                case "pwd"    -> handlePwd();
                case "help"   -> handleHelp();
                case "exit"   -> { System.out.println("  Goodbye!"); return; }
                default       -> err("Unknown command: '" + command + "'. Type 'help'.");
            }
        }
    }

    // list contents of current directory
    private void handleLs() {
        List<TreeNode> children = cwd.getChildren();
        System.out.println("\n " + fs.getPath(cwd));
        System.out.println("  " + "─".repeat(40));
        if (children.isEmpty()) { System.out.println("  (empty)"); return; }
        children.stream().filter(TreeNode::isFolder)
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .forEach(n -> System.out.println("   " + n.getName() + "/"));
        children.stream().filter(TreeNode::isFile)
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .forEach(n -> System.out.println("   " + n.getName()));
        System.out.println("  " + "─".repeat(40));
        long folders = children.stream().filter(TreeNode::isFolder).count();
        long files   = children.stream().filter(TreeNode::isFile).count();
        System.out.println("  " + folders + " folder(s),  " + files + " file(s)");
    }

    // create a folder in cwd
    private void handleMkdir(String name) {
        if (name.isEmpty())              { err("Usage: mkdir <folder-name>"); return; }
        if (cwd.findChild(name) != null) { err("'" + name + "' already exists."); return; }
        fs.insertFolder(cwd, name);
        ok("Folder '" + name + "' created.");
    }

    // delete a named child from cwd
    private void handleRm(String name) {
        if (name.isEmpty())              { err("Usage: rm <name>"); return; }
        TreeNode target = cwd.findChild(name);
        if (target == null)              { err("'" + name + "' not found."); return; }
        int removed = fs.countNodes(target);
        fs.deleteByRef(target);
        ok("Deleted '" + name + "' (" + removed + " node(s) removed).");
    }

    // move into a child folder
    private void handleMvin(String name) {
        if (name.isEmpty())              { err("Usage: mvin <folder-name>"); return; }
        TreeNode target = cwd.findChild(name);
        if (target == null)              { err("'" + name + "' not found."); return; }
        if (target.isFile())             { err("'" + name + "' is a file — can only enter folders."); return; }
        cwd = target;
        ok("Moved into '" + name + "'. Path: " + fs.getPath(cwd));
    }

    // move one level up
    private void handleMvout() {
        if (cwd.isRoot()) { err("Already at root."); return; }
        String prev = cwd.getName();
        cwd = cwd.getParent();
        ok("Moved up from '" + prev + "'. Path: " + fs.getPath(cwd));
    }

    // jump to root from anywhere
    private void handleMvroot() {
        if (cwd.isRoot()) { info("Already at root."); return; }
        cwd = fs.getRoot();
        ok("Jumped to root.");
    }

    // pretty-print full directory tree from root
    private void handleTree() {
        System.out.println();
        TreeTraversal.print(fs.getRoot());
        System.out.println();
        System.out.println("  Total nodes : " + (fs.getSize() - 1));
        System.out.println("  Height      : " + fs.height());
        System.out.println("  Files       : " + fs.countByType(TreeNode.NodeType.FILE));
        System.out.println("  Folders     : " + (fs.countByType(TreeNode.NodeType.FOLDER) - 1));
    }

    // print current working path
    private void handlePwd() {
        System.out.println("  " + fs.getPath(cwd));
    }

    // print command reference
    private void handleHelp() {
        System.out.println();
        System.out.println("  ┌───────────────────────────────────────────┐");
        System.out.println("  │      File System Explorer — Commands      │");
        System.out.println("  ├─────────────────┬─────────────────────────┤");
        System.out.println("  │ ls              │ list current directory  │");
        System.out.println("  │ mkdir <n>       │ create a folder         │");
        System.out.println("  │ rm <n>          │ delete file or folder   │");
        System.out.println("  │ mvin <n>        │ enter a child folder    │");
        System.out.println("  │ mvout           │ go up one level         │");
        System.out.println("  │ mvroot          │ jump back to root       │");
        System.out.println("  │ tree            │ print full tree         │");
        System.out.println("  │ pwd             │ show current path       │");
        System.out.println("  │ help            │ show this help          │");
        System.out.println("  │ exit            │ quit                    │");
        System.out.println("  └─────────────────┴─────────────────────────┘");
    }


    private void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║      N-ary File System Explorer          ║");
        System.out.println("  ║      Assignment 2 — Trees                ║");
        System.out.println("  ║      Type 'help' for commands            ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    private void ok(String msg)   { System.out.println("  ✔  " + msg); }
    private void err(String msg)  { System.out.println("  ✖  " + msg); }
    private void info(String msg) { System.out.println("  ℹ  " + msg); }


}