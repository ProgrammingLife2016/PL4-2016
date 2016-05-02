package application;

import java.util.StringTokenizer;

/**
 * A simple parser that will parse a single tree in newick format
 * to a simple tree.
 *
 * @author Albert Smit
 *
 */

public final class TreeParser {

    /**
     * Uninstantiable class.
     */
    private TreeParser() {
        // do nothing
    }

    /**
     * parses the String the parser has been given.
     * the String is first tokenized,
     * after which the method iterates over all tokens.
     *
     * @param tree
     *            The string describing the tree
     * @return The parsed tree.
     */

    public static TreeItem parse(final String tree) {
        TreeItem root = new TreeItem();

        // split the string into tokens, and keep the delimiters in the list so
        // we have
        // information about the next token.
        StringTokenizer tokenizer = new StringTokenizer(tree, "(:,);", true);

        TreeItem currentNode = root;
        // loop over all tokens
        while (tokenizer.hasMoreTokens()) {
            String currentToken = tokenizer.nextToken();
            // this starts a list of child nodes, add a child node and make it
            // our current node
            if ("(".equals(currentToken)) {
                // parser needs to instantiate objects.
                @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
                TreeItem newChild = new TreeItem();
                newChild.setParent(currentNode);
                currentNode = newChild;
            } else if (":".equals(currentToken)) {
                // next token is a distance, add it to the current node
                String distanceToken = tokenizer.nextToken();
                double distance = Double.parseDouble(distanceToken);
                currentNode.setDistance(distance);
            } else if (",".equals(currentToken)) {
                // next token is another child, add a new child to the list and
                // make it the current node
                currentNode = currentNode.getParent();
                // parser needs to instantiate objects.
                @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
                TreeItem newChild = new TreeItem();
                newChild.setParent(currentNode);
                currentNode = newChild;

            } else if (")".equals(currentToken)) {
                // list of children ends, go up 1 level so we can add a name and
                // distance to this node
                // if they are specified
                currentNode = currentNode.getParent();
            } else if (";".equals(currentToken)) {
                // tree is done, nothing special needs to be done
                break;
            } else {
                // final case, the current token is text,
                // so the current token is a name.

                currentNode.setName(currentToken);
            }
        }

        return root;
    }
}