package no.uio.ifi.IN2000.team24_app.data.metAlerts

/**
 * Data class representing a warning card.
 * @param ongoingDanger: The ongoing status of the warning. Either "Pågår", "Ventes" or "Ferdig".
 * @param imageUrl: The URL of the image to display on the card.
 * @param location: The location of the warning.
 * @param dangerLevel: The danger level of the warning. Either "Gult nivå", "Oransje nivå" or "Rødt nivå".
 */
data class WarningCard(
    val ongoingDanger:String,
    val imageUrl: String,
    val location: String,
    val dangerLevel:String

)
