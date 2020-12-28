import java.io.*;
import java.util.*;



/*************************************************************************
 *  Compilation:  javac DLB.java
 *  Execution:    java DLB < TinyInput.txt
 *  Execution:    java DLB < LargeInput.txt
 *  Dependencies: StdIn.java, StdOut.java, Queue.java
 *
 *  A symbol table for String keys implemented using
 *  a De La Briandais (DLB) Trie.
 *
 *************************************************************************/

public class DLB<Value> implements DictInterface{
    private static final char SENTINEL = '^';

    private Node root;

    // test client

public DLB()
{
  Node root = new Node();
}
    

   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
   

    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return true if the key is in the symbol table
     *         and false if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    

    public Node get(Node x, String key, int pos) {
        Node result = null;
        if(x != null){
          if(x.letter == key.charAt(pos)){
            if(pos == key.length()-1){
              result = x;
            } else {
              result = get(x.child, key, pos+1);/*TODO: Recurse on the child node*/;
            }
          } else {
            result=get(x.sibling, key ,pos);/*TODO: Recurse on the next sibling node*/;
          }
        }
        return result;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key) {
        
        key = key + SENTINEL;
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int pos) { //val is index of word in file, key is the word, pos starts at 0
        Node result = x;
        if (x == null){
            result = new Node();
            result.letter = key.charAt(pos);
          
            if(pos < key.length()-1){
              result.child = put(result.child, key, pos+1);/*TODO: Recurse on the child node*/;
            } 
        } 
        else if(x.letter == key.charAt(pos)) {
            if(pos < key.length()-1){
             result.child=put(result.child, key, pos+1); /*TODO: Recurse on the child node*/;
            } 
        } 

        else {
          result.sibling=put(result.sibling, key, pos);/*TODO: Recurse on the sibling node*/;
        }
        return result;
    }


    public boolean add(String s) //add the nodes using put. return true if add was successful
    {

      s=s+SENTINEL;

      if (root==null){
        put(s);
      }
      if (put(root, s, 0 ).letter != SENTINEL){   //if add successful
        return true;
      }

      return false; //add not successful
    }//end add


    public int searchPrefix(StringBuilder s){       

      String ss = s.toString();

      Node res = get(root,ss, 0);

      if (res ==null){           //not word or prefix in dictionary
        return 0;
      }

      if (res.child.letter==SENTINEL)  //if the  child node is the sentinel
      {

        if (res.child.sibling !=null)  //and there is a sibling to the sentinel, it's a word and prefix
        {
          return 3;
        }

        else{

          return 2;                   //if sentinel has no siblings, it's just a word
        }

    }

      else{

        return 1;                   //get doesn't return null, it's a prefix
      }

 }


  public int searchPrefix(StringBuilder s, int start, int end)
  {


    String ss = s.toString().substring(start,end+1);

    Node res = get(root,ss, 0);

    if(res == null)
      {
        return 0;
      }


    if (res.child.letter == SENTINEL)
    {

      if (res.child.sibling != null){
        return 3;
      }

      else{

        return 2;
      }


    }

    else{

      return 1;
    }


  }

   

 

    private class Node {
        private char letter;
        private Node sibling;
        private Node child;
         //for level-order traversal
    }
}//end class