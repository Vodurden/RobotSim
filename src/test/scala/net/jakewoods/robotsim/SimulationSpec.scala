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
        val commands = Seq(Move(), Left(), Right(), Report())

        val originalSim = Simulation.create(5, 5)
        commands.foreach(command => {
          val stepResults = Simulation.step(originalSim, command)

          // Commands that are ignored should return an identical simulation!
          assert(stepResults.simulation == originalSim)

          // Commands that are ignored should produce no messages
          assert(stepResults.messages.isEmpty)
        })
      }

      it("should place a new robot when given the Place command") {
        val originalSim = Simulation.create(5, 5)
        val stepResults = Simulation.step(originalSim, Place(1, 1, North()))

        assert(stepResults.simulation.robot.isDefined)
      }

      it("should place the new robot correctly when given the Place command") {
        val originalSim = Simulation.create(5, 5)
        val stepResults = Simulation.step(originalSim, Place(1, 2, North()))

        assert(stepResults.simulation.robot == Some(Robot(1, 2, North())))
      }
    }
  }
}
