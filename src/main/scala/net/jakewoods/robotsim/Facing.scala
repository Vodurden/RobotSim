package net.jakewoods.robotsim

sealed abstract class Facing
case object North extends Facing
case object South extends Facing
case object East extends Facing
case object West extends Facing

object Facing {
  /** Maps a single string to a [[net.jakewoods.robotsim.Facing]].
    *
    * If the string is valid returns the corrsponding [[net.jakewoods.robotsim.Facing]] in an Option.
    *
    * If the string is invalid None is returneed
    *
    * Valid strings are:
    *
    * - NORTH
    * - SOUTH
    * - EAST
    * - WEST
    *
    * Whitespace around the input string will be ignored.
    *
    * Facings **are not case insensitive**. E.g. `North` is not a valid facing.
    *
    * @param s the string to parse
    * @returns the parsed [[net.jakewoods.robotsim.Facing]] or None
    */
  def string2facing(s: String): Option[Facing] = {
    s.trim() match {
      case "NORTH" => Some(North)
      case "SOUTH" => Some(South)
      case "EAST" => Some(East)
      case "WEST" => Some(West)
      case _ => None
    }
  }

  def facing2string(facing: Facing): String = {
    facing match {
      case North => "NORTH"
      case South => "SOUTH"
      case East => "EAST"
      case West => "WEST"
    }
  }

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
