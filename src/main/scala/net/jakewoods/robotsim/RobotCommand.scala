package net.jakewoods.robotsim

sealed abstract class Facing
case class North() extends Facing
case class South() extends Facing
case class East() extends Facing
case class West() extends Facing

sealed abstract class RobotCommand
case class Place(x: Integer, y: Integer, facing: Facing) extends RobotCommand
case class Move() extends RobotCommand
case class Left() extends RobotCommand
case class Right() extends RobotCommand
case class Report() extends RobotCommand
