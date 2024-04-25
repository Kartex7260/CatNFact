package kanti.catnfact.ui.components.fact

class ExpanderManager {

	private var currentHash: String? = null

	fun isExpand(hash: String): Boolean {
		return currentHash == hash
	}

	fun expand(hash: String?) {
		currentHash = if (currentHash == hash) null else hash
	}
}