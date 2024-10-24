// DoublyLinkedList.h
#ifndef DOUBLYLINKEDLIST_H
#define DOUBLYLINKEDLIST_H

#include <iostream>
#include <memory> 
#include <stdexcept>
using std::unique_ptr;
using std::shared_ptr;
using std::enable_shared_from_this;
using std::make_shared;
using std::make_unique;
using std::out_of_range;
using std::cout;
using std::endl;

template <typename T>
class Node : public std::enable_shared_from_this<Node<T>> {
    template <typename T>
    friend class DoublyLinkedList;
private:
    shared_ptr<Node<T>> next;
    shared_ptr<Node<T>> prev;
    T data;

public:
    Node();
    Node(T data);
    void detach();
    void insertAfter(T data);
    void insertBefore(T data);
};

template <typename T>
class DoublyLinkedList {
private:
    shared_ptr<Node<T>> head;
    shared_ptr<Node<T>> tail;
    int size;
public:
    DoublyLinkedList();
    DoublyLinkedList(T data);

    void throwExceptionIfEmpty();
    void throwExceptionIfOutOfRange(int index);
    T getAtIndex(int index);
    void insertAtFront(T data);
    void insertAtEnd(T data);
    void insertAtIndex(int index, T data);
    void deleteHead();
    void deleteTail();
    void deleteAtIndex(int index);
    void printList();
};

#endif // DOUBLYLINKEDLIST_H