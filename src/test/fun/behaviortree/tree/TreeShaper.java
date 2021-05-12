package test.fun.behaviortree.tree;

import test.fun.behaviortree.tree.Node;

public interface TreeShaper {
    void shapeTree(Node root, Strategy strategy);
}
