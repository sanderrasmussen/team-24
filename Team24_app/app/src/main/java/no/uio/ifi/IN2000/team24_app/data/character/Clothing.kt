package no.uio.ifi.IN2000.team24_app.data.character

/**
 * This class represents a piece of clothing in the game.
 *
 * @param name The name of the clothing item.
 * @param heatValue The temperature appropriate for the clothing item. If a hat is best suited for 12 degrees, the heatValue is 12
 * @param imageAsset The resource ID of the image representing the clothing item, used for rendering the character.
 * @param price The price of the clothing item.
 * @param altAsset The resource ID of the alternate image representing the clothing item. The alternate image is used in the inventory. and store.
 * @param unlocked A boolean indicating whether the clothing item is unlocked.
 */
abstract class Clothing(
    open val name:String,
    open val heatValue: Int,
    open val imageAsset: Int,
    open val price: Int,
    open val altAsset: Int,
    var unlocked: Boolean = false
 )


fun writeClothesToDisk(character: Character){
    //TODO, IMPORTANT! THIS SHOULD CALL A SEPARATE ASYNC-METHOD, TO WRITE ON AN IO-THREAD
}



