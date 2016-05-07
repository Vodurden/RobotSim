package net.jakewoods.robotsim

class SimulationSpec extends UnitSpec {
  describe("Simulation") {
    describe("when created") {
      it("should not have a robot") {
        val simulation = Simulation.create(5, 5)

        assert(simulation.robot == None)
      }
    }

    describe("when it has no robot") {
      it("should ignore all commands other than Place") {
        val commands = Seq(Move, Left, Right, Report)

        val originalSim = Simulation.create(5, 5)
        commands.foreach(command => {
          val simulation = originalSim.step(command)

          // Commands that are ignored should return an identical simulation!
          assert(simulation == originalSim)
        })
      }

      it("should place a new robot when given the Place command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1, 1, North))

        assert(simulation.robot.isDefined)
      }

      it("should place the new robot correctly when given the Place command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1, 2, North))

        assert(simulation.robot == Some(Robot(1, 2, North)))
      }
    }

    describe("when it has a robot") {
      it("the robot should turn left when given the Left command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1,1,North))
          .step(Left)

        assert(simulation.robot == Some(Robot(1, 1, West)))
      }

      it("the robot should turn right when given the Right command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1,1,North))
          .step(Right)

        assert(simulation.robot == Some(Robot(1, 1, East)))
      }

      it("it should return a message containing the robots details when given the Report command") {
        val simulation = Simulation
          .create(5, 5)
          .step(Place(1, 1, North))
          .step(Report)

        assert(simulation.messages == List("Robot(1,1,North)"))
      }
    }
  }
}
