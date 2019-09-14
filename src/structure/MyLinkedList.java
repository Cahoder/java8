package structure;

//单链表
public class MyLinkedList {

    public static void main(String[] args) {
        MyLinkedList singleLinkedList = new MyLinkedList();
        singleLinkedList.addAtHead(1);
        singleLinkedList.print();
        singleLinkedList.addAtTail(3);
        singleLinkedList.print();
        singleLinkedList.addAtIndex(1,2);
        singleLinkedList.print();
        singleLinkedList.deleteAtIndex(1);
        singleLinkedList.print();
    }

    //定义一个Node节点类储存链表中每一个节点对象
    private class MyNode{
        int value;
        MyNode next;
        private MyNode(int value){
            this.value = value;
            this.next = null;
        }
    }

    //定义一个头(根)节点
    private MyNode root;
    //定义链表的长度
    private int length;

    /** Initialize your data structure here. */
    public MyLinkedList() {
        this.root = new MyNode(-1);
        this.length = 0;
    }

    /** Get the value of the index-th node in the linked list. If the index is invalid, return -1. */
    public int get(int index) {
        if (index >= length || index < 0 ) return -1;
        MyNode current = null;
        for (int i = 0; i <= index; i++) {
            if (i == 0) current = root;
            else current = current.next;
        }
        return current.value;
    }

    /** Add a node of value val before the first element of the linked list. After the insertion, the new node will be the first node of the linked list. */
    public void addAtHead(int val) {
        if (length == 0 ) root.value = val;
        else{
            MyNode temp = new MyNode(val);
            temp.next = this.root;
            this.root = temp;
        }
        length++;
    }

    /** Append a node of value val to the last element of the linked list. */
    public void addAtTail(int val) {
        if (length == 0 ) root.value = val;
        else {
            MyNode temp = new MyNode(val);
            MyNode current = root;
            while (current.next!=null){
                current = current.next;
            }
            current.next = temp;
        }
        length++;
    }

    /** Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list, the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted. */
    public void addAtIndex(int index, int val) {
        if (index > length) return;
        if (index == this.length) {addAtTail(val);return;}
        if (index <= 0) {addAtHead(val);return;}

        MyNode temp = new MyNode(val);
        MyNode node = null;  //找需要插入的节点的前一个节点
        for (int i = 0; i < index; i++) {
            if (i == 0) node = root;
            else node = node.next;
        }
        temp.next = node.next;
        node.next = temp;
        length++;
    }

    /** Delete the index-th node in the linked list, if the index is valid. */
    public void deleteAtIndex(int index) {
        if (index < 0 || index >= length) return;
        if (index == 0){
            root = root.next;
            if (root == null) root = new MyNode(-1);
            length--;
            return;
        }

        MyNode node = root; //找需要插入的节点的前一个节点
        for (int i = 1; i < index; i++) {
            node = node.next;
        }

        //判断是否有下一个节点
        if (node.next.next == null){
            node.next = null;
        }else{
            node.next = node.next.next;
        }
        length--;
    }

    public void print(){
        MyNode current = root;
        StringBuilder builder = new StringBuilder();
        while(current != null){
            builder.append(String.valueOf(current.value) + " ");
            current = current.next;
        }
        System.out.println("LinkedList Length:" + length + "-->" + builder);
    }
}

/**
 * Your MyLinkedList object will be instantiated and called as such:
 * MyLinkedList obj = new MyLinkedList();
 * int param_1 = obj.get(index);
 * obj.addAtHead(val);
 * obj.addAtTail(val);
 * obj.addAtIndex(index,val);
 * obj.deleteAtIndex(index);
 */