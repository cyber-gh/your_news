package dev.skyit.yournews.api.models

import java.util.*

enum class CountryFilter {
    AE,AR,AT,AU,BE,BG,BR,CA,CH,CN,CO,CU,CZ,DE,EG,FR,GB,GR,HK,HU,ID,IE,IL,IN,IT,JP,KR,LT,LV,MA,MX,MY,NG,NL,NO,NZ,PH,PL,PT,RO,RS,RU,SA,SE,SG,SI,SK,TH,TR,TW,UA,US,VE,ZA;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }

    fun longName() : String {
        val loc = Locale("", this.toString().toUpperCase())
        return loc.displayCountry
    }

    companion object {

        fun fromString(str: String): CountryFilter {
            return CountryFilter.values().firstOrNull {
                it.toString() == str
            } ?: US
        }
    }
}