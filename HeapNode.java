
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
