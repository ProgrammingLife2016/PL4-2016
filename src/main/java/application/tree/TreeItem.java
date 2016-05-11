package application.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * A tree to store the relation between samples.
 *
 * @author Albert Smit
 * @author Rutger van den Berg
 */
public class TreeItem {
    /**
     * The list of children of this node.
     */
    private final Set<TreeItem> children;

    /**
     * The distance between samples. This is an optional field.
     */
    private double distance;

    /**
     * The name of the sample. This is an optional field.
     */
    private String name;

    /**
     * The parent node, null when this node is the root node.
     */
    private TreeItem parent;

    /**
     * Creates a new PhylogeneticTreeItem. Will initialize the ArrayList storing
     * the children and assign a new and unique id to the node.
     */
    public TreeItem() {
        children = new CopyOnWriteArraySet<TreeItem>();
    }

    /**
     * Method to determine the amount of descendant nodes each node has.
     *
     * @return the amount of descendant nodes
     */
    public int numberDescendants() {
        int result = 0;
        for (TreeItem child : children) {
            result += child.numberDescendants() + 1;
        }

        return result;
    }

    /**
     * Method to determine the maximum amount of layers.
     *
     * @return the amount of layers
     */
    public int maxDepth() {
        int result = 0;
        for (TreeItem child : children) {
            result = Math.max(result, child.maxDepth() + 1);
        }

        return result;
    }

    /**
     * Adds a child to the PhylogeneticTreeItem. This method will add the
     * PhylogeneticTreeItem child to the ArrayList storing the children of this
     * node.
     *
     * @param child the PhylogeneticTreeItem that needs to be added to the tree
     */
    public void addChild(final TreeItem child) {
        children.add(child);
    }

    /**
     * Returns the ArrayList of children.
     *
     * @return the ArrayList containing all children of this node
     */
    public List<TreeItem> getChildren() {
        return new ArrayList<TreeItem>(children);
    }

    /**
     * Returns the distance stored in this node. distance is an optional
     * property, so the distance can often be 0.0.
     *
     * @return the distance of this node
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the name stored in this node. name is an optional property, so
     * this method can return null.
     *
     * @return the name of this node
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this nodes parent node.
     *
     * @return the PhylogeneticTreeItem that is this nodes parent
     */
    public TreeItem getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + children.hashCode();
        long temp;
        temp = Double.doubleToLongBits(distance);
        result = prime * result + (int) (temp ^ (temp >>> prime + 1));
        result = prime * result;
        if (name != null) {
            result += name.hashCode();
        }
        return result;
    }

    /**
     * Sets this nodes distance to the passed double.
     *
     * @param distance the distance between the nodes
     */
    public void setDistance(final double distance) {
        this.distance = distance;
    }

    /**
     * Set this nodes name to the passed String.
     *
     * @param name the name of this node
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Compares this with another Object. returns true when both are the same.
     * two PhylogeneticTreeItems are considered the same when both have the
     * same:
     * <p/>
     * <ol>
     * <li>name or both have no name</li>
     * <li>distance</li>
     * <li>children, order does not matter</li>
     * </ol>
     *
     * @param obj the object to compare with
     * @return true if both are the same, otherwise false
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TreeItem)) {
            return false;
        }
        TreeItem other = (TreeItem) obj;
        if (!Double.valueOf(this.distance).equals(other.distance)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return children.equals(other.children);
    }

    /**
     * Sets the parent to be the node passed to the method This method will set
     * the parent and also add itself to the list of children in the parent
     * node.
     *
     * @param parentNode the node that will be this nodes parent
     */
    public void setParent(final TreeItem parentNode) {
        this.parent = parentNode;
        this.parent.addChild(this);
    }

    /**
     * Returns a String representation of the PhylogeneticTreeItem. The String
     * will have the following format:
     * <code>&lt;Node: id, name: name, Distance:distance, parent: parentID&gt;
     * </code> or when the PhylogeneticTreeItem has no parent:
     * <code>&lt;Node: id, Name: name, Distance: distance, ROOT&gt;</code>
     *
     * @return the String version of the object.
     */
    @Override
    public String toString() {

        return toStringWithDepth(0);
    }

    /**
     * @param depth The depth of this treeitem as compared to the depth of the
     *              treeitem on which toString was called.
     * @return Recursive string representation.
     */
    private String toStringWithDepth(final int depth) {

        StringBuffer output = new StringBuffer("<Node: Name: "
                + name + ", Distance: " + distance + ">");
        for (TreeItem child : children) {
            output.append('\n');
            for (int i = 0; i <= depth; i++) {
                output.append('\t');
            }
            output.append(child.toStringWithDepth(depth + 1));
        }
        return output.toString();
    }

}
