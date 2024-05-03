package kanti.catnfact.data.retrofit.libretranslate.translate

import com.google.gson.annotations.SerializedName

data class TranslatedResponse(
	@SerializedName("translatedText") val text: String = "",

)