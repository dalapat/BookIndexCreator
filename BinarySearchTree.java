package homework3;
import java.util.List;

/**
 * An interface specifying methods for a binary search tree.
 *
 * @author more
 *
 * @param <K>
 *            the type of keys stored in the binary search tree
 * @param <V>
 *            the type of the associated data stored in the tree
 */

public interface BinarySearchTree<K extends Comparable<? super K>, V> {

    /**
     * Insert a new item into the tree.
     *
     * An attempt to insert an item with a key that matches an existing item in
     * the tree will fail gracefully; the new item will not be inserted, and the
     * method will return false.
     *
     * @param theKey
     *            the key to insert
     * @param theValue
     *            the data associated with the key, must be non-null
     * @return true if successful insert; false otherwise
     */
    boolean insert(K theKey, V theValue);

    /**
     * Given a particular key value, return the associated data.
     *
     * @param theKey
     *            the key whose associated data is desired
     * @return the value held in the same node as the specified key, 
     *         or null if key not found
     */
    V getValueFromKey(K theKey);

    /**
     * Update the associated data stored with this particular key.
     *
     * @param theKey
     *            the key item to check for
     * @param theNewVal
     *            the updated data value, must be non-null
     * @return true if found; false otherwise
     */
    boolean update(K theKey, V theNewVal);

    /**
     * Remove an item with the given key from the tree, and
     * return its associated value.
     *
     * An attempt to remove item with a key that does not appear in
     * the tree will fail gracefully; the tree will retain all of
     * its nodes, and the method will return null.
     *
     * @param theKey
     *            the key value of the node to remove
     * @return the value in the removed node, or null if key not found
     */
    V remove(K theKey);
    
    /**
     * Determine how big this tree is.
     *
     * @return number of nodes in this tree
     */
    int size();

    /**
     * Find out whether there are any nodes in this tree.
     *
     * @return true if 0 nodes; false otherwise
     */
    boolean isEmpty();
    
    /**
     * Preorder traversal of entire tree.
     *
     * @return An ordered, iterable set of keys generated from a preorder
     *         traversal of the entire tree
     */
    List<K> preOrder();

    /**
     * Inorder traversal of entire tree.
     *
     * @return An ordered, iterable set of keys generated from an inorder
     *         traversal of the entire tree
     */
    List<K> inOrder();

    /**
     * Postorder traversal of entire tree.
     *
     * @return An ordered, iterable set of keys generated from a postorder
     *         traversal of the entire tree
     */
    List<K> postOrder();

}

