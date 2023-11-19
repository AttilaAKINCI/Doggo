package com.akinci.doggoappcompose.jsonresponses

class GetSubBreedListResponse {
    companion object{
        val subBreedList = """
            {
                "message": [
                    "afghan",
                    "basset",
                    "blood",
                    "english",
                    "ibizan",
                    "plott",
                    "walker"
                ],
                "status": "success"
            }
        """.trimIndent()
    }
}

