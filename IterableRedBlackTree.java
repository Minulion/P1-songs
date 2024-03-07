import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IterableRedBlackTree<T extends Comparable<T>>
    extends RedBlackTree<T> implements IterableSortedCollection<T> {
  private Comparable<T> startPoint = (T) -> {return -1;};

  public void setIterationStartPoint(Comparable<T> startPoint) {
    // initialize start point
    this.startPoint = startPoint;
    // reset when null is passed
    if (startPoint == null) {
      this.startPoint = (T) -> {return -1;};
    }
  }

  public Iterator<T> iterator() {
    // returns a new RBTIterator object
    return new RBTIterator<T>(root, startPoint);
  }

  private static class RBTIterator<R> implements Iterator<R> {
    private Comparable<R> startPoint;
    private Stack<Node<R>> tracker;

	public RBTIterator(Node<R> root, Comparable<R> startPoint) {
      this.startPoint = startPoint; // store start point
      tracker = new Stack<Node<R>>(); // empty stack
      buildStackHelper(root); // calls buildStackHelper method
    }

	private void buildStackHelper(Node<R> node) {
      // node is null, add nothing
      if (node == null) {
        return;
      }
      // node less than start point, go right
      if (startPoint.compareTo(node.data) > 0) {
        buildStackHelper(node.down[1]);
      }
      // node greater than or equal to start point, add then go left
      else if (startPoint.compareTo(node.data) <= 0) {
        tracker.push(node);
        buildStackHelper(node.down[0]);
      }
    }

	public boolean hasNext() {
      return !tracker.isEmpty(); // if stack empty, no next
    }

	public R next() {
      // check if there is a next node, throw exception otherwise
      if (!hasNext()){
        throw new NoSuchElementException();
      }
      // pop the top node and store
      Node<R> data = tracker.pop();
      // if there is a right child, add it to the stack with helper method
      if (data.down[1] != null){
        buildStackHelper(data.down[1]);
      }
      // return data in popped node
      return data.data;
    }
  }

  /**
   * Performs a naive insertion into a binary search tree: adding the new node
   * in a leaf position within the tree. After this insertion, no attempt is made
   * to restructure or balance the tree.
   * @param newNode the new node to be inserted
   * @return true if the value was inserted, false if is was in the tree already
   * @throws NullPointerException when the provided node is null
   */
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
        //        if (compare == 0) {
        //          return false;
        //        }
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

  /**
   * Tests the iterator over red black trees containing String values and Integer values
   */
  @Test
  void case1() {
    // Case 1: Integer
    // new iterable RBT object that stores integers
    IterableRedBlackTree<Integer> test = new IterableRedBlackTree<Integer>();
    // inserts values
    test.insert(7);
    test.insert(11);
    test.insert(1);
    test.insert(120);
    test.insert(99);
    test.insert(30);
    test.insert(2);
    test.insert(50);
    test.insert(79);
    test.insert(-2);

    // expected return
    int[] expected = {-2, 1, 2, 7, 11, 30, 50, 79, 99, 120};
    int index = 0;
    // loop through using iterator (for each loop) and compare to expected array
    for (Integer data : test){
      assertEquals(data, expected[index], data + " not in correct place");
      index++;
    }

    // check that iterator looped through everything
    assertEquals(index, expected.length);

    // ---------------------------------------------------------------------------------
    // Case 2: String
    // new iterable RBT object that stores strings
    IterableRedBlackTree<String> test2 = new IterableRedBlackTree<String>();
    test2.insert("B");
    test2.insert("A");
    test2.insert("Z");
    test2.insert("F");
    test2.insert("X");
    test2.insert("M");

    // inserts values
    String[] expected2 = {"A", "B", "F", "M", "X", "Z"};
    index = 0;
    // loop through using iterator (for each loop) and compare to expected array
    for (String data : test2){
      assertEquals(data, expected2[index], data + " not in correct place");
      index++;
    }

    // check that iterator looped through everything
    assertEquals(index, expected2.length);
  }

  /**
   * Tests red black trees that have duplicate values
   */
  @Test
  void case2() {
    // Case 1: duplicate 11
    // new iterable RBT object that stores integers
    IterableRedBlackTree<Integer> test = new IterableRedBlackTree<Integer>();
    // inserts values
    test.insert(7);
    test.insert(11);
    test.insert(1);
    test.insert(120);
    test.insert(11);
    test.insert(99);
    test.insert(11);

    // expected return
    int[] expected = {1, 7, 11, 11, 11, 99, 120};
    int index = 0;
    // loop through using iterator (for each loop) and compare to expected array
    for (Integer data : test){
      assertEquals(data, expected[index], data + " not in correct place");
      index++;
    }

    // check that iterator looped through everything
    assertEquals(index, expected.length);

    // ------------------------------------------------------------------------------------
    // Case 2: multiple duplicates
    // inserts values
    test.insert(120);
    test.insert(7);

    // expected return
    int[] expected2 = {1, 7, 7, 11, 11, 11, 99, 120, 120};
    index = 0;
    // loop through using iterator (for each loop) and compare to expected array
    for (Integer data : test){
      assertEquals(data, expected2[index], data + " not in correct place");
      index++;
    }

    // check that iterator looped through everything
    assertEquals(index, expected2.length);
  }

  /**
   * Tests the functionality of serIterationStartPoint method. Should only iterate through values
   * larger than or equal to the input.
   */
  @Test
  void case3() {
    // Case 1: start point exists
    IterableRedBlackTree<Integer> test = new IterableRedBlackTree<Integer>();
    // insert data
    test.insert(7);
    test.insert(11);
    test.insert(1);
    test.insert(120);
    test.insert(11);
    test.insert(99);
    test.insert(100);
    test.insert(5);
    test.insert(120);

    // set start point at 11
    test.setIterationStartPoint(11);

    // expected return
    int[] expected = {11, 11, 99, 100, 120, 120};
    int index = 0;
    // loop through using iterator (for each loop) and compare to expected array
    for (Integer data : test){
      assertEquals(data, expected[index], data + " not in correct place");
      index++;
    }

    // check that iterator looped through everything
    assertEquals(index, expected.length);

    // ------------------------------------------------------------------------------------
    // Case 2: change start point
    test.setIterationStartPoint(12);

    // expected return
    int[] expected2 = {99, 100, 120, 120};
    index = 0;
    // loop through using iterator (for each loop) and compare to expected array
    for (Integer data : test){
      assertEquals(data, expected2[index], data + " not in correct place");
      index++;
    }

    // check that iterator looped through everything
    assertEquals(index, expected2.length);

    // ------------------------------------------------------------------------------------
    // Case 3: change to null
    test.setIterationStartPoint(null);

    // expected return
    int[] expected3 = {1, 5, 7, 11, 11, 99, 100, 120, 120};
    index = 0;
    // loop through using iterator (for each loop) and compare to expected array
    for (Integer data : test){
      assertEquals(data, expected3[index], data + " not in correct place");
      index++;
    }

    // check that iterator looped through everything
    assertEquals(index, expected3.length);
  }
}
