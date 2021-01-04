
/**
 This class tests the code for Lab2: Exercise4. It describes a simple
 GenericQueue class using generics.
 It expands upon a framework provided by Srini.
 */

import java.util.ArrayList;

public class Queue<T>{
    // declare all required fields
    private ArrayList<T> queue;
    private int size;
    private int cursor;

    /**
     Constructor 1
     No args.
     */
    public Queue(){
        queue = new ArrayList<>();
        size = 0;
        cursor = 0;
    }

    //return the size of the queue
    public int size(){
        return queue.size();
    }

    //return the first element of the queue
    public T peek(){
        //return null id the queue is empty
        if (queue==null){
            return null;
        }else{
            return queue.get(0);
        }
    }

    //removing the first element from queue
    public T dequeue(){
        //return null if queue is empty
        if (queue==null){
            return null;
        }else{
            size--;
            return queue.remove(0);
        }
    }

    //adding the element to queue
    public void enqueue(T element){
        queue.add(element);
        size++;
    }

    //checking is queue is empty
    public boolean isEmpty(){
        return queue.isEmpty();
    }

    //clear the queue
    public void clear(){
        queue.clear();
    }

    //return the position of item in the queue
    public int positionOf(T item){
        if (queue.contains(item)){
            return queue.indexOf(item);
        }else{
            return -1;
        }
    }

    //remove the first element from the queue
    public void remove(T item){
        if(queue.remove(item)){
            size--;
            if(cursor>0){
                cursor--;
            }
        }
    }

    //return the first element from queue
    public T first(){
        if(queue==null){
            return null;
        }else{
            return queue.get(0);
        }
    }

    //return the next element according to the previous element
    public T next(){
        if(cursor>=size){
            cursor=0;
            return null;
        }
        return queue.get(cursor);
    }

    //print the queue
    public void printQueue(){
        for (int i = 0; i <= (queue.size() - 1); i++) {
            System.out.println(queue.get(i).toString());
        }
    }


}

