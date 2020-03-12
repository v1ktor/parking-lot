package parking

import java.util.Scanner

data class Car(val color: String, val regNumber: String)
data class Spot(var isOccupied: Boolean, var car: Car?)

class Parking {
    var parkingLot = emptyArray<Spot>()
    private val MESSAGE_PARKING_LOT_IS_FULL = "Sorry, parking lot is full."
    private val MESSAGE_PARKING_LOT_IS_EMPTY = "Parking lot is empty."

    fun status() {
        when {
            isParkingLotEmpty() -> {
                println(MESSAGE_PARKING_LOT_IS_EMPTY)
            }
            getLowestFreeSpot() == -1 -> {
                println(MESSAGE_PARKING_LOT_IS_FULL)
            }
            else -> {
                parkingLot.forEachIndexed { index, _ ->
                    if (!parkingLot[index].isOccupied) return@forEachIndexed
                    println("${index + 1} ${parkingLot[index].car?.regNumber} ${parkingLot[index].car?.color}")
                }
            }
        }
    }

    fun create(size: Int) {
        parkingLot = Array(size) { Spot(isOccupied = false, car = null) }
        println("Created a parking lot with $size spots.")
    }

    private fun isParkingLotEmpty(): Boolean {
        parkingLot.forEach { spot ->
            if (spot.isOccupied && spot.car != null) return false
        }
        return true
    }

    private fun isParkingSpotOccupied(spotNumber: Int): Boolean {
        return parkingLot[spotNumber].isOccupied
    }

    private fun getLowestFreeSpot(): Int {
        parkingLot.forEachIndexed { index, spot ->
            if (!spot.isOccupied && spot.car == null) return index
        }
        return -1
    }

    fun park(car: Car) {
        if (getLowestFreeSpot() != -1) {
            val spotNumber = getLowestFreeSpot()
            parkingLot[spotNumber].isOccupied = true
            parkingLot[spotNumber].car = car
            println("${car.color.capitalize()} car parked on the spot ${spotNumber + 1}.")
        }
    }

    fun leave(spotNumber: Int) {
        if (isParkingSpotOccupied(spotNumber - 1)) {
            parkingLot[spotNumber - 1].isOccupied = false
            parkingLot[spotNumber - 1].car = null
            println("Spot $spotNumber is free.")
        }
    }

    fun spotByColor(color: String) {
        val output = ArrayList<Int>(0)
        if (!isParkingLotEmpty() || getLowestFreeSpot() != -1) {
            parkingLot.forEachIndexed { index, spot ->
                if (spot.isOccupied && spot.car?.color == color.toLowerCase()) output.add(index + 1)
            }
        }
        when (output.size) {
            0 -> println("No cars with color $color were found.")
            else -> println(output.joinToString())
        }
    }

    fun spotByReg(regNumber: String) {
        var spotNumber = -1
        if (!isParkingLotEmpty() || getLowestFreeSpot() != -1) {
            parkingLot.forEachIndexed { index, spot ->
                if (spot.isOccupied && spot.car?.regNumber == regNumber) spotNumber = index + 1
            }
        }
        when (spotNumber) {
            -1 -> println("No cars with registration number $regNumber were found.")
            else -> println(spotNumber)
        }
    }

    fun regByColor(color: String) {
        val output = ArrayList<String>(0)
        if (!isParkingLotEmpty() || getLowestFreeSpot() != -1) {
            parkingLot.forEachIndexed { _, spot ->
                if (spot.isOccupied && spot.car?.color == color.toLowerCase()) output.add(spot.car!!.regNumber)
            }
        }
        when (output.size) {
            0 -> println("No cars with color $color were found.")
            else -> println(output.joinToString())
        }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val parkingLot = Parking()

    do {
        val commands = scanner.nextLine().split(" ")

        if (commands[0] == "exit") continue

        when {
            commands[0] == "create" -> parkingLot.create(commands[1].toInt())
            parkingLot.parkingLot.isEmpty() -> println("Sorry, parking lot is not created.")
            else -> {
                when (commands[0]) {
                    "create" -> parkingLot.create(commands[1].toInt())
                    "status" -> parkingLot.status()
                    "park" -> parkingLot.park(Car(regNumber = commands[1], color = commands[2].toLowerCase()))
                    "leave" -> parkingLot.leave(commands[1].toInt())
                    "spot_by_color" -> parkingLot.spotByColor(commands[1])
                    "spot_by_reg" -> parkingLot.spotByReg(commands[1])
                    "reg_by_color" -> parkingLot.regByColor(commands[1])
                }
            }
        }
    } while (commands[0] != "exit")
}
