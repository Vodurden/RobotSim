package net.jakewoods.robotsim

case class Robot(x: Int, y: Int, facing: Facing)

case class Simulation(
  xBounds: Range,
  yBounds: Range,
  robot: Option[Robot]
)

case class SimulationResult(
  simulation: Simulation,
  messages: Seq[String]
)

object Simulation {
  /** Creates a new simulation.
    *
    * @param width the width of the simulated tabletop in robo-spaces (from 0 to width inclusive)
    * @param height the height of the simulated tabletop in robo-spaces (from 0 to height inclusive)
    * @returns a new simulation with no robot
    */
  def create(width: Int, height: Int): Simulation = Simulation(
    xBounds = Range.inclusive(0, width),
    yBounds = Range.inclusive(0, height),
    robot = None
  )

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
    * be returned.
    *
    * This function also returns a list of messages. Any command may result in a message.
    *
    * @param simulation the simulation to run the command on
    * @param command the command to execute
    * @returns a [[SimulationResult]] with the updated simulation and a list of messages
    */
  def step(simulation: Simulation, command: RobotCommand): SimulationResult = {
    // Apply our command to the robot
    val nextRobot = command match {
      case Place(x, y, facing) => Some(Robot(x, y, facing))
      case _ => simulation.robot
    }

    // Produce our new simulation
    val nextSim = simulation.copy(robot = nextRobot)

    // We don't support any message commands yet.
    val messages = Seq()

    SimulationResult(nextSim, messages)
  }
}
