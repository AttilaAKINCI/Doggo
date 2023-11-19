package com.akinci.doggo.jsonresponses

class GetBreedListResponse {
    companion object {
        val breedList = """
            {
                "message": {
                    "affenpinscher": [],
                    "african": [],
                    "airedale": [],
                    "akita": [],
                    "appenzeller": [],
                    "australian": [
                        "shepherd"
                    ],
                    "basenji": [],
                    "beagle": [],
                    "bluetick": [],
                    "borzoi": [],
                    "bouvier": [],
                    "boxer": [],
                    "brabancon": [],
                    "briard": [],
                    "buhund": [
                        "norwegian"
                    ],
                    "hound": [
                        "afghan",
                        "basset",
                        "blood",
                        "english",
                        "ibizan",
                        "plott",
                        "walker"
                    ]
                },
                "status": "success"
            }
        """
    }
}