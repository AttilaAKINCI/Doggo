package com.akinci.doggoappcompose.common.network

class RestConfig {
    companion object {
        const val API_BASE_URL = "https://dog.ceo/"
        const val BREED_LIST = "api/breeds/list/all"
        const val SUB_BREED_LIST = "api/breed/{breed}/list"
        const val GET_BREED = "api/breed/{breed}/images/random/{count}"
        const val GET_SUB_BREED = "api/breed/{breed}/{subBread}/images/random/{count}"
    }
}