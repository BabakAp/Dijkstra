/*
 * File: FibonacciHeap.java
 * Author: Babak Alipour (babak.ap@ufl.edu)(babak@cise.ufl.edu)
 * Last Edited: March 12, 2015
 */
package fibonacciHeap;

/**
 *
 * @author Babak Alipour (babak.ap@ufl.edu)(babak@cise.ufl.edu)
 * @param <T>
 */
public class FibonacciHeap<T> {

    /**
     * Pointer to min node
     */
    private static Node min;
    /**
     * Count of nodes in heap
     */
    private int n;

    private int maxDegree = 45;

    /**
     * Initializes an empty Fibonacci Min Heap
     */
    public FibonacciHeap() {
        min = null;
        n = 0;
    }

    /**
     * Clears all nodes in heap
     */
    public void clear() {
        min = null;
        n = 0;
    }

    /**
     * Returns size of this heap
     * <p>
     * @return total number of nodes in heap
     */
    public int size() {
        return n;
    }

    /**
     * Returns true if heap is empty, false otherwise
     * <p>
     * @return true if heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return min == null;
    }

    /**
     * Returns but not removes the min element data
     * <p>
     * @return
     */
    public T min() {
        return (T) min.data;
    }

    /**
     * Creates a node with an integer key and object data and inserts it into
     * heap
     * <p>
     * @param data
     * @param key  <p>
     * @return inserted node
     */
    public Node<T> insert(T data, int key) {
        //Create node
        Node theNode = new Node(key, data);
        //insert into topmost list and correct min pointer, if necessary
        if (min != null) {
            theNode.right = min;
            theNode.left = min.left;
            min.left = theNode;
            theNode.left.right = theNode;
            if (theNode.key < min.key) {
                min = theNode;
            }
        } else {
            min = theNode;
        }
        //increase heap size
        n++;
        maxDegree = (int) (Math.log(n) + 2) * 2;
        return theNode;
    }

    /**
     * Inserts the given node into heap
     * <p>
     * @param theNode
     */
    public void insert(Node theNode) {
        if (theNode.child != null || theNode.parent != null) {
            return;
        }
        //insert into topmost list and correct min pointer, if necessary
        if (min != null) {
            theNode.right = min;
            theNode.left = min.left;
            min.left = theNode;
            theNode.left.right = theNode;
            if (theNode.key < min.key) {
                min = theNode;
            }
        } else {
            min = theNode;
        }
        //increase heap size
        maxDegree = (int) (Math.log(n) + 2) * 2;
        n++;
    }

    /**
     * Removes and returns node with the minimum key
     * <p>
     * @return node with lowest key
     */
    public T removeMin() {
        /**
         * This operation is done in three phases as explained in Wikipedia
         * (http://en.wikipedia.org/wiki/Fibonacci_heap)
         */
        if (isEmpty()) {
            return null;
        }
        Node oldMin = min;
        n--;
        /**
         * PHASE 1: First we take the root containing the minimum element and
         * remove it
         */
        //if min had children, set their parent fields to null and insert them into topmost list
        if (oldMin.child != null) {
            //parents set to null
            oldMin.child.parent = null;
            for (Node temp = oldMin.child.right; temp != oldMin.child; temp = temp.right) {
                temp.parent = null;
            }
            //insert them into topmost list
            Node BP = oldMin.child.left;
            Node minleft = min.left;
            min.left = BP;
            BP.right = min;
            minleft.right = oldMin.child;
            oldMin.child.left = minleft;
        }
        //if heap contains only 1 element, return that and set min to null
        if (min.right == min && min.child == null) {
            min = null;
            return (T) oldMin.data;
        } //else remove the min from topmost list
        else {
            oldMin.right.left = oldMin.left;
            oldMin.left.right = oldMin.right;
            if (oldMin == oldMin.right) {
                min = null;
            } else {
                min = oldMin.right;
            }
        }
        /**
         * Phase 1 COMPLETED
         */
        /**
         * Phase 2: We decrease the number of roots by successively linking
         * together roots of the same degree
         */
        //Create a tree table for pairwise combining of size maxDegree (which is ~log base phi of number of nodes)
        Node[] degreeTreeTable = new Node[maxDegree];

        Node begin = min;
        Node current = min;
        do {
            Node x = current;
            Node next = current.right;
            int currentDegree = x.degree;
            //while there are trees of same degree, combine
            while (degreeTreeTable[currentDegree] != null) {

                Node y = degreeTreeTable[currentDegree];
                if (x.key > y.key) {
                    Node temp = y;
                    y = x;
                    x = temp;
                }
                if (y == begin) {
                    begin = begin.right;
                }
                if (y == next) {
                    next = next.right;
                }
                y.setParent(x);
                degreeTreeTable[currentDegree] = null;
                currentDegree++;
            }
            degreeTreeTable[currentDegree] = x;
            current = next;
        } while (current != begin);

        /**
         * Phase 2 COMPLETE
         */
        /**
         * Phase 3: We check each of the remaining roots and find the minimum
         */
        min = begin;
        for (Node combinedNode : degreeTreeTable) {
            if (combinedNode != null && combinedNode.key < min.key) {
                min = combinedNode;
            }
        }
        /**
         * Phase 3 COMPLETE
         */
        return (T) oldMin.data;
    }

    /**
     * Melds two Fibonacci heaps in O(1)
     * <p>
     * @param fh1
     * @param fh2 <p>
     * @return
     */
    public FibonacciHeap<T> MeldHeaps(FibonacciHeap<T> fh1, FibonacciHeap<T> fh2) {
        /**
         * There are 4 possible outcomes depending on either one being null or
         * not
         */
        if (fh1 != null && fh2 != null) {
            FibonacciHeap result = new FibonacciHeap();
            Node A = fh1.min.right;
            fh1.min.right = fh2.min.right;
            fh2.min.right = A;
            result.n = fh1.n + fh2.n;
            if (fh1.min.key < fh2.min.key) {
                result.min = fh1.min;
            } else {
                result.min = fh2.min;
            }
            return result;
        } else if (fh1 == null && fh2 != null) {
            return fh2;
        } else if (fh1 != null && fh2 == null) {
            return fh1;
        } else { //both null
            return null;
        }

    }

    /**
     * Removes the given node from heap
     * <p>
     * @param theNode
     */
    public void remove(Node theNode) {
        decreaseKey(theNode, Integer.MIN_VALUE);
        removeMin();
    }

    /**
     * Decrease the key of some given node to the new given key, updates min
     * pointer and does a cascading cut, if necessary
     * <p>
     * @param theNode
     * @param newKey
     */
    public void decreaseKey(Node theNode, int newKey) {
        theNode.key = newKey;
        Node y = theNode.parent;
        //if theNode is not a root and new key < parent key, remove subtree
        if (theNode.parent != null && theNode.key < theNode.parent.key) {
            theNode.cascadingCut();
        }

        //possible update of min pointer
        if (min != null && theNode.key < min.key) {
            min = theNode;
        } else if (min == null) {
            min = theNode;
        }
    }

    /**
     * Deletes the given node from heap
     * <p>
     * @param theNode
     */
    public void delete(Node theNode) {
        decreaseKey(theNode, Integer.MIN_VALUE);
        removeMin();
    }

    public static class Node<T> implements Comparable {

        T data;
        int key;

        private Node parent;
        private Node child;
        private Node right;
        private Node left;

        private int degree;

        private boolean childCut;

        /**
         * Create a new node with the given key and data
         * <p>
         * @param key
         * @param data
         */
        public Node(int key, T data) {
            this.key = key;
            this.data = data;
            degree = 0;
            selfLinking();
            childCut = false;
            child = null;
            parent = null;
        }

        private void selfLinking() {
            right = this;
            left = this;
        }

        /**
         * Make this node child of another given node
         * <p>
         * @param parent
         */
        public void setParent(Node parent) {
            //removing this node from its linked list
            left.right = right;
            right.left = left;

            this.parent = parent;

            if (parent.child == null) {
                parent.child = this;
                right = this;
                left = this;
            } else {
                right = parent.child;
                left = parent.child.left;
                left.right = this;
                parent.child.left = this;
                parent.child = this;
            }
            parent.degree++;
            childCut = false;
        }

        /**
         * Removes this node from its linked list and does a cascading cut, if
         * necessary
         * <p>
         */
        public void cascadingCut() {
            childCut = false;
            if (parent == null) {
                return;
            }
            //remove from its linked list if it has siblings
            if (right != this) {
                right.left = left;
                left.right = right;
            }

            //adjust parent
            if (parent.child == this) {
                if (right != this) {
                    parent.child = right;
                } else {
                    parent.child = null;
                }
            }
            parent.degree--;
            if (parent.degree == 0) {
                parent.child = null;
            } else if (parent.child == this) {
                parent.child = right;
            }
            //No longer need the siblings, nullify them, add this to topmost list
            right = min;
            left = min.left;
            min.left = this;
            left.right = this;
            //Cascade to parent if necessary
            if (parent.childCut) {
                parent.cascadingCut();
            } else {
                parent.childCut = true;
            }
            //Node is in topmost list ==> doesn't have a parent :(
            parent = null;
        }

        @Override
        public int compareTo(Object o) {
            if (this.key == ((Node) o).key) {
                return 0;
            } else if (this.key > ((Node) o).key) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}
