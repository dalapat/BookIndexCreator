package homework3;
import java.util.LinkedList;
import java.util.List;

/**
 * Homework 3 Splay Tree Implementation. Data Structures Fall 2014. Sara
 * More.
 *
 * @author Anish
 *
 * @param <K>
 * @param <V>
 */
public class MySplayTree<K extends Comparable<? super K>, V> implements
        BinarySearchTree<K, V> {
    /**
     * Flag for null cases in rotations.
     */
    public static final int FLAG = -2;
    /**
     * Variables used in order to check whether to increment 
     * or decrement numNodes during an insertion or deletion
     * respectively. 
     */
    int insertState;
    /**
     * Pointer to the root node.
     */

    Node<K, V> rootPtr;
    /**
     * Number of nodes in the tree.
     */
    int numNodes;

    public static void main(String[] args) {
        MySplayTree<Integer, Integer> m = new MySplayTree<>();
        m.insert(1, 3);
        System.out.println(m.getValueFromKey(1));
        System.out.println(m.getValueFromKey(5));
    }
    /**
     * Node class to provide nodes to splay tree.
     * @author Anish
     *
     * @param <K>
     * @param <V>
     */
    @SuppressWarnings("hiding")
    private final class Node<K extends Comparable<? super K>, V> {
        /**
         * The left child of the Node.
         */
        public Node<K, V> leftChild;
        /**
         * The right child of the Node.
         */
        public Node<K, V> rightChild;
        /**
         * The key stored in the node.
         */
        public K key;
        /**
         * The data stored in the node.
         */
        public V data;
        
        /**
         * Node constructor.
         */
        @SuppressWarnings("unused")
        public Node() {
            this.key = null;
            this.data = null;
            this.leftChild = null;
            this.rightChild = null;
        }
        
        /**
         * Node constructor.
         * @param pkey
                 the key stored in the node. 
         * @param pdata
                 the data stored in the node. 
         * @param pleftChild
                 the left child of the node. 
         * @param prightChild
                 the right child of the node.
         */
        public Node(K pkey, V pdata, Node<K, V> pleftChild, 
                Node<K, V> prightChild) {
            this.key = pkey;
            this.data = pdata;
            this.leftChild = pleftChild;
            this.rightChild = prightChild;
        }
    }

    /**
     * NodePlus class to supplement functions of splay tree
     * by designating which nodes need to be splayed.
     * @author Anish
     *
     * @param <K>
     * @param <V>
     */
    @SuppressWarnings("hiding")
    private final class NodePlus<K extends Comparable<? super K>, V> {
        /**
         * Pointer to a Node. 
         */
        Node<K, V> node;
        /**
         * 0: Current node needs to be splayed.
         * -1: node's right child needs to be splayed.
         * 1: node's left child needs to be splayed.
         */
        int kind;

        /**
         * NodePlus constructor with parameters.
         * @param passedNode
                 value of the node saved in NodePlus.
         * @param passedKind
                 value of the kind of the node.
         */
        public NodePlus(Node<K, V> passedNode, int passedKind) {
            this.node = passedNode;
            this.kind = passedKind;
        }
    }
    
    /**
     * MySplayTree constructor.
     */
    public MySplayTree() {
        this.rootPtr = null;
        this.insertState = 0;
        this.numNodes = 0;
    }

    @Override
    public boolean insert(K theKey, V theValue) {
        this.rootPtr = this.insert(theKey, theValue, this.rootPtr);
        if (this.insertState != 0) {
            NodePlus<K, V> toBeRoot = this.splay(this.rootPtr, theKey);
            this.rootPtr = toBeRoot.node;
            this.insertState = 0;
            return false;
        }
        this.numNodes++;
        NodePlus<K, V> np = this.splay(this.rootPtr, theKey);
        this.rootPtr = np.node;
        return true;
    }

    /**
     * Supplementary function to public insert().
     * @param theKey
             the key to be inserted.
     * @param val
             the value to be stored in the node.
     * @param node
             the node to be checked.
     * @return
             a node.
     */
    private Node<K, V> insert(K theKey, V val, Node<K, V> node) {
        
        if (node == null) {
            node = new Node<K, V>(theKey, val, null, null);
        } else if (theKey.compareTo(node.key) < 0) {
            node.leftChild = this.insert(theKey, val, node.leftChild);
        } else if (theKey.compareTo(node.key) > 0) {
            node.rightChild = this.insert(theKey, val, node.rightChild);
        } else if (theKey.compareTo(node.key) == 0) {
            this.insertState = 1;
        }
        
        return node;
    }
    
    /**
     * Determines how to move splay node to root.
     * @param node
             node to be checked.
     * @param key
             key to be stored in node.
     * @return
             a NodePlus object.
     */
    public NodePlus<K, V> splay(Node<K, V> node, K key) {
        
        if (this.rootPtr == null) {
            return null;
        }
        if (node == null) {
            return new NodePlus<K, V>(node, FLAG);
        }

       
        if (key.compareTo(node.key) < 0) {
            NodePlus<K, V> np = this.splay(node.leftChild, key);
            node.leftChild = np.node;

            if (np.kind == 0) {
                
                return this.checkRootLeft(node, np);
            } else {
                
                return this.checkRotationLeft(node, np);
            }
        } else if (key.compareTo(node.key) > 0) {
            NodePlus<K, V> np = this.splay(node.rightChild, key);
            // Node<K, V> myChild = np.node;
            node.rightChild = np.node;

            if (np.kind == 0) {
                
                return this.checkRootRight(node, np);
            } else {
                
                return this.checkRotationRight(node, np);
            }
        } else if (key.compareTo(node.key) == 0) {
            return new NodePlus<K, V>(node, 0);
        }
        return null;
    }
    
    /**
     * Checks root pointer for left rotation.
     * @param node
         node to be rotated upon.
     * @param np
         NodePlus object used to check kind.
     * @return
         a NodePlus object.
     */
    public NodePlus<K, V> checkRootLeft(Node<K, V> node, NodePlus<K, V> np) {
        if (np.node == this.rootPtr.leftChild) {
            // do a right rotation on rootPtr
            Node<K, V> n = this.singleRightRotation(this.rootPtr);
            NodePlus<K, V> np3 = new NodePlus<>(n, 0);
            return np3;

        } else if (np.node == this.rootPtr.rightChild) {
            // do a left rotation on rootPtr
            Node<K, V> n = this.singleLeftRotation(this.rootPtr);
            NodePlus<K, V> np3 = new NodePlus<>(n, 0);
            return np3;
        }
        return new NodePlus<K, V>(node, 1);
    }
    
    /**
     * Checks the root for a right rotation.
     * @param node
         Node to be rotated upon.
     * @param np
         NodePlus object used to check kind. 
     * @return
         a NodePlus object.
     */
    public NodePlus<K, V> checkRootRight(Node<K, V> node, NodePlus<K, V> np) {
        if (np.node == this.rootPtr.leftChild) {
            // do a right rotation on rootPtr
            Node<K, V> n = this.singleRightRotation(this.rootPtr);
            NodePlus<K, V> np3 = new NodePlus<>(n, 0);
            return np3;

        } else if (np.node == this.rootPtr.rightChild) {
            // do a left rotation on rootPtr
            Node<K, V> n = this.singleLeftRotation(this.rootPtr);
            NodePlus<K, V> np3 = new NodePlus<>(n, 0);
            return np3;
        }
        return new NodePlus<K, V>(node, -1);
    }
    
    /**
     * Checks the node for right rotation in Splay.
     * @param node
         node to be rotated upon
     * @param np
         NodePlus object used to check kind.
     * @return
         a NodePlus object.
     */
    public NodePlus<K, V> checkRotationRight(Node<K, V> node, 
            NodePlus<K, V> np) {
        NodePlus<K, V> np2 = null;
        if (np.kind == -1) {
            // my child has the node to be splayed as the right child
            // right right
            //Node<K, V> n = rightRightRotation(node);
            Node<K, V> n = this.leftLeftRotation(node);
            np2 = new NodePlus<>(n, 0);
            //return np2;
        } else if (np.kind == 1) {
            // my child has the node to be splayed as its left child
            // right left
            Node<K, V> n = this.rightLeftRotation(node);
            np2 = new NodePlus<>(n, 0);
            //return np2;
        } else if (np.kind == FLAG) {
            return new NodePlus<K, V>(node.rightChild, 0);
        }
        return np2;
    }
    
    /**
     * Checks the node for right rotation in Splay.
     * @param node
         node to be rotated upon
     * @param np
         NodePlus object used to check kind.
     * @return
         a NodePlus object.
     */
    public NodePlus<K, V> checkRotationLeft(Node<K, V> node, 
            NodePlus<K, V> np) {
        NodePlus<K, V> np2 = null;
        if (np.kind == -1) {
            // my child has the node to be splayed as the right child
            // left right
            // Node n = leftRight(node)
            // NodePlus np = new NodePlus(n, 0);
            // return np;
            Node<K, V> n = this.leftRightRotation(node);
            np2 = new NodePlus<>(n, 0);
            //return np2;

        } else if (np.kind == 1) {
            // my child has the node to be splayed as the left child
            // left left
            //Node<K, V> n = leftLeftRotation(node);
            Node<K, V> n = this.rightRightRotation(node);
            np2 = new NodePlus<>(n, 0);
            //return np2;
        } else if (np.kind == FLAG) {
            return new NodePlus<K, V>(node.leftChild, 0);
        }
        return np2;
    }
    
    /**
     * Method for a single left rotation.
     * @param k2
         node to be rotated upon.
     * @return
         root of rotated subtree.
     */
    public Node<K, V> singleLeftRotation(Node<K, V> k2) {
        // k2 is node to be rotated upon.
        Node<K, V> k1 = k2.rightChild;
        k2.rightChild = k1.leftChild;
        k1.leftChild = k2;
        return k1;
    }
    
    /**
     * Method for a single right rotation.
     * @param k2
         node to be rotated upon.
     * @return
         root of rotated subtree.
     */
    public Node<K, V> singleRightRotation(Node<K, V> k2) {
        // k2 is node to be rotated upon)
        Node<K, V> k1 = k2.leftChild;
        k2.leftChild = k1.rightChild;
        k1.rightChild = k2;
        return k1;
    }
    
    /**
     * Method for a left rotation rotation.
     * @param k3
         node to be rotated upon.
     * @return
         root of rotated subtree.
     */
    public Node<K, V> leftRightRotation(Node<K, V> k3) {
        // k3 is top node
        // Node<K,V> tempLeft = k3.leftChild;
        k3.leftChild = this.singleLeftRotation(k3.leftChild);
        return this.singleRightRotation(k3);
    }
    
    /**
     * Method for a right left rotation.
     * @param k3
         node to be rotated upon.
     * @return
         root of rotated subtree.
     */
    public Node<K, V> rightLeftRotation(Node<K, V> k3) {
        k3.rightChild = this.singleRightRotation(k3.rightChild);
        return this.singleLeftRotation(k3);
    }
    
    /**
     * Method for a single left rotation.
     * @param k3
         node to be rotated upon.
     * @return
         root of rotated subtree.
     */
    public Node<K, V> rightRightRotation(Node<K, V> k3) {
        return this.singleRightRotation(this.singleRightRotation(k3));
    }

    /**
     * Method for a single left rotation.
     * @param k3
         node to be rotated upon.
     * @return
         root of rotated subtree.
     */
    public Node<K, V> leftLeftRotation(Node<K, V> k3) {
        return this.singleLeftRotation(this.singleLeftRotation(k3));
    }

    @Override
    public V getValueFromKey(K theKey) {
        Node<K, V> n = this.getValueFromKey(theKey, this.rootPtr);
        if (n == null) {
            return null;
        }
        V data = n.data;
        NodePlus<K, V> np = this.splay(this.rootPtr, n.key);
        this.rootPtr = np.node;
        return data;
    }

    /**
     * Returns the value of the corresponding key in the tree.
     * @param theKey
         the key to search for in the tree.
     * @param node
         the node to start from in the tree.
     * @return
         a node object to parse for the data.
     */
    public Node<K, V> getValueFromKey(K theKey, Node<K, V> node) {
        // V o = null;
        Node<K, V> n = null;
        if (node == null) {
            return null;
        }
        if (theKey.compareTo(node.key) == 0) {
            n = node;
        } else if (theKey.compareTo(node.key) < 0) {
            if (node.leftChild == null) {
                n = node;
            } else {
                n = this.getValueFromKey(theKey, node.leftChild);
            }
        } else if (theKey.compareTo(node.key) > 0) {
            if (node.rightChild == null) {
                n = node;
            } else {
                n = this.getValueFromKey(theKey, node.rightChild);
            }
        } else {
            n = null;
        }
        return n;
    }

    @Override
    public boolean update(K theKey, V theNewVal) {
        Node<K, V> n = this.update(theKey, theNewVal, this.rootPtr);
        NodePlus<K, V> np = this.splay(this.rootPtr, n.key);
        this.rootPtr = np.node;
        return false;
    }

    /**
     * Change the value of a node in the tree.
     * @param theKey
         the key whose data needs to be modified.
     * @param theNewVal
         the new value to use as the modified data.
     * @param node
         the node to start searching from in the tree.
     * @return
         a node object.
     */
    private Node<K, V> update(K theKey, V theNewVal, Node<K, V> node) {

        Node<K, V> n = null;
        if (node == null) {
            return null;
        }
        if (theKey.compareTo(node.key) == 0) {
            node.data = theNewVal;
            n = node;
        } else if (theKey.compareTo(node.key) < 0) {
            if (node.leftChild == null) {
                n = node;
            } else {
                n = this.update(theKey, theNewVal, node.leftChild);
            }
        } else if (theKey.compareTo(node.key) > 0) {
            if (node.rightChild == null) {
                n = node;
            } else {
                n = this.update(theKey, theNewVal, node.rightChild);
            }
        } else {
            n = null;
        }
        return n;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(K theKey) {
        Object[] o = new Object[1];
        Node<K, V> toDelete = this.remove(this.rootPtr, theKey, o);
        // Node<K,V> toDeleteLS = toDelete.leftChild;
        NodePlus<K, V> toBeRoot = this.splay(toDelete, theKey);
        this.rootPtr = toBeRoot.node;
        Node<K, V> maxFromLeftSubtree = this.findMax(this.rootPtr.leftChild);
        NodePlus<K, V> np = this.splay(this.rootPtr.leftChild,
                maxFromLeftSubtree.key);
        this.rootPtr.leftChild = np.node;
        Node<K, V> rightTemp = this.rootPtr.rightChild;
        Node<K, V> leftTemp = this.rootPtr.leftChild;
        this.rootPtr = null;
        leftTemp.rightChild = rightTemp;
        this.rootPtr = leftTemp;
        
        this.numNodes--;
        return (V) o[0];
    }
    
    /**
     * Find maximum value of a tree.
     * @param node
         root of subtree.
     * @return
         maximum value of subtree.
     */
    private Node<K, V> findMax(Node<K, V> node) {
        if (node == null) {
            return null;
        } else if (node.rightChild == null) {
            return node;
        }
        return this.findMax(node.rightChild);
    }

    /**
     * Remove an object from the tree.
     * @param node
         node to search from.
     * @param key
         key to search for.
     * @param o
         stores the data of the found node.
     * @return
         node object for bookkeeping.
     */
    private Node<K, V> remove(Node<K, V> node, K key, Object[] o) {
        /**
         * See whether the node contains the result and store the result in some
         * object
         * 
         * Then splay the key
         */
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            node.leftChild = this.remove(node.leftChild, key, o);
        } else if (key.compareTo(node.key) > 0) {
            node.rightChild = this.remove(node.rightChild, key, o);
        } else if (key.compareTo(node.key) == 0) {
            o[0] = node.data;
            return node;
        }
        
        return node;
    }

    @Override
    public int size() {
        
        return this.numNodes;
    }

    @Override
    public boolean isEmpty() {
        
        if (this.numNodes == 0 || this.rootPtr == null) {
            return true;
        }
        return false;
    }

    @Override
    public List<K> preOrder() {

        List<K> input = new LinkedList<K>();
        List<K> output = new LinkedList<K>();
        // input.add((T)rootPtr.key);
        output = this.preOrder(this.rootPtr, input);
        return output;
    }

    /**
     *
     * @param node
     *            node that is currently being traversed pre order.
     * @param al
     *            list that collects keys of nodes being traversed.
     * @return list that collects keys of nodes being traversed.
     */
    public List<K> preOrder(Node<K, V> node, List<K> al) {

        if (this.isEmpty()) {
            return null;
        }
        if (node == null) {
            return null;
        }
        al.add(node.key);
        this.preOrder(node.leftChild, al);
        this.preOrder(node.rightChild, al);

        return al;
    }

    @Override
    public List<K> inOrder() {
        List<K> input = new LinkedList<K>();
        List<K> output = new LinkedList<K>();
        output = this.inOrder(this.rootPtr, input);
        return output;
    }

    /**
     *
     * @param node
     *            node that is currently being traversed in order.
     * @param l
     *            list that collects nodes currently being traversed.
     * @return list that collects nodes currently being traversed.
     */
    public List<K> inOrder(Node<K, V> node, List<K> l) {
        if (this.isEmpty()) {
            return null;
        }
        if (node == null) {
            return null;
        }

        this.inOrder(node.leftChild, l);
        l.add(node.key);
        this.inOrder(node.rightChild, l);

        return l;
    }

    @Override
    public List<K> postOrder() {
        List<K> input = new LinkedList<K>();
        List<K> output = new LinkedList<K>();
        output = this.postOrder(this.rootPtr, input);
        return output;
    }

    /**
     *
     * @param node
     *            node that is currently being traversed post order.
     * @param l
     *            list that collects nodes currently being traversed.
     * @return list that collects nodes currently being traversed.
     */
    public List<K> postOrder(Node<K, V> node, List<K> l) {
        if (this.isEmpty()) {
            return null;
        }
        if (node == null) {
            return null;
        }
        this.postOrder(node.leftChild, l);
        this.postOrder(node.rightChild, l);
        l.add(node.key);
        return l;
    }

}
