import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.Arrays;


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
        if(newNode == null) throw new NullPointerException("new node cannot be null");

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
        ((RBTNode<T>)this.root).isBlack = true;
        return success;
    }

    @Test 
    public void testIntStartPoint() {
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(4, 5, 6));
        tree.insert(5);
        tree.insert(4);
        tree.insert(6);
        tree.insert(3);
        tree.setIterationStartPoint(4);
        for (Integer item : tree) {
            list.add(item);
        }

        Assertions.assertTrue(list.equals(expected));
    }

    @Test 
    public void testStringBasic() {
        IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));
        tree.insert("a");
        tree.insert("b");
        tree.insert("c");
        tree.insert("d");

        for (String item : tree) {
            list.add(item);
        }

        Assertions.assertTrue(list.equals(expected));
    }

    @Test 
    public void testIntDupes() {
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(4, 4, 5, 5, 6, 6));
        tree.insert(5);
        tree.insert(6);
        tree.insert(4);
        tree.insert(6);
        tree.insert(3);
        tree.insert(5);
        tree.insert(4);
        tree.insert(3);
        tree.setIterationStartPoint(4);
        for (Integer item : tree) {
            list.add(item);
        }

        Assertions.assertTrue(list.equals(expected));
    }

}
