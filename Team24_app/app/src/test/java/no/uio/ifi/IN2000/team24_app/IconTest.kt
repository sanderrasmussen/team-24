package no.uio.ifi.IN2000.team24_app

import org.junit.Test

class IconTest{
    @Test
    fun warning_icons_are_present(){
       val s = "icon_warning_avalanches_orange"
       val img = R.drawable.icon_warning_avalanches_orange
        val other = R.drawable.clearsky_day
        println(s)
        println(img)
    }
}