package net.jakewoods.robotsim

sealed abstract class Facing
case object North extends Facing
case object South extends Facing
case object East extends Facing
case object West extends Facing

object Facing {
  def rotateLeft(facing: Facing): Facing = {
    facing match {
      case North => West
      case West => South
      case South => East
      case East => North
    }
  }

  def rotateRight(facing: Facing): Facing = {
    facing match {
      case North => East
      case West => North
      case South => West
      case East => South
    }
  }
}
