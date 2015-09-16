package org.gudmap.assemblers;

public class CollectionAssembler { //singleton
	
	static private CollectionAssembler _instance = null;

	protected CollectionAssembler() {
	
	}

	/**
	 * @return The unique instance of this class.
	 */
	static public CollectionAssembler instance() {
		if (null == _instance) {
			_instance = new CollectionAssembler();
		}
		return _instance;
	}

}
