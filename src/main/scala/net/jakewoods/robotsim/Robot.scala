package net.jakewoods.robotsim

/** Represents a robot.
  *
  * Robots have a position and a direction they are Facing. This
  * data is used to drive a [[Simulation]]
  *
  * Robots do not care about the coordinates given to them and will happily
  * exist in a negative space.
  */
case class Robot(x: Int, y: Int, facing: Facing) {
  /** Returns the space the robot is looking at
    */
  def targetedSpace(): (Int,Int) = {
    val directionVector = facing match {
      case North => (0, 1)
      case South => (0, -1)
      case East => (1, 0)
      case West => (-1, 0)
    }

    val newX = x + directionVector._1
    val newY = y + directionVector._2
    (newX, newY)
  }
  /** Returns a new robot that has moved robo-unit in the direction it's facing
    *
    * @return the updated robot
    */
  def move() = {
    val (newX, newY) = targetedSpace()

    this.copy(x = newX, y = newY)
  }

  /** Returns a new robot that has turned 90 degrees to the left
    *
    * @return the updated robot with a new facing
    */
  def turnLeft() = {
    this.copy(facing = Facing.rotateLeft(facing))
  }

  /** Returns a new robot that has turned 90 degrees to the right
    *
    * @return the updated robot with a new facing
    */
  def turnRight() = {
    this.copy(facing = Facing.rotateRight(facing))
  }
}

object Robot {
  /** Maps a robot to a user-facing information string.
    *
    * Returns a string of the form:
    *
    *     X,Y,FACING
    *
    * Where X and Y are integers and FACING is either NORTH, SOUTH, EAST or WEST
    *
    * @param robot the robot to stringify
    * @return a string representing the robots information
    */
  def robot2string(robot: Robot): String = {
    val facing = Facing.facing2string(robot.facing)

    s"${robot.x},${robot.y},$facing"
  }
}
