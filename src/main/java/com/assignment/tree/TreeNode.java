package main.java.com.assignment.tree;

import java.util.ArrayList;
import java.util.List;

// Represents a single node in the N-ary File System Tree.
public class TreeNode {

    public enum NodeType { FOLDER, FILE }

    private static int idCounter = 1; // auto-increment id

    private final int          id;
    private       String       name;
    private final NodeType     type;
    private       TreeNode     parent;
    private       List<TreeNode> children;

    // create new node
    public TreeNode(String name, NodeType type, TreeNode parent) {
        this.id       = idCounter++;
        this.name     = name;
        this.type     = type;
        this.parent   = parent;
        this.children = new ArrayList<>();
    }

    // append child
    public void addChild(TreeNode child) {
        children.add(child);
        child.setParent(this);
    }

    // remove child by reference
    public boolean removeChild(TreeNode child) {
        boolean removed = children.remove(child);
        if (removed) child.setParent(null);
        return removed;
    }

    // finds a direct child by name
    public TreeNode findChild(String name) {
        for (TreeNode child : children)
            if (child.getName().equals(name)) return child;
        return null;
    }

    // check type convenience
    public boolean isFolder() { return type == NodeType.FOLDER; }
    public boolean isFile()   { return type == NodeType.FILE;   }
    public boolean isRoot()   { return parent == null;          }
    public boolean isLeaf()   { return children.isEmpty();      }

    // getters and setters
    public int            getId()       { return id;       }
    public String         getName()     { return name;     }
    public NodeType       getType()     { return type;     }
    public TreeNode       getParent()   { return parent;   }
    public List<TreeNode> getChildren() { return children; }

    public void setName(String name)       { this.name   = name;   }
    public void setParent(TreeNode parent) { this.parent = parent; }

    @Override
    public String toString() {
        return String.format("[%s] %s (id=%d)", type, name, id);
    }
}