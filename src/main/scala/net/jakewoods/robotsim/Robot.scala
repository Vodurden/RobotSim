package net.jakewoods.robotsim

/** Represents a robot.
  *
  * Robots have a position and a direction they are Facing. This
  * data is used to drive a [[Simulation]]
  *
  * Robots do not care about the coordinates given to them and will happily
  * exist in a negative space.
  */
case class Robot(pos: Position, facing: Facing) {
  /** Returns the space the robot is looking at
    */
  def targetedSpace(): Position = {
    val directionVector = facing match {
      case North => Position(0, 1)
      case South => Position(0, -1)
      case East => Position(1, 0)
      case West => Position(-1, 0)
    }

    Position(
      pos.x + directionVector.x,
      pos.y + directionVector.y
    )
  }
  /** Returns a new robot that has moved robo-unit in the direction it's facing
    *
    * @return the updated robot
    */
  def move() = {
    this.copy(pos = targetedSpace())
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

    s"${robot.pos.x},${robot.pos.y},$facing"
  }
}
