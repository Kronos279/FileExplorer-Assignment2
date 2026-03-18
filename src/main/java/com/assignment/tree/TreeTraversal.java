package main.java.com.assignment.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class TreeTraversal {

    // pre-order DFS — visits node then its children, O(n) time O(h) space
    public static void preOrder(TreeNode node, Consumer<TreeNode> visitor) {
        if (node == null) return;
        visitor.accept(node);
        for (TreeNode child : node.getChildren()) preOrder(child, visitor);
    }

    // returns all nodes in pre-order as a list — O(n)
    public static List<TreeNode> preOrderList(TreeNode root) {
        List<TreeNode> result = new ArrayList<>();
        preOrder(root, result::add);
        return result;
    }

    // post-order DFS — visits children before node, O(n) time O(h) space
    public static void postOrder(TreeNode node, Consumer<TreeNode> visitor) {
        if (node == null) return;
        for (TreeNode child : node.getChildren()) postOrder(child, visitor);
        visitor.accept(node);
    }

    // returns all nodes in post-order as a list — O(n)
    public static List<TreeNode> postOrderList(TreeNode root) {
        List<TreeNode> result = new ArrayList<>();
        postOrder(root, result::add);
        return result;
    }

    // BFS level-order — visits nodes level by level using a queue, O(n) time O(w) space
    public static void levelOrder(TreeNode root, Consumer<TreeNode> visitor) {
        if (root == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            visitor.accept(cur);
            queue.addAll(cur.getChildren());
        }
    }

    // returns all nodes in level-order as a list — O(n)
    public static List<TreeNode> levelOrderList(TreeNode root) {
        List<TreeNode> result = new ArrayList<>();
        levelOrder(root, result::add);
        return result;
    }

    // pretty-prints the tree as an indented directory listing — O(n) time O(h) space
    public static void prettyPrint(TreeNode node, String prefix, boolean isLast) {
        String connector = isLast ? "└── " : "├── ";
        String icon      = node.isFolder() ? "📁" : "📄";
        String label     = node.isFolder() ? node.getName() + "/" : node.getName();

        if (node.getParent() == null)
            System.out.println(icon + " " + label);   // root — no connector
        else
            System.out.println(prefix + connector + icon + " " + label);

        String childPrefix = prefix + (isLast ? "    " : "│   ");
        List<TreeNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++)
            prettyPrint(children.get(i), childPrefix, i == children.size() - 1);
    }

    // convenience entry point — prints from root
    public static void print(TreeNode root) {
        prettyPrint(root, "", true);
    }
}