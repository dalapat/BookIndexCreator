package homework3;
import java.util.LinkedList;
import java.util.List;

/**
 * Homework 3 Binary Search Tree Implementation. Data Structures Fall 2014. Sara
 * More.
 *
 * @author Anish
 *
 * @param <K>
 * @param <V>
 */
public class MyBST<K extends Comparable<? super K>, V> implements
        BinarySearchTree<K, V> {

    /**
     * Pointer to the root node.
     */
    Node<K, V> rootPtr;
    /**
     * Number of nodes in the tree. 
     */
    int numNodes;
    /**
     * Number of levels in the tree.
     */
    int numLevels;
    /**
     * Variables used in order to check whether to increment 
     * or decrement numNodes during an insertion or deletion
     * respectively. 
     */
    int insertState, delState;

    public static void main(String[] args) {
        System.out.println("hello");
        MyBST<Integer, Integer> b = new MyBST<>();
        b.insert(1, 5);
        System.out.println(b.getValueFromKey(1));
        b.update(1, 4);
        System.out.println(b.getValueFromKey(1));
        b.insert(2, 3);
        b.update(2, 4);
        System.out.println(b.getValueFromKey(2));
        System.out.println("Removed Val: " + b.remove(2));
    }
    /**
     * Inner node class to store key data pairs in BST.
     * @author Anish
     *
     * @param <K>
     * @param <V>
     */
    @SuppressWarnings("hiding")
    private final class Node<K extends Comparable<? super K>, V> {
        /**
         * Key to be stored in the node. 
         */
        K key;
        /**
         * Data to be stored in the node. 
         */
        V data;
        /**
         * left child of the node. 
         */
        Node<K, V> leftChild;
        /**
         * right child of the node. 
         */
        Node<K, V> rightChild;

        // int myLevel;
        
        
        /**
         * Node constructor.
         * @param passedKey
                 the value to insert for key
         * @param passedData
                 the value to insert for data
         * @param passedLeftChild
         *       the left child of the node
         * @param passedRightChild
                 the right child of the node
         */
        public Node(K passedKey, V passedData,
                Node<K, V> passedLeftChild, Node<K, V> passedRightChild) {
            this.key = passedKey;
            this.data = passedData;
            this.leftChild = passedLeftChild;
            this.rightChild = passedRightChild;
            // this.myLevel = myLevel;
        }

        /*
         * public int getLevel(){ return this.myLevel; }
         */

    }
    
    /**
     * Construct for the Binary Search Tree.
     */
    public MyBST() {
        this.rootPtr = null;
        this.numNodes = 0;
        this.numLevels = 0;
        this.insertState = 0; 
        this.delState = 0;
    }

    @Override
    public boolean insert(K theKey, V theValue) {

        /*
         * Node returnedNode = insert(theKey, theValue, rootPtr);
         * if(returnedNode == null){ return false; }
         */
        this.rootPtr = this.insert(theKey, theValue, this.rootPtr);
        if (this.insertState != 0) {
            this.insertState = 0;
            return false;
        }
        this.numNodes++;
        return true;
    }
    
    /**
     * 
     * @param theKey
             the key to be passed to the function.
     * @param val
             the value to be passed to the function.
     * @param node
             the node to be searched.
     * @return
             return the location the modified node.
     */
    public Node<K, V> insert(K theKey, V val, Node<K, V> node) {
        if (node == null) {
            node = new Node<K, V>(theKey, val, null, null);
        } else if (theKey.compareTo(node.key) < 0) {
            node.leftChild = this.insert(theKey, val, node.leftChild);
        } else if (theKey.compareTo(node.key) > 0) {
            node.rightChild = this.insert(theKey, val, node.rightChild);
        } else if (theKey.compareTo(node.key) == 0) {
            this.insertState = 1;
        }
        // numNodes++;
        return node;
    }

    @Override
    public V getValueFromKey(K theKey) {
        return this.getValueFromKey(theKey, this.rootPtr);
    }
    
    /**
     * 
     * @param theKey
             the key to be used for checking in the tree.
     * @param node
             the node to check for the key and return data from.
     * @return
             return data from node in found key
     */
    public V getValueFromKey(K theKey, Node<K, V> node) {
        V o = null;
        if (node == null) {
            return null;
        }
        if (theKey.compareTo(node.key) == 0) {
            o = node.data;
        } else if (theKey.compareTo(node.key) < 0) {
            o = this.getValueFromKey(theKey, node.leftChild);
        } else if (theKey.compareTo(node.key) > 0) {
            o = this.getValueFromKey(theKey, node.rightChild);
        } else {
            o = null;
        }
        return o;
    }

    @Override
    public boolean update(K theKey, V theNewVal) {
        return this.update(theKey, theNewVal, this.rootPtr);
    }

    /**
     * 
     * @param theKey
             the key that contains the data to be updated.
     * @param theNewVal
             the value to replace the node that contains theKey.
     * @param node
             the node to check for theKey.
     * @return
             return whether theKey was found in BST or not.
     */
    public boolean update(K theKey, V theNewVal, Node<K, V> node) {
        boolean bool;
        if (theKey.compareTo(node.key) == 0) {
            node.data = theNewVal;
            bool = true;
        } else if (theKey.compareTo(node.key) < 0) {
            bool = this.update(theKey, theNewVal, node.leftChild);
        } else if (theKey.compareTo(node.key) > 0) {
            bool = this.update(theKey, theNewVal, node.rightChild);
        } else {
            bool = false;
        }
        return bool;
    }

    @Override
    public V remove(K theKey) {
        Object[] result = new Object[1];
        // V result = null;
        this.rootPtr = this.remove(theKey, this.rootPtr, result);
        if (this.delState != 0) {
            this.delState = 0;
            return null;
        }
        @SuppressWarnings("unchecked")
        V result2 = (V) result[0]; // ask about this
        this.numNodes--;
        return result2;
    }

    /**
     * @param theKey
             the key to be searched and removed. 
     * @param node
             the node to search in.
     * @param result
             saves the data of the removed node.
     * @return
             return the location of the removed node. 
     */
    public Node<K, V> remove(K theKey, Node<K, V> node, Object[] result) {
        if (node == null) {
            this.delState = 1;
            return node;
        }
        if (theKey.compareTo(node.key) < 0) {
            // go to left subtree
            node.leftChild = this.remove(theKey, node.leftChild, result);
        } else if (theKey.compareTo(node.key) > 0) {
            // go to the right subtree
            node.rightChild = this.remove(theKey, node.rightChild, result);
        } else if (theKey.compareTo(node.key) == 0) {
            // result = node.data;
            result[0] = node.data;
            if (node.rightChild == null) {
                node = node.leftChild;
            } else if (node.leftChild == null) {
                node = node.rightChild;
            } else {
                Node<K, V> foundNode = this.findMin(node.rightChild);
                node.key = foundNode.key;
                node.data = foundNode.data;
                node.rightChild = this.remove(node.key, 
                        node.rightChild, result);
            }
            // numNodes--;
            /*
             * if(node.leftChild != null && node.rightChild != null){ Node
             * foundNode = findMin(node.rightChild); node.key = foundNode.key;
             * node.data = foundNode.data; node.rightChild = remove(node.key,
             * node.rightChild); }else{ if(node.leftChild != null){ node =
             * node.leftChild; }else{ node = node.rightChild; } }
             */
            /*
             * if(node.leftChild == null && node.rightChild == null){ o =
             * node.data; node = null; numNodes--; return o; }
             * 
             * //the node has both children or just the right child
             * if((node.leftChild != null && node.rightChild != null) ||
             * (node.leftChild == null && node.rightChild != null)){
             * //Comparable keyToReplace = findMinRightSubtree(node.rightChild);
             * //Object dataToReplace = getValueFromKey(keyToReplace);
             * Comparable keyToReplace = findMin(node.rightChild).key; o =
             * node.data; node.data = remove(keyToReplace, node); node.key =
             * keyToReplace; numNodes--; return o; }
             * 
             * //node has just the left child if(node.leftChild != null &&
             * node.rightChild == null){ //Comparable keyToReplace =
             * findMaxLeftSubtree(node.leftChild); //Object dataToReplace =
             * getValueFromKey(keyToReplace); Comparable keyToReplace =
             * findMax(node.leftChild).key; o = node.data; node.data =
             * remove(keyToReplace, node); node.key = keyToReplace; numNodes--;
             * return o; }
             */
        }
        // numNodes--;
        return node;
    }
    
    /**
     * 
     * @param node
             the root of the subtree to search for the minimum key.
     * @return
             return the node that contains the minimum key.
     */
    private Node<K, V> findMin(Node<K, V> node) {
        if (node == null) {
            return null;
        } else if (node.leftChild == null) {
            return node;
        }
        return this.findMin(node.leftChild);
    }

    /*
     * private Comparable findMinRightSubtree(Node node){ if(node.leftChild ==
     * null) return node.key; else return findMinRightSubtree(node.leftChild);
     * /*while(node.leftChild != null) node = node.leftChild; return node.key;
     */
    // }

    /*
     * private Comparable findMaxLeftSubtree(Node node){ if(node.rightChild ==
     * null) return node.key; else return findMaxLeftSubtree(node.rightChild);
     * /*while(node.rightChild != null) node = node.rightChild; return node.key;
     */
    // }

    @Override
    public int size() {
        return this.numNodes;
    }

    @Override
    public boolean isEmpty() {
        if (this.numNodes == 0) {
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
             node that is currently being traversed pre order.
     * @param al
             list that collects keys of nodes being traversed.
     * @return
             list that collects keys of nodes being traversed.
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
             node that is currently being traversed in order.
     * @param l
             list that collects nodes currently being traversed.
     * @return
             list that collects nodes currently being traversed.
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
             node that is currently being traversed post order.
     * @param l
             list that collects nodes currently being traversed.
     * @return
             list that collects nodes currently being traversed.
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
