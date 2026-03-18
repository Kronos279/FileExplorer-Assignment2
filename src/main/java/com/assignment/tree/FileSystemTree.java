package com.assignment.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// N-ary File System Tree — models folders as internal nodes, files as leaves.
public class FileSystemTree {

    private final TreeNode root;
    private int size;

    // initialises tree with a single root folder
    public FileSystemTree(String rootName) {
        this.root = new TreeNode(rootName, TreeNode.NodeType.FOLDER, null);
        this.size = 1;
    }


    // inserts a new folder under parent
    public TreeNode insertFolder(TreeNode parent, String name) {
        validate(parent);
        TreeNode node = new TreeNode(name, TreeNode.NodeType.FOLDER, parent);
        parent.addChild(node);
        size++;
        return node;
    }

    // inserts a new file under parent
    public TreeNode insertFile(TreeNode parent, String name) {
        validate(parent);
        TreeNode node = new TreeNode(name, TreeNode.NodeType.FILE, parent);
        parent.addChild(node);
        size++;
        return node;
    }

    // deletes by id
    public boolean delete(int nodeId) {
        if (root.getId() == nodeId) throw new IllegalArgumentException("Cannot delete root.");
        TreeNode target = findById(nodeId);
        if (target == null) return false;
        return deleteByRef(target);
    }

    // deletes by direct reference, skipping the DFS find step
    public boolean deleteByRef(TreeNode node) {
        if (node.isRoot()) throw new IllegalArgumentException("Cannot delete root.");
        size -= countNodes(node);
        node.getParent().removeChild(node);
        return true;
    }

    // DFS search — case-insensitive substring match
    public List<TreeNode> searchDFS(String query) {
        List<TreeNode> results = new ArrayList<>();
        dfsCollect(root, query.toLowerCase(), results);
        return results;
    }

    private void dfsCollect(TreeNode node, String query, List<TreeNode> results) {
        if (node.getName().toLowerCase().contains(query)) results.add(node);
        for (TreeNode child : node.getChildren()) dfsCollect(child, query, results);
    }

    // BFS search — returns shallowest matches first
    public List<TreeNode> searchBFS(String query) {
        List<TreeNode> results = new ArrayList<>();
        Queue<TreeNode> queue  = new LinkedList<>();
        queue.add(root);
        String q = query.toLowerCase();
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur.getName().toLowerCase().contains(q)) results.add(cur);
            queue.addAll(cur.getChildren());
        }
        return results;
    }

    // walks parent pointers to build absolute path
    public String getPath(TreeNode node) {
        StringBuilder sb = new StringBuilder();
        TreeNode cur = node;
        while (cur != null) {
            sb.insert(0, "/" + cur.getName());
            cur = cur.getParent();
        }
        return sb.toString();
    }

    // returns folder-hierarchy height (files do not count)
    public int height() {
        return computeHeight(root);
    }

    private int computeHeight(TreeNode node) {
        if (node.isFile()) return 0; // files are leaves — do not add height
        int max = 0;
        for (TreeNode child : node.getChildren())
            if (child.isFolder()) max = Math.max(max, 1 + computeHeight(child));
        return max;
    }

    // counts all nodes in a subtree including the subtree root — O(subtree)
    public int countNodes(TreeNode subtreeRoot) {
        int count = 1;
        for (TreeNode child : subtreeRoot.getChildren()) count += countNodes(child);
        return count;
    }

    // counts all nodes of a given type across the whole tree — O(n)
    public int countByType(TreeNode.NodeType type) {
        return countByTypeHelper(root, type);
    }

    private int countByTypeHelper(TreeNode node, TreeNode.NodeType type) {
        int count = node.getType() == type ? 1 : 0;
        for (TreeNode child : node.getChildren()) count += countByTypeHelper(child, type);
        return count;
    }

    // finds a node by id using DFS — O(n) worst case
    public TreeNode findById(int id) {
        return findByIdHelper(root, id);
    }

    private TreeNode findByIdHelper(TreeNode node, int id) {
        if (node.getId() == id) return node;
        for (TreeNode child : node.getChildren()) {
            TreeNode result = findByIdHelper(child, id);
            if (result != null) return result;
        }
        return null;
    }

    // throws if parent is null or a file
    private void validate(TreeNode parent) {
        if (parent == null)  throw new IllegalArgumentException("Parent cannot be null.");
        if (parent.isFile()) throw new IllegalArgumentException("Cannot insert into a FILE node.");
    }

    public TreeNode getRoot() { return root; }
    public int      getSize() { return size; }

    @Override
    public String toString() {
        return String.format("FileSystemTree { root='%s', size=%d, height=%d }",
                root.getName(), size, height());
    }
}