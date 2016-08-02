package net.jakewoods.robotsim

/** Simulates a command interpreter controlling a robot on a dangerously tall tabletop.
  */
case class Simulation(
  xBounds: Range,
  yBounds: Range,
  obstacles: Set[Position],
  robot: Option[Robot],
  messages: Seq[String]) {

  /** Returns a string representation of the current mapping
    *
    * Legend:
    * 0: Empty space
    * X: Obstacle
    * ^: The Robot (Facing North)
    * <: The Robot (Facing West)
    * >: The Robot (Facing East)
    * V: The Robot (Facing Down)
    */
  def asciiMap(): String = {
    def facingToCharacter(facing: Facing): String = facing match {
      case North => "^"
      case South => "V"
      case East => ">"
      case West => "<"
    }

    def positionToCharacter(pos: Position): String = {
      // Are we the robot?
      val robotSpace = robot.filter(_.pos == pos).map(r => facingToCharacter(r.facing))

      // Are we an obstacle?
      val obstacle = if(obstacles.contains(pos)) Some("X") else None

      // Default back to '0'
      robotSpace
        .orElse(obstacle)
        .getOrElse("0")
    }

    val rows = for {
      y <- yBounds
      chars = for(x <- xBounds) yield positionToCharacter(Position(x,y))
      row = chars.mkString("")
    } yield row

    // We reverse our rows because 0,0 is the *bottom left* of the board but we've
    // mapped it like it's the top left.
    rows.reverse.mkString("\n")
  }

  /** Executes the command on the simulation and returns the updated simulation and any messages
    *
    * Commands update the state of the simulation. At the moment they can:
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
    * @param command the command to execute
    * @return a [[Simulation]] the updated simulation
    */
  def step(command: RobotCommand): Simulation = {
    // Apply our commands to our simulation state
    val nextRobot = stepRobot(this.robot, command)
    val nextObstacles = stepObstacles(nextRobot, obstacles, command)
    val nextMessages = stepMessages(nextRobot, command)

    // Produce our new simulation
    val nextSimulation = this.copy(
      robot = nextRobot,
      obstacles = nextObstacles,
      messages = nextMessages
    )

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
      case Place(x, y, facing) => Some(Robot(Position(x,y), facing))
      case Move => robot.map(_.move)
      case Left => robot.map(_.turnLeft)
      case Right => robot.map(_.turnRight)
      case _ => robot
    }
  }

  private def stepObstacles(robot: Option[Robot], obstacles: Set[Position], command: RobotCommand): Set[Position] = {
    robot.map { r =>
      command match {
        case PlaceObject => obstacles + r.targetedSpace()
        case _ => obstacles
      }
    }.getOrElse(obstacles)
  }

  private def stepMessages(robot: Option[Robot], command: RobotCommand): List[String] = {
    command match {
      case Report => robot.map { r => List(Robot.robot2string(r)) }.getOrElse(List())
      case Map => List(this.asciiMap())
      case _ => List()
    }
  }

  /** Validates the state of this simulation
    *
    * A simulation is valid if:
    *
    * - The robot is within the bounds of the simulation
    * - The robot is not colliding with an object
    * - No obstacles are placed off the table
    *
    * @return True if the simulation is valid. False otherwise.
    */
  private def isValid(): Boolean = {
    val robotWithinBounds = this.robot
      .map(r => xBounds.contains(r.pos.x) && yBounds.contains(r.pos.y))
      .getOrElse(true) // If we don't have a robot then our robot position is valid.

    val robotNotInObjects = this.robot
      .map(r => !obstacles.contains(r.pos))
      .getOrElse(true) // If we don't have a robot then it's not in an object

    val obstaclesWithinBounds = obstacles
      .map(o => xBounds.contains(o.x) && yBounds.contains(o.y))
      .foldRight(true)((a, b) => a && b)

    robotWithinBounds && robotNotInObjects && obstaclesWithinBounds
  }
}

object Simulation {
  /** Creates a new simulation
    *
    * @param width the width of the simulated tabletop in robo-spaces (from 0 to width inclusive)
    * @param height the height of the simulated tabletop in robo-spaces (from 0 to height inclusive)
    * @return a new simulation with no robot
    */
  def create(width: Int, height: Int): Simulation = Simulation(
    xBounds = Range.inclusive(0, width - 1),
    yBounds = Range.inclusive(0, height - 1),
    robot = None,
    obstacles = Set(),
    messages = List()
  )
}
