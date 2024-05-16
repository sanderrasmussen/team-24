package no.uio.ifi.IN2000.team24_app.data.character

/**
 * Data class representing a Character in the game.
 *
 * @property head The head clothing item of the character.
 * @property torso The torso clothing item of the character.
 * @property legs The legs clothing item of the character.
 * @property temperature The average temperature of the clothing items.
 */
data class Character(var head: Head, var torso: Torso, var legs: Legs, var temperature: Double = 0.0) {
    init {
       temperature = findAppropriateTemp()
    }

    /**
     * Function to find the appropriate temperature for the character.
     *
     * @return The average temperature of the clothing items.
     */
    fun findAppropriateTemp():Double{
        return (head.heatValue + torso.heatValue + legs.heatValue)/3.0
    }
}




