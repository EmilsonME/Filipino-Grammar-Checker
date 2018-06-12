
import java.util.*;

public class Tree<T> {

	// fields
	private List< Tree<T> > lstChildren = new ArrayList< Tree<T> >();
	private Tree<T> parent = null;
	private T data = null;

	// constructors
	public Tree(T data) {
		this.data = data;
	}

	public Tree(T data, Tree<T> parent) {
		this.data = data;
		this.parent = parent;
	}

	// getters/setters
	public List< Tree<T> > getChildren() {
		return lstChildren;
	}

	public Tree<T> getParent() {
		return this.parent;
	}

	public void setParent(Tree<T> parent) {
		this.parent = parent;
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void addChild(T data) {
		this.lstChildren.add( new Tree<T>(data, this) );
	}

	public void addChild(Tree<T> child) {
		child.setParent(this);
		this.lstChildren.add(child);
	}

	// reorder a branch based on its corresponding branch on the destination language grammar
	public void reorderBranch(List< Tree > newOrder) {

		// transform/reorder using the order we got from the rules
		// by reordering the children (in lstChildren)
	}

	// traverses tree (breadth first)
	// batch reordering
	public void goToChildren() {

		System.out.println();

		if( this.getChildren().isEmpty() ) {
			return;

		} else {

			// DFS recursive version
			/*for(Tree<T> branch : this.getChildren()) {
				/* if branch parent is IN destination grammar rules
				 *		if children order is the same as the rule found
				 *			then SKIP
				 *		else
				 *			branch.reorderBranch( // branch parent children order in rules )
				 *

				//ParseTree<T> br = (ParseTree<T>) branch;
				System.out.println(branch.getData());
				branch.goToChildren();
			}*/

			// BFS version
			// this is the root node
			Queue<Tree<T>> queue = new LinkedList<Tree<T>>();
			queue.add(this);
			System.out.println(this.getData());

			String L = "L ";

			while(!queue.isEmpty()) {
				Tree<T> node = queue.remove();
				List<Tree<T>> branch = node.getChildren();

				L += "L ";
				//System.out.println("LEVEL!!");
				for(Tree<T> child : branch) {

					//L += "L";
					/* if branch parent is IN destination grammar rules
					 *		if children order is the same as the rule found
					 *			then SKIP
					 *		else
					 *			branch.reorderBranch( // branch parent children order in rules )
					 */

					System.out.println(L + child.getData());
					queue.add(child);

				}
				StringBuilder sb = new StringBuilder(L);
				sb.deleteCharAt(0);
				sb.deleteCharAt(1);
			}
		}

	}

}
