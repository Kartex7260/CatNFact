package kanti.catnfact.ui.components

import javax.inject.Inject

class ExpanderManager @Inject constructor() {

	private var currentHash: String? = null

	fun isExpand(hash: String): Boolean {
		return currentHash == hash
	}

	fun expand(hash: String?) {
		currentHash = if (currentHash == hash) null else hash
	}
}