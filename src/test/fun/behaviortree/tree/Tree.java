package test.fun.behaviortree.tree;

public interface Tree {

    void init(Strategy strategy);

    void add(Node node);

    void delete(Node node);

    void shapeTree(TreeShaper shaper);

    Node getRoot();

    int getNodesCount();

}
