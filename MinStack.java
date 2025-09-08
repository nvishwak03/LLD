class MinStack {
    Stack<Integer> s;
    int min=Integer.MAX_VALUE;
    List<Integer> al = new ArrayList<>();
    public MinStack() {
        s =new Stack<>();
    }
    
    public void push(int val) {
        if(val<min)
        min=Math.min(min, val);
        s.push(val);
        al.add(min);
    }
    
    public void pop() {
        if(!s.isEmpty())
        {
            s.pop();
            al.remove(al.size() - 1);
        }
        if (!al.isEmpty()) {
                min = al.get(al.size() - 1);
            } else {
                min = Integer.MAX_VALUE;
            }
    }
    
    public int top() {
        if(!s.isEmpty())
        return s.peek();
        return 0;
    }
    
    public int getMin() {
        return min;
    }
}
