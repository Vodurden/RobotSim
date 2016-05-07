package net.jakewoods.robotsim

sealed abstract class RobotCommand
case class Place(x: Int, y: Int, facing: Facing) extends RobotCommand
case object Move extends RobotCommand
case object Left extends RobotCommand
case object Right extends RobotCommand
case object Report extends RobotCommand
