

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap {

    // fibonacciHeap`s fields
    private int size;
    private HeapNode firstRoot;
    private HeapNode minNode;
    private int numOfTrees;
    private int numOfMarkedNodes;

    // static fields of the class
    public static int countCuts=0;
    private static int countLinks=0;

    // fibonacciHeap`s constructor
    public FibonacciHeap(){
        this.size = 0;
        this.firstRoot = null;
        this.minNode = null;
        this.numOfTrees = 0;
        this.numOfMarkedNodes = 0;
    }

    // GETTERS:

    // returns heap`s size (=num of nodes in the heap)
    // complexity: O(1)
    public int getSize() {return this.size;}

    // returns first root in the heap`s root-list
    // complexity: O(1)
    public HeapNode getFirstRoot() {return this.firstRoot;}

    // returns the node with the minimal key in the heap
    // complexity: O(1)
    public HeapNode getMinNode() {return this.minNode;}

    // returns the number of trees in the heap
    // complexity: O(1)
    public int getNumOfTrees() {return this.numOfTrees;}

    // returns the number of marked-nodes in the heap
    // complexity: O(1)
    public int getNumOfMarkedNodes() {return this.numOfMarkedNodes;}



    // SETTERS:

    // sets heap`s size (number of nodes in the heap)
    // complexity: O(1)
    public void setsize(int size) {this.size = size;}

    // sets first root in the heap`s root-list
    // complexity: O(1)
    public void setFirstRoot(HeapNode firstRoot) {this.firstRoot = firstRoot;}

    // sets the node with the minimal key in the heap
    // complexity: O(1)
    public void setMinNode(HeapNode minNode) {this.minNode = minNode;}

    // sets the number of trees in the heap
    // complexity: O(1)
    public void setNumOfTrees(int numOfTrees) {this.numOfTrees = numOfTrees;}

    // sets the number of marked-nodes in the heap
    // complexity: O(1)
    public void setNumOfMarkedNodes(int numOfMarkedNodes) {this.numOfMarkedNodes = numOfMarkedNodes;}


    /**
     * public boolean isEmpty()
     *
     * Returns true if and only if the heap is empty.
     *
     */
    //complexity: O(1)
    public boolean isEmpty() {
        return this.size == 0;
    }

    // Inserts node B after A (to a circular linked-list)
    //complexity: O(1)
    private void insertAfter (HeapNode A, HeapNode B) {
        B.prev = A;
        B.next = A.next;
        A.next = B;
        B.next.prev = B;
    }

    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     *
     * Returns the newly created node.
     */
    //complexity: O(1)
    public HeapNode insert(int key)
    {
        // creates a node (of type HeapNode) which contains the given key
        HeapNode node = new HeapNode(key);
        node.setRank(0);

        // inserts it into the heap
        // case 1: heap is empty
        if (this.isEmpty()) {
            node.next = node;
            node.prev = node;
            this.minNode = node;
        }

        // case 2: heap is not empty
        else {
            insertAfter(firstRoot.prev, node);
            if (node.getKey() < this.minNode.getKey()) {this.minNode = node;}
        }

        this.firstRoot = node;
        this.size++;
        this.numOfTrees++;

        return node;
    }

    // deletes node (with children) from root-list -> inserts children as roots, at the proper position, by insertion order
    // assume: heap is not empty
    // complexity: O(k) = O(logn) (because ranks are O(logn)
    private void deleteNodeWithChildren(HeapNode node)
    {
        HeapNode firstChild = node.getChild();
        HeapNode lastChild = node.getChild().getPrev();
        
        // sets the children`s parent to null - becomes roots
        HeapNode child = node.getChild();
        if (child.getNext() == child) {
            child.setParent(null);
            if (child.isMark()) {
            	child.setMark(false);
            }
        }
        else {
            while (child.getNext() != firstChild){
                child.setParent(null);
                if (child.isMark()) {
                	child.setMark(false);
                }
                child = child.getNext();
            }
            lastChild.setParent(null);
            if (lastChild.isMark()) {
            	lastChild.setMark(false);
            }
        }

        // node is the only node in the heap -> heap becomes the node`s children`s list
        if (node.getNext() == node && node.getPrev() == node) {
            this.firstRoot = firstChild;
        }

        // node is not the only node in the heap -> removes node from root`s list and adds node`s children`s list to it
        else {
            node.getPrev().setNext(firstChild);
            firstChild.setPrev(node.getPrev());
            lastChild.setNext(node.getNext());
            node.getNext().setPrev(lastChild);

            if (this.firstRoot == node) {
                this.firstRoot = firstChild;
            }
        }

        this.numOfTrees = this.numOfTrees + node.getRank()-1;
        this.size--;
    }

    // deletes node (without children) from root-list
    // assume: heap is not empty
    // complexity: O(1)
    private void deleteNodeWithoutChildren(HeapNode node){

        // node is the only node in the heap -> heap becomes empty
        if (node.getNext() == node && node.getPrev() == node) {
            this.firstRoot = null;
            this.minNode = null;
        }

        else {
            if (this.firstRoot == node) {
                this.firstRoot = node.getNext();
            }

            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }

        this.size--;
        this.numOfTrees--;
    }

    // connects two trees with the same rank to one tree
    // complexity: O(1)
    private HeapNode link(HeapNode x, HeapNode y){
        // x turns to be minimum{x,y}
        if (x.getKey() > y.key) {
            HeapNode tmp = x;
            x=y;
            y=tmp;
        }

        // deletes y from root list
        y.getPrev().setNext(y.getNext());
        y.getNext().setPrev(y.getPrev());

        if (this.firstRoot == y) {
            this.firstRoot = y.getNext();
        }

        // y turns to a root
        y.setNext(y);
        y.setPrev(y);

        // Connect children`s lists
        if (x.child != null) {
            y.setPrev(x.child.prev);
            x.child.prev.setNext(y);
            y.setNext(x.child);
            x.child.setPrev(y);
        }

        // y becomes x`s child
        x.child = y;
        y.parent = x;

        // fields update
        x.rank++;
        countLinks++;
        this.numOfTrees--;

        return x;
    }

    // helper for consolidate - puts every tree in its proper bucket according to its rank
    // complexity: O(number of trees)~O(n)
    private HeapNode[] toBuckets(){
        HeapNode x = this.firstRoot;
        if (this.firstRoot == null) {
            return new HeapNode[0];
        }
        HeapNode lastRoot = this.firstRoot.getPrev();

        double phi = (1+Math.sqrt(5))/2;
        int arr_size = (int) Math.floor((Math.log10(this.size))/(Math.log10(phi)))+1;
        
        HeapNode[] B = new HeapNode[arr_size];
        HeapNode y;

        while (x != lastRoot) {
            y=x;
            x=x.getNext();
            while (B[y.rank]!=null){
                y=link(y,B[y.rank]);
                B[y.rank-1] = null;
            }
            B[y.rank] = y;
        }

        // last root
        y=x;
        x=x.next;
        while (B[y.rank]!=null){
            y=link(y,B[y.rank]);
            B[y.rank-1] = null;
        }
        B[y.rank] = y;

        return B;
    }

    // helper for consolidate - connects the trees from the rank-buckets to creat an "almost binomial" heap
    // complexity: O(log(n))
    private HeapNode fromBuckets(HeapNode[] B){
        HeapNode x = null;
        for (HeapNode heapNode : B) {
            if (heapNode != null) {
                if (x == null) {
                    x = heapNode;
                    x.setNext(x);
                    x.setPrev(x);
                } else {
                    insertAfter(x, heapNode);
                    if (heapNode.getKey() < x.getKey()) {
                        x = heapNode;
                    }
                }
            }
        }
        return x;
    }

    //finds the minimum root, and connects the trees to get a binomial heap
    //complexity: O(log(n)) + O(numOfTrees) ~ O(n)
    private void consolidate (){
        HeapNode[] B = toBuckets();
        this.minNode = fromBuckets(B);
    }

    /**
     * public void deleteMin()
     *
     * Deletes the node containing the minimum key.
     *
     */
    // complexity: O(n)
    public void deleteMin() {

        if(isEmpty()){
            return;
        }

        if(this.minNode.getChild()==null) {
            deleteNodeWithoutChildren(this.minNode);
        }

        else {
            deleteNodeWithChildren(this.minNode);
        }

        // finding the new minimum and making the heap an almost binomial heap
        if (!(this.isEmpty())){
            consolidate();
        }
    }

    /**
     * public HeapNode findMin()
     *
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     *
     */
    //complexity: O(1)
    public HeapNode findMin()
    {
        if (this.isEmpty()){
            return null;
        }
        return this.minNode;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Melds heap2 with the current heap.
     *
     */
    //complexity: O(1)
    public void meld (FibonacciHeap heap2)
    {
        this.firstRoot.prev.next = heap2.firstRoot;
        heap2.firstRoot.prev.next = this.firstRoot;
        this.firstRoot.prev = heap2.firstRoot.prev;
        heap2.firstRoot.prev = this.firstRoot.prev;

        // finding new min
        if (heap2.minNode.key < this.minNode.getKey()){
            this.minNode = heap2.minNode;
        }

        this.size += heap2.size();
        this.numOfTrees += heap2.numOfTrees;
    }

    /**
     * public int size()
     *
     * Returns the number of elements in the heap.
     *
     */
    //complexity: O(1)
    public int size()
    {
        return this.size;
    }

    /**
     * public int[] countersRep()
     *
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
     *
     */
    //complexity: O(n)
    public int[] countersRep() {
    	
    	// empty heap
        if (this.isEmpty()){
            return new int[0];
        }
        
        // find max rank
        int maxRank = 0;
        HeapNode node = this.firstRoot;
        HeapNode lastRoot = this.firstRoot.getPrev();
        while(node != lastRoot) {
        	if (node.getRank() > maxRank) {
        		maxRank = node.getRank();
        	}
    		node = node.getNext();
        }
        if (lastRoot.getRank() > maxRank) {
        	maxRank = lastRoot.getRank();
        }
        int[] arr = new int[maxRank + 1];
        
        // update array
        node = this.firstRoot;
        while (node != lastRoot){
            arr[node.getRank()]++;
            node = node.getNext();
        }
        arr[lastRoot.rank]++;
        
        return arr;
    }

    /**
     * public void delete(HeapNode x)
     *
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     *
     */
    //complexity: O(n)
    public void delete(HeapNode x)
    {
        decreaseKey(x, Integer.MAX_VALUE); // making x the min node
        deleteMin();
    }

    //separates the child from his parent
    //complexity: O(1)
    private void cut(HeapNode child, HeapNode parent){
        // changing child to be a root
        if (child.isMark()) {
            child.setMark(false);
            numOfMarkedNodes--;
        }

        // child was the only child of the parent
        if (child.getNext() == child){
            parent.setChild(null);
            child.setParent(null);
        }

        // child wasn't the only child of the parent
        else {
            parent.setChild(child.getNext());
            child.getNext().setParent(parent);
            child.getPrev().setNext(child.getNext());
            child.getNext().setPrev(child.getPrev());
            child.setParent(null);
        }
        
        // insert child to roots-lost
        insertAfter(this.firstRoot.getPrev(), child);
        this.firstRoot = child;
        
        // update min-node
        if (child.getKey() < this.minNode.getKey()) {
            this.minNode = child;
        }

        parent.setRank(parent.getRank()-1);
        countCuts++;
        numOfTrees++;
    }

    //calls cut and makes sure the tree is fixed: has at least 2^k nodes
    //complexity: O(log(n))
    private void cascadingCuts(HeapNode node){
        if (node == null) {
            return;
        }
        HeapNode parent = node.getParent();
        cut(node, node.getParent());
        if (parent != null && parent.getParent() != null) { //parent is not a root
            if (!parent.isMark()) {
                parent.setMark(true);
                numOfMarkedNodes++;
            }
            else {
                cascadingCuts(parent);
            }
        }
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    //complexity: O(log(n))
    public void decreaseKey(HeapNode x, int delta)
    {
        x.setKey(x.getKey()-delta);

        if (x.getParent() == null) {
            if (x.getKey() < this.minNode.getKey()) {
                this.minNode = x;
            }
        }
        else {
            if (x.getKey() < x.getParent().getKey()) {
                cascadingCuts(x);
            }
        }
    }

    /**
     * public int potential()
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     *
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     */
    //complexity: O(1)
    public int potential()
    {
        return numOfTrees + 2*numOfMarkedNodes;
    }

    /**
     * public static int totalLinks()
     *
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     */
    //complexity: O(1)
    public static int totalLinks()
    {
        return countLinks;
    }

    /**
     * public static int totalCuts()
     *
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     */
    //complexity: O(1)
    public static int totalCuts()
    {
        return countCuts;
    }

    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     *
     * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     *
     * ###CRITICAL### : you are NOT allowed to change H.
     */
    //complexity: O(klog(k))
    public static int[] kMin(FibonacciHeap H, int k) {
        int[] arr = new int[k];
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(H.minNode.getKey());
        heap.firstRoot.setInfo(H.firstRoot);
        for (int i=0; i<k; i++) {
            HeapNode node = heap.minNode;
            HeapNode loc = node.getInfo();
            arr[i] = node.getKey();
            heap.deleteMin();

            if (loc.getChild()!=null) {
                loc = loc.getChild();
                HeapNode last = loc.getPrev();
                while (loc != last) {
                    heap.insert(loc.getKey());
                    heap.firstRoot.setInfo(loc);
                    loc = loc.next;
                }
                heap.insert(last.getKey());
                heap.firstRoot.setInfo(loc);
            }
        }
        return arr;
    }

    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in another file.
     *
     */
    public static class HeapNode{

        // HeapNode`s fields
        public int key;
        private HeapNode info;
        private int rank;
        private boolean mark;
        private HeapNode child;
        private HeapNode parent;
        private HeapNode next;
        private HeapNode prev;

        // HeapNode`s constructor
        public HeapNode(int key) {
            this.key=key;
            this.info=null;
            this.rank=0;
            this.mark=false;
            this.child=null;
            this.parent=null;
            this.next=null;
            this.prev=null;
        }

        // returns node`s key
        // complexity: O(1)
        public int getKey() {return this.key;}

        // sets node`s key
        // complexity: O(1)
        public void setKey(int x) {this.key=x;}

        // returns node`s info
        // complexity: O(1)
        public HeapNode getInfo() {return this.info;}

        // sets node`s info
        // complexity: O(1)
        public void setInfo(HeapNode info) {this.info = info;}

        // returns node`s rank
        // complexity: O(1)
        public int getRank() {return this.rank;}

        // sets node`s rank
        // complexity: O(1)
        public void setRank(int k) {this.rank = k;}

        // returns true iff node is marked
        // complexity: O(1)
        public boolean isMark() {return this.mark;}

        // set mark-field of node
        // complexity: O(1)
        public void setMark(boolean b) {this.mark=b;}

        // returns node`s key
        // complexity: O(1)
        public HeapNode getChild() {return this.child;}

        // sets node`s key
        // complexity: O(1)
        public void setChild(HeapNode node){this.child=node;}

        // returns node`s parent
        // complexity: O(1)
        public HeapNode getParent() {return this.parent;}

        // sets node`s parent
        // complexity: O(1)
        public void setParent(HeapNode node) {this.parent=node;}

        // returns node`s next-node
        // complexity: O(1)
        public HeapNode getNext() { return next;}

        // sets node`s next-node
        // complexity: O(1)
        public void setNext(HeapNode next) {this.next = next;}

        // returns node`s previous-node
        // complexity: O(1)
        public void setPrev(HeapNode prev) {this.prev = prev;}

        // sets node`s previous-node
        // complexity: O(1)
        public HeapNode getPrev() {return prev;}
    }
}
