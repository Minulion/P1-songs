// == CS400 Spring 2024 File Header Information ==
// Name: Andrew Kim
// Email: <akim227@wisc.edu>
// Lecturer: Florian Heimerl
// Notes to Grader:

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Implementation of Red Black Tree using the Binary Search Tree class from week 1.
 */
public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {
    
    /**
     * Class that represents a node in the RBT. Contains color data.
     */
    protected static class RBTNode<T> extends Node<T> {
        public boolean isBlack = false;
        public RBTNode(T data) { super(data); }
        public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
        public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
        public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1]; }
    }

    /**
     * Balances the RBT when given a new red node to be added.
     * @param redNode the node to be inserted
     */
    protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> redNode) {
        if (redNode.getUp() == null) { //if root, make black and resolve
            redNode.isBlack = true;
        } else if (!(redNode.getUp().isBlack)) { //check if two reds in a row
            if (redNode.getUp().getUp() != null &&  redNode.getUp().getUp().getDownLeft() == redNode.getUp()) { //if parent is left, check right for aunt
                if (redNode.getUp().getUp().getDownRight() != null && !(redNode.getUp().getUp().getDownRight().isBlack)) { //if aunt exists and red
                    //color swap
                    redNode.getUp().isBlack = true;
                    redNode.getUp().getUp().getDownRight().isBlack = true;
                    redNode.getUp().getUp().isBlack = false;
                    this.enforceRBTreePropertiesAfterInsert(redNode.getUp().getUp());
                } else { //aunt is black or doesnt exist
                    if (redNode.getUp().getDownRight() == redNode) { //if child is right, rotate
                        super.rotate(redNode, redNode.getUp());
                        this.enforceRBTreePropertiesAfterInsert(redNode.getDownLeft());
                    } else { //child is left, left left case
                        //color swap and rotate
                        redNode.getUp().isBlack = true;
                        redNode.getUp().getUp().isBlack = false;
                        super.rotate(redNode.getUp(), redNode.getUp().getUp());
                    }
                }
            } else { //parent is right, check left for aunt
                if (redNode.getUp().getUp().getDownLeft() != null && !(redNode.getUp().getUp().getDownLeft().isBlack)) { //if aunt exists and red
                    //color swap
                    redNode.getUp().isBlack = true;
                    redNode.getUp().getUp().getDownLeft().isBlack = true;
                    redNode.getUp().getUp().isBlack = false;
                    this.enforceRBTreePropertiesAfterInsert(redNode.getUp().getUp());
                } else { //aunt is black or doesnt exist
                    if (redNode.getUp().getDownLeft() == redNode) { //if child is left, rotate
                        super.rotate(redNode, redNode.getUp());
                        this.enforceRBTreePropertiesAfterInsert(redNode.getDownRight());
                    } else { //child is right, right right case
                        //color swap and rotate
                        redNode.getUp().isBlack = true;
                        redNode.getUp().getUp().isBlack = false;
                        super.rotate(redNode.getUp(), redNode.getUp().getUp());
                    }
                }
            }
            
        }
    }

    /**
     * Inserts a new RBTNode into the RBT. Calls method to uphold properties.
     * @param data value of RBT node
     * @return true if value successfully inserted, false otherwise
     * @throws NullPointerException when the provided data argument is null
     */
    @Override
    public boolean insert(T data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("Cannot insert data value null into the tree.");
        RBTNode insert = new RBTNode<T>(data);
        boolean success = super.insertHelper(insert);
        this.enforceRBTreePropertiesAfterInsert(insert);
        ((RBTNode<T>)this.root).isBlack = true;
        return success;
    }

    /**
     * Tests insertion at the root
     */
    @Test 
    public void testInsertRoot() {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);
        Assertions.assertTrue(((RBTNode<T>)tree.root).data.equals(5));
        Assertions.assertTrue(((RBTNode<T>)tree.root).isBlack);
    } 

    /**
     * Tests recoloring operation
     */
    @Test
    public void testRecolor() {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);
        tree.insert(4);
        tree.insert(6);
        tree.insert(3);
        Assertions.assertTrue(((RBTNode<T>)tree.root).data.equals(5));
        Assertions.assertTrue(((RBTNode<T>)tree.root).isBlack);
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownLeft().isBlack);
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownRight().isBlack);
        Assertions.assertFalse(((RBTNode<T>)tree.root).getDownLeft().getDownLeft().isBlack);
    }

    /**
     * Tests rotation operation 
     */
    @Test
    public void testRotate() {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);
        tree.insert(4);
        tree.insert(6);
        tree.insert(3);
        tree.insert(2);
        tree.insert(1);
        System.out.println(tree.toString());
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownLeft().data.equals(3));
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownRight().data.equals(6));
        Assertions.assertFalse(((RBTNode<T>)tree.root).getDownLeft().isBlack);
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownRight().isBlack);
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownLeft().getDownLeft().isBlack);
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownLeft().getDownRight().isBlack);
        tree.insert(8);
        tree.insert(7);
        System.out.println(tree.toString());
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownRight().data.equals(7));
        Assertions.assertTrue(((RBTNode<T>)tree.root).getDownRight().isBlack);
        Assertions.assertFalse(((RBTNode<T>)tree.root).getDownRight().getDownRight().isBlack);
        Assertions.assertFalse(((RBTNode<T>)tree.root).getDownRight().getDownLeft().isBlack);

    }
}
