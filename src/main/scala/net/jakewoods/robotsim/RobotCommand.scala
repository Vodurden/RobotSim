package net.jakewoods.robotsim

sealed abstract class RobotCommand
case class Place(x: Int, y: Int, facing: Facing) extends RobotCommand
case class Move() extends RobotCommand
case class Left() extends RobotCommand
case class Right() extends RobotCommand
case class Report() extends RobotCommand
