package net.jakewoods.robotsim

/** Represents a robot.
  *
  * Robots have a position and a direction they are Facing. This
  * data is used to drive a [[net.jakewoods.robotsim.Simulation]]
  */
case class Robot(x: Int, y: Int, facing: Facing)
