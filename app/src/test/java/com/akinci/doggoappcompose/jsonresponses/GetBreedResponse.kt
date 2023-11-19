package com.akinci.doggoappcompose.jsonresponses

class GetBreedResponse {
    companion object{
        val contentResponse = """
            {
                "message": [
                   "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
                   "https://images.dog.ceo/breeds/hound-afghan/n02088094_1007.jpg",
                   "https://images.dog.ceo/breeds/hound-afghan/n02088094_1023.jpg",
                   "https://images.dog.ceo/breeds/hound-afghan/n02088094_10263.jpg"
                ],
                "status": "success"
            }
        """.trimIndent()
    }
}