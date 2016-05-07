package net.jakewoods.robotsim

/** Represents a robot.
  *
  * Robots have a position and a direction they are Facing. This
  * data is used to drive a [[net.jakewoods.robotsim.Simulation]]
  *
  * Robots do not care about the coordinates given to them and will happily
  * exist in a negative space.
  */
case class Robot(x: Int, y: Int, facing: Facing)

object Robot {
  /** Maps a robot to a user-facing information string.
    *
    * Returns a string of the form:
    *
    *     X,Y,FACING
    *
    * Where X and Y are integers and FACING is either NORTH, SOUTH, EAST or WEST
    */
  def robot2string(robot: Robot): String = {
    val facing = Facing.facing2string(robot.facing)

    s"${robot.x},${robot.y},$facing"
  }
}
