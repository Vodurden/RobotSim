package net.jakewoods.robotsim

case class Simulation(
  xBounds: Range,
  yBounds: Range,
  robot: Option[Robot],
  messages: Seq[String]) {

  /** Executes the command on the simulation and returns the updated simulation and any messages
    *
    * Commands can do up to two things:
    *
    * - Change the state of the robot
    * - Produce one or more messages
    *
    * Each command affects the simulation in a different way. Consult PROBLEM.md for
    * more information about the available commands.
    *
    * If a command results in an invalid simulation the existing simulation will
    * be returned without it's messages.
    *
    * This function also returns a list of messages. Any command may result in a message.
    *
    * @param simulation the simulation to run the command on
    * @param command the command to execute
    * @returns a [[SimulationResult]] with the updated simulation and a list of messages
    */
  def step(command: RobotCommand): Simulation = {
    // Apply our commands to our simulation state
    val nextRobot = stepRobot(this.robot, command)
    val nextMessages = stepMessages(nextRobot, command)

    // Produce our new simulation
    val nextSimulation = this.copy(robot = nextRobot, messages = nextMessages)

    // We only want to return the new simulation if it's valid. If it's not
    // we effectively 'undo' it by returning ourselves without any messages.
    if(nextSimulation.isValid())
      nextSimulation
    else
      this.copy(messages = List())
  }

  private def stepRobot(robot: Option[Robot], command: RobotCommand): Option[Robot] = {
    // We don't use `robot.map` for `Place()` since we don't care if the robot already exists.
    command match {
      case Place(x, y, facing) => Some(Robot(x, y, facing))
      case Move => robot.map(_.move)
      case Left => robot.map(_.turnLeft)
      case Right => robot.map(_.turnRight)
      case _ => robot
    }
  }

  private def stepMessages(robot: Option[Robot], command: RobotCommand): List[String] = {
    robot.map { r =>
      command match {
        case Report => List(Robot.robot2string(r))
        case _ => List()
      }
    }.getOrElse(List()) // No robot means no messages
  }

  /** Validates the state of this simulation
    *
    * A simulation is valid if:
    *
    * - The robot is within the bounds of the simulation
    *
    * @returns True if the simulation is valid. False otherwise.
    */
  private def isValid(): Boolean = {
    this.robot
      .map(r => xBounds.contains(r.x) && yBounds.contains(r.y))
      .getOrElse(true) // If we don't have a robot then our simulation is valid.
  }
}

object Simulation {
  /** Creates a new simulation
    *
    * @param width the width of the simulated tabletop in robo-spaces (from 0 to width inclusive)
    * @param height the height of the simulated tabletop in robo-spaces (from 0 to height inclusive)
    * @returns a new simulation with no robot
    */
  def create(width: Int, height: Int): Simulation = Simulation(
    xBounds = Range.inclusive(0, width - 1),
    yBounds = Range.inclusive(0, height - 1),
    robot = None,
    messages = List()
  )
}
