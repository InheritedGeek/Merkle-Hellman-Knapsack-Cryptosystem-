package cryptosystem;


public class SinglyLinkedList {
	ObjectNode head;
	ObjectNode tail;
	int countNodes;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SinglyLinkedList s = new SinglyLinkedList();
		
		s.head = new ObjectNode("1", s.tail);
		ObjectNode beginningNode = s.head;
		

		
		s.addAtEndNode(new ObjectNode("2", s.tail));
		s.addAtEndNode(new ObjectNode("3", s.tail));
		s.addAtEndNode(new ObjectNode("4", s.tail));
		
		s.reset(beginningNode);
		
		while(s.hasNext()) {
			 System.out.println(s.next());
		 }

	}
	
	public void reset(ObjectNode b) {
		head = b;
	}
	
	public boolean hasNext() {
		
		if (head == null)
			return false;
		
		if (head.getLink() != null)
			return true;
		return false;
	}
	
	public Object next() {
		head = head.getLink();
		return head.getData();
	}
	
	public void addAtEndNode(ObjectNode c) {
		
		if (head == null) {
			head.setLink(c);
		}
		else {
			
			ObjectNode currNode = head;
			
			while (currNode.getLink() != null) {
				currNode = currNode.getLink();
			}
			currNode.setLink(c);
		}
		
	}
	
	public void countNodes() {	
		ObjectNode currentNode = head;
		
		while (currentNode != null) {
			countNodes++;
			currentNode = currentNode.getLink();
		}
	}
	
	public Object getObjectAt (int i){
		
		ObjectNode currNode = head;
		int val =0;
		
		while (val <i) {
			currNode = currNode.getLink();
			val+=1;
		}
		return currNode.getData();
	}
}
