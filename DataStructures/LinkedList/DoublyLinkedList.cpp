/*
I am planning to implement this classic data structure with modern C++ idiomatics
Using Smart pointers rather than raw pointers to handle linkages.
*/
#include "DoublyLinkedList.h"


template <typename T>
Node<T>::Node() {
    this->next = nullptr;
    this->prev = nullptr;
}

template <typename T>
Node<T>::Node(T data) {
    this->next = nullptr;
    this->prev = nullptr;
    this->data = data;
}

template <typename T>
void Node<T>::detach() {
    // Removes all outgoing and incoming links
    auto prev = this->prev;
    auto next = this->next;
    this->next = nullptr;
    this->prev = nullptr;
    if (prev != nullptr) {
        prev->next = next;
    }
    if (next != nullptr) {
        next->prev = prev;
    }
}

template <typename T>
void Node<T>::insertAfter(T data) {
    // Insert data as a new Node after `this`
    auto newNode = make_shared<Node<T>>(data);
    auto right = this->next;
    this->next = newNode;
    newNode->prev = this->shared_from_this();
    newNode->next = right;
    if (right != nullptr) {
        right->prev = newNode;
    }
}

template <typename T>
void Node<T>::insertBefore(T data) {
    // Insert data as a new Node before `this`
    auto newNode = make_shared<Node<T>>();
    auto left = this->prev;
    this->prev = newNode;
    newNode->next = this->shared_from_this();
    newNode->prev = left;
    if (left != nullptr) {
        left->next = newNode;
    }
}


template <typename T>
DoublyLinkedList<T>::DoublyLinkedList() {
    // Create empty DoublyLinkedList
    this->head = nullptr;
    this->tail = nullptr;
    this->size = 0;
}

template <typename T>
DoublyLinkedList<T>::DoublyLinkedList(T data) {
    // Create a DoublyLinkedList with a single node
    this->head = make_shared<Node<T>>(data);
    this->tail = this->head;
    this->size = 1;
}

template <typename T>
void DoublyLinkedList<T>::throwExceptionIfEmpty() {
    // Called whenever the operation requires a non-empty list
    if (this->size == 0) {
        throw out_of_range("The DoublyLinkedList is empty");
    }
}

template <typename T>
void DoublyLinkedList<T>::throwExceptionIfOutOfRange(int index) {
    if (index >= this->size) {
        throw out_of_range("The index is out of range");
    }
}

template <typename T>
T DoublyLinkedList<T>::getAtIndex(int index) {
    this->throwExceptionIfEmpty();
    this->throwExceptionIfOutOfRange(index);
    int i = 0;
    auto current = this->head;
    while (i < index) {
        i++;
        current = current->next;
    }
    return current->data;
}

template <typename T>
void DoublyLinkedList<T>::insertAtFront(T data) {
    auto newHead = make_shared<Node<T>>(data);
    // Check if it's empty
    if (this->size == 0) {
        this->head = newHead;
        this->tail = newHead;
        this->size = 1;
        return;
    }
    newHead->next = this->head;
    this->head->prev = newHead;
    this->head = newHead;
    this->size += 1;
}
template <typename T>
void DoublyLinkedList<T>::insertAtEnd(T data) {
    // Check if it's empty
    if (this->size == 0) {
        this->head = make_shared<Node<T>>(data);
        this->tail = this->head;
        this->size = 1;
        return;
    }
    this->tail->insertAfter(data);
    this->tail = this->tail->next;
    this->size += 1;
}
template <typename T>
void DoublyLinkedList<T>::insertAtIndex(int index, T data) {
    // We need to reach index-1, where we will call insertAfter
    // Thus we need to check if index-1 is within range
    int target = index - 1;
    this->throwExceptionIfOutOfRange(target);

    // If inserting at the front or at the end
    // We will update the head and tail with specific methods
    if (index == 0) {
        this->insertAtFront(data);
        return;
    }
    // The last insertable position is [size]
    else if (index == this->size) {
        this->insertAtEnd(data);
        return;
    }

    // Check if it's faster to traverse from the beginning or the end
    if (target <= size - 1 - target) {
        // Traverse from the beginning
        auto current = this->head;
        int i = 0;
        while (i < target) {
            i++;
            current = current->next;
        }
        current->insertAfter(data);
        this->size += 1;
    }
    else {
        // Traverse from the end
        auto current = this->tail;
        int i = this->size - 1;
        while (i > target) {
            i--;
            current = current->prev;
        }
        current->insertAfter(data);
        this->size += 1;
    }

}
template <typename T>
void DoublyLinkedList<T>::deleteHead() {
    this->throwExceptionIfEmpty();
    // Check if there's only one node
    if (this->size == 1) {
        this->head = nullptr;
        this->tail = nullptr;
        this->size = 0;
        return;
    }
    auto newHead = this->head->next;
    this->head->next = nullptr;
    newHead->prev = nullptr;
    this->head = newHead;
    this->size -= 1;
}

template <typename T>
void DoublyLinkedList<T>::deleteTail() {
    this->throwExceptionIfEmpty();
    if (this->size == 1) {
        this->head = nullptr;
        this->tail = nullptr;
        this->size = 0;
        return;
    }
    auto newTail = this->tail->prev;
    this->tail->prev = nullptr;
    newTail->next = nullptr;
    this->tail = newTail;
    this->size -= 1;
}

template <typename T>
void DoublyLinkedList<T>::deleteAtIndex(int index) {
    // Use specific methods to handle updating of head and tail

    if (index == 0) {
        this->deleteHead();
        return;
    }
    else if (index == this->size - 1) {
        this->deleteTail();
        return;
    }

    this->throwExceptionIfEmpty();

    // We need to traverse to index and call current->detach()
    // Check if it's faster to traverse from the front or the end
    if (index <= size - 1 - index) {
        // Traverse from the beginning
        auto current = this->head;
        int i = 0;
        while (i < index) {
            i++;
            current = current->next;
        }
        current->detach();
        this->size -= 1;
    }
    else {
        // Traverse from the end
        auto current = this->tail;
        int i = this->size - 1;
        while (i > index) {
            i--;
            current = current->prev;
        }
        current->detach();
        this->size -= 1;
    }
}

template <typename T>
void DoublyLinkedList<T>::printList() {
    if (head == nullptr) {
        cout << "The DoublyLinkedList is empty" << endl;
        return;
    }
    auto current = this->head;
    cout << current->data << ", ";
    while (current->next != nullptr) {
        current = current->next;
        cout << current->data << ", ";
    }
    cout << endl;
}


int main() {
    auto list = make_unique<DoublyLinkedList<int>>(1);  // 1
    list->insertAtFront(2);                             // 2, 1
    list->insertAtFront(3);                             // 3, 2, 1
    list->printList();
    list->insertAtEnd(4);                               // 3, 2, 1, 4
    list->printList();
    list->insertAtEnd(5);                               // 3, 2, 1, 4, 5
    list->printList();
    list->insertAtIndex(0, 6);                          // 6, 3, 2, 1, 4, 5
    list->insertAtIndex(1, 7);                          // 6, 7, 3, 2, 1, 4, 5
    list->deleteAtIndex(0);                             // 7, 3, 2, 1, 4, 5
    list->deleteAtIndex(1);                             // 7, 2, 1, 4, 5
    list->printList();
    list->deleteAtIndex(4);                             // 7, 2, 1, 4
    list->printList();
}