import java.util.ArrayList;
import java.util.Stack;

public class PlaylistTree {
	
	public PlaylistNode primaryRoot;		//root of the primary B+ tree
	public PlaylistNode secondaryRoot;	//root of the secondary B+ tree
	public PlaylistTree(Integer order) {
		PlaylistNode.order = order;
		primaryRoot = new PlaylistNodePrimaryLeaf(null);
		primaryRoot.level = 0;
		secondaryRoot = new PlaylistNodeSecondaryLeaf(null);
		secondaryRoot.level = 0;
	}
	
	public void addSong(CengSong song) {
		// TODO: Implement this method

		// Primary B+Tree
		PlaylistNodePrimaryLeaf leafToInsert = findIndexPrimary(song.audioId());
		ArrayList<CengSong> songsOfLeaf = leafToInsert.getSongs();
		Integer indexToInsert = -1;

		for (int i = 0; i < leafToInsert.songCount() ; i++) {
			if (songsOfLeaf.get(i).audioId() > song.audioId())
			{
				indexToInsert = i;
				break;
			}
		}
		if (indexToInsert == -1)
			indexToInsert = leafToInsert.songCount();

		// Insert song to the tree
		leafToInsert.addSong(indexToInsert, song);

		// if leaf is a root
		if (leafToInsert.getParent() == null) {

			/* if overflow in root (leaf) */
			if (leafToInsert.songCount() == (2 * PlaylistNode.order) + 1)
			{
				PlaylistNodePrimaryIndex newRoot = new PlaylistNodePrimaryIndex(null);
				PlaylistNodePrimaryLeaf newNode = new PlaylistNodePrimaryLeaf(newRoot);
				for (int index = PlaylistNode.order, j = 0 ; index < leafToInsert.songCount() ; index++,j++)
					newNode.addSong(j,leafToInsert.songAtIndex(index));
				leafToInsert.getSongs().subList(PlaylistNode.order, leafToInsert.getSongs().size()).clear();
				leafToInsert.setParent(newRoot);
				newRoot.addAudioId(0, newNode.songAtIndex(0).audioId());
				newRoot.addChild(0, leafToInsert);
				newRoot.addChild(1, newNode);
				primaryRoot = newRoot;
			}
		}

		// leaf is not root
		else {

			/* if overflow in node (leaf) */
			if (leafToInsert.songCount() == (2 * PlaylistNode.order) + 1)
			{
				// Divide node into two and fill them
				PlaylistNode parent = leafToInsert.getParent();
				PlaylistNodePrimaryLeaf newNode = new PlaylistNodePrimaryLeaf(parent);
				for (int index = PlaylistNode.order, j = 0 ; index < leafToInsert.songCount() ; index++,j++)
					newNode.addSong(j,leafToInsert.songAtIndex(index));
				// Remove additional items from old leaf node
				leafToInsert.getSongs().subList(PlaylistNode.order, leafToInsert.getSongs().size()).clear();
				// leafToInsert.setParent(parent);

				// Add new leaf node to its Internal parent node
				Integer audioIdToUp = newNode.songAtIndex(0).audioId();
				boolean isSet = false;
				for (int i = 0; i < ((PlaylistNodePrimaryIndex)parent).audioIdCount(); i++) {
					if (((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(i) > audioIdToUp)
					{
						((PlaylistNodePrimaryIndex)parent).addAudioId(i, audioIdToUp);
						// parent.addChild(i, leafToInsert);
						((PlaylistNodePrimaryIndex)parent).addChild(i + 1, newNode);
						isSet = true;
						break;
					}
				}
				if (!isSet)
				{
					((PlaylistNodePrimaryIndex)parent).addAudioId(audioIdToUp);
					// ((PlaylistNodePrimaryIndex)parent).addChild(((PlaylistNodePrimaryIndex)parent).keyCount(), leafToInsert);
					((PlaylistNodePrimaryIndex)parent).addChild(newNode);
				}

				// check if overflow exists in leaf's Internal parent node
				PlaylistNode nextParent;
				while (((PlaylistNodePrimaryIndex)parent).audioIdCount() == (2 * PlaylistNode.order) + 1)
				{
					isSet = false;

					// Fetch parent of the Internal parent node
					nextParent = parent.getParent();

					// if Internal parent node is root
					if (nextParent == null)
					{
						PlaylistNodePrimaryIndex newRoot = new PlaylistNodePrimaryIndex(null);
						// Create a new internal node
						PlaylistNodePrimaryIndex newInternalNode = new PlaylistNodePrimaryIndex(newRoot);
						// Fill new internal node
						for (int index = PlaylistNode.order + 1, j = 0 ; index < ((PlaylistNodePrimaryIndex)parent).audioIdCount() ; index++,j++)
						{
							// Here, parent refers to old internal node (sibling of newInternalNode)
							newInternalNode.addAudioId(j,((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(index));
							newInternalNode.addChild(j,((PlaylistNodePrimaryIndex)parent).getChildrenAt(index));
							((PlaylistNodePrimaryIndex)parent).getChildrenAt(index).setParent(newInternalNode);
						}
						newInternalNode.addChild(newInternalNode.audioIdCount(),((PlaylistNodePrimaryIndex)parent).getChildrenAt(((PlaylistNodePrimaryIndex)parent).audioIdCount()));
						((PlaylistNodePrimaryIndex)parent).getChildrenAt(((PlaylistNodePrimaryIndex)parent).audioIdCount()).setParent(newInternalNode);

						audioIdToUp = ((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(PlaylistNode.order);

						// Remove additional items from old internal node
						((PlaylistNodePrimaryIndex)parent).getAllAudioIds().subList(PlaylistNode.order, ((PlaylistNodePrimaryIndex)parent).getAllAudioIds().size()).clear();
						((PlaylistNodePrimaryIndex)parent).getAllChildren().subList(PlaylistNode.order + 1, ((PlaylistNodePrimaryIndex)parent).getAllChildren().size()).clear();

						// Bind both Internal parent nodes to the new Root
						newRoot.addAudioId(0, audioIdToUp);
						newRoot.addChild(0, parent);
						parent.setParent(newRoot);
						newRoot.addChild(1, newInternalNode);
						primaryRoot = newRoot;
						break;
					}

					// if Internal parent node is an ordinary index node
					else
					{
						// Create a new internal node
						PlaylistNodePrimaryIndex newInternalNode = new PlaylistNodePrimaryIndex(nextParent);
						// Fill new internal node
						for (int index = PlaylistNode.order + 1, j = 0 ; index < ((PlaylistNodePrimaryIndex)parent).audioIdCount() ; index++,j++)
						{
							// Here, parent refers to old internal node (sibling of newInternalNode)
							newInternalNode.addAudioId(j,((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(index));
							newInternalNode.addChild(j,((PlaylistNodePrimaryIndex)parent).getChildrenAt(index));
							((PlaylistNodePrimaryIndex)parent).getChildrenAt(index).setParent(newInternalNode);
						}
						newInternalNode.addChild(newInternalNode.audioIdCount(),((PlaylistNodePrimaryIndex)parent).getChildrenAt(((PlaylistNodePrimaryIndex)parent).audioIdCount()));
						((PlaylistNodePrimaryIndex)parent).getChildrenAt(((PlaylistNodePrimaryIndex)parent).audioIdCount()).setParent(newInternalNode);

						audioIdToUp = ((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(PlaylistNode.order);

						// Remove additional items from old internal node
						((PlaylistNodePrimaryIndex)parent).getAllAudioIds().subList(PlaylistNode.order, ((PlaylistNodePrimaryIndex)parent).getAllAudioIds().size()).clear();
						((PlaylistNodePrimaryIndex)parent).getAllChildren().subList(PlaylistNode.order + 1, ((PlaylistNodePrimaryIndex)parent).getAllChildren().size()).clear();

						// Add new Internal parent node to its Internal parent node
						for (int i = 0; i < ((PlaylistNodePrimaryIndex)nextParent).audioIdCount(); i++) {
							if (((PlaylistNodePrimaryIndex)nextParent).audioIdAtIndex(i) > audioIdToUp)
							{
								((PlaylistNodePrimaryIndex)nextParent).addAudioId(i, audioIdToUp);
								((PlaylistNodePrimaryIndex)nextParent).addChild(i, parent);
								// Need to set child since we are adding child to start and it was pointing to "parent"
								((PlaylistNodePrimaryIndex)nextParent).setChild(i + 1, newInternalNode);
								isSet = true;
								break;
							}
						}
						if (!isSet)
						{

							((PlaylistNodePrimaryIndex)nextParent).addAudioId(audioIdToUp);
							// No Need to set child since we are adding child to the end and it was pointing nothing
							// ((PlaylistNodePrimaryIndex)nextParent).setChild(((PlaylistNodePrimaryIndex)nextParent).keyCount(),parent);
							((PlaylistNodePrimaryIndex)nextParent).addChild(newInternalNode);
						}
						parent = nextParent;
					}
				}
			}
		}
		
		// Secondary B+Tree
		PlaylistNodeSecondaryLeaf leafToInsert2 = findIndexSecondary(song.genre());
		ArrayList<ArrayList<CengSong>> songsOfLeaf2 = leafToInsert2.getSongBucket();
		Integer indexToInsert2 = -1;
		boolean c = false;
		boolean c1 = true;

		for (int i = 0; i < songsOfLeaf2.size() && !c ; i++) {
			for (int j = 0; j < songsOfLeaf2.get(i).size(); j++) {
				if (songsOfLeaf2.get(i).get(j).genre().compareTo(song.genre()) == 0) {
					indexToInsert2 = i;
					c1 = false;
					c = true;
					break;
				}
			}
		}

		for (int i = 0; i < songsOfLeaf2.size() && !c && c1; i++) {
			for(int j = 0; j < songsOfLeaf2.get(i).size();j++){
			if (songsOfLeaf2.get(i).get(j).genre().compareTo(song.genre()) > 0)
			{
				indexToInsert2 = i;
				c = true;
				break;
			}
		}}









		if (indexToInsert2 == -1)
			indexToInsert2 = leafToInsert2.genreCount();

		// Insert song to the tree

		leafToInsert2.addSong(indexToInsert2, song);


		// if leaf is a root
		if (leafToInsert2.getParent() == null) {

			/* if overflow in root (leaf) */
			if (leafToInsert2.genreCount() == (2 * PlaylistNode.order) + 1)
			{
				PlaylistNodeSecondaryIndex newRoot2 = new PlaylistNodeSecondaryIndex(null);
				PlaylistNodeSecondaryLeaf newNode2 = new PlaylistNodeSecondaryLeaf(newRoot2);
				for (int index = PlaylistNode.order, j = 0 ; index < leafToInsert2.genreCount() ; index++,j++) {
					for(int i = 0; i < songsOfLeaf2.get(index).size(); i++) {
						newNode2.addSong(j, leafToInsert2.songsAtIndex(index).get(i));
					}

				}

				leafToInsert2.getSongBucket().subList(PlaylistNode.order, leafToInsert2.getSongBucket().size()).clear();
				leafToInsert2.setParent(newRoot2);
				newRoot2.addGenre(0, newNode2.genreAtIndex(0));
				newRoot2.addChild(0, leafToInsert2);
				newRoot2.addChild(1, newNode2);
				secondaryRoot = newRoot2;
			}
		}

		// leaf is not root
		else {

			/* if overflow in node (leaf) */
			if (leafToInsert2.genreCount() == (2 * PlaylistNode.order) + 1)
			{
				// Divide node into two and fill them
				PlaylistNode parent2 = leafToInsert2.getParent();
				PlaylistNodeSecondaryLeaf newNode2 = new PlaylistNodeSecondaryLeaf(parent2);
				for (int index = PlaylistNode.order, j = 0 ; index < leafToInsert2.genreCount() ; index++,j++) {
					for(int i=0; i < songsOfLeaf2.get(index).size();i++) {
						newNode2.addSong(j, leafToInsert2.songsAtIndex(index).get(i));
					}
				}
				// Remove additional items from old leaf node
				leafToInsert2.getSongBucket().subList(PlaylistNode.order, leafToInsert2.getSongBucket().size()).clear();
				// leafToInsert.setParent(parent);

				// Add new leaf node to its Internal parent node
				String genreToUp = newNode2.genreAtIndex(0);
				boolean isSet = false;
				for (int i = 0; i < ((PlaylistNodeSecondaryIndex)parent2).genreCount(); i++) {
					if (((PlaylistNodeSecondaryIndex)parent2).genreAtIndex(i).compareTo(genreToUp) > 0)
					{
						((PlaylistNodeSecondaryIndex)parent2).addGenre(i, genreToUp);
						// parent.addChild(i, leafToInsert);
						((PlaylistNodeSecondaryIndex)parent2).addChild(i + 1, newNode2);
						isSet = true;
						break;
					}
				}
				if (!isSet)
				{
					((PlaylistNodeSecondaryIndex)parent2).addGenre(genreToUp);
					// ((PlaylistNodePrimaryIndex)parent).addChild(((PlaylistNodePrimaryIndex)parent).keyCount(), leafToInsert);
					((PlaylistNodeSecondaryIndex)parent2).addChild(newNode2);
				}

				// check if overflow exists in leaf's Internal parent node
				PlaylistNode nextParent2;
				while (((PlaylistNodeSecondaryIndex)parent2).genreCount() == (2 * PlaylistNode.order) + 1)
				{
					isSet = false;

					// Fetch parent of the Internal parent node
					nextParent2 = parent2.getParent();

					// if Internal parent node is root
					if (nextParent2 == null)
					{
						PlaylistNodeSecondaryIndex newRoot2 = new PlaylistNodeSecondaryIndex(null);
						// Create a new internal node
						PlaylistNodeSecondaryIndex newInternalNode2 = new PlaylistNodeSecondaryIndex(newRoot2);
						// Fill new internal node
						for (int index = PlaylistNode.order + 1, j = 0 ; index < ((PlaylistNodeSecondaryIndex)parent2).genreCount() ; index++,j++)
						{
							// Here, parent refers to old internal node (sibling of newInternalNode)
							newInternalNode2.addGenre(j,((PlaylistNodeSecondaryIndex)parent2).genreAtIndex(index));
							newInternalNode2.addChild(j,((PlaylistNodeSecondaryIndex)parent2).getChildrenAt(index));
							((PlaylistNodeSecondaryIndex)parent2).getChildrenAt(index).setParent(newInternalNode2);
						}
						newInternalNode2.addChild(newInternalNode2.genreCount(),((PlaylistNodeSecondaryIndex)parent2).getChildrenAt(((PlaylistNodeSecondaryIndex)parent2).genreCount()));
						((PlaylistNodeSecondaryIndex)parent2).getChildrenAt(((PlaylistNodeSecondaryIndex)parent2).genreCount()).setParent(newInternalNode2);

						genreToUp = ((PlaylistNodeSecondaryIndex)parent2).genreAtIndex(PlaylistNode.order);

						// Remove additional items from old internal node
						((PlaylistNodeSecondaryIndex)parent2).getAllGenres().subList(PlaylistNode.order, ((PlaylistNodeSecondaryIndex)parent2).getAllGenres().size()).clear();
						((PlaylistNodeSecondaryIndex)parent2).getAllChildren().subList(PlaylistNode.order + 1, ((PlaylistNodeSecondaryIndex)parent2).getAllChildren().size()).clear();

						// Bind both Internal parent nodes to the new Root
						newRoot2.addGenre(0, genreToUp);
						newRoot2.addChild(0, parent2);
						parent2.setParent(newRoot2);
						newRoot2.addChild(1, newInternalNode2);
						secondaryRoot = newRoot2;
						break;
					}

					// if Internal parent node is an ordinary index node
					else
					{
						// Create a new internal node
						PlaylistNodeSecondaryIndex newInternalNode2 = new PlaylistNodeSecondaryIndex(nextParent2);
						// Fill new internal node
						for (int index = PlaylistNode.order + 1, j = 0 ; index < ((PlaylistNodeSecondaryIndex)parent2).genreCount() ; index++,j++)
						{
							// Here, parent refers to old internal node (sibling of newInternalNode)
							newInternalNode2.addGenre(j,((PlaylistNodeSecondaryIndex)parent2).genreAtIndex(index));
							newInternalNode2.addChild(j,((PlaylistNodeSecondaryIndex)parent2).getChildrenAt(index));
							((PlaylistNodeSecondaryIndex)parent2).getChildrenAt(index).setParent(newInternalNode2);
						}
						newInternalNode2.addChild(newInternalNode2.genreCount(),((PlaylistNodeSecondaryIndex)parent2).getChildrenAt(((PlaylistNodeSecondaryIndex)parent2).genreCount()));
						((PlaylistNodeSecondaryIndex)parent2).getChildrenAt(((PlaylistNodeSecondaryIndex)parent2).genreCount()).setParent(newInternalNode2);

						genreToUp = ((PlaylistNodeSecondaryIndex)parent2).genreAtIndex(PlaylistNode.order);

						// Remove additional items from old internal node
						((PlaylistNodeSecondaryIndex)parent2).getAllGenres().subList(PlaylistNode.order, ((PlaylistNodeSecondaryIndex)parent2).getAllGenres().size()).clear();
						((PlaylistNodeSecondaryIndex)parent2).getAllChildren().subList(PlaylistNode.order + 1, ((PlaylistNodeSecondaryIndex)parent2).getAllChildren().size()).clear();

						// Add new Internal parent node to its Internal parent node
						for (int i = 0; i < ((PlaylistNodeSecondaryIndex)nextParent2).genreCount(); i++) {
							if (((PlaylistNodeSecondaryIndex)nextParent2).genreAtIndex(i).compareTo(genreToUp) > 0)
							{
								((PlaylistNodeSecondaryIndex)nextParent2).addGenre(i, genreToUp);
								((PlaylistNodeSecondaryIndex)nextParent2).addChild(i, parent2);
								// Need to set child since we are adding child to start and it was pointing to "parent"
								((PlaylistNodeSecondaryIndex)nextParent2).setChild(i + 1, newInternalNode2);
								isSet = true;
								break;
							}
						}
						if (!isSet)
						{

							((PlaylistNodeSecondaryIndex)nextParent2).addGenre(genreToUp);
							// No Need to set child since we are adding child to the end and it was pointing nothing
							// ((PlaylistNodePrimaryIndex)nextParent).setChild(((PlaylistNodePrimaryIndex)nextParent).keyCount(),parent);
							((PlaylistNodeSecondaryIndex)nextParent2).addChild(newInternalNode2);
						}
						parent2 = nextParent2;
					}
				}
			}
		}

	}

	public CengSong searchSong(Integer audioId) {
		// TODO: Implement this method
		PlaylistNode node = primaryRoot;
		boolean cont;

		while (node.getType() == PlaylistNodeType.Internal)
		{
			System.out.println("<index>");
			for (int i = 0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount(); i++) {
				System.out.println(((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i));
			}
			System.out.println("</index>");
			cont = false;
			for (int i = 0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount() ; i++) {
				// if ith audioId is greater than our audioId, then i is the index of next child
				if (((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i) > audioId)
				{
					node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(i);
					cont = true;
					break;
				}
			}
			if (!cont)
				node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(((PlaylistNodePrimaryIndex) node).audioIdCount());
		}
		for (int i = 0; i < ((PlaylistNodePrimaryLeaf) node).songCount(); i++) {
			if (((PlaylistNodePrimaryLeaf) node).audioIdAtIndex(i) == audioId)
			{
				System.out.println("<data>");
				System.out.print("<record>");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).audioId());
				System.out.print("|");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).genre());
				System.out.print("|");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).songName());
				System.out.print("|");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).artist());
				System.out.print("</record>\n");
				System.out.println("</data>");
				return (((PlaylistNodePrimaryLeaf) node).songAtIndex(i));
			}
		}
		System.out.print("No match for ");
		System.out.println(audioId);
		return null;
	}
	
	
	public void printPrimaryPlaylist() {
		// TODO: Implement this method
		// print the primary B+ tree in Depth-first order
		Stack<PlaylistNode> itemStack = new Stack<PlaylistNode>();
		PlaylistNode pTreeRoot = primaryRoot;

		itemStack.add(pTreeRoot);

		while (!itemStack.isEmpty())
		{
			PlaylistNode node = itemStack.pop();
			// if node is an internal node
			if (node.getType() == PlaylistNodeType.Internal)
			{
				// push all children to stack, from left to right
				ArrayList<PlaylistNode> children = ((PlaylistNodePrimaryIndex) node).getAllChildren();
				for (int i = children.size() - 1; i >= 0 ; i--)
					itemStack.add(children.get(i));

				// print keys all keys on this internal node
				System.out.println("<index>");
				for (int i = 0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount() ; i++)
					System.out.println(((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i));
				System.out.println("</index>");
			}

			else if (node.getType() == PlaylistNodeType.Leaf)
			{
				// print all books, from left to right
				System.out.println("<data>");
				for (int i = 0; i < ((PlaylistNodePrimaryLeaf) node).songCount() ; i++)
				{
					System.out.print("<record>");
					System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).audioId());
					System.out.print("|");
					System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).genre());
					System.out.print("|");
					System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).songName());
					System.out.print("|");
					System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).artist());
					System.out.print("</record>\n");
				}
				System.out.println("</data>");
			}
		}


	}
	
	public void printSecondaryPlaylist() {
		// TODO: Implement this method
		Stack<PlaylistNode> itemStack2 = new Stack<PlaylistNode>();
		PlaylistNode sTreeRoot = secondaryRoot;

		itemStack2.add(sTreeRoot);

		while (!itemStack2.isEmpty())
		{
			PlaylistNode node = itemStack2.pop();
			// if node is an internal node
			if (node.getType() == PlaylistNodeType.Internal)
			{
				// push all children to stack, from left to right
				ArrayList<PlaylistNode> children = ((PlaylistNodeSecondaryIndex) node).getAllChildren();
				for (int i = children.size() - 1; i >= 0 ; i--)
					itemStack2.add(children.get(i));

				// print keys all keys on this internal node
				System.out.println("<index>");
				for (int i = 0; i < ((PlaylistNodeSecondaryIndex) node).genreCount() ; i++)
					System.out.println(((PlaylistNodeSecondaryIndex) node).genreAtIndex(i));
				System.out.println("</index>");
			}

			else if (node.getType() == PlaylistNodeType.Leaf)
			{
				// print all books, from left to right
				System.out.println("<data>");
				for(int j=0; j < ((PlaylistNodeSecondaryLeaf) node).getSongBucket().size(); j++) {
					System.out.println(((PlaylistNodeSecondaryLeaf) node).genreAtIndex(j));
					for (int i = 0; i < ((PlaylistNodeSecondaryLeaf) node).getSongBucket().get(j).size(); i++) {
						System.out.print("<record>");
						System.out.print(((PlaylistNodeSecondaryLeaf) node).songsAtIndex(i).get(i).audioId());
						System.out.print("|");
						System.out.print(((PlaylistNodeSecondaryLeaf) node).songsAtIndex(j).get(i).genre());
						System.out.print("|");
						System.out.print(((PlaylistNodeSecondaryLeaf) node).songsAtIndex(j).get(i).songName());
						System.out.print("|");
						System.out.print(((PlaylistNodeSecondaryLeaf) node).songsAtIndex(j).get(i).artist());
						System.out.print("</record>\n");
					}
				}
				System.out.println("</data>");
			}
		}
	}
	
	// Extra functions if needed
	public PlaylistNodePrimaryLeaf findIndexPrimary(Integer audioId) {

		PlaylistNode node = primaryRoot;
		boolean cont;

		while (node.getType() == PlaylistNodeType.Internal)
		{
			cont = false;
			for (int i = 0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount() ; i++) {
				// if ith audioId is greater than our audioId, then i is the index of next child
				if (((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i) > audioId)
				{
					node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(i);
					cont = true;
					break;
				}
			}
			if (!cont)
				node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(((PlaylistNodePrimaryIndex) node).audioIdCount());
		}

		return ((PlaylistNodePrimaryLeaf) node);
	}



	public PlaylistNodeSecondaryLeaf findIndexSecondary(String genre) {

		PlaylistNode node = secondaryRoot;
		boolean cont;

		while (node.getType() == PlaylistNodeType.Internal)
		{
			cont = false;
			for (int i = 0; i < ((PlaylistNodeSecondaryIndex) node).genreCount() ; i++) {
				// if ith audioId is greater than our audioId, then i is the index of next child
				if (((PlaylistNodeSecondaryIndex) node).genreAtIndex(i).compareTo(genre) > 0)
				{
					node = ((PlaylistNodeSecondaryIndex) node).getChildrenAt(i);
					cont = true;
					break;
				}
			}
			if (!cont)
				node = ((PlaylistNodeSecondaryIndex) node).getChildrenAt(((PlaylistNodeSecondaryIndex) node).genreCount());
		}

		return ((PlaylistNodeSecondaryLeaf) node);
	}


}




