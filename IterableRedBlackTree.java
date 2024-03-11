import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;


public class IterableRedBlackTree<T extends Comparable<T>>
    extends RedBlackTree<T> implements IterableSortedCollection<T> {

    private Comparable<T> point = (new Comparable<T>() {
        public int compareTo(T o) {
            return (-1);
        }
    });

    public void setIterationStartPoint(Comparable<T> startPoint) {
        if (startPoint != null) {
            point = startPoint;
        }
    }

    public Iterator<T> iterator() {
        Iterator<T> it = new RBTIterator<T>(root, point);
        return it;
    }

    private static class RBTIterator<R> implements Iterator<R> {

        private Comparable<R> start = (new Comparable<R>() {
            public int compareTo(R o) {
                return (-1);
            }
        });

        private Stack<Node<R>> stack = new Stack<Node<R>>();

        public RBTIterator(Node<R> root, Comparable<R> startPoint) {
            start = startPoint;
            stack = new Stack<Node<R>>();
            buildStackHelper(root);
        }

        private void buildStackHelper(Node<R> node) {
            if (node == null) {
                return;
            }
            if (start.compareTo(node.data) > 0) {
                buildStackHelper(node.down[1]);
            } else {
                stack.push(node);
                buildStackHelper(node.down[0]);
            }
        }

        public boolean hasNext() {
            return (!stack.empty());
        }

        public R next() {
            if (stack.empty()) {
                throw new NoSuchElementException();
            }
            Node<R> popped = stack.pop();
            buildStackHelper(popped.down[1]);
            return popped.data;

        }
    }

    @Override
    protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
        if (newNode == null)
            throw new NullPointerException("new node cannot be null");

        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                // if (compare == 0) {
                //     return false;
                // }
                if (compare <= 0) {
                    // insert in left subtree
                    if (current.down[0] == null) {
                        // empty space to insert into
                        current.down[0] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[0];
                    }
                } else {
                    // insert in right subtree
                    if (current.down[1] == null) {
                        // empty space to insert into
                        current.down[1] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[1];
                    }
                }
            }
        }
    }

    @Override
    public boolean insert(T data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("Cannot insert data value null into the tree.");
        RBTNode insert = new RBTNode<T>(data);
        boolean success = insertHelper(insert);
        this.enforceRBTreePropertiesAfterInsert(insert);
        ((RBTNode<T>) this.root).isBlack = true;
        return success;
    }
}
