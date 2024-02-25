package com.akinci.doggo.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogNameProvider @Inject constructor() {
    private val doggoNames = listOf(
        "COOPER", "OAKLEY", "MAC", "CHARLIE", "REX", "RUDY", "TEDDY", "BAILEY", "CHIP",
        "BEAR", "CASH", "WALTER", "MILO", "JASPER", "BLAZE", "BENTLEY", "BO", "OZZY",
        "OLLIE", "BOOMER", "ODIN", "BUDDY", "LUCKY", "AXEL", "ROCKY", "RUGER", "BRUCE",
        "LEO", "BEAU", "ODIE", "ZEUS", "BAXTER", "ARLO", "DUKE", "OREO", "ECHO", "FINN",
        "GUNNER", "TANK", "APOLLO", "HENRY", "ROMEO", "MURPHY", "SIMBA", "PORTER", "DIESEL",
        "GEORGE", "HARLEY", "TOBY", "COCO", "OTIS", "LOUIE", "ROCKET", "ROCCO", "TUCKER",
        "ZIGGY", "REMI", "JAX", "PRINCE", "WHISKEY", "ACE", "SHADOW", "SAM", "JACK", "RILEY",
        "BUSTER", "KODA", "COPPER", "BUBBA", "WINSTON", "LUKE", "JAKE", "OLIVER", "MARLEY",
        "BENNY", "GUS", "ZEKE", "BOWIE", "LOKI", "LEVI", "DOZER", "MOOSE", "BENJI", "RUSTY",
        "ARCHIE", "RANGER", "JOEY", "BANDIT", "REMY", "KYLO", "SCOUT", "DEXTER", "RYDER",
        "THOR", "GIZMO", "TYSON", "BRUNO", "CHASE", "SAMSON", "KING", "CODY", "RAMBO", "BLUE",
        "SARGE", "HARRY", "ATLAS", "CHESTER", "GUCCI", "THEO", "MAVERICK", "MILES", "JACKSON",
        "LINCOLN", "WATSON", "HANK", "WALLY", "PEANUT", "TITAN"
    )

    fun getRandomDogName(): String {
        return doggoNames[doggoNames.indices.random()]
    }
}
