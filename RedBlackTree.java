// -== CS400 Spring 2024 File Header Information ==-
// Name: Yanjun Yang
// Email: yyang.748@wisc.edu
// Lecturer: Florian Heimerl
// Notes to Grader: None

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {
  /**
   * This class represents a node holding a single value within a Red Black tree.
   */
  protected static class RBTNode<T> extends Node<T> {
    public boolean isBlack = false;

    public RBTNode(T data) { super(data); }

    public RBTNode<T> getUp() { return (RBTNode<T>) this.up; }

    public RBTNode<T> getDownLeft() { return (RBTNode<T>) this.down[0]; }

    public RBTNode<T> getDownRight() { return (RBTNode<T>) this.down[1]; }
  }
  
  /**
   * Resolves any red property violations that are introduced by inserting 
   * a new node into the Red-Black Tree
   * @param added the node that might have a property violation
   */
  protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> added) {
    // root node
    if (added.getUp() == null || added.getUp().getUp() == null) {
      return;
    }

    // store parent, aunt, and grandparent
    RBTNode<T> parent = added.getUp();
    RBTNode<T> gParent = parent.getUp();
    RBTNode<T> aunt;
    if (parent.isRightChild()){
      aunt = gParent.getDownLeft();
    } else {
      aunt = gParent.getDownRight();
    }
    // reordering required with double red node
    if (!parent.isBlack){
      // red aunt
      if (aunt != null && !aunt.isBlack) {
        // recolor parent, aunt, and grandparent
        parent.isBlack = true;
        aunt.isBlack = true;
        gParent.isBlack = false;
        // check if the recolored grandparent causes RBT rule violation
        enforceRBTreePropertiesAfterInsert(gParent);
      }
      // black aunt
      else {
        // check if rotation is needed beforehand
        if (added.isRightChild() != parent.isRightChild()){
          rotate(added, parent); // rotate first
          // rotate and color swap
          rotate(added, gParent);
          added.isBlack = true;
          gParent.isBlack = false;
        }
        else {
          // rotate & color swap
          rotate(parent, gParent);
          parent.isBlack = true;
          gParent.isBlack = false;
        }
      }
    }
  }

  /**
   * Inserts a new data value into the tree.
   * This tree will not hold null references, nor duplicate data values.
   * @param data to be added into this red black tree
   * @return true if the value was inserted, false if is was in the tree already
   * @throws NullPointerException when the provided data argument is null
   */
  public boolean insert(T data) throws NullPointerException {
    // check for null values
    if (data == null)
      throw new NullPointerException("Cannot insert data value null into the tree.");

    // node to be added
    RBTNode<T> toAdd = new RBTNode<T>(data);
    boolean success = this.insertHelper(toAdd);

    // check for property violations
    enforceRBTreePropertiesAfterInsert(toAdd);

    // set root to black
    ((RBTNode<T>) this.root).isBlack = true;

    // return true if succesfully added
    return success;
  }

  /**
   * Tests the insertion into a RBT where after inserting reordering is needed and the aunt is a red
   * node. (Case without recursive call)
   */
  @Test
  void case1() {
    RedBlackTree<Integer> test = new RedBlackTree<Integer>();

    // Test 1: check root gets recolored
    test.insert(15);
    assertTrue(((RBTNode<Integer>) test.root).isBlack, "Root was not changed to black");

    // Test 2: Aunt is red (simple recolor)
    test.insert(7);
    test.insert(18);
    test.insert(23);

    // check is valid tree
    String expected = "[ 7, 15, 18, 23 ]";
    assertEquals(expected, test.toInOrderString());

    // check order
    expected = "[ 15, 7, 18, 23 ]";
    assertEquals(expected, test.toLevelOrderString());

    // check color of nodes (23 should be the only red)
    RBTNode<Integer> root = ((RBTNode<Integer>) test.root);
    assertFalse(root.getDownRight().getDownRight().isBlack, "Node 23 was not added as red");
    assertTrue(root.isBlack, "Root was not changed to black"); // check root
    assertTrue(root.getDownLeft().isBlack, "Node 7 was not changed to black"); // check 7
    assertTrue(root.getDownRight().isBlack, "Node 18 was not changed to black"); // check 18
  }

  /**
   * Tests the insertion into a RBT where after inserting reordering is needed and the aunt is a
   * black or a null node
   */
  @Test
  void case2() {
    RedBlackTree<Integer> test = new RedBlackTree<Integer>();
    test.insert(7);

    // Test 1: Aunt is null (rotate & color swap)
    test.insert(15);
    test.insert(18);

    // check order
    String expected = "[ 15, 7, 18 ]";
    assertEquals(expected, test.toLevelOrderString());

    // check color of nodes
    RBTNode<Integer> root = ((RBTNode<Integer>) test.root);
    assertTrue(root.isBlack, "Root was not changed to black"); // check root
    assertFalse(root.getDownLeft().isBlack, "Node 7 was not changed to red"); // check 7
    assertFalse(root.getDownRight().isBlack, "Node 18 was not added as red"); // check 18

    // Test 2: Not all on the same side (2 rotate & color swap)
    test.insert(25);
    test.insert(1);
    test.insert(10);
    test.insert(20);

    // check is valid tree
    expected = "[ 1, 7, 10, 15, 18, 20, 25 ]";
    assertEquals(expected, test.toInOrderString());

    // check order
    expected = "[ 15, 7, 20, 1, 10, 18, 25 ]";
    assertEquals(expected, test.toLevelOrderString());

    // check color of nodes
    root = ((RBTNode<Integer>) test.root);
    assertTrue(root.isBlack, "Root was not changed to black"); // check root
    assertTrue(root.getDownRight().isBlack, "Node 20 was not changed to black"); // check 20
    assertFalse(root.getDownRight().getDownRight().isBlack,
        "Node 18 was not changed to red"); // check 18
    assertFalse(root.getDownRight().getDownLeft().isBlack,
        "Node 25 was not changed to red"); // check 25
  }

  /**
   * Tests the insertion into a RBT where after inserting multiple times of reordering and rotation
   * is needed
   */
  @Test
  void case3() {
    // set up initial tree
    RedBlackTree<Integer> test = new RedBlackTree<Integer>();
    test.insert(7);
    test.insert(15);
    test.insert(18);
    test.insert(25);
    test.insert(5);
    test.insert(10);
    test.insert(20);
    test.insert(1);
    test.insert(4);

    // Test: recolor first, then rotate & swap
    test.insert(6);

    // check is valid tree
    String expected = "[ 1, 4, 5, 6, 7, 10, 15, 18, 20, 25 ]";
    assertEquals(expected, test.toInOrderString());

    // check order
    expected = "[ 7, 4, 15, 1, 5, 10, 20, 6, 18, 25 ]";
    assertEquals(expected, test.toLevelOrderString());

    // check colors
    RBTNode<Integer> root = ((RBTNode<Integer>) test.root);
    assertTrue(root.isBlack, "Root was not changed to black");
    assertFalse(root.getDownRight().isBlack, "Node 4 was not changed to black"); // check 20
    assertFalse(root.getDownLeft().isBlack, "Node 15 was not changed to red"); // check 18
    assertTrue(root.getDownLeft().getDownLeft().isBlack, "Node 1 was not changed to black"); // check 1
    assertTrue(root.getDownLeft().getDownRight().isBlack, "Node 5 was not changed to black"); // check 5
    assertFalse(root.getDownLeft().getDownRight().getDownRight().isBlack, "Node 6 not red black"); // check 6
  }

}

