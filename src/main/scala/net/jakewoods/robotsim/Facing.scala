package net.jakewoods.robotsim

sealed abstract class Facing
case class North() extends Facing
case class South() extends Facing
case class East() extends Facing
case class West() extends Facing
